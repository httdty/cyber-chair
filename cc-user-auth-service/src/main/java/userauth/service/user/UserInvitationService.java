package userauth.service.user;

import userauth.config.RemoteServiceConfig;
import userauth.domain.Meeting;
import userauth.domain.Notice;
import userauth.domain.PCMemberRelation;
import userauth.repository.*;
import userauth.request.user.InvitationRepoRequest;
import userauth.utility.ApiUtil;
import userauth.utility.contract.PCmemberRelationStatus;
import userauth.utility.response.ResponseGenerator;
import userauth.utility.response.ResponseWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.rmi.Remote;
import java.util.*;

@Service
public class UserInvitationService {
    private UserRepository userRepository;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RemoteServiceConfig api;
    @Autowired
    private ApiUtil apiUtil;

    @Autowired
    public UserInvitationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseWrapper<?> undealedNotifications(String username) {
        Long userId = userRepository.findByUsername(username).getId();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pcmemberId", String.valueOf(userId));
        params.add("status", PCmemberRelationStatus.undealed);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, new HttpHeaders());
        ResponseEntity<List<PCMemberRelation>> result = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, api.getFindPcmemberByPcmemberIdAndStatus()),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<PCMemberRelation>>() {
                });

        List<PCMemberRelation> relationList = Objects.requireNonNull(result.getBody());
        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (PCMemberRelation relation : relationList) {
            MultiValueMap<String, String> p = new LinkedMultiValueMap<>();
            p.add("meetingId", String.valueOf(relation.getMeetingId()));
            HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(p);
            ResponseEntity<Meeting> res = restTemplate.exchange(
                    apiUtil.encodeUriForGet(p, api.getFindMeetingById()),
                    HttpMethod.GET,
                    httpEntity,
                    Meeting.class);

            Meeting meeting = res.getBody();
            assert meeting != null;
            HashMap<String, Object> invitationInfo = ResponseGenerator.generate(meeting,
                    new String[]{"meetingName", "chairName", "topic"}, null);
            response.add(invitationInfo);
        }
        body.put("invitations", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public ResponseWrapper<?> invitationRepo(InvitationRepoRequest request) {
        Long userId = userRepository.findByUsername(request.getUsername()).getId();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("meetingName", request.getMeetingName());
        ResponseEntity<Meeting> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, api.getFindMeetingByMeetingName()),
                HttpMethod.GET,
                null,
                Meeting.class
        );

        Long meetingId = Objects.requireNonNull(resp.getBody()).getId();
        params = new LinkedMultiValueMap<>();
        params.add("pcmemberId", String.valueOf(userId));
        params.add("meetingId", String.valueOf(meetingId));
        ResponseEntity<List<PCMemberRelation>> res = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, api.getFindPcmemberRelationByPcmemberIdAndMeetingId()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PCMemberRelation>>() {
                });
        List<PCMemberRelation> relationList = res.getBody();
        assert relationList != null;
        // 实际上， relationList只有一个元素，因为pcmemberId和meetingId会构成联合主键
        for (PCMemberRelation relation : relationList) {
            if (relation.getStatus().equals(PCmemberRelationStatus.undealed)) {
                relation.setStatus(request.getResponse());
                if (request.getResponse().equals(PCmemberRelationStatus.accepted)) {
                    relation.setTopic(request.getTopics());
                }
                ObjectMapper oMapper = new ObjectMapper();
                Map<String, Object> map = oMapper.convertValue(relation, new TypeReference<Map<String, Object>>() {
                });
                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, new HttpHeaders());
                ResponseEntity<PCMemberRelation> respEntity = restTemplate.exchange(
                        api.getUpdatePcmemberRelation(),
                        HttpMethod.PUT,
                        entity,
                        PCMemberRelation.class);
                if (respEntity.getStatusCode() == HttpStatus.OK) {
                    System.out.println("update pcmemberRelation success.");
                }
                // pcMemberRelationRepository.save(relation);
                break;
            }
        }
        // TODO: 在此处post一条message，chairname在Meeting中有，可以调用
        String receiver = Objects.requireNonNull(resp.getBody()).getChairName();
        String content = request.getUsername() + ": " + request.getResponse() + " your invitation";

        HttpHeaders httpHeaders = new HttpHeaders();
        Notice notice = new Notice(receiver, content);
        HttpEntity<Notice> entity = new HttpEntity<>(notice,httpHeaders);
        restTemplate.postForObject(api.getSaveNotice(),entity,String.class);

        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    public ResponseWrapper<?> undealedNotificationsNum(String username) {
        Long userId = userRepository.findByUsername(username).getId();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pcmemberId", String.valueOf(userId));
        params.add("status", PCmemberRelationStatus.undealed);
        String uri = apiUtil.encodeUriForGet(params, api.getFindPcmemberByPcmemberIdAndStatus());
        ResponseEntity<List<PCMemberRelation>> res = restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<PCMemberRelation>>() {
        });
        List<PCMemberRelation> relationList = Objects.requireNonNull(res.getBody());

        HashMap<String, Object> body = new HashMap<>();
        body.put("undealedNotificationsNum", relationList.size());
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

    public ResponseWrapper<?> alreadyDealedNotifications(String username) {
        Long userId = userRepository.findByUsername(username).getId();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("pcmemberId", String.valueOf(userId));
        params.add("status", PCmemberRelationStatus.undealed);
        ResponseEntity<List<PCMemberRelation>> res = restTemplate.exchange(
                apiUtil.encodeUriForGet(params, api.getFindPcmemberRelationByPcmemberIdAndStatusNot()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PCMemberRelation>>() {
                });

        List<PCMemberRelation> relationList = Objects.requireNonNull(res.getBody());

        HashMap<String, Set<HashMap<String, Object>>> body = new HashMap<>();
        Set<HashMap<String, Object>> response = new HashSet<>();
        for (PCMemberRelation relation : relationList) {
            HashMap<String, Object> invitaionInfo = ResponseGenerator.generate(relation,
                    new String[]{"status"}, null);

            params = new LinkedMultiValueMap<>();
            params.add("meetingId", String.valueOf(relation.getMeetingId()));
            ResponseEntity<Meeting> resp = restTemplate.exchange(
                    apiUtil.encodeUriForGet(params, api.getFindMeetingById()),
                    HttpMethod.GET,
                    null,
                    Meeting.class);
            Meeting m = Objects.requireNonNull(resp.getBody());

            invitaionInfo.put("meetingName", m.getMeetingName());
            invitaionInfo.put("chairName", m.getChairName());
            response.add(invitaionInfo);
        }
        body.put("alreadyDealedNotifications", response);
        return new ResponseWrapper<>(200, ResponseGenerator.success, body);
    }

}
