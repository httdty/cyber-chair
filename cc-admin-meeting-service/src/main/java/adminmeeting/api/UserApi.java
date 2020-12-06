package adminmeeting.api;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.domain.User;
import adminmeeting.utility.ApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Component
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
        System.out.println("url" + remote.getFindUserByName());
        System.out.println("encode url" + apiUtil.encodeUriForGet(params, remote.getFindUserByName()));
        ResponseEntity<User> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindUserByName()),
                HttpMethod.GET,
                null,
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
