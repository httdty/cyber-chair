package chair.service;

import chair.repository.*;
import chair.request.meeting.PCMemberInvitationRequest;
import chair.request.meeting.*;

import chair.service.meeting.ChairMeetingService;
//import chair.service.user.ChairInvitationService;
//import chair.service.user.UserMeetingService;
import chair.utility.response.ResponseGenerator;
import chair.utility.response.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@org.springframework.stereotype.Service
@RestController
public class Service {

    Logger logger = LoggerFactory.getLogger(Service.class);

    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private ChairRelationRepository chairRelationRepository;
    private MeetingRepository meetingRepository;
    private PCMemberRelationRepository pcMemberRelationRepository;
    private ReviewRelationRepository reviewRelationRepository;
    private PostRepository postRepository;

    private ChairMeetingService chairMeetingService;
    private static String fetched = " have been fetched";
    private static String requested = " have been requested";
    private static String forArticle = "for Article ";

    @Autowired
    public Service(UserRepository userRepository,
                   ArticleRepository articleRepository,
                   ChairRelationRepository chairRelationRepository,
                   MeetingRepository meetingRepository,
                   PCMemberRelationRepository pcMemberRelationRepository,
                   ReviewRelationRepository reviewRelationRepository,
                   PostRepository postRepository,
                   ChairMeetingService chairMeetingService

    ) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.chairRelationRepository = chairRelationRepository;
        this.meetingRepository = meetingRepository;
        this.pcMemberRelationRepository = pcMemberRelationRepository;
        this.reviewRelationRepository = reviewRelationRepository;
        this.postRepository = postRepository;
        this.chairMeetingService = chairMeetingService;
    }


    public Service(){}


    public ResponseWrapper<?> pcmInvitation(PCMemberInvitationRequest request) {
        ResponseWrapper<?> ret = chairMeetingService.pcmInvitation(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Invitation of Meeting named "+ request.getMeetingName() +" to "+"User named " + request.getPcMemberName() + " has been added");
        }
        return  ret;
    }


    public  ResponseWrapper<?> beginSubmission(BeginSubmissionRequest request) {
        ResponseWrapper<?> ret = chairMeetingService.beginSubmission(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Submission begin for Meeting named " + request.getMeetingName());
        }
        return ret;
    }

    public ResponseWrapper<?> reviewPublish(ResultPublishRequest request) {
        ResponseWrapper<?> ret = chairMeetingService.reviewPublish(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Meeting named " + request.getMeetingName() + " have published reviews");
        }
        return ret;
    }

    public ResponseWrapper<?> finalPublish(FinalPublishRequest request) {
        ResponseWrapper<?> ret = chairMeetingService.finalPublish(request);
        if(ret.getResponseMessage().equals(ResponseGenerator.success)){
            logger.info("Final Publish: " + request.toString());
        }
        return ret;
    }

}
