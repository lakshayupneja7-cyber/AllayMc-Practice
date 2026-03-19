package com.allaymc.practice.player;

import java.util.UUID;

public class PracticePlayer {

    private final UUID uuid;
    private PlayerState state;

    public PracticePlayer(UUID uuid) {
        this.uuid = uuid;
        this.state = PlayerState.LOBBY;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }
}
