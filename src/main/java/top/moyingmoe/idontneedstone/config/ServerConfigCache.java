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

    /**
     * <p>缓存并更新玩家设置</p>
     * <p>如果玩家已有设置，会覆盖原先的设置。</p>
     * <p>如果玩家没有设置，那么会创建一个新的。</p>
     * @param uuid 玩家uuid
     * @param config 玩家设置
     */
    public static void updatePlayerConfig(UUID uuid, Config config) {
        CONFIG_CACHE.put(uuid, config);
    }

    /**
     * <p>获取uuid对应的玩家的设置</p>
     * <p>如果玩家未曾同步过设置，则会返回null</p>
     * @param uuid 玩家uuid
     * @return 玩家设置
     */
    @Nullable
    public static Config getPlayerConfig(UUID uuid) {
        return CONFIG_CACHE.get(uuid);
    }
}
