package adminmeeting.adminmeeting.controller.meeting;


import adminmeeting.config.RemoteServiceConfig;
import adminmeeting.controller.meeting.MeetingArticleController;
import adminmeeting.domain.Meeting;
import adminmeeting.domain.PCMemberRelation;
import adminmeeting.repository.MeetingRepository;
import adminmeeting.repository.PCMemberRelationRepository;
import adminmeeting.service.Service;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class MeetingSelfController {
    Logger logger = LoggerFactory.getLogger(MeetingArticleController.class);

    private Service service;

    @Autowired
    private PCMemberRelationRepository pcMemberRelationRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private RemoteServiceConfig remote;

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    public MeetingSelfController(Service service) { this.service = service; }

    public HttpStatus checkToken(String token){
        String checkApi = remote.getCheck(); //检查token
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> resp = restTemplate.exchange(checkApi, HttpMethod.GET, entity, String.class);
        return resp.getStatusCode();// 检查结束
    }

    @ApiOperation(value = "根据meeting名字查找meeting", response = Meeting.class)
    @GetMapping("/meeting/getByName")
    public ResponseEntity<?> mt_getByName(String meetingName) {
        logger.debug("Get Meeting By Name: " + meetingName);

//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查

        Meeting meeting = meetingRepository.findByMeetingName(meetingName);

        return ResponseEntity.ok(meeting);
    }
    @ApiOperation(value = "根据meetingId查找meeting", response = Meeting.class)
    @GetMapping("/meeting/getById")
    public ResponseEntity<?> mt_getById(long meetingId) {
        logger.debug("Get Meeting By Id: " + meetingId);
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        Meeting meeting = meetingRepository.findById(meetingId);
        return ResponseEntity.ok(meeting);
    }

    @ApiOperation(value = "根据chairName查找meeting列表", response = Meeting.class, responseContainer = "List")
    @GetMapping("/meeting/getByChairName")
    public ResponseEntity<?> mt_getByChairName(String chairName) {
        logger.debug("Get Meeting By chairName: " + chairName);
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        List<Meeting> meetingList = meetingRepository.findByChairName(chairName);
        return ResponseEntity.ok(meetingList);
    }

    @ApiOperation(value = "根据chairName和会议状态查找meeting列表", response = Meeting.class, responseContainer = "List")
    @GetMapping("/meeting/getByStatusAndChairNameNot")
    public ResponseEntity<?> mt_getByStatusAndChairNameNot(String status, String chairName) {
        logger.debug("Get Meeting By chairName NOT and status NOT: " + chairName);
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        List<Meeting> meetingList = meetingRepository.findByStatusAndChairNameNot(status, chairName);
        return ResponseEntity.ok(meetingList);
    }
    @ApiOperation(value = "通过post方式保存meeting进入数据库", response = Meeting.class)
    @PostMapping("/meeting/save")
    public ResponseEntity<?> mt_save_post(@RequestBody Meeting meeting) {
        logger.debug("mt_save");
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        meetingRepository.save(meeting);
        return ResponseEntity.ok(meeting);
    }
    @ApiOperation(value = "通过put方式修改数据库中的meeting", response = Meeting.class)
    @PutMapping("/meeting/save")
    public ResponseEntity<?> mt_save_put(@RequestBody Meeting meeting) {
        logger.debug("mt_save");
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        meetingRepository.save(meeting);
        return ResponseEntity.ok(meeting);
    }


    @ApiOperation(value = "通过meetingId来获取该meeting下的所有pcMemberRelation", response = PCMemberRelation.class,responseContainer = "List")
    @GetMapping("/meeting/pcMemberRelation/getByMeetingId")
    public ResponseEntity<?> mt_pcrelation_getByMeetingId(long meetingId) {
        logger.debug("Get PCMemberRelation By MeetingId: " + meetingId);
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        List<PCMemberRelation> pcMemberRelations = pcMemberRelationRepository.findByMeetingId(meetingId);
        return ResponseEntity.ok(pcMemberRelations);
    }
    @ApiOperation(value = "通过pcmemberId和状态来筛选所有pcMemberRelation", response = PCMemberRelation.class,responseContainer = "List")
    @GetMapping("/meeting/pcMemberRelation/getByPcmemberIdAndStatus")
    public ResponseEntity<?> mt_pcrelation_getByPcmemberIdAndStatus(long pcmemberId, String status) {
        logger.debug("getByPcmemberIdAndStatus");
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        List<PCMemberRelation> pcMemberRelations = pcMemberRelationRepository.findByPcmemberIdAndStatus(pcmemberId, status);
        return ResponseEntity.ok(pcMemberRelations);
    }
    @ApiOperation(value = "通过pcmemberId和MeetingId来筛选所有pcMemberRelation", response = PCMemberRelation.class,responseContainer = "List")
    @GetMapping("/meeting/pcMemberRelation/getByPcmemberIdAndMeetingId")
    public ResponseEntity<?> mt_pcrelation_getByPcmemberIdAndMeetingId(long pcmemberId, long meetingId) {
        logger.debug("getByPcmemberIdAndMeetingId");
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        List<PCMemberRelation> pcMemberRelations = pcMemberRelationRepository.findByPcmemberIdAndMeetingId(pcmemberId, meetingId);
        return ResponseEntity.ok(pcMemberRelations);
    }
    @ApiOperation(value = "通过meetingId和状态来筛选所有pcMemberRelation", response = PCMemberRelation.class,responseContainer = "List")
    @GetMapping("/meeting/pcMemberRelation/getByMeetingIdAndStatus")
    public ResponseEntity<?> mt_pcrelation_getByMeetingIdAndStatus(long meetingId,String status) {
        logger.debug("getByMeetingIdAndStatus");
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        List<PCMemberRelation> pcMemberRelations = pcMemberRelationRepository.findByMeetingIdAndStatus(meetingId, status);
        return ResponseEntity.ok(pcMemberRelations);
    }
    @ApiOperation(value = "通过pcmemberId和非状态来筛选所有pcMemberRelation", response = PCMemberRelation.class,responseContainer = "List")
    @GetMapping("/meeting/pcMemberRelation/getByPcmemberIdAndStatusNot")
    public ResponseEntity<?> mt_pcrelation_getByPcmemberIdAndStatusNot(long pcmemberId, String status) {
        logger.debug("getByPcmemberIdAndStatusNot");
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        List<PCMemberRelation> pcMemberRelations = pcMemberRelationRepository.findByPcmemberIdAndStatusNot(pcmemberId, status);
        return ResponseEntity.ok(pcMemberRelations);
    }
    @ApiOperation(value = "通过put来修改数据库中pcMemberRelation", response = PCMemberRelation.class)
    @PutMapping("/meeting/pcMemberRelation/save")
    public ResponseEntity<?> mt_pcrelation_save_put(@RequestBody PCMemberRelation relation) {
        logger.debug("pcMemberRelation save");
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        pcMemberRelationRepository.save(relation);
        return ResponseEntity.ok(relation);
    }
    @ApiOperation(value = "通过post向数据库中添加pcMemberRelation", response = PCMemberRelation.class)
    @PostMapping("/meeting/pcMemberRelation/save")
    public ResponseEntity<?> mt_pcrelation_save_post(@RequestBody PCMemberRelation relation) {
        logger.debug("pcMemberRelation save");
//        if(this.checkToken(token)!= HttpStatus.OK){ return ResponseEntity.badRequest().build(); } // 检查
        pcMemberRelationRepository.save(relation);
        return ResponseEntity.ok(relation);
    }





}
