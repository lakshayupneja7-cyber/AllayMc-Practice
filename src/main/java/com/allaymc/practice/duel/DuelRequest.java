package com.allaymc.practice.duel;

import java.util.UUID;

public class DuelRequest {

    private final UUID sender;
    private final UUID target;
    private final String kitName;
    private final long createdAt;

    public DuelRequest(UUID sender, UUID target, String kitName) {
        this.sender = sender;
        this.target = target;
        this.kitName = kitName;
        this.createdAt = System.currentTimeMillis();
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getTarget() {
        return target;
    }

    public String getKitName() {
        return kitName;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() - createdAt > 30000;
    }
}
