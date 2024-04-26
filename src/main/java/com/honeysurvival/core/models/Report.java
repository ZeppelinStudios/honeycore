package com.honeysurvival.core.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Report {
    private UUID uuid;
    private UUID reporter;
    private UUID reported;
    private String reason;
    private boolean open;

    @Override
    public String toString() {
        return "Report{" +
                "uuid=" + uuid +
                ", reporter=" + reporter +
                ", reported=" + reported +
                ", reason='" + reason + '\'' +
                ", open=" + open +
                '}';
    }
}