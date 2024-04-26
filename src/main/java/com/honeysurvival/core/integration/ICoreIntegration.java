package com.honeysurvival.core.integration;

public interface ICoreIntegration {
    default void onLoad() {}
    default void onEnable() {}
    default void onDisable() {}
}
