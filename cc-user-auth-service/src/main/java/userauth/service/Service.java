package userauth.service;

import userauth.repository.UserRepository;
import userauth.request.user.ArticleRequest;
import userauth.request.user.InvitationRepoRequest;
import userauth.request.util.LoginRequest;
import userauth.request.util.RegisterRequest;
import userauth.service.user.UserArticleService;
import userauth.service.user.UserInvitationService;
import userauth.service.user.UserMeetingService;
import userauth.service.util.UtilService;
import userauth.utility.response.ResponseGenerator;
import userauth.utility.response.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@org.springframework.stereotype.Service
@RestController
public class Service {

    Logger logger = LoggerFactory.getLogger(Service.class);

    private UserRepository userRepository;

    private UtilService utilService;
    private UserArticleService userArticleService;
    private UserInvitationService userInvitationService;
    private UserMeetingService userMeetingService;
    private static final String fetched = " have been fetched";

    @Autowired
    public Service(UserRepository userRepository,
                   UtilService utilService,
                   UserArticleService userArticleService,
                   UserInvitationService userInvitationService,
                   UserMeetingService userMeetingService) {
        this.userRepository = userRepository;
        this.utilService = utilService;
        this.userArticleService = userArticleService;
        this.userInvitationService = userInvitationService;
        this.userMeetingService = userMeetingService;
    }


    public Service() {
    }

    public ResponseWrapper<?> register(RegisterRequest request) {
        ResponseWrapper<?> ret = utilService.Register(request);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.info("Added registered user's username: " + request.getUsername());
        }
        return ret;
    }

    public ResponseWrapper<?> login(LoginRequest request) {
        ResponseWrapper<?> ret = utilService.login(request);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.info("User named " + request.getUsername() + " login success");
        }
        return ret;
    }

    public ResponseWrapper<?> getUserinfo(String username) {
        ResponseWrapper<?> ret = utilService.getUserinfo(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("Information of User named " + username + fetched);
        }
        return ret;
    }

    public ResponseWrapper<?> searchUsersbyFullname(String fullname) {
        ResponseWrapper<?> ret = utilService.searchUsersbyFullname(fullname);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("Information of User whose fullname is " + fullname + fetched);
        }
        return ret;
    }

    public byte[] getPdfContent(String pdfUrl) {
        byte[] ret = utilService.getPdfContent(pdfUrl);
        if (ret != null) {
            logger.debug("File content of  " + pdfUrl + " has been fetched");
        }
        return ret;
    }


    public ResponseWrapper<?> getArticleDetail(String articleId, String token) {
        logger.debug("service for article detail called. article id = " + articleId);
        return userArticleService.getArticleDetail(articleId, token);
    }


    public ResponseWrapper<?> chairMeeting(String username) {
        ResponseWrapper<?> ret = userMeetingService.chairMeeting(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("Meeting list " + username + " role as chair has been fetched.");
        }
        return ret;
    }

    public ResponseWrapper<?> pcMemberMeeting(String username) {
        ResponseWrapper<?> ret = userMeetingService.pcMemberMeeting(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("Meeting list " + username + " role as pcMember has been fetched.");
        }
        return ret;
    }

    public ResponseWrapper<?> authorMeeting(String username) {
        ResponseWrapper<?> ret = userMeetingService.authorMeeting(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("Meeting list " + username + " role as author has been fetched.");
        }
        return ret;
    }

    public ResponseWrapper<?> availableMeeting(String username) {
        ResponseWrapper<?> ret = userMeetingService.availableMeeting(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("Meeting list available to " + username + fetched);
        }
        return ret;
    }

    public ResponseWrapper<?> undealedNotifications(String username) {
        ResponseWrapper<?> ret = userInvitationService.undealedNotifications(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("undealed messages of " + username + fetched);
        }
        return ret;
    }

    public ResponseWrapper<?> getReviewsOfArticle(String articleId) {
        ResponseWrapper<?> ret = userArticleService.getAllReviews(articleId);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("the reviews of article with id " + articleId + " is requested");
        }
        return ret;
    }


    public ResponseWrapper<?> invitationRepo(InvitationRepoRequest request) {
        ResponseWrapper<?> ret = userInvitationService.invitationRepo(request);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("Invitation Repo by " + request.getUsername() + "to " + request.getMeetingName() + " have done.");
        }
        return ret;
    }

    public ResponseWrapper<?> undealedNotificationsNum(String username) {
        ResponseWrapper<?> ret = userInvitationService.undealedNotificationsNum(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("the num of undealed messages of " + username + "has been fetched.");
        }
        return ret;
    }


    public ResponseWrapper<?> alreadyDealedNotifications(String username) {
        ResponseWrapper<?> ret = userInvitationService.alreadyDealedNotifications(username);
        if (ret.getResponseMessage().equals(ResponseGenerator.success)) {
            logger.debug("alreadyDealed messages of " + username + "has been fetched.");
        }
        return ret;
    }

}
