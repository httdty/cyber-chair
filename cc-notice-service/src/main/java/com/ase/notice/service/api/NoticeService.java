package com.ase.notice.service.api;

import com.ase.notice.domain.Notice;
import com.ase.notice.util.response.ResponseWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NoticeService {

    ResponseWrapper<?> newNotice(String receiver, String content);
    ResponseWrapper<?> readNotice(Long id);
    ResponseWrapper<?> findNoticesByReceiverAndState(String receiver, int state);
    ResponseWrapper<?> getUnreadMessageNum(String receiver);
}
