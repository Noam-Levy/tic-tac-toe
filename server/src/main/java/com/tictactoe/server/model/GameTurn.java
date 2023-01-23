package com.tictactoe.server.model;

import lombok.Data;

import java.util.Map;

@Data
public class GameTurn {
    private String gameId;
    private Player player;
    private Map<String, Integer> coordinates;
}
