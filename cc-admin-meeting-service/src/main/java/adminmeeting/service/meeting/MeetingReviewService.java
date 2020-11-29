package adminmeeting.service.meeting;

import adminmeeting.api.*;
import adminmeeting.domain.*;
import adminmeeting.repository.*;
import adminmeeting.request.meeting.*;
import adminmeeting.utility.contract.ArticleStatus;
import adminmeeting.utility.contract.MeetingStatus;
import adminmeeting.utility.contract.RebuttalStatus;
import adminmeeting.utility.contract.ReviewStatus;
import adminmeeting.utility.response.ResponseGenerator;
import adminmeeting.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class MeetingReviewService {
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private ArticleRepository articleRepository;
    @Autowired
    private MeetingRepository meetingRepository;
    @Autowired
    private PCMemberRelationRepository pcMemberRelationRepository;
//    @Autowired
//    private ReviewRelationRepository reviewRelationRepository;
//    @Autowired
//    private PostRepository postRepository;
//    @Autowired
//    private RebuttalRepository rebuttalRepository;

    private UserApi userApi = new UserApi();
    private ReviewRelationApi reviewRelationApi = new ReviewRelationApi();
    private ArticleApi articleApi = new ArticleApi();
    private PostApi postApi = new PostApi();
    private RebuttalApi rebuttalApi = new RebuttalApi();

    @Autowired
    public MeetingReviewService(MeetingRepository meetingRepository, PCMemberRelationRepository pcMemberRelationRepository) {
        this.meetingRepository = meetingRepository;
        this.pcMemberRelationRepository = pcMemberRelationRepository;
    }
//
//    @Autowired
//    public MeetingReviewService(UserRepository userRepository, ArticleRepository articleRepository, MeetingRepository meetingRepository, PCMemberRelationRepository pcMemberRelationRepository, ReviewRelationRepository reviewRelationRepository,PostRepository postRepository,RebuttalRepository rebuttalRepository) {
//        this.userRepository = userRepository;
//        this.articleRepository = articleRepository;
//        this.meetingRepository = meetingRepository;
//        this.pcMemberRelationRepository = pcMemberRelationRepository;
//        this.reviewRelationRepository = reviewRelationRepository;
//        this.postRepository = postRepository;
//        this.rebuttalRepository = rebuttalRepository;
//    }



    @Transactional
    public ResponseWrapper<?> reviewPost(ReviewPostRequest request, String token) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        PostMessage post = new PostMessage(
                userApi.findByUsername(request.getPosterName(), token).getId(),    // * userApi
                Long.parseLong(request.getArticleId()),
                Long.parseLong(request.getTargetId()),
                request.getContent(),
                request.getStatus(),
                timestamp.toString()
        );
        postApi.save(post, token);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    public ResponseWrapper<?> getPostList(String articleId, String postStatus, String token) {
        ArrayList<PostMessage> postList = (ArrayList<PostMessage>) postApi.findByArticleIdAndStatus(Long.parseLong(articleId),postStatus, token);
        postList.sort(new Comparator<PostMessage>() {
            @Override
            public int compare(PostMessage o1, PostMessage o2) {
                return Timestamp.valueOf(o1.getTimeStamp()).before(Timestamp.valueOf(o2.getTimeStamp()))?1:-1;
            }
        });
        HashMap<String, ArrayList<HashMap<String, Object>>> body = new HashMap<>();
        ArrayList<HashMap<String, Object>> retList = new ArrayList<>();

        for(PostMessage x: postList){
            HashMap<String,Object> ret = new HashMap<>();

            String targetContent = "";
            PostMessage target = postApi.findById(x.getTargetId(), token);
            if(target!=null) {
                User targetUser = userApi.findById(target.getPosterId(), token);
                targetContent = "Response to " + targetUser.getUsername() + ": " + target.getContent();
            }
            ret.put("postId",x.getId());
            ret.put("targetContent",targetContent);
            ret.put("postContent",x.getContent());
            ret.put("posterName",userApi.findById((long)Long.valueOf(x.getPosterId()), token).getUsername());
            ret.put("timeStamp",x.getTimeStamp());

            retList.add(ret);
        }
        body.put("postlist",retList);

        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public ResponseWrapper<?> updateReview(UpdateReviewRequest request, String token) {
        ReviewRelation reviewRelation = reviewRelationApi.findByReviewerIdAndArticleId(userApi.findByUsername(request.getPcMemberName(), token).getId(),Long.valueOf(request.getArticleId()), token);  // * userApi
        Meeting meeting = meetingRepository.findById((long)reviewRelation.getMeetingId());
        String meetingStatus = meeting.getStatus();
        String updateStatus = request.getStatus();

        if(updateStatus.equals(RebuttalStatus.beforeRebuttal)){
            if (meetingStatus.equals(MeetingStatus.resultPublished) && reviewRelation.getReviewStatus().equals(ReviewStatus.alreadyReviewed)){
                reviewRelation.setScore(Integer.valueOf(request.getScore()));
                reviewRelation.setReviews(request.getReviews());
                reviewRelation.setConfidence(request.getConfidence());
                reviewRelation.setReviewStatus(ReviewStatus.firstUpdated);
                reviewRelationApi.save(reviewRelation, token);
                ArticleStatusUpdate(Long.valueOf(request.getArticleId()), token);

                return new ResponseWrapper<>(200, ResponseGenerator.success, null);
            }
            else{
                return new ResponseWrapper<>(200, "failed : meeting or review status unavailable to do first update(Meeting should be resultPublished and Review should be alreadyReviewed)", null);
            }
        }
        else {
            if (meetingStatus.equals(MeetingStatus.rebuttalFnish) && reviewRelation.getReviewStatus().equals(ReviewStatus.reviewConfirmed)){
                reviewRelation.setScore(Integer.valueOf(request.getScore()));
                reviewRelation.setReviews(request.getReviews());
                reviewRelation.setConfidence(request.getConfidence());
                reviewRelation.setReviewStatus(ReviewStatus.secondUpdated);
                reviewRelationApi.save(reviewRelation, token);
                ArticleStatusUpdate(Long.valueOf(request.getArticleId()), token);

                return new ResponseWrapper<>(200, ResponseGenerator.success, null);
            }
            else{
                return new ResponseWrapper<>(200, "failed : meeting or review status unavailable to do second update(Meeting should be rebuttalFnish and Review should be reviewConfirmed)", null);
            }

        }
    }

    public ResponseWrapper<?> reviewConfirm(ReviewConfirmRequest request, String token) {
        ReviewRelation reviewRelation = reviewRelationApi.findByReviewerIdAndArticleId(userApi.findByUsername(request.getPcMemberName(), token).getId(),Long.valueOf(request.getArticleId()), token);  // * userApi
        Meeting meeting = meetingRepository.findById((long)reviewRelation.getMeetingId());
        String meetingStatus = meeting.getStatus();
        String confirmStatus = request.getStatus();

        if(confirmStatus.equals(RebuttalStatus.beforeRebuttal)){//第一次确认
            if((reviewRelation.getReviewStatus().equals(ReviewStatus.alreadyReviewed)||reviewRelation.getReviewStatus().equals(ReviewStatus.firstUpdated)) && meetingStatus.equals(MeetingStatus.resultPublished)){
                reviewRelation.setReviewStatus(ReviewStatus.reviewConfirmed);
                reviewRelationApi.save(reviewRelation, token);  // * reviewRelationApi
                meetingStatusModifyBeforeRebuttal(meeting, ReviewStatus.reviewConfirmed, MeetingStatus.reviewConfirmed, token);
                return new ResponseWrapper<>(200, ResponseGenerator.success, null);
            }
            else {
                return new ResponseWrapper<>(200, "failed : current status unable to do first confirm", null);
            }
        }
        else{//第二次确认
            if((reviewRelation.getReviewStatus().equals(ReviewStatus.reviewConfirmed)||reviewRelation.getReviewStatus().equals(ReviewStatus.secondUpdated)) && meetingStatus.equals(MeetingStatus.rebuttalFnish)){
                reviewRelation.setReviewStatus(ReviewStatus.finalConfirmed);//最终确认
                reviewRelationApi.save(reviewRelation, token);
                meetingStatusModifyBeforeRebuttal(meeting, ReviewStatus.finalConfirmed, MeetingStatus.reviewFinish, token);
                return new ResponseWrapper<>(200, ResponseGenerator.success, null);
            }
            else {
                return new ResponseWrapper<>(200, "failed : current status unable to do second confirm", null);
            }
        }
    }

    private void meetingStatusModifyBeforeRebuttal(Meeting meeting, String reviewConfirmed, String reviewConfirmed2, String token) {
        if (reviewRelationApi.findByReviewStatusAndMeetingId(reviewConfirmed, meeting.getId(), token).size() == reviewRelationApi.findByMeetingId(meeting.getId(), token).size()) {      // * reviewRelationApi
            meeting.setStatus(reviewConfirmed2);
            meetingRepository.save(meeting);
        }
    }

    public ResponseWrapper<?> rebuttal(RebuttalRequest request,  String token) {
        Article article = articleApi.findById((long)Long.valueOf(request.getArticleId()), token);           // * articleApi
        Meeting meeting = meetingRepository.findByMeetingName(article.getMeetingname());
        if(!meeting.getStatus().equals(MeetingStatus.reviewConfirmed)){
            return new ResponseWrapper<>(200, "failed : current status unable to rebuttal for meeting status isn't being reviewConfirmed", null);
        }
        if(!article.getStatus().equals(ArticleStatus.rejected) && request.getStatus().equals(RebuttalStatus.rebuttal)){//没有被拒绝
            return new ResponseWrapper<>(200, "failed : current status unable to rebuttal for accepted article", null);
        }
        Rebuttal rebuttal = new Rebuttal(Long.valueOf(request.getArticleId()),request.getContent(),request.getStatus());
        rebuttalApi.save(rebuttal, token);
        if(rebuttalApi.findByIdNot(-1L, token).size()==articleApi.findByIdNot(-1L, token).size()){
            meeting.setStatus(MeetingStatus.rebuttalFnish);
            meetingRepository.save(meeting);
        }
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    public ResponseWrapper<?> getRebuttalInfo(String articleId, String token) {
        List<Rebuttal> rebuttals = rebuttalApi.findByArticleId(Long.valueOf(articleId), token);
        if(rebuttals.isEmpty()){
            return new ResponseWrapper<>(200, "failed : no rebuttal for this article exist", null);
        }
        else{
            Rebuttal rebuttal = rebuttals.get(0);
            return ResponseGenerator.injectObjectFromObjectToResponse("rebuttal",rebuttal,new String[]{"content"},null);
        }
    }

    public ResponseWrapper<?> finalPublish(FinalPublishRequest request, String token) {
        Meeting meeting = meetingRepository.findByMeetingName(request.getMeetingName());
        if (meeting.getStatus().equals(MeetingStatus.reviewFinish)) {
            meeting.setStatus(MeetingStatus.reviewPublish);
            meetingRepository.save(meeting);
            return new ResponseWrapper<>(200, ResponseGenerator.success, null);
        } else {
            return new ResponseWrapper<>(200, "failed: unable to do final publish for incorrect meeting status", null);
        }
    }

    private void ArticleStatusUpdate(long articleId, String token){
        List<ReviewRelation> reviewRelations = reviewRelationApi.findByArticleId(articleId, token);
        Article article = articleApi.findById(articleId, token);
        int acceptSign = 1;
        for(ReviewRelation x : reviewRelations){
            if(x.getScore()<0){
                acceptSign = 0;
                break;
            }
        }
        article.setStatus(acceptSign>0?ArticleStatus.accepted:ArticleStatus.rejected);
        articleApi.save(article, token);
    }

}
