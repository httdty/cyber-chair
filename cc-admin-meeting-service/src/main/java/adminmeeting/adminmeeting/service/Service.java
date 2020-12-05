package adminmeeting.adminmeeting.service;

import adminmeeting.request.admin.ApplicationRatifyRequest;
import adminmeeting.request.meeting.*;
import adminmeeting.service.admin.AdminService;
import adminmeeting.service.meeting.MeetingArticleService;
import adminmeeting.service.meeting.MeetingReviewService;
import adminmeeting.service.meeting.MeetingUtilService;
import adminmeeting.utility.response.ResponseGenerator;
import adminmeeting.utility.response.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@org.springframework.stereotype.Service
@RestController
public class Service {

    Logger logger = LoggerFactory.getLogger(Service.class);

    private AdminService adminService;
    private MeetingArticleService meetingArticleService;
    private MeetingUtilService meetingUtilService;
    private MeetingReviewService meetingReviewService;
    private static String fetched = " have been fetched";
    private static String requested = " have been requested";
    private static String forArticle = "for Article ";




    //------------------------
    @Autowired
    public Service(

            AdminService adminService,
            MeetingArticleService meetingArticleService,
            MeetingUtilService meetingUtilService,
            MeetingReviewService meetingReviewService

    ) {

        this.adminService = adminService;
        this.meetingArticleService = meetingArticleService;
        this.meetingUtilService = meetingUtilService;
        this.meetingReviewService = meetingReviewService;

    }


    public Service(){}


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

    public ResponseWrapper<?> review(ReviewRequest request, String token) {
        ResponseWrapper<?> ret = meetingArticleService.review(request, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Review of Reviewer " + request.getPcMemberName() + forArticle + request.getArticleid() + " have been submitted");
        }
        return ret;
    }

    public ResponseWrapper<?> getAlreadyReviewedInfo(String pcMemberName, String articleId, String token) {
        ResponseWrapper<?> ret = meetingArticleService.getAlreadyReviewedInfo(pcMemberName,articleId, token);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.debug("Review of Reviewer " + pcMemberName + forArticle + articleId + requested);
        }
        return ret;
    }

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
