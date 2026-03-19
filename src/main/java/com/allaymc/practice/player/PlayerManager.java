package com.allaymc.practice.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, PracticePlayer> players = new HashMap<>();

    public PracticePlayer getOrCreate(UUID uuid) {
        return players.computeIfAbsent(uuid, PracticePlayer::new);
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }
}
