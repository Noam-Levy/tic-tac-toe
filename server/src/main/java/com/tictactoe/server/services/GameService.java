package com.tictactoe.server.services;

import com.tictactoe.server.exceptions.InvalidGameException;
import com.tictactoe.server.exceptions.InvalidInputException;
import com.tictactoe.server.exceptions.NotFoundException;
import com.tictactoe.server.model.GameTurn;
import com.tictactoe.server.model.TicToe;
import com.tictactoe.server.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import com.tictactoe.server.model.Game;
import com.tictactoe.server.model.Player;

import java.util.Map;

import static com.tictactoe.server.model.GameStatus.*;

@Service
@AllArgsConstructor
public class GameService {
    private final int BOARD_SIZE = 3;

    public Game initializeGame(Player player) {
        Game game = new Game();
        game.setGameId(RandomStringUtils.randomAlphanumeric(6));
        game.setBoard(new int[BOARD_SIZE][BOARD_SIZE]);
        game.setTurnsPlayed(0);
        addNewPlayerToGame(game, player);
        GameStorage.getInstance().saveGame(game);
        return game;
    }

    public Game connectToSpecificGame(String gameId, Player anotherPlayer) {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if (game == null) // no game with passed gameId
            throw new NotFoundException("Unknown game id: " + gameId);

        addNewPlayerToGame(game, anotherPlayer);
        GameStorage.getInstance().saveGame(game);
        return game;
    }

    public Game connectToRandomGame(Player anotherPlayer) {
        Game game = GameStorage.getInstance().getGames().values()
                .stream()
                .filter(it -> it.getP2() == null && it.getStatus().equals(NEW)) // filter out unavailable games to join into
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Sorry! there are no available games at the moment"));

        addNewPlayerToGame(game, anotherPlayer);
        GameStorage.getInstance().saveGame(game);
        return game;
    }

    public Game doTurn(GameTurn turn) {
        validateTurn(turn);
        Game game = GameStorage.getInstance().getGames().get(turn.getGameId());
        int[][] board = game.getBoard();
        int turnsPlayed = game.getTurnsPlayed();
        Player player = turn.getPlayer();
        int xCoordinate = turn.getCoordinates().get("x"), yCoordinate = turn.getCoordinates().get("y");

        board[xCoordinate][yCoordinate] = player.getSign().getValue();
        game.setTurnsPlayed(++turnsPlayed);
        if (isGameWon(board)) {
            game.setWinner(player);
            game.setStatus(FINISHED);
        }

        if (turnsPlayed == BOARD_SIZE * BOARD_SIZE)
            game.setStatus(FINISHED); // game concluded. might be with a draw (winner = null)
        return game;
    }

    private void addNewPlayerToGame(Game game, Player player) {
        if (game.getP2() != null || game.getStatus() != null && game.getStatus().equals(IN_PROGRESS)) // game has already started and/or is full
            throw new InvalidGameException("Cannot join to requested game");

        if (game.getP1() == null) {     // first player to join the game (assigned as X)
            player.setSign(TicToe.X);
            game.setP1(player);
            game.setStatus(NEW);
        } else {                        // second player to join (assigned as O)
            player.setSign(TicToe.O);
            game.setP2(player);
            game.setStatus(IN_PROGRESS);
        }
    }

    private void validateTurn(GameTurn turn) {
        Game game = GameStorage.getInstance().getGames().get(turn.getGameId());
        Map<String, Integer> coordinates = turn.getCoordinates();
        Player player = turn.getPlayer();
        // no game with passed gameId
        if (game == null)
            throw new NotFoundException("Unknown game id: " + turn.getGameId());

        // game is not in progress
        if (!game.getStatus().equals(IN_PROGRESS))
            throw new InvalidGameException("Cannot complete turn. Game is not in progress");

        // bad coordinates
        if (coordinates == null || !coordinates.containsKey("x") || !coordinates.containsKey("y"))
            throw new InvalidInputException("Missing coordinates");
        Integer xCoordinate = coordinates.get("x"), yCoordinate = coordinates.get("y");
        if (xCoordinate == null || (xCoordinate < 0 || xCoordinate >= BOARD_SIZE) ||
            yCoordinate == null || (yCoordinate < 0 || yCoordinate >= BOARD_SIZE))
            throw new InvalidInputException("Missing coordinates or out of bounds");
        if (game.getBoard()[xCoordinate][yCoordinate] != 0)
            throw new InvalidInputException("Selected cell has already been taken");

        // bad player
        if (player == null || player.getSign() == null)
            throw new InvalidInputException("Missing player or player sign");
        // bad player sign
        if (turn.getPlayer().equals(game.getP1()) && player.getSign().equals(TicToe.O) ||
            turn.getPlayer().equals(game.getP2()) && player.getSign().equals(TicToe.X))
            throw new InvalidInputException("Invalid sign for player");
        // wrong player sent turn request
        if (game.getTurnsPlayed() % 2 == 0 && turn.getPlayer().equals(game.getP2()) ||
            game.getTurnsPlayed() % 2 != 0 && turn.getPlayer().equals(game.getP1()))
            throw new InvalidInputException("Wrong player to play turn");
    }

    public boolean isGameWon(int[][] board) {
        return  isHorizontalWinner(board) ||
                isVerticalWinner(board) ||
                isPrimaryDiagonalWinner(board) ||
                isSecondaryDiagonalWinner(board);
    }

    private boolean isHorizontalWinner(int[][] board) {
        for (int r = 0; r < BOARD_SIZE; r++) {
            int sign = 0, count = 0;
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[r][c] == 0) return false;
                if (board[r][c] == sign) count++;
                else sign = board[r][c]; // sign == 0
            }
            if (count == BOARD_SIZE - 1) return true;
        }
        return false;
    }

    private boolean isVerticalWinner(int[][] board) {
        for (int c = 0; c < BOARD_SIZE; c++) {
            int sign = 0, count = 0;
            for (int r = 0; r < BOARD_SIZE; r++) {
                if (board[r][c] == 0) return false;
                if (board[r][c] == sign) count++;
                else sign = board[r][c]; // sign == 0
            }
            if (count == BOARD_SIZE - 1) return true;
        }
        return false;
    }

    private boolean isPrimaryDiagonalWinner(int[][] board) {
        int sign = 0, count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][i] == 0) return false;
            if (board[i][i] == sign) count++;
            else sign = board[i][i]; // sign is 0
        }
        return count == BOARD_SIZE - 1;
    }

    private boolean isSecondaryDiagonalWinner(int[][] board) {
        int sign = 0, count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (board[i][BOARD_SIZE - i - 1] == 0) return false;
            if (board[i][BOARD_SIZE - i - 1] == sign) count++;
            else sign = board[i][BOARD_SIZE - i - 1]; // sign is 0
        }
        return count == BOARD_SIZE - 1;
    }
}
