package com.tictactoe.server.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Player {
    @NonNull
    private String name;
    private TicToe sign;
}
