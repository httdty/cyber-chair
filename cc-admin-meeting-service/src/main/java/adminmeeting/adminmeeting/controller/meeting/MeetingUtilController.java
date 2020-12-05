package adminmeeting.adminmeeting.controller.meeting;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.request.meeting.MeetingApplicationRequest;
import adminmeeting.request.meeting.PCMemberInvitationRequest;
import adminmeeting.service.Service;
import adminmeeting.utility.response.ResponseWrapper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class MeetingUtilController {
    Logger logger = LoggerFactory.getLogger(MeetingUtilController.class);

    private Service service;

    @Autowired
    private RemoteServiceConfig remote;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    public MeetingUtilController(Service service) { this.service = service; }

    @ApiOperation(value = "提交新增meeting的申请", response = ResponseWrapper.class)
    @PostMapping("/meeting/application")
    public ResponseEntity<?> meetingApplication(@RequestBody MeetingApplicationRequest request,  @RequestHeader("authorization") String token) {
        logger.debug("Meeting application: " + request.toString());
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.meetingApplication(request, token));
    }

    @ApiOperation(value = "根据meetingName获取会议信息", response = ResponseWrapper.class)
    @GetMapping("/meeting/meetingInfo")
    public ResponseEntity<?> getmeetingInfo(String meetingName,  @RequestHeader("authorization") String token) {
        logger.debug("Meeting Information: " + meetingName);
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.getmeetingInfo(meetingName, token));
    }

    @ApiOperation(value = "邀请用户成为pcmember", response = ResponseWrapper.class)
    @PostMapping("/meeting/pcmInvitation")
    public ResponseEntity<?> pcmInvitation(@RequestBody PCMemberInvitationRequest request,  @RequestHeader("authorization") String token) {
        logger.debug("PCMember Invitation: " + request.toString());
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.pcmInvitation(request, token));
    }

    @ApiOperation(value = "获取会议所有邀请的状态", response = ResponseWrapper.class)
    @GetMapping("/meeting/invitationStatus")
    public ResponseEntity<?> getInvitationStatus(String meetingName,  @RequestHeader("authorization") String token) {
        logger.debug("Invitation Status: " + meetingName);
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.getInvitationStatus(meetingName, token));
    }

    @ApiOperation(value = "根据会议名称和作者获取相应的投稿", response = ResponseWrapper.class)
    @GetMapping("/meeting/submissionList")
    public ResponseEntity<?> getSubmissionList(String authorName,String meetingName,  @RequestHeader("authorization") String token) {
        logger.debug("Submission List");
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.getSubmissionList(authorName,meetingName,token));
    }

}
