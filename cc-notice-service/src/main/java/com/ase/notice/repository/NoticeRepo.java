package com.ase.notice.repository;

import com.ase.notice.domain.Notice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepo extends CrudRepository<Notice, Long> {

    List<Notice> findAllByReceiverAndState(String receiver, int state);
    long countAllByReceiverAndState(String receiver, int state);
}
