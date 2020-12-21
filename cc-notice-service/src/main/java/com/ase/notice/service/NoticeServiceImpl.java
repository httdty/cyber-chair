package com.ase.notice.service;

import com.ase.notice.domain.Notice;
import com.ase.notice.repository.NoticeRepo;
import com.ase.notice.service.api.NoticeService;
import com.ase.notice.util.response.ResponseGenerator;
import com.ase.notice.util.response.ResponseWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeRepo noticeRepo;

    @Override
    public ResponseWrapper<?> newNotice(String receiver, String content) {
        Notice newMessage = new Notice(receiver, content);
        noticeRepo.save(newMessage);
        return new ResponseWrapper<>(200, ResponseGenerator.success, null);
    }

    @Override
    public ResponseWrapper<?> readNotice(Long id) {
        Optional<Notice> msg = noticeRepo.findById(id);
        if (msg.isPresent()) {
            Notice no = msg.get();
            no.setState(1);
            noticeRepo.save(no);
            return new ResponseWrapper<>(200, ResponseGenerator.success, null);
        }
        return new ResponseWrapper<>(400, "id not valid", null);
    }

    @Override
    public ResponseWrapper<?> findNoticesByReceiverAndState(String receiver, int state) {
        List<Notice> notices = noticeRepo.findAllByReceiverAndState(receiver, state);
        return new ResponseWrapper<>(200, ResponseGenerator.success, notices);
    }

    @Override
    public ResponseWrapper<?> getUnreadMessageNum(String receiver) {
        long res = noticeRepo.countAllByReceiverAndState(receiver, 0);
        return new ResponseWrapper<>(200, ResponseGenerator.success, res);
    }
}
