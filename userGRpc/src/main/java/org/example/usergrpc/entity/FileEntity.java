package org.example.usergrpc.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;  // 파일 고유 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserEntity sender;  // 사용자 엔티티 (sender)

    @Column(name = "receive_list")
    private String receiveList;  // 수신자 리스트 (","로 구분된 문자열)

    @Column(name = "original_file_name")
    private String originalFileName;  // 원본 파일 이름

    @Column(name = "file_path")
    private String filePath;  // 파일 경로

    @Column(name = "file_extension")
    private String fileExtension;  // 파일 확장자

    @Column(name = "file_size")
    private String fileSize;  // 파일 크기 (사이즈)

    @Column(name = "file_uuid")
    private String fileUuid;  // 파일 UUID (고유 ID)

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="messageId")
    private MessageEntity message;


    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
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

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public MessageEntity getMessage() {
        return message;
    }

    public void setMessage(MessageEntity message) {
        this.message = message;
    }
}
