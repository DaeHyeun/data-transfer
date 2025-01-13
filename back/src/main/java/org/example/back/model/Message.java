package org.example.back.model;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class Message {

    private String senderId;
    private String message;
    /*
    //sendId recieverId message
    private String recieverId;//<받는사람기준 개체 생성>
    // 리스트<해쉬맵<보낸사람, 리스트<내용>>>
    //리스트<내용> <- index 0 에서 안읽은 메세지 계산
    private List<HashMap<String, List<String>>> message;
    */


}
