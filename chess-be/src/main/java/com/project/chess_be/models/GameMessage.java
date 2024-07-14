package com.project.chess_be.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameMessage {
    private Integer gameId;

    private String board;

    private String turn;

    private String status;

}
