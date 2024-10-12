package com.project.chess_be.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameReceived {

    private String moveFrom;

    private String moveTo;

    private Integer userId;

}
