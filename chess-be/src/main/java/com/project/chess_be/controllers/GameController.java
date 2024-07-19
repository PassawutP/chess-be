package com.project.chess_be.controllers;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.project.chess_be.entities.Game;
import com.project.chess_be.entities.User;
import com.project.chess_be.models.GameMessage;
import com.project.chess_be.models.GameReceived;
import com.project.chess_be.models.QueueMessage;
import com.project.chess_be.service.GameService;
import com.project.chess_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class GameController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final GameService gameService;
    private final UserService userService;

    public GameController(GameService gameService, UserService userService){

        this.gameService = gameService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<GameMessage> move(@RequestBody GameReceived message){
        Optional<User> user = userService.getUser(message.getUserId());
        if ( user.isPresent() && user.get().getGame() != null ){
            Game game = user.get().getGame();
            String turn = user.get().getSide();
            System.out.println(game);
            if (game.getTurn().equals(turn)){
                if (turn.equals("WHITE")){
                    String newTurn = "BLACK";
                    GameMessage gameMessage = GameMessage.builder()
                            .moveTo(message.getMoveTo())
                            .moveFrom(message.getMoveFrom())
                            .turn(newTurn)
                            .build();
                    System.out.println(gameMessage.getMoveFrom());
                    System.out.println(gameMessage.getMoveTo());
                    System.out.println(gameMessage.getTurn());
                    System.out.println("/game/"+user.get().getGame().getGameId().toString());
                    simpMessagingTemplate.convertAndSend("/game/"+user.get().getGame().getGameId().toString(), gameMessage);
                    // CHESS LOGIC
                    String fen = game.getBoard();
                    Board board = new Board();
                    board.loadFromFen(fen);
                    Square squareMoveFrom = Square.fromValue(message.getMoveFrom().toUpperCase());
                    Square squareMoveTo = Square.fromValue(message.getMoveTo().toUpperCase());
//                    if (){
//                        # legal move
//                    }
                    board.doMove(new Move(squareMoveFrom, squareMoveTo));
                    Game updatedGame = Game.builder().gameId(game.getGameId()).turn(newTurn).playerNum(game.getPlayerNum()).board(board.getFen()).build();
                    gameService.updateGame(updatedGame);
                    return ResponseEntity.ok(gameMessage);
                } else if (turn.equals("BLACK")) {
                    String newTurn = "WHITE";
                    GameMessage gameMessage = GameMessage.builder()
                            .moveTo(message.getMoveTo())
                            .moveFrom(message.getMoveFrom())
                            .turn(newTurn)
                            .build();
                    System.out.println(user.get().getGame().getGameId());
                    simpMessagingTemplate.convertAndSend("/game/"+user.get().getGame().getGameId().toString(), gameMessage);
                    // CHESS LOGIC
                    String fen = game.getBoard();
                    Board board = new Board();
                    board.loadFromFen(fen);
                    Square squareMoveFrom = Square.fromValue(message.getMoveFrom().toUpperCase());
                    Square squareMoveTo = Square.fromValue(message.getMoveTo().toUpperCase());
//                    if (){
//                        # legal move
//                    }
                    board.doMove(new Move(squareMoveFrom, squareMoveTo));
                    Game updatedGame = Game.builder().gameId(game.getGameId()).turn(newTurn).playerNum(game.getPlayerNum()).board(board.getFen()).build();
                    gameService.updateGame(updatedGame);
                    return ResponseEntity.ok(gameMessage);
                }
            }
        };
        return ResponseEntity.badRequest().build();
    }

//    public GameMessage updateTime(){
//        return null;
//   }

//   public GameMessage checkForCheckmate(){
//        return null;
//   }
}
