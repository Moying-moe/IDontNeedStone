/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.config;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 服务器端缓存用户Config的类
 */
public class ServerConfigCache {
    private static final Map<UUID, Config> CONFIG_CACHE = new HashMap<>();

    public static void updatePlayerConfig(UUID uuid, Config config) {
        CONFIG_CACHE.put(uuid, config);
    }

    @Nullable
    public static Config getPlayerConfig(UUID uuid) {
        return CONFIG_CACHE.get(uuid);
    }
}
