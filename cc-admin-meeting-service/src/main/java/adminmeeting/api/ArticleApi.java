package adminmeeting.api;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.domain.Article;
import adminmeeting.utility.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class ArticleApi {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RemoteServiceConfig remote;


    public Article findById(Long id, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("articleId", id.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<Article> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindArticleById()),
                HttpMethod.GET,
                entity,
                Article.class);
        Article article = resp.getBody();
        return article;
    }

    public List<Article> findByMeetingNameAndStatus(String meetingName,String status, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("meetingName", meetingName);
        params.add("status", status);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<List<Article>> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindArticleByMeetingNameAndStatus()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Article>>() {
                });
        List<Article> articleList = Objects.requireNonNull(resp.getBody());
        return articleList;
    }

    public Article save(Article article, String token){
//        HttpHeaders httpHeaders = new HttpHeaders();
////        httpHeaders.set("authorization", token);
//        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//        params.add("article", article);
//        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, httpHeaders);
//        restTemplate.exchange(
//                remote.getSaveArticle(),
//                HttpMethod.PUT,
//                entity,
//                Article.class);
//        return article;
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Article> entity = new HttpEntity<>(article, httpHeaders);
        String answer = restTemplate.postForObject(remote.getSaveArticle(), entity, String.class );
        return article;
    }



    public List<Article> findByIdNot(Long id, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("articleId", id.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<List<Article>> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindArticleByIdNot()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Article>>() {
                });
        List<Article> articleList = Objects.requireNonNull(resp.getBody());
        return articleList;
    }


    public List<Article> findByContributorNameAndMeetingName(String contributeName,String meetingName, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("contributeName", contributeName);
        params.add("meetingName", meetingName);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<List<Article>> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindArticleByContributorNameAndMeetingName()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Article>>() {
                });
        List<Article> articleList = Objects.requireNonNull(resp.getBody());
        return articleList;
    }


}
