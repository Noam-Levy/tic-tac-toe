package com.tictactoe.server;

import com.tictactoe.server.exceptions.NotFoundException;
import com.tictactoe.server.model.Game;
import com.tictactoe.server.model.GameStatus;
import com.tictactoe.server.model.Player;
import com.tictactoe.server.services.GameService;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class GameServiceTests {
    @Autowired
    private GameService service;
    private Game game;
    private Player player1;

    @PostConstruct
    public void init() {
        this.player1 = new Player("player1");
        this.game = this.service.initializeGame(player1);
    }

    @BeforeEach
    public void gameInitialized() {
        assertThat(game).isNotNull();
        assertThat(game.getP1()).isEqualTo(player1);
        assertThat(game.getBoard().length).isEqualTo(3);
        assertThat(game.getTurnsPlayed()).isEqualTo(0);
        assertThat(game.getStatus()).isEqualTo(GameStatus.NEW);
        assertThat(game.getWinner()).isNull();
    }

    @Test
    public void joinToSpecificGameFailure() {
        Player player2 = new Player("player2");
        assertThatThrownBy(() ->
                this.service.connectToSpecificGame("1", player2))
                .isInstanceOf(NotFoundException.class)
                .extracting("message")
                .isEqualTo("Unknown game id: 1");
    }

    @Test
    public void joinToSpecificGameSuccess() {
        Player player2 = new Player("player2");
        Game game = this.service.connectToSpecificGame(this.game.getGameId(), player2);
        assertThat(game.getP1()).isEqualTo(player1);
        assertThat(game.getP2()).isEqualTo(player2);
        assertThat(game.getTurnsPlayed()).isEqualTo(0);
        assertThat(game.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(game.getWinner()).isNull();
    }

    @Test
    public void joinToRandomGameSuccess() {
        Player player2 = new Player("player2");
        this.service.connectToRandomGame(player2);
        assertThat(game.getP1()).isEqualTo(player1);
        assertThat(game.getP2()).isEqualTo(player2);
        assertThat(game.getTurnsPlayed()).isEqualTo(0);
        assertThat(game.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(game.getWinner()).isNull();
    }
}
