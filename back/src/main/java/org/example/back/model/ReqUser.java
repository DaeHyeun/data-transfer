package org.example.back.model;

import lombok.*;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReqUser {
    private String username;
    private String password;
}
