package com.project.chess_be.controllers;

import com.project.chess_be.entities.Game;
import com.project.chess_be.entities.User;
import com.project.chess_be.models.QueueMessage;
import com.project.chess_be.service.GameService;
import com.project.chess_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/queue")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class QueueController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final GameService gameService;
    private final UserService userService;

    public QueueController(GameService gameService, UserService userService ){
        this.gameService = gameService;
        this.userService = userService;
    }

    @GetMapping("{userId}")
    public ResponseEntity<QueueMessage> queue(@PathVariable Integer userId){
        List<Game> room = gameService.getRoomWithPlayerNum();
        long count = room.size();
        if ( count > 0 ){
            int randomisedRoomInt = (int)(Math.random() * count);
            int gameId = room.get(randomisedRoomInt).getGameId();
            Game game = Game.builder().gameId(gameId).status("READY").playerNum(2).turn("WHITE").build();
            Game updatedGame = gameService.updateGame(game);
            Optional<User> user = userService.getUser(userId);
            if (user.isPresent()){
                User senderUser = user.get();
                User otherUser = userService.getUserIdByGameId( gameId ).get(0);
                int randomisedSideInt = (int)(Math.random() * 2);
                switch (randomisedSideInt){
                    case 0:
                    {
                        QueueMessage senderStatus = QueueMessage.builder().status("READY").side("WHITE").build();
                        simpMessagingTemplate.convertAndSend("/queue/"+senderUser.getUserId(), senderStatus);
                        senderUser.setSide("WHITE");
                        senderUser.setGame(updatedGame);
                        userService.saveUser(senderUser);

                        QueueMessage otherStatus = QueueMessage.builder().status("READY").side("BLACK").build();
                        simpMessagingTemplate.convertAndSend("/queue/"+otherUser.getUserId(), otherStatus);
                        otherUser.setSide("BLACK");
                        userService.saveUser(otherUser);
                    }
                    case 1:
                    {
                        QueueMessage senderStatus = QueueMessage.builder().status("READY").side("BLACK").build();
                        simpMessagingTemplate.convertAndSend("/queue/"+senderUser.getUserId(), senderStatus);
                        senderUser.setSide("BLACK");
                        senderUser.setGame(updatedGame);
                        userService.saveUser(senderUser);

                        QueueMessage otherStatus = QueueMessage.builder().status("READY").side("WHITE").build();
                        simpMessagingTemplate.convertAndSend("/queue/"+otherUser.getUserId(), otherStatus);
                        otherUser.setSide("WHITE");
                        userService.saveUser(otherUser);
                    }
                }
            }
        } else {
            Game game = Game.builder().status("WAITING").playerNum(1).turn("WHITE").build();
            Game updatedGame = gameService.updateGame(game);
            Optional<User> user = userService.getUser(userId);
            if (user.isPresent()) {
                User senderUser = user.get();
                senderUser.setGame(updatedGame);
                userService.saveUser(senderUser);
            }
        }
        return ResponseEntity.ok(QueueMessage.builder().status("WAITING").build());
    }
}
