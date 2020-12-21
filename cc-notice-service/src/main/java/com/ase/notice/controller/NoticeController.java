package com.ase.notice.controller;


import com.ase.notice.domain.Notice;
import com.ase.notice.request.NewMessageReq;
import com.ase.notice.request.ReadMessagReq;
import com.ase.notice.service.api.NoticeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;


    @ApiOperation("新增消息")
    @PostMapping
    public ResponseEntity<?> newMessage(@RequestBody NewMessageReq req) {
        return ResponseEntity.ok(noticeService.newNotice(req.getReceiver(), req.getContent()));
    }

    @ApiOperation("mark as read")
    @PutMapping
    public ResponseEntity<?> readMessage(@RequestBody ReadMessagReq req) {
        return ResponseEntity.ok(noticeService.readNotice(req.getId()));
    }

    @ApiOperation("根据receiver name和state获取消息列表")
    @GetMapping
    public ResponseEntity<?> getMessageList(String receiver, int state) {
        return ResponseEntity.ok(noticeService.findNoticesByReceiverAndState(receiver, state));
    }

    @ApiOperation("根据receiver name获取未读消息数量")
    @GetMapping("/unread")
    public ResponseEntity<?> getUnreadMsgNum(String receiver) {
        return ResponseEntity.ok(noticeService.getUnreadMessageNum(receiver));
    }
}
