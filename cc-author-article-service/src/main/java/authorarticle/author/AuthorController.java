package authorarticle.author;

import authorarticle.config.RemoteServiceConfig;
import authorarticle.domain.Author;
import authorarticle.request.user.ArticleRequest;
import authorarticle.service.Service;
import authorarticle.utility.response.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * TODO write your description here
 *
 * @author Ray
 * @version 1.0
 * @projectName lab2
 * @date 2020/11/06 20:56
 * @email httdty2@163.com
 * @software IntelliJ IDEA
 */

@Api(value="AuthorController",tags={"Author接口"})
@RestController
public class AuthorController {

    Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private final Service service;
    private final RestTemplate restTemplate;
    private final RemoteServiceConfig remote;

    @Autowired
    public AuthorController(Service service, RestTemplate restTemplate, RemoteServiceConfig remote) {
        this.service = service;
        this.restTemplate = restTemplate;
        this.remote = remote;
    }


//    @GetMapping("/author/easySubmission")
////    TODO: Don't finished
//    public ResponseEntity<?> easySubmission() {
//        logger.debug("Get queuing applications by admin");
//        return ResponseEntity.ok(service.getqueueingApplication());
//    }
//
//    @GetMapping("/author/getMeetingList")
////    TODO: Don't finished
//    public ResponseEntity<?> getMeetingList() {
//        logger.debug("Get dealed applications by admin");
//        return ResponseEntity.ok(service.getalreadyApplication());
//    }
//
//    @PostMapping("/author/getArticleList")
////    TODO: Don't finished
//    public ResponseEntity<?> getArticleList(@RequestBody ApplicationRatifyRequest request) {
//        logger.debug("Ratification for Meeting named "+ request.getMeetingName());
//        return ResponseEntity.ok(service.applicationRatify(request));
//    }

    @ApiOperation(value = "获取一个author投稿过的所有的meeting", response = ResponseWrapper.class)
    @GetMapping("/user/authorMeeting")
    public ResponseEntity<?> authorMeeting(String username, @RequestHeader("authorization") String token) {
        logger.debug("Get author meeting info : " + username);
        if (checkUser(token)) {
            return ResponseEntity.ok(service.authorMeeting(username));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(value = "获取一个author在一个meeting上的所有提交的文章", response = ResponseWrapper.class)
    @GetMapping("/meeting/submissionList")
    public ResponseEntity<?> getSubmissionList(String authorName, String meetingName, @RequestHeader("authorization") String token) {
        logger.debug("Submission List");
        if (checkUser(token)) {
            return ResponseEntity.ok(service.getSubmissionList(authorName, meetingName));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(value = "获取一篇文章的详细信息", response = ResponseWrapper.class)
    @GetMapping("/user/articleDetail")
    public ResponseEntity<?> getArticleDetail(String articleId, @RequestHeader("authorization") String token) {
        logger.debug("article detail get request received, article id = " + articleId);
        if (checkUser(token)) {
            return ResponseEntity.ok(service.getArticleDetail(articleId));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    //user submit a new article for a meeting
    @ApiOperation(value = "提交一个文章", response = ResponseWrapper.class)
    @PostMapping("/user/articleSubmission")
    public ResponseEntity<?> submitNewArticle(
            @RequestParam("meetingName") String meetingName,
            @RequestParam("username") String username,
            @RequestParam("essayTitle") String essayTitle,
            @RequestParam("essayAbstract") String essayAbstract,
            @RequestParam("submitTime") String submitTime,
            @RequestParam("topic") Set<String> topic,
            @RequestParam("authors") String authors,
            @RequestParam("essayPDF") MultipartFile pdfFile,
            HttpServletRequest servletRequest,
            @RequestHeader("authorization") String token

    ) {
        if (checkUser(token)) {
            Set<Pair<Author, Integer>> authorArgument = generateAuthor(authors);

            ArticleRequest request = new ArticleRequest(
                    meetingName, username, essayTitle, essayAbstract,
                    submitTime, pdfFile, topic, authorArgument
            );
            String parentDir = servletRequest.getServletContext().getRealPath("src/resources/");
            return ResponseEntity.ok(service.submitNewArticle(request, parentDir));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    //user update an existing paper
    @ApiOperation(value = "更新文章信息", response = ResponseWrapper.class)
    @PostMapping("/user/updateArticle")
    public ResponseEntity<?> updateArticle(
            @RequestParam("articleId") String articleId,
            @RequestParam("meetingName") String meetingName,
            @RequestParam("username") String username,
            @RequestParam("essayTitle") String essayTitle,
            @RequestParam("essayAbstract") String essayAbstract,
            @RequestParam("submitTime") String submitTime,
            @RequestParam("topic") Set<String> topic,
            @RequestParam("authors") String authors,
            @RequestParam(value = "essayPDF", required = false) MultipartFile pdfFile,
            HttpServletRequest servletRequest,
            @RequestHeader("authorization") String token
    ) {
        if (checkUser(token)) {

            Set<Pair<Author, Integer>> authorArgument = generateAuthor(authors);

            ArticleRequest request = new ArticleRequest(
                    meetingName, username, essayTitle, essayAbstract,
                    submitTime, pdfFile, topic, authorArgument
            );


            String parentDir = servletRequest.getServletContext().getRealPath("src/resources/static/");
            return ResponseEntity.ok(service.updateArticle(articleId, request, parentDir));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


//    @ApiOperation(value = "查看文章", response = ResponseWrapper.class)
//    @GetMapping("/user/reviews")
//    public ResponseEntity<?> getAllReviewsOfArticle(String articleId, @RequestHeader("authorization") String token) {
//        if (checkUser(token)) {
//            return ResponseEntity.ok(service.getReviewsOfArticle(articleId));
//        } else {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    private Set<Pair<Author, Integer>> generateAuthor(String authors) {
        List<Author> authorsList = JSONArray.parseArray(authors, Author.class);
        Set<Pair<Author, Integer>> authorArgument = new HashSet<>();
        int rank = 1;
        for (Author author : authorsList) {
            authorArgument.add(new Pair<>(author, rank++));
        }
        return authorArgument;
    }

    private boolean checkUser(String token) {
        String checkApi = remote.getCheck();
        //构造请求头，加入token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        //发送check请求到user-auth服务中
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        System.out.println("check login end");
        return resp.getStatusCode() == HttpStatus.OK;
    }

//    @PostMapping("/meeting/beginSubmission")
//    public ResponseEntity<?> beginSubmission(@RequestBody BeginSubmissionRequest request) {
//        logger.debug("Begin Submission: " + request.toString());
//        return ResponseEntity.ok(service.beginSubmission(request));
//    }
//
//    @GetMapping("/meeting/reviewArticles")
//    public ResponseEntity<?> getInfoOfReview(String pcMemberName,String meetingName) {
//        logger.debug("Get review information: " + meetingName + " " + pcMemberName);
//        return ResponseEntity.ok(service.getInfoOfReview(pcMemberName,meetingName));
//    }
//
//    @GetMapping("/meeting/reviewArticle")
//    public ResponseEntity<?> getInfoOfArticleToReview(String pcMemberName,String articleId) {
//        logger.debug("Get Article information: " + articleId + " Reviewer: " + pcMemberName);
//        return ResponseEntity.ok(service.getInfoOfArticleToReview(pcMemberName,articleId));
//    }
//
//    @PostMapping("/meeting/reviewer")
//    public ResponseEntity<?> review(@RequestBody ReviewRequest request) {
//        logger.debug("Review: " + request.toString());
//        return ResponseEntity.ok(service.review(request));
//    }
//
//    @GetMapping("/meeting/alreadyReviewedInfo")
//    public ResponseEntity<?> getAlreadyReviewedInfo(String pcMemberName,String articleId) {
//        logger.debug("Get Review information: " + articleId + " Reviewer: " + pcMemberName);
//        return ResponseEntity.ok(service.getAlreadyReviewedInfo(pcMemberName,articleId));
//    }
//
//    @PostMapping("/meeting/beginReview")
//    public ResponseEntity<?> beginReview(@RequestBody BeginReviewRequest request) {
//        logger.debug("Begin Review: " + request.toString());
//        return ResponseEntity.ok(service.beginReview(request));
//    }
//
//    @PostMapping("/meeting/publish")
//    public ResponseEntity<?> reviewPublish(@RequestBody ResultPublishRequest request) {
//        logger.debug("Review Request to Publish: " + request.toString());
//        return ResponseEntity.ok(service.reviewPublish(request));
//    }
//


}