package com.allaymc.practice.arena;

import org.bukkit.Location;

public class Arena {

    private final String name;
    private final String kitName;
    private final Location spawn1;
    private final Location spawn2;
    private ArenaState state;

    public Arena(String name, String kitName, Location spawn1, Location spawn2) {
        this.name = name;
        this.kitName = kitName;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
        this.state = ArenaState.FREE;
    }

    public String getName() {
        return name;
    }

    public String getKitName() {
        return kitName;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public ArenaState getState() {
        return state;
    }

    public void setState(ArenaState state) {
        this.state = state;
    }
}
