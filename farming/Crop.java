package com.minhafarm.farming;

public class Crop {
    private final String name;
    private final float yaw;
    private final float pitch;

    public Crop(String name, float yaw, float pitch) {
        this.name = name;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public String getName() {
        return name;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}