package authorarticle.author;

import authorarticle.domain.Article;
import authorarticle.service.Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for repository
 * @author      Ray
 * @version     1.0
 * @projectName lab2
 * @date        2020/11/06 20:56
 * @email       httdty2@163.com
 * @software    IntelliJ IDEA
 */

@Api(value="ArticleController",tags={"Article接口"})
@RestController
public class ArticleController {

    Logger logger = LoggerFactory.getLogger(ArticleController.class);
    private Service service;

    @Autowired
    public ArticleController(Service service){this.service = service;}

    @ApiOperation(value = "根据文章ID获取文章", notes = "根据文章id获取文章详细信息", response = Article.class)
    @GetMapping("/article/findArticleById")
    public ResponseEntity<?> findArticleById(String articleId){
        logger.info("Article repository accessed "+articleId);
        return  ResponseEntity.ok(service.findArticleById(articleId));
    }

    @ApiOperation(value = "获取不等于此文章ID的文章", response = Article.class, responseContainer = "List" )
    @GetMapping("/article/findArticleByIdNot")
    public ResponseEntity<?> findArticleByIdNot(String articleId){
        logger.debug("Article repository accessed "+articleId);
        return  ResponseEntity.ok(service.findArticleByIdNot(articleId));
    }

    @ApiOperation(value = "获取对应contributor的所有文章", response = Article.class, responseContainer = "List" )
    @GetMapping("/article/findArticleByContributorName")
    public ResponseEntity<?> findArticleByContributorName(String contributorName){
        logger.debug("Article repository accessed "+contributorName);
        return  ResponseEntity.ok(service.findArticleByContributorName(contributorName));
    }

    @ApiOperation(value = "获取对应meetingName的所有文章", response = Article.class, responseContainer = "List" )
    @GetMapping("/article/findArticleByMeetingName")
    public ResponseEntity<?> findArticleByMeetingName(String meetingName){
        logger.debug("Article repository accessed "+meetingName);
        return  ResponseEntity.ok(service.findArticleByMeetingName(meetingName));
    }

    @ApiOperation(value = "获取对应meetingName和status的所有文章", response = Article.class, responseContainer = "List" )
    @GetMapping("/article/findArticleByMeetingNameAndStatus")
    public ResponseEntity<?> findArticleByMeetingNameAndStatus(String meetingName,String status){
        logger.debug("Article repository accessed "+meetingName+status);
        return  ResponseEntity.ok(service.findArticleByMeetingNameAndStatus(meetingName, status));
    }

    @ApiOperation(value = "获取对应contributor和meeting的所有文章", response = Article.class, responseContainer = "List" )
    @GetMapping("/article/findArticleByContributorNameAndMeetingName")
    public ResponseEntity<?> findArticleByContributorNameAndMeetingName(String contributeName,String meetingName){
        logger.debug("Article repository accessed "+contributeName+meetingName);
        return ResponseEntity.ok(service.findArticleByContributorNameAndMeetingName(contributeName, meetingName));
    }

    @PutMapping("/article/save")
    public ResponseEntity<?> saveArticle(Article article) {
        logger.debug("Article repository accessed "+article.toString());
        return ResponseEntity.ok(service.saveArticle(article));
    }

    @PostMapping("/article/save")
    public ResponseEntity<?> saveArticle_post(Article article) {
        logger.debug("Article repository accessed "+article.toString());
        return ResponseEntity.ok(service.saveArticle(article));
    }

    @GetMapping("/utils/pdf")
    @ResponseBody
    public byte[] getImage(String pdfUrl)  {
        logger.debug("Get file for pdfUrl " + pdfUrl + " : ");
        return service.getPdfContent(pdfUrl);
    }
}