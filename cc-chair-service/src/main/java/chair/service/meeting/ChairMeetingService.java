package chair.service.meeting;

import chair.config.RemoteServiceConfig;
import chair.domain.*;
import chair.exception.*;
import chair.request.meeting.*;
import chair.utility.ApiUtil;
import chair.utility.contract.*;
import chair.utility.response.ResponseGenerator;
import chair.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ChairMeetingService {


    @Autowired
    private RemoteServiceConfig remote;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApiUtil apiUtil;
    public ChairMeetingService() {


    }


    /**
     * 开启提交,将meeting的状态更新为可以提交
     * @param request :String meetingName
     * @return null
     * save meeting
     */
    @Transactional
    public ResponseWrapper<?> beginSubmission(BeginSubmissionRequest request) {
        String meetingName = request.getMeetingName();
        Meeting meeting = MeetingGet(meetingName);//通过API获取meeting
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }

        String meetingStatus = meeting.getStatus();
        if (!meetingStatus.equals(MeetingStatus.applyPassed)) {
            throw new MeetingUnavaliableToOperateException(meetingName);
        }
        meeting.setStatus(MeetingStatus.submissionAvaliable);
        MeetingPost(meeting);
//        meetingRepository.save(meeting);//在数据库建立了一条meeting记录
        //？将结果返回给原始的meeting表，要对原先meetingId对应行的数据进行更新
        return new ResponseWrapper<>(200, ResponseGenerator.success, meeting);

    }
    /**
     * PCMember邀请，建立pcmember与meeting的联系
     * @param request:String meetingName,String pcMemberName
     * @return null
     * save PCMemberRelation pcMemberRelation
     */

    public ResponseWrapper<?> pcmInvitation(PCMemberInvitationRequest request) {
        String meetingName = request.getMeetingName();
        Meeting meeting = MeetingGet(meetingName);
        //Meeting meeting = meetingRepository.findByMeetingName(meetingName);//PCM邀请时，meeting表已经在当前服务对数据库建立

        if (meeting == null) {
            throw new MeetingOfNoExistenceException(meetingName);
        }//会议是否存在

        if (meeting.getChairName().equals(request.getPcMemberName())) {
            throw new InvitationTargetIsForbiddenException(meetingName);
        }//邀请对象不能是chair本人

        String meetingStatus = meeting.getStatus();
        if (meetingStatus.equals(MeetingStatus.applyFailed) || meetingStatus.equals(MeetingStatus.unprocessed) || meetingStatus.equals(MeetingStatus.reviewing) || meetingStatus.equals(MeetingStatus.reviewCompleted)) {
            throw new MeetingStatusUnavailableForPCMemberInvitationException(meetingName);
        }//会议状态是否允许进行成员邀请

        //User user = userRepository.findByUsername(request.getPcMemberName());
        User user = UserGet(request.getPcMemberName());
        if (user == null) {
            throw new UserNamedidntExistException(request.getPcMemberName());
        }//邀请对象是否存在邀请

        PCMemberRelation pcMemberRelation = new PCMemberRelation(user.getId(), meeting.getId(), PCmemberRelationStatus.undealed, null);
        PCMemberRelationPost(pcMemberRelation);
      //  pcMemberRelationRepository.save(pcMemberRelation);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }


    /**
     * 将meeting的状态更新为第一次评论发布
     * @param request：String meetingName
     * @return null
     * Save meeting
     */

    public ResponseWrapper<?> reviewPublish(ResultPublishRequest request) {
        String meetingName = request.getMeetingName();
        Meeting meeting = MeetingGet(meetingName);
//        Meeting meeting = meetingRepository.findByMeetingName(request.getMeetingName());//在更新publish意见时，meeting表已经建立
        if (meeting == null) {
            throw new MeetingOfNoExistenceException(request.getMeetingName());
        }//会议是否存在
        if (!meeting.getStatus().equals(MeetingStatus.reviewCompleted)) {
            throw new MeetingStatusUnAvailableToReviewException();
        }
        meeting.setStatus(MeetingStatus.resultPublished);
//        meetingRepository.save(meeting);
        MeetingPost(meeting);
        return new ResponseWrapper<>(200, ResponseGenerator.success, meeting);
    }

    /**
     * 将meeting的状态更新为第二次评论发布，即最终发布
     * @param request：String meetingName
     * @return if 会议没有最终发布评论则进行最终发布：null   else 会议更新操作失败：null
     */
    public ResponseWrapper<?> finalPublish(FinalPublishRequest request) {
        String meetingName = request.getMeetingName();
        Meeting meeting = MeetingGet(meetingName);
        //Meeting meeting = meetingRepository.findByMeetingName(request.getMeetingName());//在更新最终publish意见时，meeting表已经建立
        if (meeting.getStatus().equals(MeetingStatus.reviewFinish)) {
            meeting.setStatus(MeetingStatus.reviewPublish);
            MeetingPost(meeting);
           // meetingRepository.save(meeting);
            return new ResponseWrapper<>(200, ResponseGenerator.success, meeting);
        } else {
            return new ResponseWrapper<>(200, "failed: unable to do final publish for incorrect meeting status", null);
        }
    }

    /**
     *
     * @param meetingName
     * @return
     */


    public Meeting MeetingGet(String meetingName){

//        //构造请求头，加入token
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set("authorization", token);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("meetingName", meetingName);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params);
        //获取Meeting
        ResponseEntity<Meeting> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params,remote.getFindMeetingByMeetingName()),
                HttpMethod.GET,
                entity,
                Meeting.class);
        Meeting meeting = resp.getBody();
        return  meeting;
    }
    public User UserGet(String userName){

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username",userName);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params);
        //获取Meeting
        ResponseEntity<User> resp = restTemplate.exchange(
                apiUtil.encodeUriForGet(params,remote.getFindUserByUserName()),
                HttpMethod.GET,
                entity,
                User.class);
        User user = resp.getBody();
        return  user;
    }
    public void MeetingPost(Meeting meeting){

//

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("Meeting", meeting);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params);
        //发送meetingStatus
        restTemplate.exchange(
                remote.getUpdateMeetingStatus(),//接口
                HttpMethod.POST,
                entity,
                Meeting.class);

    }
    public void PCMemberRelationPost( PCMemberRelation pcMemberRelation){


        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("PCMemberRelation", pcMemberRelation);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params);
        //发送pcMemberRelationStatus
        restTemplate.exchange(
                remote.getUpdatePcmemberRelation(),//接口
                HttpMethod.POST,
                entity,
                PCMemberRelation.class);
    }

}
