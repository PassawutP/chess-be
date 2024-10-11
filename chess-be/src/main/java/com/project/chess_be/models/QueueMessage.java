package com.project.chess_be.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueueMessage {

    private Status status;

    private Side side;
}
