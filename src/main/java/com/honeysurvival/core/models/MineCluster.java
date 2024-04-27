package com.honeysurvival.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class MineCluster {
    private UUID uuid;
    private Player player;
    private Material material;
    private int amount;
}