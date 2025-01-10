package org.example.back.dto;

import lombok.Getter;

@Getter
public class ChatMessage {

    // Getter & Setter
    private String sendId;   // 메시지를 보낸 사람의 ID
    private String receiveId; // 메시지를 받는 사람의 ID
    private String message;   // 메시지 본문

    // 기본 생성자
    public ChatMessage() {}

    // 모든 필드를 받는 생성자
    public ChatMessage(String sendId, String receiveId, String message) {
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.message = message;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // 객체를 문자열로 변환하여 확인할 수 있도록
    @Override
    public String toString() {
        return "ChatMessage{" +
                "sendId='" + sendId + '\'' +
                ", receiveId='" + receiveId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
