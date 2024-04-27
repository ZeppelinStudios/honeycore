package com.honeysurvival.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
@Setter
public class Job {
    private JobData data;
    private int basePay;
    private int levelMultiplier;
}

@Getter
@Setter
@AllArgsConstructor
class JobData {
    String name;
    String description;
    String fullDescription;
    String displayName;
    Material icon;
}
