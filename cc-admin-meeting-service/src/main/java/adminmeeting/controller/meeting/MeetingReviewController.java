package adminmeeting.controller.meeting;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.request.meeting.*;
import adminmeeting.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.ApiOperation;

@RestController
public class MeetingReviewController {
    Logger logger = LoggerFactory.getLogger(MeetingArticleController.class);

    private Service service;

    @Autowired
    private RemoteServiceConfig remote;

    @Autowired
    private RestTemplate restTemplate;


    @ApiOperation(value = "发送review文章的消息", response = ResponseEntity.class)
    @PostMapping("/meeting/reviewPost")
    public ResponseEntity<?> reviewPost(@RequestBody ReviewPostRequest request,@RequestHeader("authorization") String token) {
        logger.debug("Review post: " + request.toString());
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.reviewPost(request, token));
    }
    @ApiOperation(value = "获取当前文章对应的post消息", response = ResponseEntity.class)
    @GetMapping("/meeting/postList")
    public ResponseEntity<?> getPostList(String articleId,String postStatus, @RequestHeader("authorization") String token) {
        logger.debug("Get postList article: ID " + articleId +" Post Status: " + postStatus);
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.getPostList(articleId,postStatus,token));
    }
}
