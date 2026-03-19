package com.allaymc.practice.duel;

import com.allaymc.practice.arena.Arena;

import java.util.UUID;

public class Match {

    private final UUID player1;
    private final UUID player2;
    private final String kitName;
    private final Arena arena;
    private MatchState state;

    public Match(UUID player1, UUID player2, String kitName, Arena arena) {
        this.player1 = player1;
        this.player2 = player2;
        this.kitName = kitName;
        this.arena = arena;
        this.state = MatchState.STARTING;
    }

    public UUID getPlayer1() {
        return player1;
    }

    public UUID getPlayer2() {
        return player2;
    }

    public String getKitName() {
        return kitName;
    }

    public Arena getArena() {
        return arena;
    }

    public MatchState getState() {
        return state;
    }

    public void setState(MatchState state) {
        this.state = state;
    }

    public UUID getOpponent(UUID uuid) {
        if (player1.equals(uuid)) return player2;
        if (player2.equals(uuid)) return player1;
        return null;
    }

    public boolean contains(UUID uuid) {
        return player1.equals(uuid) || player2.equals(uuid);
    }
}
