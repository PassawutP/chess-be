package com.project.chess_be.entities;

import com.project.chess_be.models.Side;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column( nullable = false )
    private String name;

    @Enumerated(EnumType.STRING)
    private Side side;

    @ManyToOne
    @JoinColumn( name = "game_id")
    private Game game;
}
