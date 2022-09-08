/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.hotkey;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

import java.util.HashMap;
import java.util.Map;

public class HotkeyManager {
    private static final Map<String,HotkeyHandler> hotkey = new HashMap<>();

    public static void initial() {
        ClientTickEvents.END_CLIENT_TICK.register(HotkeyManager::tick);
    }

    public static void addHotkey(String id, HotkeyHandler handler) {
        hotkey.put(id, handler);
    }

    public static void modifyHotkey(String id, InputUtil.Key newKey) {
        hotkey.get(id).setHotkey(newKey);
    }

    public static void tick(MinecraftClient client) {
        hotkey.forEach((id, handler) -> handler.tick(client));
    }
}
