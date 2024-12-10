package com.project.chess_be.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPassword {

    private String user;

    private String password;
}
