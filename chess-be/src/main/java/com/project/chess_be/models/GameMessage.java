package com.project.chess_be.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameMessage {
    private String moveFrom;

    private String moveTo;

    private Side turn;

    private Status status;
}
