/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.moyingmoe.idontneedstone.api.event.ItemEntityPickupCallback;
import top.moyingmoe.idontneedstone.config.Config;
import top.moyingmoe.idontneedstone.config.ConfigManager;
import top.moyingmoe.idontneedstone.handler.FabricItemPickupHandler;
import top.moyingmoe.idontneedstone.hotkey.HotkeyHandler;
import top.moyingmoe.idontneedstone.hotkey.HotkeyManager;

public class IDontNeedStone implements ModInitializer {
    public static final String MOD_ID = "idontneedstone";
    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    public static final Logger LOGGER = LogManager.getLogger("IDontNeedStone");

    @Override
    public void onInitialize() {
        final FabricItemPickupHandler itemPickupHandler = new FabricItemPickupHandler();
        ItemEntityPickupCallback.EVENT.register(itemPickupHandler::onEntityItemPickup);

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
