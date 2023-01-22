package com.tictactoe.server.services;

import com.tictactoe.server.exceptions.InvalidGameException;
import com.tictactoe.server.exceptions.NotFoundException;
import com.tictactoe.server.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.tictactoe.server.model.Game;
import com.tictactoe.server.model.Player;

import java.util.UUID;

import static com.tictactoe.server.model.GameStatus.IN_PROGRESS;
import static com.tictactoe.server.model.GameStatus.NEW;

@Service
@AllArgsConstructor
public class GameService {
    private final int BOARD_SIZE = 3;

    public Game initializeGame(Player player) {
        Game game = new Game();
        game.setGameId(UUID.randomUUID().toString());
        game.setBoard(new int[BOARD_SIZE][BOARD_SIZE]);
        game.setP1(player);
        game.setStatus(NEW);
        GameStorage.getInstance().putGame(game);
        return game;
    }

    public Game connectToSpecificGame(String gameId, Player anotherPlayer) {
        Game game = GameStorage.getInstance().getGames().get(gameId);
        if (game == null) // no game with passed gameId
            throw new NotFoundException("Unknown game id: " + gameId);
        if (game.getP2() != null || game.getStatus().equals(IN_PROGRESS)) // game has already started and/or is full
            throw new InvalidGameException("Cannot join to requested game");

        game.setP2(anotherPlayer);
        game.setStatus(IN_PROGRESS);
        GameStorage.getInstance().putGame(game);
        return game;
    }

    public Game connectToRandomGame(Player anotherPlayer) {
        Game game =
                GameStorage.getInstance().getGames().values()
                .stream()
                .filter(it -> (it.getP2() == null && it.getStatus().equals(NEW)))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Sorry! there are no available games in the moment"));

        game.setP2(anotherPlayer);
        game.setStatus(IN_PROGRESS);
        GameStorage.getInstance().putGame(game);
        return game;
    }
}
