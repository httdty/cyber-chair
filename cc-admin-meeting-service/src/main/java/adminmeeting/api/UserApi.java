package adminmeeting.api;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.domain.Article;
import adminmeeting.domain.User;
import adminmeeting.utility.ApiUtil;
import adminmeeting.utility.response.ResponseGenerator;
import adminmeeting.utility.response.ResponseWrapper;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

public class UserApi {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RemoteServiceConfig remote;

    public User findByUsername(String name, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        // httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", name);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<User> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindUserByName()),
                HttpMethod.GET,
                entity,
                User.class);
        User user = resp.getBody();
        return user;
    }

    public User findByEmail(String email, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        // httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", email);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<User> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindUserByEmail()),
                HttpMethod.GET,
                entity,
                User.class);
        User user = resp.getBody();
        return user;
    }

    public User findById(Long id, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        // httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", id.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<User> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindUserById()),
                HttpMethod.GET,
                entity,
                User.class);
        User user = resp.getBody();
        return user;
    }

    public User findByFullnameAndEmail(String fullname,String email, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        // httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("fullname", fullname);
        params.add("email", email);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<User> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindUserByFullnameAndEmail()),
                HttpMethod.GET,
                entity,
                User.class);
        User user = resp.getBody();
        return user;
    }












}
