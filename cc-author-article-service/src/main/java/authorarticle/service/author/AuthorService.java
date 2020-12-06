package authorarticle.service.author;

import authorarticle.config.RemoteServiceConfig;
import authorarticle.domain.*;
import authorarticle.exception.InternalServerError;
import authorarticle.exception.MeetingUnavaliableToOperateException;
import authorarticle.exception.UserNamedidntExistException;
import authorarticle.exception.user.ArticleNotFoundException;
import authorarticle.repository.ArticleRepository;
import authorarticle.request.ArticleRequest;
import authorarticle.response.MeetingResponse;
import authorarticle.response.ReviewRelationResponse;
import authorarticle.response.UserResponse;
import authorarticle.utility.ApiUtil;
import authorarticle.utility.Pair;
import authorarticle.utility.contract.ArticleStatus;
import authorarticle.utility.contract.MeetingStatus;
import authorarticle.utility.response.ResponseGenerator;
import authorarticle.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * All author's service are in here
 *
 * @author Ray
 * @version 1.0
 * @projectName lab2
 * @date 2020/11/06 20:56
 * @email httdty2@163.com
 * @software IntelliJ IDEA
 */

@Service
public class AuthorService {
    private ArticleRepository articleRepository;
    private RestTemplate restTemplate;
    private ApiUtil apiUtil;
    private RemoteServiceConfig api;

    @Autowired
    public AuthorService(ArticleRepository articleRepository, RestTemplate restTemplate, ApiUtil apiUtil, RemoteServiceConfig api) {
        this.articleRepository = articleRepository;
        this.restTemplate = restTemplate;
        this.apiUtil = apiUtil;
        this.api = api;
    }

    @Transactional
    public ResponseWrapper<?> authorMeeting(String username){
        List<Article> articleList = articleRepository.findByContributorName(username);
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        Set<String>meetingCount = new HashSet<>();
//        TODO: There should be more efficient by change it into list, or finished by meeting
        for(Article article : articleList){
            // FINISHED: change to REST
             MeetingResponse meetingResponse = this.findMeetingByMeetingName(article.getMeetingname());

            if(!meetingCount.contains(meetingResponse.getMeetingName())){
                meetingCount.add(meetingResponse.getMeetingName());
                response.add(ResponseGenerator.generate(meetingResponse,
                        new String[]{"meetingName", "acronym", "submissionDeadlineDate", "topic"}, null));
            }
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Transactional
    public ResponseWrapper<?> getSubmissionList(String authorName, String meetingName) {
//        FINISHED: dont need to check the meetingName
//        Meeting meeting = meetingRepository.findByMeetingName(meetingName);
//        if (meeting == null) {
//            throw new MeetingOfNoExistenceException(meetingName);
//        }//会议是否存在
        List<Article> articles = articleRepository.findByContributorNameAndMeetingName(authorName,meetingName);
        return ResponseGenerator.injectObjectFromListToResponse("articles",articles,new String[]{"id","title","topic","status"},null);
    }

    @Transactional
    public ResponseWrapper<?> getArticleDetail(String articleId) {
        Article article = articleRepository.findById(Long.parseLong(articleId));
        if (article == null) {
            throw new ArticleNotFoundException(articleId);
        }
        HashMap<String, Object> returnMap = ResponseGenerator.generate(
                article,
                new String[]{"contributorName", "meetingName", "submitDate",
                        "title", "articleAbstract", "filePath", "status"}, null
        );
        if (returnMap == null)
            throw new InternalServerError("in Article Service, in getArticleDetail");
        Set<Author> returnAuthors = new HashSet<>();
        for(Pair<Author, Integer> p: article.getAuthors()){
            returnAuthors.add(p.getKey());
        }
        returnMap.put("authors", returnAuthors);
        returnMap.put("topic", article.getTopic());

        HashMap<String, HashMap<String, Object>> body = new HashMap<>();
        body.put("articleDetail",returnMap);

        return new ResponseWrapper<>(200, ResponseGenerator.success, body);

    }

    @Transactional
    public ResponseWrapper<?> uploadNewArticle(ArticleRequest request, String targetRootDir) {
        String meetingName = request.getMeetingName();
        String username = request.getUsername();

//        FINISHED: need to be changed into rest

        MeetingResponse meetingResponse = this.findMeetingByMeetingName(meetingName);
//        Meeting fakemeeting = new Meeting();
//        fakemeeting.setStatus(MeetingStatus.submissionAvaliable);
//        fakemeeting.setAcronym("sdasd");
//        fakemeeting.setChairName("chair");
//        fakemeeting.setCity("shanghai");
//        fakemeeting.setConferenceDate("2020-11-20");
//        fakemeeting.setOrganizer("baidu");
//        fakemeeting.setMeetingName("fake meeting");
//        fakemeeting.setRegion("china");
        UserResponse articleUploader = this.findUserByUsername(username);

        //guarantee that this operation is valid
        authenticateArticle(meetingResponse, articleUploader);

        MultipartFile pdfFile = request.getFile();
        //save the file, if exceptions happens, throw new InternalServerError
        String internalFilePath = targetRootDir +
                articleUploader.getUsername() + File.separator +
                request.getSubmitDate() + File.separator;
        try {
            saveFileToServer(pdfFile, internalFilePath);
        } catch (IOException ex) {
            throw new InternalServerError("UserArticleService.uploadNewArticle(): error occurred when saving the article pdf");
        }

        Set<String> clearTopics = getClearedTopics(request.getTopics());

        Article newArticle = new Article(
                request.getUsername(),
                request.getMeetingName(),
                request.getSubmitDate(),
                request.getEssayTitle(),
                request.getEssayAbstract(),
                internalFilePath + pdfFile.getOriginalFilename(),
                ArticleStatus.queuing,
                clearTopics,
                request.getAuthors()
        );
        articleRepository.save(newArticle);

        return new ResponseWrapper<>(200, ResponseGenerator.success, new HashMap<>());
    }

    @Transactional
    public ResponseWrapper<?> updateExistedArticle(String articleId, ArticleRequest request, String targetRootDir) {
        Article article = articleRepository.findById(Long.parseLong(articleId));
        if (article == null)
            throw new ArticleNotFoundException(articleId);

        String meetingName = request.getMeetingName();
        String username = request.getUsername();

//        FINISHED: need to be changed into rest
        MeetingResponse meetingResponse = this.findMeetingByMeetingName(meetingName);
        UserResponse userResponse = this.findUserByUsername(username);

        authenticateArticle(meetingResponse, userResponse);
        if(request.getFile() != null) {

            //delete the previous pdf file
            String previousPdfPath = article.getFilePath();
            File file = new File(previousPdfPath);
            if (file.exists()) {
                if (!file.delete())
                    throw new InternalServerError("UserArticleService.updateExistedArticle(): file delete failed");
            }
            else {
                throw new InternalServerError("UserArticleService.updateExistedArticle(): previous pdf doesn't exist");
            }
            String internalFilePath = targetRootDir +
                    userResponse.getUsername() + File.separator +
                    request.getSubmitDate() + File.separator;

            MultipartFile pdfFile = request.getFile();

            try {
                saveFileToServer(pdfFile, internalFilePath);
            } catch (IOException ex) {
                throw new InternalServerError("UserArticleService.uploadNewArticle(): error occurred when saving the article pdf");
            }
            article.setFilePath(internalFilePath + pdfFile.getOriginalFilename());
        }
        Set<String> clearTopics = getClearedTopics(request.getTopics());


        //then update all the information of the previous article
        article.setMeetingname(request.getMeetingName());
        article.setContributorName(request.getUsername());
        article.setTitle(request.getEssayTitle());
        article.setArticleAbstract(request.getEssayAbstract());
        article.setSubmitDate(request.getSubmitDate());

        article.setTopic(clearTopics);
        article.setAuthors(request.getAuthors());

        articleRepository.save(article);
        //after all the update, return the success message

        return new ResponseWrapper<>(200, ResponseGenerator.success, new HashMap<>());
    }

    @Transactional
    public ResponseWrapper<?> getAllReviews(String articleId){
        Article article = articleRepository.findById(Long.parseLong(articleId));
        if(article == null)
            throw new ArticleNotFoundException(articleId);
//        FINISHED: should to be changed into rest
        Set<ReviewRelationResponse> allReviews = this.findReviewRelationsByArticleId(articleId);

        HashMap<String, Set<HashMap<String, Object>>> respBody = new HashMap<>();
        Set<HashMap<String, Object>> reviews = new HashSet<>();
        for(ReviewRelationResponse relation: allReviews){
            HashMap<String, Object> items = new HashMap<>();
            items.put("score", relation.getScore());
            items.put("confidence", relation.getConfidence());
            items.put("review", relation.getReviews());

            reviews.add(items);
        }
        respBody.put("reviews", reviews);
        return new ResponseWrapper<>(200, ResponseGenerator.success, respBody);

    }

//        Template
//    1. 找到controller中的接口
//    2. 定位service中的函数名称
//    3. 找到对应函数
//    4. copy到author中
//    5. 修改代码
//    6. 修改service中的调用
//    7. 修改controller中的接口url


    //Before use this internal method
    //please Guarantee that file is savable (not null)
    private void saveFileToServer(MultipartFile file, String rootDirPath)
            throws IOException {

        byte[] fileBytes = file.getBytes();
        Path restorePath = Paths.get(rootDirPath + file.getOriginalFilename());

        //如果没有rootDirPath文件夹，则创建
        if (!Files.isWritable(restorePath)) {
            Files.createDirectories(Paths.get(rootDirPath));
        }

        Files.write(restorePath, fileBytes);
    }

    //this function is used to guarantee a article can be upload or update
    private void authenticateArticle(MeetingResponse meetingResponse, UserResponse userResponse) {
        if (meetingResponse == null)
            throw new MeetingUnavaliableToOperateException("Not created");
        if (userResponse == null)
            throw new UserNamedidntExistException("not a valid user");

        if (!meetingResponse.getStatus().equals(MeetingStatus.submissionAvaliable))
            throw new MeetingUnavaliableToOperateException("update or upload articles");
    }

    private Set<String> getClearedTopics(Set<String> topic){
        Set<String> clearTopics = new HashSet<>();

        for(String t: topic){
            t = t.replaceAll("\"", "");
            t = t.replace("[", "");
            t = t.replace("]", "");
            clearTopics.add(t);
        }

        return clearTopics;

    }


    // sealed all data access into functions
    // make rest api just like local function
    private MeetingResponse findMeetingByMeetingName(String meetingName) {
        MultiValueMap<String, String> paramsMeeting = new LinkedMultiValueMap<>();
        paramsMeeting.add("meetingName", meetingName);
        ResponseEntity<MeetingResponse> res = restTemplate.exchange(
                apiUtil.encodeUriForGet(paramsMeeting, api.getFindMeetingByMeetingName()),
                HttpMethod.GET,
                null,
                MeetingResponse.class
        );
        return Objects.requireNonNull(res.getBody());
    }

    private UserResponse findUserByUsername(String username) {
        System.out.println(username);
        MultiValueMap<String, String> paramsUser = new LinkedMultiValueMap<>();
        paramsUser.add("username", username);
        ResponseEntity<UserResponse> resUser = restTemplate.exchange(
                apiUtil.encodeUriForGet(paramsUser, api.getFindUserByUsername()),
                HttpMethod.GET,
                null,
                UserResponse.class
        );
        return Objects.requireNonNull(resUser.getBody());
    }

    private Set<ReviewRelationResponse> findReviewRelationsByArticleId(String articleId) {
        MultiValueMap<String, String> paramsReview = new LinkedMultiValueMap<>();
        paramsReview.add("articleId", articleId);
        ResponseEntity<Set<ReviewRelationResponse>> resUser = restTemplate.exchange(
                apiUtil.encodeUriForGet(paramsReview, api.getFindReviewRelationsByArticleId()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Set<ReviewRelationResponse>>(){}
        );
        return Objects.requireNonNull(resUser.getBody());
    }
}