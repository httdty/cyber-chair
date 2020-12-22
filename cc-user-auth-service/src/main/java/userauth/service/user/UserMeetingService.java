package userauth.service.user;

import userauth.config.RemoteServiceConfig;
import userauth.domain.*;
import userauth.repository.*;
import userauth.utility.ApiUtil;
import userauth.utility.contract.MeetingStatus;
import userauth.utility.contract.PCmemberRelationStatus;
import userauth.utility.response.ResponseGenerator;
import userauth.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserMeetingService {
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RemoteServiceConfig api;
    @Autowired
    private ApiUtil apiUtil;

    @Autowired
    public UserMeetingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseWrapper<?> chairMeeting(String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("chairName", username);
        ResponseEntity<List<Meeting>> res = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, api.getFindMeetingByChairName()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Meeting>>() {
                }
        );
        List<Meeting> meetingList = Objects.requireNonNull(res.getBody());

        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (Meeting meeting : meetingList) {
            HashMap<String, Object> meetingInfo = ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "acronym", "conferenceDate", "topic"}, null);
            response.add(meetingInfo);
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Transactional
    public ResponseWrapper<?> pcMemberMeeting(String username) {
        Long userId = userRepository.findByUsername(username).getId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pcmemberId", String.valueOf(userId));
        params.add("status", PCmemberRelationStatus.accepted);
        ResponseEntity<List<PCMemberRelation>> result = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, api.getFindPcmemberByPcmemberIdAndStatus()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PCMemberRelation>>() {
                });

        List<PCMemberRelation> relationList = Objects.requireNonNull(result.getBody());

        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (PCMemberRelation relation : relationList) {

            params = new LinkedMultiValueMap<>();
            params.add("meetingId", String.valueOf(relation.getMeetingId()));
            ResponseEntity<Meeting> res = restTemplate.exchange(
                    apiUtil.encodeUriForGet(params, api.getFindMeetingById()),
                    HttpMethod.GET,
                    null,
                    Meeting.class);


            Meeting meeting = Objects.requireNonNull(res.getBody());

            HashMap<String, Object> meetingInfo = ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "acronym", "conferenceDate", "topic"}, null);
            response.add(meetingInfo);
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Transactional
    public ResponseWrapper<?> authorMeeting(String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("contributorName", username);
        ResponseEntity<List<Article>> res = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, api.getFindArticleByContributorName()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Article>>() {
                });
        List<Article> articleList = Objects.requireNonNull(res.getBody());

        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        Set<Long> meetingCount = new HashSet<>();
        for (Article article : articleList) {
            params = new LinkedMultiValueMap<>();
            params.add("meetingName", article.getMeetingname());
            ResponseEntity<Meeting> resp = restTemplate.exchange(
                    apiUtil.encodeUriForGet(params, api.getFindMeetingByMeetingName()),
                    HttpMethod.GET,
                    null,
                    Meeting.class
            );
            Meeting meeting = resp.getBody();

            if (meeting != null && !meetingCount.contains(meeting.getId())) {
                meetingCount.add(meeting.getId());
                response.add(ResponseGenerator.generate(meeting,
                        new String[]{"meetingName", "acronym", "submissionDeadlineDate", "topic"}, null));
            }
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    @Transactional
    public ResponseWrapper<?> availableMeeting(String username) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("status", MeetingStatus.submissionAvaliable);
        params.add("chairName", username);
        ResponseEntity<List<Meeting>> res = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, api.getFindMeetingByStatusAndChairNameNot()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Meeting>>() {
                });

        List<Meeting> allMeeting = Objects.requireNonNull(res.getBody());

        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (Meeting meeting : allMeeting) {
            response.add(ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "acronym", "submissionDeadlineDate", "topic"}, null));
        }
        body.put("meetings", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }
}
