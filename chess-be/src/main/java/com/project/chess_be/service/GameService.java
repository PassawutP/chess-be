package com.project.chess_be.service;

import com.project.chess_be.entities.Game;
import com.project.chess_be.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getGame(){
        return gameRepository.findAll();
    }

    public List<Game> getRoomWithPlayerNum(){
        return gameRepository.getRoomWithPlayerNum();
    }

    public Game updateGame(Game game){
        return gameRepository.save(game);
    }
}
