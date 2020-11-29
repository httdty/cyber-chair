package adminmeeting.service.meeting;

import adminmeeting.api.ArticleApi;
import adminmeeting.api.ReviewRelationApi;
import adminmeeting.api.UserApi;
import adminmeeting.domain.*;
import adminmeeting.exception.*;
import adminmeeting.repository.*;
import adminmeeting.request.meeting.BeginReviewRequest;
import adminmeeting.request.meeting.BeginSubmissionRequest;
import adminmeeting.request.meeting.ReviewRequest;
import adminmeeting.utility.contract.*;
import adminmeeting.utility.response.ResponseGenerator;
import adminmeeting.utility.response.ResponseWrapper;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MeetingArticleService {
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

    private UserApi userApi = new UserApi();
    private ReviewRelationApi reviewRelationApi = new ReviewRelationApi();
    private ArticleApi articleApi = new ArticleApi();



    private Random random = new Random();

//    @Autowired
//    public MeetingArticleService(UserRepository userRepository, ArticleRepository articleRepository, MeetingRepository meetingRepository, PCMemberRelationRepository pcMemberRelationRepository, ReviewRelationRepository reviewRelationRepository) {
//        this.userRepository = userRepository;
//        this.articleRepository = articleRepository;
//        this.meetingRepository = meetingRepository;
//        this.pcMemberRelationRepository = pcMemberRelationRepository;
//        this.reviewRelationRepository = reviewRelationRepository;
//    }
    @Autowired
    public MeetingArticleService(MeetingRepository meetingRepository, PCMemberRelationRepository pcMemberRelationRepository) {
        this.meetingRepository = meetingRepository;
        this.pcMemberRelationRepository = pcMemberRelationRepository;
    }

    @Transactional
    public ResponseWrapper<?> beginSubmission(BeginSubmissionRequest request, String token) {
        String meetingName = request.getMeetingName();
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }

        String meetingStatus = meeting.getStatus();
        if(!meetingStatus.equals(MeetingStatus.applyPassed)){
            throw new MeetingUnavaliableToOperateException(meetingName);
        }
        meeting.setStatus(MeetingStatus.submissionAvaliable);
        meetingRepository.save(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }
    @Transactional
    public ResponseWrapper<?> getInfoOfReview(String pcMemberName, String meetingName, String token) {
        User reviewer = userApi.findByUsername(pcMemberName, token);             // * userApi
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);

        if (reviewer == null) {
            throw new UserNamedidntExistException(pcMemberName);
        }//用户是否存在

        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }//会议是否存在

        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> responseSet = new HashSet<>();

        // * reviewRelationApi
        List<ReviewRelation> reviewRelationList = reviewRelationApi.findByReviewerIdAndMeetingId(reviewer.getId(),meeting.getId(), token);

        for(ReviewRelation x: reviewRelationList){
            Article article = articleApi.findById((long)x.getArticleId(), token);  // * articleApi

            HashMap<String, Object> response = ResponseGenerator.generate(article,
                    new String[]{"title","topic"}, null);
            response.put("articleId",x.getArticleId());
            response.put("reviewStatus",x.getReviewStatus());

            responseSet.add(response);
        }
        body.put("reviewArticles", responseSet);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }
    @Transactional
    public ResponseWrapper<?> getInfoOfArticleToReview(String pcMemberName, String articleId, String token) {
        ReviewRelation reviewRelation = securityCheckForReview(pcMemberName,articleId, token);
        long id = Long.valueOf(reviewRelation.getArticleId());
        Article article = articleApi.findById(id, token);   // * articleApi

        return ResponseGenerator.injectObjectFromObjectToResponse("reviewArticle",article,new String[]{"title","articleAbstract","submitDate","filePath","topic"},null);
    }
    @Transactional
    public ResponseWrapper<?> review(ReviewRequest request, String token) {

        Meeting meeting = meetingRepository.findByMeetingName(articleApi.findById((long)Long.valueOf(request.getArticleid()), token).getMeetingname());
                                                                                            // * articleApi
        if(!meeting.getStatus().equals(MeetingStatus.reviewing)){
            throw new MeetingStatusUnAvailableToReviewException();
        }
        ReviewRelation reviewRelation = securityCheckForReview(request.getPcMemberName(),request.getArticleid(), token);

        reviewRelation.setScore(Integer.valueOf(request.getScore()));
        reviewRelation.setConfidence(request.getConfidence());
        reviewRelation.setReviews(request.getReviews());
        reviewRelation.setReviewStatus(ReviewStatus.alreadyReviewed);
        reviewRelationApi.save(reviewRelation, token);        // * reviewRelationApi
        ArticleStatusUpdate(Long.valueOf(request.getArticleid()), token);

        List<ReviewRelation> reviewRelations = reviewRelationApi.findByReviewStatusAndMeetingId(ReviewStatus.unReviewed,meeting.getId(), token);
        if(reviewRelations.isEmpty()){
            meeting.setStatus(MeetingStatus.reviewCompleted);
            meetingRepository.save(meeting);
        }

        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }
    @Transactional
    public ResponseWrapper<?> getAlreadyReviewedInfo(String pcMemberName, String articleId, String token) {
        return ResponseGenerator.injectObjectFromObjectToResponse("alreadyReviewedInfo",securityCheckForReview(pcMemberName,articleId, token),new String[]{"score","confidence","reviews"},null);
    }
    @Transactional
    public ResponseWrapper<?> beginReview(BeginReviewRequest request, String token) {
        String meetingName = request.getMeetingName();
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }

        List<Article> articles = articleApi.findByMeetingNameAndStatus(meetingName, ArticleStatus.queuing, token);
        if(!meeting.getStatus().equals(MeetingStatus.submissionAvaliable)){
            throw new MeetingUnavaliableToOperateException(meetingName);
        }

        if (articles.isEmpty()){
            throw new NoneArticleToReviewException(meetingName);
        }//暂时无人投稿

        List<PCMemberRelation> pcMemberRelations = pcMemberRelationRepository.findByMeetingIdAndStatus(meeting.getId(), PCmemberRelationStatus.accepted);
        if(pcMemberRelations.size()<3){
            throw new AtLeastThreePCMemberException();
        }//pcmember过少

        //选择分配策略
        if(request.getAssignStrategy().equals(ArticleAssignStrategy.loadBalancing)){
            ArticleAssignInLoadBalancing(articles,pcMemberRelations,meeting.getId(), token);
        }
        else if(request.getAssignStrategy().equals(ArticleAssignStrategy.topicRelevant)){
            ArticleAssignInTopicRelevant(articles,pcMemberRelations,meeting.getId(), token);
        }
        //变更会议状态
        meeting.setStatus(MeetingStatus.reviewing);
        meetingRepository.save(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    private ReviewRelation securityCheckForReview(String pcMemberName, String articleId, String token){
        User reviewer = userApi.findByUsername(pcMemberName, token);    // * userApi

        if (reviewer == null) {
            throw new UserNamedidntExistException(pcMemberName);
        }//用户是否存在

        ReviewRelation reviewRelation = reviewRelationApi.findByReviewerIdAndArticleId(reviewer.getId(),Long.valueOf(articleId), token);

        if(reviewRelation==null){
            throw new RejectToReviewException(pcMemberName,articleId);
        }
        return reviewRelation;
    }

    private static boolean flag;

    int countMax(int[] count){
        int mx = -1;
        for(int i : count){
            mx = Math.max(mx, i);
        }
        return mx;
    }

    int countMin(int[] count){
        int mn = 1<<30;
        for(int i : count){
            mn = Math.min(mn, i);
        }
        return mn;
    }

    boolean balanceCheck(int mx, int mn){
        return (mx - mn <= 1);
    }

    boolean checkSamePCMember(int p, int articleNum, int i, int[] ass){
        if((p >= articleNum && ass[p - articleNum] == i)||(p >= articleNum *2 && ass[p - articleNum*2] == i)) return false;
        return true;
    }

    void dfs(List<Article> articles, List<User> pcmembers, int[] ass, int[] count, int p, String token){
        if(flag) return;
        int articleNum = articles.size();
        int mx = countMax(count);
        int mn = countMin(count);
        if(mx - mn > articleNum * 3 - p + 1){ return; }
        if(p == articleNum * 3){
            flag = balanceCheck(mx, mn);
            return;
        }
        Article article = articles.get(p % articleNum);
        Set<Pair<Author,Integer>> authorsAndRank = article.getAuthors();
        Set<User> authors = new HashSet<>();
        for(Pair<Author, Integer> pair : authorsAndRank){
            User author = userApi.findByEmail(pair.getKey().getEmail(), token);  // * userApi
            if(author == null) continue;
            authors.add(author);
        }
        for(int i=0; i<pcmembers.size(); i++){
            if(authors.contains(pcmembers.get(i))) continue;
            if(!checkSamePCMember(p, articleNum, i, ass)) continue;
            ass[p] = i;
            count[i] += 1;
            dfs(articles, pcmembers, ass, count, p+1, token);
            if(flag)return;
            count[i] -= 1;
            ass[p] = 0;
        }
    }

    private void ArticleAssignInLoadBalancing(List<Article> articles, List<PCMemberRelation> pcMemberRelations, long meetingId, String token){
        List<User> pcmembers = new ArrayList<>();
        for(PCMemberRelation relation : pcMemberRelations){
            pcmembers.add(userApi.findById((long)relation.getPcmemberId(), token));
        }
        int[] ass = new int[articles.size() * 3];
        int[] count = new int[pcmembers.size()];
        flag = false;
        dfs(articles, pcmembers, ass, count, 0, token);
        if(flag){
            for(int i=0; i < articles.size(); i++){
                reviewRelationApi.save(new ReviewRelation(pcmembers.get(ass[i]).getId(), meetingId, articles.get(i).getId(),
                        ReviewStatus.unReviewed, 0, null, null), token);
                reviewRelationApi.save(new ReviewRelation(pcmembers.get(ass[i + articles.size()]).getId(), meetingId, articles.get(i).getId(),
                        ReviewStatus.unReviewed, 0, null, null), token);
                reviewRelationApi.save(new ReviewRelation(pcmembers.get(ass[i + articles.size()*2]).getId(), meetingId, articles.get(i).getId(),
                        ReviewStatus.unReviewed, 0, null, null), token);
            }
        }
        else throw new cannotAssignArticlesException();
    }

    private void ArticleAssignInTopicRelevant(List<Article> articles,List<PCMemberRelation> pcMemberRelations,long meetingId, String token){

        for(Article article: articles){
            List<PCMemberRelation> pcMemberRelationSetUnConsiderTopic = pcMemberMatchFilter(article,pcMemberRelations,false, token);
            List<PCMemberRelation> pcMemberRelationSetConsiderTopic = pcMemberMatchFilter(article,pcMemberRelations,true, token);

            if(pcMemberRelationSetUnConsiderTopic.size()<3){
                throw new AtLeastThreePCMemberException();
            }
            int numOfPCMember = pcMemberRelationSetConsiderTopic.size();
            Set<PCMemberRelation> assignSet = generateAssignSet(numOfPCMember,pcMemberRelationSetUnConsiderTopic,pcMemberRelationSetConsiderTopic);

            for(PCMemberRelation x: assignSet){
                ReviewRelation reviewRelation = new ReviewRelation(x.getPcmemberId(),meetingId,article.getId(),ReviewStatus.unReviewed,0,null,null);
                reviewRelationApi.save(reviewRelation, token);
            }
        }
    }

    Set<PCMemberRelation> generateAssignSet(int numOfPCMember,List<PCMemberRelation> pcMemberRelationSetUnConsiderTopic,List<PCMemberRelation> pcMemberRelationSetConsiderTopic){
        Set<PCMemberRelation> assignSet = new HashSet<>();

        if(numOfPCMember<3){
            int size = pcMemberRelationSetUnConsiderTopic.size();
            while (assignSet.size()<3){
                assignSet.add(pcMemberRelationSetUnConsiderTopic.get(random.nextInt(size)));
            }
        }
        else if(numOfPCMember>3){
            int size = pcMemberRelationSetConsiderTopic.size();
            while (assignSet.size()<3){
                assignSet.add(pcMemberRelationSetConsiderTopic.get(random.nextInt(size)));
            }
        }
        else{
            for(PCMemberRelation x: pcMemberRelationSetConsiderTopic){
                assignSet.add(x);
            }
        }
        return assignSet;
    }

    //筛选符合分配要求的PCMemberRelation,必须筛选的是Article的author是否包含该PCMember，通过 topicConsider布尔值决定是否使用topic有交集这一筛选条件
    private List<PCMemberRelation> pcMemberMatchFilter(Article article,List<PCMemberRelation> pcMemberRelations,boolean topicConsider, String token){

        Set<Pair<Author,Integer>> authors = article.getAuthors();
        Set<Long> authorId = new HashSet<>();
        for(Pair<Author,Integer> pair: authors){
            Author author = pair.getKey();
            User authorUser = userApi.findByFullnameAndEmail(author.getFullname(),author.getEmail(), token);
            if(authorUser==null){
                authorId.add((long)-1);
            }
            else {
                authorId.add(authorUser.getId());
            }
        }

        return generatePcMemberRelationSet(topicConsider,authorId,article.getTopic(),pcMemberRelations);
    }

    private List<PCMemberRelation> generatePcMemberRelationSet(boolean topicConsider,Set<Long> authorId, Set<String> topic,List<PCMemberRelation> pcMemberRelations){
        List<PCMemberRelation> pcMemberRelationSet = new ArrayList<>();
        for (PCMemberRelation pcMemberRelation : pcMemberRelations) {
            if(authorId.contains(pcMemberRelation.getPcmemberId())){
                continue;
            }
            if(topicConsider) {
                Set<String> topicForPcm = pcMemberRelation.getTopic();
                for (String x : topicForPcm) {
                    if (topic.contains(x)) {
                        pcMemberRelationSet.add(pcMemberRelation);
                        break;
                    }
                }
            }
            else{
                pcMemberRelationSet.add(pcMemberRelation);
            }
        }
        return pcMemberRelationSet;
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
