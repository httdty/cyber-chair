package adminmeeting.service.meeting;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.domain.Article;
import adminmeeting.domain.Meeting;
import adminmeeting.domain.ReviewRelation;
import adminmeeting.domain.User;
import adminmeeting.exception.MeetingOfNoExistenceException;
import adminmeeting.exception.MeetingUnavaliableToOperateException;
import adminmeeting.exception.UserNamedidntExistException;
import adminmeeting.repository.MeetingRepository;
import adminmeeting.request.admin.ApplicationRatifyRequest;
import adminmeeting.request.meeting.BeginSubmissionRequest;
import adminmeeting.request.meeting.GetByNameRequest;
import adminmeeting.utility.ApiUtil;
import adminmeeting.utility.contract.MeetingStatus;
import adminmeeting.utility.response.ResponseGenerator;
import adminmeeting.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class MeetingSelfService {

    @Autowired
    private MeetingRepository meetingRepository;
//
//    @Autowired
//    private RestTemplate restTemplate;

    @Autowired
    private RemoteServiceConfig remote;

    @Autowired
    private ApiUtil apiUtil;


    @Autowired
    public MeetingSelfService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    @Transactional
    public ResponseWrapper<?> queueingApplication() {
        List<Meeting> meetings = meetingRepository.findByStatus(MeetingStatus.unprocessed); // 这里还是用queueingApplication是为了和前端一致
        return ResponseGenerator.injectObjectFromListToResponse("queueingApplication",meetings,new String[]{"chairName","meetingName","acronym","region","city","venue","topic","organizer","webPage","submissionDeadlineDate","notificationOfAcceptanceDate","conferenceDate"},null);
    }
    @Transactional
    public ResponseWrapper<?> alreadyApplication() {
        List<Meeting> meetings = meetingRepository.findByStatusNot(MeetingStatus.unprocessed); // 这里还是用alreadyApplication是为了和前端一致
        return ResponseGenerator.injectObjectFromListToResponse("alreadyApplication",meetings,new String[]{"chairName","meetingName","acronym","region","city","venue","topic","organizer","webPage","submissionDeadlineDate","notificationOfAcceptanceDate","conferenceDate"},null);
    }
    @Transactional
    public ResponseWrapper<?> mt_applicationRatify(ApplicationRatifyRequest request) {
        // 管理员修改meeting
        String meetingName = request.getMeetingName();
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if(meeting==null){
            throw new MeetingOfNoExistenceException(meetingName);
        }
        if(!meeting.getStatus().equals(MeetingStatus.unprocessed)){
            throw new MeetingUnavaliableToOperateException(meetingName);
        }
        meeting.setStatus(request.getApprovalStatus());
        meetingRepository.save(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    @Transactional
    public ResponseWrapper<?> mt_beginSubmission(BeginSubmissionRequest request) {
        // 开始向meeting中提交文章，检查meeting是否存在以及可以投稿
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
    public ResponseWrapper<?> mt_getByName(String meetingName) {
        // 根据meeting name查询meeting 详细信息的, 返回的键： "id","chairName","meetingName","acronym","region","city","venue","topic","organizer","webPage","submissionDeadlineDate","notificationOfAcceptanceDate","conferenceDate","status"
        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }
        return ResponseGenerator.injectObjectFromObjectToResponse("meetingInfo",meeting,new String[]{"id","chairName","meetingName","acronym","region","city","venue","topic","organizer","webPage","submissionDeadlineDate","notificationOfAcceptanceDate","conferenceDate","status"}, null);
    }

    @Transactional
    public ResponseWrapper<?> mt_getById(Long meetingId) {
        Meeting meeting = meetingRepository.findById((long)meetingId);
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingId.toString());
        }
//        if(meeting == null){
//            meeting = new Meeting("a","a","a","a","a","a",null,"a","a","a","a","a","a");
//        }
        return ResponseGenerator.injectObjectFromObjectToResponse("meetingInfo",meeting,new String[]{"id","chairName","meetingName","acronym","region","city","venue","topic","organizer","webPage","submissionDeadlineDate","notificationOfAcceptanceDate","conferenceDate","status"}, null);
    }


    public ResponseWrapper<?> mt_getByChairName(String chairName) {
        List<Meeting> meetingList = meetingRepository.findByChairName(chairName);
        Meeting meeting1 = new Meeting("a","a","a","a","a","a",null,"a","a","a","a","a","a");
        Meeting meeting2 = new Meeting("b","b","b","a","a","a",null,"a","a","a","a","a","a");
        meetingList.add(meeting1);
        meetingList.add(meeting2);
        return ResponseGenerator.injectObjectFromListToResponse("meetingList",meetingList,new String[]{"id","chairName","meetingName","acronym","region","city","venue","topic","organizer","webPage","submissionDeadlineDate","notificationOfAcceptanceDate","conferenceDate","status"}, null);
    }

    public ResponseWrapper<?> mt_getByStatusAndChairNameNot(String status, String chairName) {
        List<Meeting> meetingList = meetingRepository.findByStatusAndChairNameNot(status, chairName);
        Meeting meeting1 = new Meeting("a","a","a","a","a","a",null,"a","a","a","a","a","a");
        Meeting meeting2 = new Meeting("b","b","b","a","a","a",null,"a","a","a","a","a","a");
        meetingList.add(meeting1);
        meetingList.add(meeting2);
        return ResponseGenerator.injectObjectFromListToResponse("meetingList",meetingList,new String[]{"id","chairName","meetingName","acronym","region","city","venue","topic","organizer","webPage","submissionDeadlineDate","notificationOfAcceptanceDate","conferenceDate","status"}, null);
    }
}
