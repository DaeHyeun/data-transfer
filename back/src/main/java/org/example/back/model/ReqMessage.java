package org.example.back.model;

import lombok.*;

import java.io.File;
import java.util.List;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqMessage {
    private String sender;
    private List<String> receiverList;
    private String message;
}
