package com.project.chess_be.entities;

import com.project.chess_be.models.Side;
import com.project.chess_be.models.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gameId;

    @Enumerated(EnumType.STRING)
    private Side turn;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String board;

    private Integer playerNum;

}
