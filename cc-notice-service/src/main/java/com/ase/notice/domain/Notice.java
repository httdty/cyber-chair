package com.ase.notice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String receiver;
    private String content;
    private int state; // 0 - unread; 1 - read already

    public Notice(String receiver, String content) {
        this.receiver = receiver;
        this.content = content;
        this.state = 0;
    }

    public Notice() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receive) {
        this.receiver = receive;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
