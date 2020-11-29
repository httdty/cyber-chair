package userauth.controller.user;

import userauth.domain.User;
import userauth.request.user.InvitationRepoRequest;
import userauth.service.Service;
import userauth.utility.response.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserInvitationController {
    Logger logger = LoggerFactory.getLogger(UserInvitationController.class);
    private Service service;

    @Autowired
    UserInvitationController(Service service) {
        this.service = service;
    }

    @ApiOperation(value = "根据username来找用户下的未处理的通知", response = ResponseWrapper.class)
    @GetMapping("/user/undealedNotifications")
    public ResponseEntity<?> undealedNotifications(String username) {
        logger.debug("Get undealedNotifications info : " + username);
        return ResponseEntity.ok(service.undealedNotifications(username));
    }

    @ApiOperation(value = "回复邀请", response = ResponseWrapper.class)
    @PostMapping("/user/invitationRepo")
    public ResponseEntity<?> invitationRepo(@RequestBody JSONObject request) {
        logger.info("Post invitationRepo info : " + request.toString());
        // Set<String> 并不能直接接受JSONArray对象，需要通过List来中转
        JSONArray temp = request.getJSONArray("topic");
        List<String> tempList = temp.toJavaList(String.class);
        InvitationRepoRequest req = request.toJavaObject(InvitationRepoRequest.class);
        req.setTopics(new HashSet<>(tempList));
        logger.info("req: " + req);
        return ResponseEntity.ok(service.invitationRepo(req));
    }

    @ApiOperation(value = "根据username获取用户未处理的通知的数量", response = ResponseWrapper.class)
    @GetMapping("/user/undealedNotificationsNum")
    public ResponseEntity<?> undealedNotificationsNum(String username) {
        logger.debug("Get undealedNotificationsNum info : " + username);
        return ResponseEntity.ok(service.undealedNotificationsNum(username));
    }

    @ApiOperation(value = "根据username获取用户已经处理的通知", response = ResponseWrapper.class)
    @GetMapping("/user/alreadyDealedNotifications")
    public ResponseEntity<?> alreadyDealedNotifications(String username) {
        logger.debug("Get alreadyDealedNotifications info : " + username);
        return ResponseEntity.ok(service.alreadyDealedNotifications(username));
    }

}
