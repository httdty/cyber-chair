package adminmeeting.adminmeeting.api;

import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.domain.ReviewRelation;
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

public class ReviewRelationApi {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RemoteServiceConfig remote;

    public List<ReviewRelation> findByReviewerIdAndMeetingId(Long reviewerId, Long meetingId, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("reviewerId", reviewerId.toString());
        params.add("meetingId", meetingId.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<List<ReviewRelation>> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindReviewRelationByReviewerIdAndMeetingId()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ReviewRelation>>() {
                });

        List<ReviewRelation> reviewRelationList = Objects.requireNonNull(resp.getBody());
        return reviewRelationList;
    }

    public ReviewRelation save(ReviewRelation reviewRelation, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.set("authorization", token);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("reviewRelation", reviewRelation);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, httpHeaders);
        restTemplate.exchange(
                remote.getSaveReviewRelation(),
                HttpMethod.POST,
                entity,
                ReviewRelation.class);
        return reviewRelation;
    }

    public List<ReviewRelation> findByReviewStatusAndMeetingId(String reviewStatus,Long meetingId, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("reviewStatus", reviewStatus.toString());
        params.add("meetingId", meetingId.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<List<ReviewRelation>> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindReviewRelationByReviewStatusAndMeetingId()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ReviewRelation>>() {
                });
        List<ReviewRelation> reviewRelationList = Objects.requireNonNull(resp.getBody());
        return reviewRelationList;
    }

    public ReviewRelation findByReviewerIdAndArticleId(Long reviewerId, Long articleId, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("reviewerId", reviewerId.toString());
        params.add("articleId", articleId.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<ReviewRelation> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindReviewRelationByReviewerIdAndArticleId()),
                HttpMethod.GET,
                entity,
                ReviewRelation.class);
        ReviewRelation reviewRelation = resp.getBody();
        return reviewRelation;
    }


    public List<ReviewRelation> findByMeetingId(Long meetingId, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("meetingId", meetingId.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<List<ReviewRelation>> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindReviewRelationByMeetingId()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ReviewRelation>>() {
                });
        List<ReviewRelation> reviewRelationList = Objects.requireNonNull(resp.getBody());
        return reviewRelationList;
    }

    public List<ReviewRelation> findByArticleId(Long articleId, String token){
        HttpHeaders httpHeaders = new HttpHeaders();
        //httpHeaders.set("authorization", token);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("articleId", articleId.toString());
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, httpHeaders);
        ApiUtil apiUtil = new ApiUtil();
        ResponseEntity<List<ReviewRelation>> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, remote.getFindReviewRelationByArticleId()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ReviewRelation>>() {
                });
        List<ReviewRelation> reviewRelationList = Objects.requireNonNull(resp.getBody());
        return reviewRelationList;
    }

}