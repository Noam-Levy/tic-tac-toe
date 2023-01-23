package com.tictactoe.server.model;

import lombok.Data;

@Data
public class Game {
    private String gameId;
    private GameStatus status;
    private Player p1;
    private Player p2;
    private Player winner;
    private int[][] board;
    private int turnsPlayed;
}
