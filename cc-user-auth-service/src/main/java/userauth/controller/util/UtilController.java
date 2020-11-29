package userauth.controller.util;

import userauth.domain.User;
import userauth.request.util.LoginRequest;
import userauth.request.util.RegisterRequest;
import userauth.service.Service;
import userauth.utility.response.ResponseGenerator;
import userauth.utility.response.ResponseWrapper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UtilController {

    Logger logger = LoggerFactory.getLogger(UtilController.class);
    private Service service;

    @Autowired
    public UtilController(Service service) {
        this.service = service;
    }

    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        Map<String, String> response = new HashMap<>();
        String message = "Welcome to 2020 Software Engineering Lab2";
        response.put("message", message);
        return ResponseEntity.ok(response);
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.debug("RegistrationForm: " + request.toString());

        return ResponseEntity.ok(service.register(request));
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        logger.debug("LoginForm: " + request.toString());

        return ResponseEntity.ok(service.login(request));
    }

    @ApiOperation(value = "根据username返回用户", response = ResponseWrapper.class)
    @GetMapping("/user/userinfo")
    public ResponseEntity<?> getUserinfo(String username) {
        logger.debug("Get user info: " + username);
        return ResponseEntity.ok(service.getUserinfo(username));
    }

    @ApiOperation(value = "根据fullname查找用户", response = ResponseWrapper.class)
    @GetMapping("/util/users")
    public ResponseEntity<?> searchUsersbyFullname(String fullname) {
        logger.debug("Users with fullname " + fullname + " : ");
        return ResponseEntity.ok(service.searchUsersbyFullname(fullname));
    }

    @ApiOperation("检查登陆状态")
    @GetMapping("/check")
    public ResponseEntity<?> check() {
        return ResponseEntity.ok(new ResponseWrapper<>(200, ResponseGenerator.success, null));
    }
}
