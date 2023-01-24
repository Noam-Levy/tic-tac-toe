package com.tictactoe.server;

import com.tictactoe.server.exceptions.InvalidInputException;
import com.tictactoe.server.exceptions.NotFoundException;
import com.tictactoe.server.model.Game;
import com.tictactoe.server.model.GameStatus;
import com.tictactoe.server.model.GameTurn;
import com.tictactoe.server.model.Player;
import com.tictactoe.server.services.GameService;
import com.tictactoe.server.storage.GameStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
public class GameServiceTests {
    @Autowired
    private GameService service;

    @AfterEach
    public void cleanUp() { GameStorage.getInstance().deleteAllGames(); }

    @Test
    public void gameInitialized() {
        Player player1 = new Player("player1");
        Game game = this.service.initializeGame(player1);
        assertThat(game).isNotNull();
        assertThat(game.getP1()).isEqualTo(player1);
        assertThat(game.getBoard().length).isEqualTo(3);
        assertThat(game.getTurnsPlayed()).isEqualTo(0);
        assertThat(game.getStatus()).isEqualTo(GameStatus.NEW);
        assertThat(game.getWinner()).isNull();
    }

    @Test
    public void joinToSpecificGameFailure() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        this.service.initializeGame(player1);
        assertThatThrownBy(() ->
                this.service.connectToSpecificGame("1", player2))
                .isInstanceOf(NotFoundException.class)
                .extracting("message")
                .isEqualTo("Unknown game id: 1");
    }

    @Test
    public void joinToSpecificGameSuccess() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = this.service.initializeGame(player1);
        game = this.service.connectToSpecificGame(game.getGameId(), player2);
        assertThat(game.getP1()).isEqualTo(player1);
        assertThat(game.getP2()).isEqualTo(player2);
        assertThat(game.getTurnsPlayed()).isEqualTo(0);
        assertThat(game.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(game.getWinner()).isNull();
    }

    @Test
    public void joinToRandomGameSuccess() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = this.service.initializeGame(player1);
        game = this.service.connectToRandomGame(player2);
        assertThat(game.getP1()).isEqualTo(player1);
        assertThat(game.getP2()).isEqualTo(player2);
        assertThat(game.getTurnsPlayed()).isEqualTo(0);
        assertThat(game.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(game.getWinner()).isNull();
    }

    @Test
    public void joinToRandomGameFailure() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = this.service.initializeGame(player1);
        this.service.connectToSpecificGame(game.getGameId(), player2);
        assertThatThrownBy(() ->
                this.service.connectToRandomGame(player2))
                .isInstanceOf(NotFoundException.class)
                .extracting("message")
                .isEqualTo("Sorry! there are no available games in the moment");
    }

    @Test
    public void doValidTurn() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = this.service.initializeGame(player1);
        this.service.connectToSpecificGame(game.getGameId(), player2);
        GameTurn turn = new GameTurn();
        turn.setGameId(game.getGameId());
        turn.setPlayer(player1);
        turn.setCoordinates(Map.of("x", 0, "y", 0));
        game = this.service.doTurn(turn);
        assertThat(game.getP1()).isEqualTo(player1);
        assertThat(game.getP2()).isEqualTo(player2);
        assertThat(game.getTurnsPlayed()).isEqualTo(1);
        assertThat(game.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
        assertThat(game.getWinner()).isNull();
        assertThat(game.getBoard()[0][0]).isEqualTo(1);
    }

    @Test
    public void doInvalidTurn() {
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = this.service.initializeGame(player1);
        this.service.connectToSpecificGame(game.getGameId(), player2);
        GameTurn turn = new GameTurn();
        turn.setGameId(game.getGameId());
        turn.setPlayer(player2);
        turn.setCoordinates(Map.of("x", 1, "y", 2));
        assertThatThrownBy(() ->
                this.service.doTurn(turn))
                .isInstanceOf(InvalidInputException.class)
                .extracting("message")
                .isEqualTo("Wrong player to play turn");
    }

    @Test
    public void isWinnerHorizontal() {
        int[][] board = {
                {0,2,2},
                {1,1,1},
                {2,2,0}
        };
        assertThat(this.service.isGameWon(board)).isTrue();
    }

    @Test
    public void isWinnerVertical() {
        int[][] board = {
                {0,2,2},
                {0,1,2},
                {0,1,2}
        };
        assertThat(this.service.isGameWon(board)).isTrue();
    }

    @Test
    public void isWinnerPrimaryDiagonal() {
        int[][] board = {
                {1,2,2},
                {1,1,0},
                {2,2,1}
        };
        assertThat(this.service.isGameWon(board)).isTrue();
    }

    @Test
    public void isWinnerSecondaryDiagonal() {
        int[][] board = {
                {0,2,2},
                {1,2,0},
                {2,2,1}
        };
        assertThat(this.service.isGameWon(board)).isTrue();
    }
}
