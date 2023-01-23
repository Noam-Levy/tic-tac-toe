package com.tictactoe.server.model;


public enum TicToe {
    X(1), O(2);

    private Integer value;

    TicToe(int value) { this.value = value; }

    public int getValue() {
        return value;
    }

}
