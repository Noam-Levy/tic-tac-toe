package com.tictactoe.server.controlles;

import com.tictactoe.server.model.Game;
import com.tictactoe.server.model.GameTurn;
import com.tictactoe.server.model.Player;
import com.tictactoe.server.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/tictactoe")
@AllArgsConstructor
public class TicTacToeController {
    private SimpMessagingTemplate messagingTemplate;
    private GameService service;

    @PostMapping("/init")
    public ResponseEntity<Game> initializeGame(@RequestBody Player player) {
        return ResponseEntity.ok(this.service.initializeGame(player));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connectToGame(
            @RequestBody Player player,
            @RequestParam(name="gameId", defaultValue="") String gameId) {
        Game game;
        if (gameId.equals(""))
            game =  this.service.connectToRandomGame(player);
        else
            game = this.service.connectToSpecificGame(gameId, player);

        this.messagingTemplate.convertAndSend("/topic/connection/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/doTurn")
    public ResponseEntity<Game> doTurn(@RequestBody GameTurn turn) {
        Game game = this.service.doTurn(turn);
        this.messagingTemplate.convertAndSend("/topic/progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/rematch")
    public ResponseEntity<Game> rematch(@RequestBody Player player,
                                        @RequestParam(name="gameId", defaultValue="") String gameId) {
        Game game;
        if (player.isAcceptRematch())
            game = this.service.acceptRematch(gameId, player);
        else
            game = this.service.declineRematch(gameId);

        this.messagingTemplate.convertAndSend("/topic/progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}
