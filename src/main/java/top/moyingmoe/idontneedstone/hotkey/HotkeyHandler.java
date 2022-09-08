/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.hotkey;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;

public abstract class HotkeyHandler {
    private InputUtil.Key hotkey;
    private boolean isOn = false;

    protected abstract void onPress(MinecraftClient client);      // 按下时触发一次
    protected abstract void onHold(MinecraftClient client);       // 按下时持续触发
    protected abstract void onRelease(MinecraftClient client);    // 松开时触发一次

    public HotkeyHandler(InputUtil.Key hotkey) {
        this.hotkey = hotkey;
    }

    public void setHotkey(InputUtil.Key hotkey) {
        this.hotkey = hotkey;
    }

    public void tick(MinecraftClient client) {
        boolean nowIsOn = InputUtil.isKeyPressed(client.getWindow().getHandle(), hotkey.getCode());
        if (nowIsOn) {
            // 不管什么情况，只要这一帧在按 就触发onHold
            onHold(client);
        }
        if (!isOn && nowIsOn) {
            // 之前未按下 这一tick按下了 触发onPress
            onPress(client);
            isOn = true;
        } else if (isOn && !nowIsOn) {
            // 之前正在按 这一tick不按了 触发onRelease
            onRelease(client);
            isOn = false;
        }
    }
}
