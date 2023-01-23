package com.tictactoe.server.storage;

import com.tictactoe.server.model.Game;

import java.util.HashMap;
import java.util.Map;

public class GameStorage {
    /*
        A singleton class to act as a non-persistent data storage for game statuses and history.
     */
    private static Map<String, Game> games;
    private static GameStorage instance;

    private GameStorage() {
        this.games = new HashMap<>();
    }

    public static synchronized GameStorage getInstance() {
        /*
            getInstance might be called simultaneously in many server instances (threads)
            and should still act as a singleton class.
            a race condition might occur right after server start, while two threads try to call getInstance in the same time
            and the server will return to each a different instance of the class.
         */
        if (instance == null)
            instance = new GameStorage();
        return instance;
    }

    public Map<String, Game> getGames() {
        return games;
    }

    public void saveGame(Game game) {
        games.put(game.getGameId(), game);
    }
}
