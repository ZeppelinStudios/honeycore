package com.honeysurvival.core.loaders;

import com.honeysurvival.core.component.PluginComponent;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface CoreLoader<T> extends PluginComponent {
    void save(T t);
    Optional<T> load(String id);
    List<T> all();
    List<T> filtered(Predicate<T> filter);
}