package com.project.chess_be.repositories;

import com.project.chess_be.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    @Query("SELECT g FROM Game g WHERE g.playerNum = 1")
    List<Game> getRoomWithPlayerNum();
}
