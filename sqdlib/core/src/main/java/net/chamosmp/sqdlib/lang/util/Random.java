package net.chamosmp.sqdlib.lang.util;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Random {
    public @Nullable <T> T getRandomWithChance(@NonNull Map<@Nullable T, @NonNull Integer> map) {
        List<T> list = new ArrayList<>(map.keySet());
        T selected = null;

        int attempts = 0;
        while (attempts < 10) {
            T reward = list.get(ThreadLocalRandom.current().nextInt(list.size()));
            double chance = ThreadLocalRandom.current().nextDouble(0, 100);
            int rewardChance = map.get(reward);
            if (rewardChance >= 100 || rewardChance >= chance) {
                selected = reward;
                break;
            }
            attempts++;
        }

        return selected;
    }
}
