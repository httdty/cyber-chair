package adminmeeting.adminmeeting.api;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.domain.PostMessage;
import adminmeeting.utility.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

public class PostApi {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RemoteServiceConfig remote;

    public PostMessage save(PostMessage postMessage, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("postMessage", postMessage);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, httpHeaders);
        restTemplate.exchange(
                remote.getSavePostMessage(),
                HttpMethod.POST,
                entity,
                PostMessage.class);
        return postMessage;
    }


    public List<PostMessage> findByArticleIdAndStatus(Long articleId, String status, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("articleId", articleId.toString());
        params.add("status", status);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<List<PostMessage>> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindPostMessageByArticleIdAndStatus()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<PostMessage>>() {
                });
        List<PostMessage> postMessageList = Objects.requireNonNull(resp.getBody());
        return postMessageList;
    }

    public PostMessage findById(Long id, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", id.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<PostMessage> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindPostMessageById()),
                HttpMethod.GET,
                entity,
                PostMessage.class);
        PostMessage postMessage = resp.getBody();
        return postMessage;
    }


}
