package com.honeysurvival.core.loaders;

import com.honeysurvival.core.models.MineCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MineClusterLoader implements CoreLoader<MineCluster>  {
    protected ArrayList<MineCluster> cached;

    @Override
    public void onEnable() {
        this.cached = new ArrayList<>();
    }

    @Override
    public void save(MineCluster mineCluster) {
        cached.add(mineCluster);
    }

    @Override
    public Optional<MineCluster> load(String id) {
        return cached.stream().filter(c -> c.getUuid().toString().equals(id)).findAny();
    }

    @Override
    public List<MineCluster> all() {
        return cached;
    }

    @Override
    public List<MineCluster> filtered(Predicate<MineCluster> filter) {
        return cached.stream().filter(filter).toList();
    }
}
