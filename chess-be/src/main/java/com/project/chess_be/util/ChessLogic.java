package com.project.chess_be.util;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;
import com.project.chess_be.entities.Game;
import com.project.chess_be.entities.User;
import com.project.chess_be.models.GameMessage;
import com.project.chess_be.models.GameReceived;
import com.project.chess_be.models.Side;
import com.project.chess_be.models.Status;
import com.project.chess_be.service.GameService;
import com.project.chess_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ChessLogic {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final GameService gameService;
    private final UserService userService;

    @Autowired
    public ChessLogic(UserService userService, GameService gameService) {
        this.gameService = gameService;
        this.userService = userService;
    }


    public ResponseEntity<GameMessage> chessCheck(GameReceived message) {
        // Check if user exists
        Optional<User> user = userService.getUser(message.getUserId());
        if (user.isEmpty()) {
            System.out.println("user doesn't exist");
            return ResponseEntity.badRequest().build();
        };
        Game game = user.get().getGame();
        Side turn = user.get().getSide();
        Side newTurn;

        // Check current turn
        if (turn.equals(Side.WHITE)) {
            newTurn = Side.BLACK;
        } else {
            newTurn = Side.WHITE;
        }
        if (!game.getTurn().equals(turn)){
            System.out.println("Wrong turn");
            System.out.println("Game turn: "+game.getTurn()+ "Player turn: "+turn);
            return ResponseEntity.badRequest().build();
        }
        return checkLegalMove(game, turn, newTurn, message, user.get());
    }


    public ResponseEntity<GameMessage> checkLegalMove(Game game, Side turn, Side newTurn, GameReceived message, User user){
        // Check Turn
//        if (!turn.equals(newTurn)) {
//            System.out.println("Wrong turn);
//            return ResponseEntity.badRequest().build();
//        }
        GameMessage gameMessage = GameMessage.builder()
                .moveTo(message.getMoveTo())
                .moveFrom(message.getMoveFrom())
                .turn(newTurn)
                .status(Status.READY)
                .build();

        // Declare Chess Variables
        String fen = game.getBoard();
        Board board = new Board();
        board.loadFromFen(fen);
        Square squareMoveFrom = Square.fromValue(message.getMoveFrom().toUpperCase());
        Square squareMoveTo = Square.fromValue(message.getMoveTo().toUpperCase());
        List<Move> moves = board.legalMoves();
        List<String> stringMoves = new ArrayList<String>();

        // Check Legal Move
        for (Move move : moves) {
            stringMoves.add(move.getFrom().value().concat(move.getTo().value()));
        }
        if (!stringMoves.contains(squareMoveFrom.value().concat(squareMoveTo.value()))) return ResponseEntity.badRequest().build();

        // Update Game
        board.doMove(new Move(squareMoveFrom, squareMoveTo));
        Game updatedGame = Game.builder().gameId(game.getGameId()).turn(newTurn).playerNum(game.getPlayerNum()).board(board.getFen()).status(Status.READY).build();

        // Check isMated()
        if (board.isMated()) {
            gameMessage.setStatus(Status.ENDING);
            updatedGame.setStatus(Status.ENDING);
        } else {
            gameMessage.setStatus(Status.READY);
        }
        gameService.updateGame(updatedGame);

        simpMessagingTemplate.convertAndSend("/game/" + user.getGame().getGameId().toString(), gameMessage);
        return ResponseEntity.ok(gameMessage);
    }
}
