/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import top.moyingmoe.idontneedstone.config.Config;
import top.moyingmoe.idontneedstone.config.ConfigManager;
import top.moyingmoe.idontneedstone.hotkey.HotkeyHandler;
import top.moyingmoe.idontneedstone.hotkey.HotkeyManager;

@Environment(EnvType.CLIENT)
public class IDontNeedStoneClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Config config = ConfigManager.initializeConfig();

        HotkeyManager.initial();
        HotkeyManager.addHotkey("filter", new HotkeyHandler(config.getFilterHotkey()) {
            @Override
            protected void onPress(MinecraftClient client) {
                Config config = ConfigManager.getConfig();
                boolean isOn = !config.getIsFilterOn();
                config.setIsFilterOn(isOn);

                Text modName = Text.translatable("idontneedstone.name.bracket", Text.translatable("idontneedstone.name")).formatted(Formatting.GOLD);
                Text titleText = Text.translatable("idontneedstone.screen.title.filter").formatted(Formatting.WHITE);

                Text titleText2;
                if (isOn) {
                    titleText2 = Text.translatable("idontneedstone.screen.title.filter.on").formatted(Formatting.GREEN).formatted(Formatting.BOLD);
                } else {
                    titleText2 = Text.translatable("idontneedstone.screen.title.filter.off").formatted(Formatting.RED).formatted(Formatting.BOLD);
                }
                MutableText result = modName.copy();
                result.append(titleText);
                result.append(titleText2);
                client.inGameHud.setOverlayMessage(result, true);
            }
            @Override
            protected void onHold(MinecraftClient client) {

            }
            @Override
            protected void onRelease(MinecraftClient client) {

            }
        });
    }
}
