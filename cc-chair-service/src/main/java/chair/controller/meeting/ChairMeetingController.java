package chair.controller.meeting;

import chair.config.RemoteServiceConfig;
import chair.domain.Meeting;
import chair.domain.PCMemberRelation;
import chair.request.meeting.BeginSubmissionRequest;

import chair.request.meeting.FinalPublishRequest;
import chair.request.meeting.PCMemberInvitationRequest;
import chair.request.meeting.ResultPublishRequest;
import chair.service.Service;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ChairMeetingController {
    Logger logger = LoggerFactory.getLogger(ChairMeetingController.class);

    private Service service;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RemoteServiceConfig remote;
    @Autowired
    public ChairMeetingController(Service service) { this.service = service; }
    @ApiOperation(value = "将meeting状态设置成submissionAvaliable", response = Meeting.class)
    @PostMapping("/meeting/beginSubmission")
    public ResponseEntity<?> beginSubmission(@RequestBody BeginSubmissionRequest request,@RequestHeader("authorization") String token) {
        //通过RemoteServiceConfig注入user-auth服务的ip和port
        String checkApi = remote.getCheck();
        //构造请求头，加入token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        logger.debug("Begin Submission: " + request.toString());
        return ResponseEntity.ok(service.beginSubmission(request));
    }

    @ApiOperation(value = "将meeting状态设置成resultPublished", response = Meeting.class)
    @PostMapping("/meeting/publish")
    public ResponseEntity<?> reviewPublish(@RequestBody ResultPublishRequest request,@RequestHeader("authorization") String token) {
        //通过RemoteServiceConfig注入user-auth服务的ip和port
        String checkApi = remote.getCheck();
        //构造请求头，加入token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        //发送check请求到user-auth服务中， 目前api写死的
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        logger.debug("Review Request to Publish: " + request.toString());
        return ResponseEntity.ok(service.reviewPublish(request));
    }
    @ApiOperation(value = "将meeting状态设置成reviewPublish", response = Meeting.class)
    @PostMapping("/meeting/finalPublish")
    public ResponseEntity<?> finalPublish(@RequestBody FinalPublishRequest request,@RequestHeader("authorization") String token) {
        //通过RemoteServiceConfig注入user-auth服务的ip和port
        String checkApi = remote.getCheck();
        //构造请求头，加入token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        logger.debug("Final Publish: " + request.toString());
        return ResponseEntity.ok(service.finalPublish(request));
    }
    @ApiOperation(value = "将pcMemberRelation状态设置成applyFailed", response = PCMemberRelation.class)
    @PostMapping("/meeting/pcmInvitation")
    public ResponseEntity<?> pcmInvitation(@RequestBody PCMemberInvitationRequest request,@RequestHeader("authorization") String token) {
        //通过RemoteServiceConfig注入user-auth服务的ip和port
        String checkApi = remote.getCheck();
        //构造请求头，加入token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        logger.debug("PCMember Invitation: " + request.toString());
        return ResponseEntity.ok(service.pcmInvitation(request));
    }
}
