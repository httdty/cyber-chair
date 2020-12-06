package adminmeeting.api;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.domain.PostMessage;
import adminmeeting.domain.Rebuttal;
import adminmeeting.utility.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
public class RebuttalApi {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RemoteServiceConfig remote;

    public Rebuttal save(Rebuttal rebuttal, String token){
//        HttpHeaders httpHeaders = new HttpHeaders();
//        //httpHeaders.set("authorization", token);
//        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
//        params.add("rebuttal", rebuttal);
//        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, httpHeaders);
//        restTemplate.exchange(
//                remote.getSaveRebuttal(),
//                HttpMethod.POST,
//                entity,
//                Rebuttal.class);
//        return rebuttal;
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<Rebuttal> entity = new HttpEntity<>(rebuttal, httpHeaders);
        String answer = restTemplate.postForObject(remote.getSaveRebuttal(), entity, String.class );
        return rebuttal;
    }

    public List<Rebuttal> findByArticleId(Long articleId, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("articleId", articleId.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<List<Rebuttal>> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindRebuttalByArticleId()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Rebuttal>>() {
                });
        List<Rebuttal> rebuttalList = Objects.requireNonNull(resp.getBody());
        return rebuttalList;
    }

    public List<Rebuttal> findByIdNot(Long id,  String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("idNot", id.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<List<Rebuttal>> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindRebuttalByIdNot()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Rebuttal>>() {
                });
        List<Rebuttal> rebuttalList = Objects.requireNonNull(resp.getBody());
        return rebuttalList;
    }
}
