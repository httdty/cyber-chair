package adminmeeting.service;

import adminmeeting.domain.Meeting;
import adminmeeting.exception.MeetingOfNoExistenceException;
import adminmeeting.exception.MeetingUnavaliableToOperateException;
import adminmeeting.repository.*;
import adminmeeting.request.admin.ApplicationRatifyRequest;
import adminmeeting.request.meeting.MeetingApplicationRequest;
import adminmeeting.request.meeting.PCMemberInvitationRequest;
import adminmeeting.request.user.InvitationRepoRequest;
import adminmeeting.request.meeting.*;
import adminmeeting.request.user.ArticleRequest;
import adminmeeting.request.util.LoginRequest;
import adminmeeting.request.util.RegisterRequest;
import adminmeeting.service.admin.AdminService;
import adminmeeting.service.meeting.MeetingArticleService;
import adminmeeting.service.meeting.MeetingReviewService;
import adminmeeting.service.meeting.MeetingSelfService;
import adminmeeting.service.meeting.MeetingUtilService;
//import adminmeeting.service.user.UserArticleService;
//import adminmeeting.service.user.UserInvitationService;
//import adminmeeting.service.user.UserMeetingService;
//import adminmeeting.service.util.UtilService;
import adminmeeting.utility.contract.MeetingStatus;
import adminmeeting.utility.response.ResponseGenerator;
import adminmeeting.utility.response.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Service
@RestController
public class Service {

    Logger logger = LoggerFactory.getLogger(Service.class);

//    private UserRepository userRepository;
//    private ArticleRepository articleRepository;
//    private ChairRelationRepository chairRelationRepository;
    private MeetingRepository meetingRepository;
    private PCMemberRelationRepository pcMemberRelationRepository;
//    private ReviewRelationRepository reviewRelationRepository;
//    private PostRepository postRepository;
//    private RebuttalRepository rebuttalRepository;

//    private UtilService utilService;
    private AdminService adminService;
    private MeetingArticleService meetingArticleService;
    private MeetingUtilService meetingUtilService;
//    private UserArticleService userArticleService;
//    private UserInvitationService userInvitationService;
//    private UserMeetingService userMeetingService;
    private MeetingReviewService meetingReviewService;
    private static String fetched = " have been fetched";
    private static String requested = " have been requested";
    private static String forArticle = "for Article ";
    // 添加拆分需要用的属性
    private MeetingSelfService meetingSelfService;




    //------------------------

    // 添加拆分需要用的方法
    public ResponseWrapper<?> queueingApplication() {
        ResponseWrapper<?> ret = meetingSelfService.queueingApplication();
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Queuing meeting have been fetched");
        }
        return  ret;
    }

    public ResponseWrapper<?> alreadyApplication() {
        ResponseWrapper<?> ret = meetingSelfService.alreadyApplication();
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Queuing meeting have been fetched");
        }
        return  ret;
    }

    public ResponseWrapper<?> mt_applicationRatify(ApplicationRatifyRequest request) {
        ResponseWrapper<?> ret = meetingSelfService.mt_applicationRatify(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Status of Meeting named "+ request.getMeetingName() + fetched);
        }
        return  ret;
    }

    public ResponseWrapper<?> mt_beginSubmission(BeginSubmissionRequest request) {
        ResponseWrapper<?> ret = meetingSelfService.mt_beginSubmission(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Submission begin for Meeting named " + request.getMeetingName());
        }
        return ret;
    }

    public ResponseWrapper<?> mt_getByName(String meetingName) {
        ResponseWrapper<?> ret = meetingSelfService.mt_getByName(meetingName);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Get Meeting Info for Meeting named " + meetingName);
        }
        return ret;
    }

    public ResponseWrapper<?>  mt_getById(Long meetingId) {
        ResponseWrapper<?> ret = meetingSelfService.mt_getById(meetingId);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Get Meeting Info for Meeting Id " + meetingId);
        }
        return ret;
    }

    public ResponseWrapper<?> mt_getByChairName(String chairName) {
        ResponseWrapper<?> ret = meetingSelfService.mt_getByChairName(chairName);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Get Meeting List for chair name " + chairName);
        }
        return ret;
    }


    public ResponseWrapper<?> mt_getByStatusAndChairNameNot(String status, String chairName) {
        ResponseWrapper<?> ret = meetingSelfService.mt_getByStatusAndChairNameNot(status, chairName);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Get Meeting List for chair name NOT and status NOT " + chairName);
        }
        return ret;
    }

    //------------------------

//    @Autowired
//    public Service(UserRepository userRepository,
//                   ArticleRepository articleRepository,
//                   ChairRelationRepository chairRelationRepository,
//                   MeetingRepository meetingRepository,
//                   PCMemberRelationRepository pcMemberRelationRepository,
//                   ReviewRelationRepository reviewRelationRepository,
//                   PostRepository postRepository,
//                   RebuttalRepository rebuttalRepository,
//                   UtilService utilService,
//                   AdminService adminService,
//                   MeetingArticleService meetingArticleService,
//                   MeetingUtilService meetingUtilService,
//                   UserArticleService userArticleService,
//                   UserInvitationService userInvitationService,
//                   UserMeetingService userMeetingService,
//                   MeetingReviewService meetingReviewService,
//                   // 拆分添加部分
//                   MeetingSelfService meetingSelfService
//    ) {
//        this.userRepository = userRepository;
//        this.articleRepository = articleRepository;
//        this.chairRelationRepository = chairRelationRepository;
//        this.meetingRepository = meetingRepository;
//        this.pcMemberRelationRepository = pcMemberRelationRepository;
//        this.reviewRelationRepository = reviewRelationRepository;
//        this.postRepository = postRepository;
//        this.rebuttalRepository = rebuttalRepository;
//        this.utilService = utilService;
//        this.adminService = adminService;
//        this.meetingArticleService = meetingArticleService;
//        this.meetingUtilService = meetingUtilService;
//        this.userArticleService = userArticleService;
//        this.userInvitationService = userInvitationService;
//        this.userMeetingService = userMeetingService;
//        this.meetingReviewService = meetingReviewService;
//        // 拆分添加部分
//        this.meetingSelfService = meetingSelfService;
//    }

    @Autowired
    public Service(
                   MeetingRepository meetingRepository,
                   PCMemberRelationRepository pcMemberRelationRepository,
                   AdminService adminService,
                   MeetingArticleService meetingArticleService,
                   MeetingUtilService meetingUtilService,
                   MeetingReviewService meetingReviewService,
                   // 拆分添加部分
                   MeetingSelfService meetingSelfService
    ) {

        this.meetingRepository = meetingRepository;
        this.pcMemberRelationRepository = pcMemberRelationRepository;
        this.adminService = adminService;
        this.meetingArticleService = meetingArticleService;
        this.meetingUtilService = meetingUtilService;
        this.meetingReviewService = meetingReviewService;
        // 拆分添加部分
        this.meetingSelfService = meetingSelfService;
    }


    public Service(){}

//    public ResponseWrapper<?> register(RegisterRequest request) {
//        ResponseWrapper<?> ret = utilService.Register(request);
//        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.info("Added registered user's username: "+request.getUsername());
//        }
//        return ret;
//    }
//
//    public ResponseWrapper<?> login(LoginRequest request) {
//        ResponseWrapper<?> ret = utilService.login(request);
//        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.info("User named "+request.getUsername() +" login success");
//        }
//        return  ret;
//    }
//
//    public ResponseWrapper<?> getUserinfo(String username) {
//        ResponseWrapper<?> ret = utilService.getUserinfo(username);
//        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("Information of User named "+username +fetched);
//        }
//        return  ret;
//    }
//
//    public ResponseWrapper<?> searchUsersbyFullname(String fullname) {
//        ResponseWrapper<?> ret = utilService.searchUsersbyFullname(fullname);
//        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("Information of User whose fullname is "+fullname +fetched);
//        }
//        return  ret;
//    }
//
//    public byte[] getPdfContent(String pdfUrl) {
//        byte[] ret = utilService.getPdfContent(pdfUrl);
//        if(ret!=null){
//            logger.debug("File content of  "+ pdfUrl +" has been fetched");
//        }
//        return  ret;
//    }

    public ResponseWrapper<?> meetingApplication(MeetingApplicationRequest request, String token) {
        ResponseWrapper<?> ret = meetingUtilService.meetingApplication(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Meeting named "+ request.getMeetingName() +" has been added");
        }
        return  ret;
    }

    public ResponseWrapper<?> getmeetingInfo(String meetingName, String token) {
        ResponseWrapper<?> ret = meetingUtilService.getmeetingInfo(meetingName, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Information of Meeting named "+ meetingName +fetched);
        }
        return  ret;
    }

    public ResponseWrapper<?> pcmInvitation(PCMemberInvitationRequest request, String token) {
        ResponseWrapper<?> ret = meetingUtilService.pcmInvitation(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Invitation of Meeting named "+ request.getMeetingName() +" to "+"User named " + request.getPcMemberName() + " has been added");
        }
        return  ret;
    }

    public ResponseWrapper<?> getInvitationStatus(String meetingName, String token) {
        ResponseWrapper<?> ret = meetingUtilService.getInvitationStatus(meetingName, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Invitations of Meeting named "+ meetingName +fetched);
        }
        return  ret;
    }
//
//    public ResponseWrapper<?> getArticleDetail(String articleId){
//        logger.debug("service for article detail called. article id = " + articleId);
//        return  userArticleService.getArticleDetail(articleId);
//    }

    public ResponseWrapper<?> getqueueingApplication(String token) {
        ResponseWrapper<?> ret = adminService.getqueueingApplication(token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Queuing applications have been fetched by admin");
        }
        return  ret;
    }

    public ResponseWrapper<?> getalreadyApplication(String token) {
        ResponseWrapper<?> ret = adminService.getalreadyApplication(token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Dealed applications have been fetched by admin");
        }
        return  ret;
    }

    public ResponseWrapper<?> applicationRatify(ApplicationRatifyRequest request, String token) {
        ResponseWrapper<?> ret = adminService.applicationRatify(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Status of Meeting named "+ request.getMeetingName() + fetched);
        }
        return  ret;
    }
//
//    public ResponseWrapper<?> chairMeeting(String username){
//        ResponseWrapper<?> ret = userMeetingService.chairMeeting(username);
//        if (ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("Meeting list "+ username + " role as chair has been fetched.");
//        }
//        return  ret;
//    }
//
//    public ResponseWrapper<?> pcMemberMeeting(String username){
//        ResponseWrapper<?> ret = userMeetingService.pcMemberMeeting(username);
//        if (ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("Meeting list "+ username + " role as pcMember has been fetched.");
//        }
//        return  ret;
//    }
//
//    public ResponseWrapper<?> authorMeeting(String username){
//        ResponseWrapper<?> ret = userMeetingService.authorMeeting(username);
//        if (ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("Meeting list "+ username + " role as author has been fetched.");
//        }
//        return  ret;
//    }
//
//    public ResponseWrapper<?> availableMeeting(String username){
//        ResponseWrapper<?> ret = userMeetingService.availableMeeting(username);
//        if (ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("Meeting list available to "+ username + fetched);
//        }
//        return  ret;
//    }
//
//    public ResponseWrapper<?> undealedNotifications(String username){
//        ResponseWrapper<?> ret = userInvitationService.undealedNotifications(username);
//        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("undealed messages of " + username + fetched);
//        }
//        return ret;
//    }
//
//    public ResponseWrapper<?> submitNewArticle(ArticleRequest request, String rootDir){
//        ResponseWrapper<?> ret = userArticleService.uploadNewArticle(request, rootDir);
//        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.info("user " + request.getUsername() + " submit a essay title " + request.getEssayTitle()
//            +" to meeting " + request.getMeetingName() + " at date " + new Date());
//        }
//        return ret;
//    }
//
//    public ResponseWrapper<?> updateArticle(String articleId, ArticleRequest request, String rootDir){
//        ResponseWrapper<?> ret = userArticleService.updateExistedArticle(articleId, request, rootDir);
//        if(ret.getResponseMessage().equals((ResponseGenerator.success))){
//            logger.info("user " + request.getUsername() + " update the article with id " +
//                    articleId + " at time " + new Date());
//        }
//        return ret;
//    }
//
//    public ResponseWrapper<?> getReviewsOfArticle(String articleId){
//        ResponseWrapper<?> ret = userArticleService.getAllReviews(articleId);
//        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("the reviews of article with id " + articleId + " is requested");
//        }
//        return ret;
//    }

    public ResponseWrapper<?> beginSubmission(BeginSubmissionRequest request, String token) {
        ResponseWrapper<?> ret = meetingArticleService.beginSubmission(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Submission begin for Meeting named " + request.getMeetingName());
        }
        return ret;
    }

    public ResponseWrapper<?> getInfoOfReview(String pcMemberName, String meetingName, String token) {
        ResponseWrapper<?> ret = meetingArticleService.getInfoOfReview(pcMemberName,meetingName, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Review Information of " + pcMemberName + "in " + meetingName + requested);
        }
        return ret;
    }

    public ResponseWrapper<?> getInfoOfArticleToReview(String pcMemberName, String articleId, String token) {
        ResponseWrapper<?> ret = meetingArticleService.getInfoOfArticleToReview(pcMemberName,articleId, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Article Information of Reviewer " + pcMemberName + forArticle + articleId + requested);
        }
        return ret;
    }
//
//    public ResponseWrapper<?> invitationRepo(InvitationRepoRequest request){
//        ResponseWrapper<?> ret = userInvitationService.invitationRepo(request);
//        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("Invitation Repo by "+ request.getUsername() + "to " + request.getMeetingName() + " have done.");
//        }
//        return ret;
//    }

    public ResponseWrapper<?> review(ReviewRequest request, String token) {
        ResponseWrapper<?> ret = meetingArticleService.review(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Review of Reviewer " + request.getPcMemberName() + forArticle + request.getArticleid() + " have been submitted");
        }
        return ret;
    }
//
//    public ResponseWrapper<?> undealedNotificationsNum(String username){
//        ResponseWrapper<?> ret = userInvitationService.undealedNotificationsNum(username);
//        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("the num of undealed messages of " + username + "has been fetched.");
//        }
//        return ret;
//    }

    public ResponseWrapper<?> getAlreadyReviewedInfo(String pcMemberName, String articleId, String token) {
        ResponseWrapper<?> ret = meetingArticleService.getAlreadyReviewedInfo(pcMemberName,articleId, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Review of Reviewer " + pcMemberName + forArticle + articleId + requested);
        }
        return ret;
    }

//    public ResponseWrapper<?> alreadyDealedNotifications(String username){
//        ResponseWrapper<?> ret = userInvitationService.alreadyDealedNotifications(username);
//        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
//            logger.debug("alreadyDealed messages of " + username + "has been fetched.");
//        }
//        return ret;
//    }
//
    public ResponseWrapper<?> beginReview(BeginReviewRequest request, String token) {
        ResponseWrapper<?> ret = meetingArticleService.beginReview(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Meeting named " + request.getMeetingName() + " review begin");
        }
        return ret;
    }

    public ResponseWrapper<?> getSubmissionList(String authorName, String meetingName, String token) {
        ResponseWrapper<?> ret = meetingUtilService.getSubmissionList(authorName,meetingName, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Meeting named " + meetingName + " Articles of "+ authorName +fetched);
        }
        return ret;
    }

    public ResponseWrapper<?> reviewPublish(ResultPublishRequest request, String token) {
        ResponseWrapper<?> ret = meetingUtilService.reviewPublish(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Meeting named " + request.getMeetingName() + " have published reviews");
        }
        return ret;
    }

    public ResponseWrapper<?> reviewPost(ReviewPostRequest request, String token) {
        ResponseWrapper<?> ret = meetingReviewService.reviewPost(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Review PostMessage: " + request.toString());
        }
        return ret;
    }

    public ResponseWrapper<?> getPostList(String articleId, String postStatus, String token) {
        ResponseWrapper<?> ret = meetingReviewService.getPostList(articleId,postStatus, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Get postList article: ID " + articleId +" Post Status: " + postStatus);
        }
        return ret;
    }

    public ResponseWrapper<?> updateReview(UpdateReviewRequest request, String token) {
        ResponseWrapper<?> ret = meetingReviewService.updateReview(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("update Review: " + request.toString());
        }
        return ret;
    }

    public ResponseWrapper<?> reviewConfirm(ReviewConfirmRequest request, String token) {
        ResponseWrapper<?> ret = meetingReviewService.reviewConfirm(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Review Confirm: " + request.toString());
        }
        return ret;
    }

    public ResponseWrapper<?> rebuttal(RebuttalRequest request, String token) {
        ResponseWrapper<?> ret = meetingReviewService.rebuttal(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Rebuttal: " + request.toString());
        }
        return ret;
    }

    public ResponseWrapper<?> getRebuttalInfo(String articleId, String token) {
        ResponseWrapper<?> ret = meetingReviewService.getRebuttalInfo(articleId, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Get Rebuttal Info for article: ID " + articleId);
        }
        return ret;
    }

    public ResponseWrapper<?> finalPublish(FinalPublishRequest request,  String token) {
        ResponseWrapper<?> ret = meetingReviewService.finalPublish(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Final Publish: " + request.toString());
        }
        return ret;
    }


}
