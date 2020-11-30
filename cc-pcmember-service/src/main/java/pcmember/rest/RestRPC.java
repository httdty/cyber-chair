package pcmember.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pcmember.utility.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component //Component对类做依赖注入的注解,Bean是对方法
public class RestRPC {
    HttpHeaders headers=new HttpHeaders(); //save用header
    HttpHeaders checkHeaders=new HttpHeaders(); //auth用header
    @Autowired
    RestTemplate restTemplate;
//    private String user_findName="http://192.168.31.112:8080/user/username";
//    private String user_findId="http://192.168.31.112:8080/user/id";
//    private String user_findEmail="http://192.168.31.112:8080/user/email";
//    private String user_findFullAdEmail="http://192.168.31.112:8080/user/author";
//    private String meet_findName="http://192.168.31.214:8090/meeting/getByName";
//    private String meet_findId="http://192.168.31.214:8090/meeting/getById";
//    private String meet_save="http://192.168.31.214:8090/meeting/save";
//    private String article_findId="http://192.168.31.214:8080/article/findArticleById";
//    private String article_findIdNot="http://192.168.31.214:8080/article/findArticleByIdNot";
//    private String article_save="http://192.168.31.214:8080/article/save";
//    private String article_findNameAdSt="http://192.168.31.214:8080/article/findArticleByMeetingNameAndStatus";
//    private String pcmember_findIdAdSt="http://192.168.31.214:8080/meeting/pcMemberRelation/getByPcmemberIdAndStatus";
//    private String check_auth="http://http://192.168.31.112:8080/check";
    private String user_findName="http://cc-user-auth-service/user/username";
    private String user_findId="http://cc-user-auth-service/user/id";
    private String user_findEmail="http://lcc-user-auth-service/user/email";
    private String user_findFullAdEmail="http://cc-user-auth-service/user/author";
    private String meet_findName="http://cc-admin-meeting-service/meeting/getByName";
    private String meet_findId="http://cc-admin-meeting-service/meeting/getById";
    private String meet_save="http://cc-admin-meeting-service/meeting/save";
    private String article_findId="http://cc-author-article-service/article/findArticleById";
    private String article_findIdNot="http://cc-author-article-service/article/findArticleByIdNot";
    private String article_save="http://cc-author-article-service/article/save";
    private String article_findNameAdSt="http://cc-author-article-service/article/findArticleByMeetingNameAndStatus";
//    TODO: 下面的地址不知道对不对
    private String pcmember_findIdAdSt="http://cc-admin-meeting-service/meeting/pcMemberRelation/getByPcmemberIdAndStatus";
    private String check_auth="http://cc-user-auth-service/check";
    //=====================user=====================
    public User userFindByUsername(String userName){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("username",userName);
        ResponseEntity<User> resp = restTemplate.exchange(encodeUriForGet(params,user_findName), HttpMethod.GET,null,User.class);
        return resp.getBody();
    }
    public User userFindById(long id){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("id",String.valueOf(id));
        //HttpEntity entity = new HttpEntity<>(params,null); //header参数为null就是默认值,也可以使entity为null就是body也不需要
        ResponseEntity<User> resp = restTemplate.exchange(encodeUriForGet(params,user_findId), HttpMethod.GET,null,User.class);
        return resp.getBody();
    }
    public User userFindByEmail(String email){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("email",email);
        ResponseEntity<User> resp = restTemplate.exchange(encodeUriForGet(params,user_findEmail), HttpMethod.GET,null,User.class);
        return resp.getBody();
    }
    public User userFindByFullnameAndEmail(String fullName,String email){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("fullname",fullName);
        params.add("email",email);
        ResponseEntity<User> resp = restTemplate.exchange(encodeUriForGet(params,user_findFullAdEmail), HttpMethod.GET,null,User.class);
        return resp.getBody();
    }

    //=====================meeting=====================
    public Meeting meetingFindByMeetingName(String meetingName){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("meetingName",meetingName);
        //HttpEntity entity = new HttpEntity<>(params,headers);
        ResponseEntity<Meeting> resp = restTemplate.exchange(encodeUriForGet(params,meet_findName), HttpMethod.GET,null,Meeting.class);
        return resp.getBody();
    }
    public Meeting meetingFindById(long id){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("meetingId",String.valueOf(id));
        ResponseEntity<Meeting> resp = restTemplate.exchange(encodeUriForGet(params,meet_findId), HttpMethod.GET,null,Meeting.class);
        return resp.getBody();
    }
    public Meeting meetingSave(Meeting meeting){
        Map<String,Object> params= new HashMap<>();
        params.put("meeting",meeting);
        HttpEntity entity = new HttpEntity<>(params,headers);
        ResponseEntity<Meeting> resp = restTemplate.exchange(meet_save, HttpMethod.POST,entity,Meeting.class);
        return resp.getBody();
    }

    //=====================article=====================
    public Article articleFindById(long id){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("articleId",String.valueOf(id));
        ResponseEntity<Article> resp = restTemplate.exchange(encodeUriForGet(params,article_findId), HttpMethod.GET,null,Article.class);
        return resp.getBody();
    }
    public List<Article> articleFindByIdNot(long id){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("articleId",String.valueOf(id));
        ResponseEntity<List<Article>> resp = restTemplate.exchange(encodeUriForGet(params,article_findIdNot), HttpMethod.GET, null, new ParameterizedTypeReference<List<Article>>(){});
        return resp.getBody();
    }

    public String articleSave(Article article){
        Map<String,Object> params= new HashMap<>();
        params.put("article",article);
        HttpEntity entity = new HttpEntity<>(params,headers);
        ResponseEntity<String> resp = restTemplate.exchange(article_save, HttpMethod.POST,entity,String.class);
        return resp.getBody();
    }
    public List<Article> articleFindByMeetingNameAndStatus(String meetingName, String status){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("meetingName",meetingName);
        params.add("status",status);
        ResponseEntity<List<Article>> resp = restTemplate.exchange(encodeUriForGet(params,article_findNameAdSt), HttpMethod.GET, null, new ParameterizedTypeReference<List<Article>>(){});
        return resp.getBody();
    }
    //=====================pcMemberRelation=====================
    public List<PCMemberRelation> pcMemberRelationFindByMeetingIdAndStatus(long meetingId,String status){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("meetingId",String.valueOf(meetingId));
        params.add("status",status);
        ResponseEntity<List<PCMemberRelation>> resp = restTemplate.exchange(encodeUriForGet(params,pcmember_findIdAdSt), HttpMethod.GET, null, new ParameterizedTypeReference<List<PCMemberRelation>>(){});
        return resp.getBody();
    }
    //=======================auth===============================
    public Boolean cheackAuth(String token){
        checkHeaders.set("authorization",token);
        HttpEntity entity = new HttpEntity<>(null, checkHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(check_auth,HttpMethod.GET,entity,String.class);
        if (resp.getStatusCode() == HttpStatus.OK) return true;
        else return false;
    }
    //=======================util===============================
    public String encodeUriForGet(MultiValueMap<String, String> params, String uri) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        return builder.queryParams(params).build().encode().toString();
    }
}
