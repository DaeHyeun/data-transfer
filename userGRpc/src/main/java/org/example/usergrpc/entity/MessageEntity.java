package org.example.usergrpc.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@ToString
@Table(name = "Message")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")  // senderName을 UserEntity로 연결할 외래 키
    private UserEntity sender;  // 변경된 부분: senderName -> UserEntity sender
    private String receiveList;  // List<String>은 그대로 사용
    private String message;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public String getReceiveList() {
        return receiveList;
    }

    public void setReceiveList(String receiveList) {
        this.receiveList = receiveList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
