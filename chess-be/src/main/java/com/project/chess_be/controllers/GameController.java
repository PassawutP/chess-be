package com.project.chess_be.controllers;

import com.project.chess_be.models.GameMessage;
import com.project.chess_be.models.GameReceived;
import com.project.chess_be.service.GameService;
import com.project.chess_be.service.UserService;
import com.project.chess_be.util.ChessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final ChessLogic chessLogic; // Inject ChessLogic

    @Autowired
    public GameController(GameService gameService, UserService userService, ChessLogic chessLogic) {
        this.chessLogic = chessLogic; // Initialize ChessLogic
    }

    @PostMapping()
    public ResponseEntity<GameMessage> move(@RequestBody GameReceived message) {
        return chessLogic.chessCheck(message);
    }
}