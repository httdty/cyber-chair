package userauth.controller.user;

import userauth.service.Service;
import userauth.utility.response.ResponseWrapper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserMeetingController {

    Logger logger = LoggerFactory.getLogger(UserMeetingController.class);
    private Service service;

    @Autowired
    UserMeetingController(Service service) {
        this.service = service;
    }

    @ApiOperation(value = "获取chair下的meeting", notes = "根据username查找该用户是chair的meeting",
            response = ResponseWrapper.class)
    @GetMapping("/user/chairMeeting")
    public ResponseEntity<?> chairMeeting(String username) {
        logger.debug("Get chair meeting info: " + username);
        return ResponseEntity.ok(service.chairMeeting(username));
    }

    @ApiOperation(value = "获取pcMember下的meeting", notes = "根据username找该用户是pcmember", response = ResponseWrapper.class)
    @GetMapping("/user/pcMemberMeeting")
    public ResponseEntity<?> pcMemberMeeting(String username) {
        logger.debug("Get pcMemberMeeting info : " + username);
        return ResponseEntity.ok(service.pcMemberMeeting(username));
    }

    @ApiOperation(value = "获取author下的meeting", notes = "根据username找该用户是author", response = ResponseWrapper.class)
    @GetMapping("/user/authorMeeting")
    public ResponseEntity<?> authorMeeting(String username) {
        logger.debug("Get author meeting info : " + username);
        return ResponseEntity.ok(service.authorMeeting(username));
    }


    @ApiOperation(value = "返回可以投稿的meeting", notes = "chair不可以投稿自己的会议", response = ResponseWrapper.class)
    @GetMapping("/user/availableMeeting")
    public ResponseEntity<?> availableMeeting(String username) {
        logger.debug("Get available meeting info : " + username);
        return ResponseEntity.ok(service.availableMeeting(username));
    }
}
