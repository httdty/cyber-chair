package adminmeeting.adminmeeting.controller.meeting;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.request.meeting.BeginReviewRequest;
import adminmeeting.request.meeting.BeginSubmissionRequest;
import adminmeeting.request.meeting.ResultPublishRequest;
import adminmeeting.request.meeting.ReviewRequest;
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
public class MeetingArticleController {
    Logger logger = LoggerFactory.getLogger(MeetingArticleController.class);

    private Service service;

    @Autowired
    public MeetingArticleController(Service service) { this.service = service; }

    @Autowired
    private RemoteServiceConfig remote;

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "会议开始接受投稿", response = ResponseWrapper.class)
    @PostMapping("/meeting/beginSubmission")
    public ResponseEntity<?> beginSubmission(@RequestBody BeginSubmissionRequest request, @RequestHeader("authorization") String token) {
        logger.debug("Begin Submission: " + request.toString());
        String checkApi = remote.getCheck();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.beginSubmission(request, token));
    }

    @ApiOperation(value = "获取审核的文章列表", response = ResponseWrapper.class)
    @GetMapping("/meeting/reviewArticles")
    public ResponseEntity<?> getInfoOfReview(String pcMemberName,String meetingName, @RequestHeader("authorization") String token) {
        logger.debug("Get review information: " + meetingName + " " + pcMemberName);
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.getInfoOfReview(pcMemberName,meetingName, token));
    }

    @ApiOperation(value = "获得文章进行审核", response = ResponseWrapper.class)
    @GetMapping("/meeting/reviewArticle")
    public ResponseEntity<?> getInfoOfArticleToReview(String pcMemberName,String articleId, @RequestHeader("authorization") String token) {
        logger.debug("Get Article information: " + articleId + " Reviewer: " + pcMemberName);
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.getInfoOfArticleToReview(pcMemberName,articleId, token));
    }

    @ApiOperation(value = "审核文章", response = ResponseWrapper.class)
    @PostMapping("/meeting/reviewer")
    public ResponseEntity<?> review(@RequestBody ReviewRequest request, @RequestHeader("authorization") String token) {
        logger.debug("Review: " + request.toString());
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.review(request, token));
    }

    @ApiOperation(value = "获取审核的信息", response = ResponseWrapper.class)
    @GetMapping("/meeting/alreadyReviewedInfo")
    public ResponseEntity<?> getAlreadyReviewedInfo(String pcMemberName,String articleId, @RequestHeader("authorization") String token) {
        logger.debug("Get Review information: " + articleId + " Reviewer: " + pcMemberName);
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.getAlreadyReviewedInfo(pcMemberName,articleId, token));
    }

    @ApiOperation(value = "开始审核文章", response = ResponseWrapper.class)
    @PostMapping("/meeting/beginReview")
    public ResponseEntity<?> beginReview(@RequestBody BeginReviewRequest request, @RequestHeader("authorization") String token) {
        logger.debug("Begin Review: " + request.toString());
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.beginReview(request, token));
    }

    @ApiOperation(value = "将会议状态设置为publish", response = ResponseWrapper.class)
    @PostMapping("/meeting/publish")
    public ResponseEntity<?> reviewPublish(@RequestBody ResultPublishRequest request, @RequestHeader("authorization") String token) {
        logger.debug("Review Request to Publish: " + request.toString());
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.reviewPublish(request, token));
    }
}
