package com.tictactoe.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Player {
    @NonNull
    private String name;
    private TicToe sign;
    private boolean acceptRematch;
}
