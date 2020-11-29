package adminmeeting.controller.admin;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.request.admin.ApplicationRatifyRequest;
import adminmeeting.service.Service;
import adminmeeting.utility.response.ResponseWrapper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class AdminController {

    Logger logger = LoggerFactory.getLogger(AdminController.class);
    private Service service;

    @Autowired
    private RemoteServiceConfig remote;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public AdminController(Service service){this.service = service;}


    @ApiOperation(value = "查找未处理的meeting申请", response = ResponseWrapper.class)
    @GetMapping("/admin/queueingApplication")
    public ResponseEntity<?> getqueueingApplication( @RequestHeader("authorization") String token) {
        logger.debug("Get queuing applications by admin");
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.getqueueingApplication(token));
    }

    @ApiOperation(value = "获取已经处理的meeting申请", response = ResponseWrapper.class)
    @GetMapping("/admin/alreadyApplication")
    public ResponseEntity<?> getalreadyApplication( @RequestHeader("authorization") String token) {
        logger.debug("Get dealed applications by admin");
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.getalreadyApplication(token));
    }

    @ApiOperation(value = "给会议打分", response = ResponseWrapper.class)
    @PostMapping("/admin/ratify")
    public ResponseEntity<?> applicationRatify(@RequestBody ApplicationRatifyRequest request,  @RequestHeader("authorization") String token) {
        logger.debug("Ratification for Meeting named "+ request.getMeetingName());
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        if(resp.getStatusCode()!= HttpStatus.OK){
            return ResponseEntity.badRequest().build();
        }// 检查结束
        return ResponseEntity.ok(service.applicationRatify(request, token));
    }


}
