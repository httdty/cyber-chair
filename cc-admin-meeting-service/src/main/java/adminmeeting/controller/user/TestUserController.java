package adminmeeting.controller.user;//package adminmeeting.controller.user;
//
//import adminmeeting.domain.Article;
//import adminmeeting.repository.ArticleRepository;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import adminmeeting.config.RemoteServiceConfig;
//import adminmeeting.controller.util.UtilController;
//import adminmeeting.domain.Author;
//import adminmeeting.domain.User;
//import adminmeeting.repository.UserRepository;
//import adminmeeting.request.user.ArticleDetailRequest;
//import adminmeeting.request.user.ArticleRequest;
//import adminmeeting.service.Service;
//import adminmeeting.service.user.UserArticleService;
//import adminmeeting.utility.ApiUtil;
//import adminmeeting.utility.response.ResponseGenerator;
//import adminmeeting.utility.response.ResponseWrapper;
//import com.alibaba.fastjson.JSONArray;
//import javafx.util.Pair;
//
//import org.apache.catalina.filters.AddDefaultCharsetFilter;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.json.GsonJsonParser;
//import org.springframework.context.annotation.Bean;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.*;
//
//@RestController
//public class TestUserController {
//
//    private Service service;
//    Logger logger = LoggerFactory.getLogger(TestUserController.class);
//
//    @Autowired
//    private RemoteServiceConfig remote;
//
//    @Autowired
//    public TestUserController(Service service){
//        this.service = service;
//    }
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//
//
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//
//
//    //get the detailed information about a article
//    @GetMapping("/user/username")
//    public ResponseEntity<?> username(String username){
//        logger.debug("user detail get request received, username = " + username);
//        User user = userRepository.findByUsername(username);
//        return ResponseEntity.ok(
//                ResponseGenerator.injectObjectFromObjectToResponse("user",user,new String[]{"id","username","fullname","password","email","institution","region"},null)
//
//        );
//    }
//
//    @GetMapping("/article/notId")
//    public ResponseEntity<?> notId(String id){
//        Long longId = Long.valueOf(id);
//        List<Article> articles = articleRepository.findByIdNot(longId);
//        Article a1 = new Article("a", "a", "a", "a", "a", "a", "a",null, null);
//        a1.setId(1L);
//        Article a2 = new Article("b", "b", "b", "b", "b", "b", "b",null, null);
//        a2.setId(2L);
//        articles.add(a1);
//        articles.add(a2);
//        return ResponseEntity.ok(
//                ResponseGenerator.injectObjectFromListToResponse("articles",articles,new String[]{"id","title","topic","status"},null)
//        );
//    }
//
//    @GetMapping("/meeting/getArticleByIdNot")
//    public ResponseEntity<?> getArticleByIdNot(Long id){
//        HttpHeaders httpHeaders = new HttpHeaders();
////        httpHeaders.set("authorization", token);
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("id", String.valueOf(id));
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
//        ApiUtil apiUtil = new ApiUtil();
//        String url = apiUtil.encodeUriForGet(params, "http://127.0.0.1:8080/article/notId");
//        ResponseEntity<ResponseWrapper> resp = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                entity,
//                ResponseWrapper.class);
//
//        ResponseWrapper<LinkedHashMap> response = resp.getBody();
//        LinkedHashMap<String, LinkedHashMap> body = response.getResponseBody();
//        String jsonString = JSONObject.toJSONString(body.get("articles"));
//        List<Article> articles = JSONArray.parseArray(jsonString, Article.class);
//        return ResponseEntity.ok(
//                ResponseGenerator.injectObjectFromListToResponse("articles",articles,new String[]{"id","title","topic","status"},null)
//        );
//    }
//
//    @GetMapping("/meeting/getUserByName")
//    public ResponseEntity<?> getUserByName(String username){
//        HttpHeaders httpHeaders = new HttpHeaders();
////        httpHeaders.set("authorization", token);
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("username", username);
//        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
//        ApiUtil apiUtil = new ApiUtil();
//        String url = apiUtil.encodeUriForGet(params, remote.getFindUserByName());
//        ResponseEntity<ResponseWrapper> resp = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                entity,
//                ResponseWrapper.class);
//
//        ResponseWrapper<LinkedHashMap> response = resp.getBody();
//        LinkedHashMap<String, LinkedHashMap> body = response.getResponseBody();
//        String jsonString = JSONObject.toJSONString(body.get("user"));
//        User user = JSONObject.parseObject(jsonString, User.class);
//        return ResponseEntity.ok(
//                ResponseGenerator.injectObjectFromObjectToResponse("get_user",user,new String[]{"id","username","fullname","password","email","institution","region"},null)
//
//        );
//    }
//
//
//
//}