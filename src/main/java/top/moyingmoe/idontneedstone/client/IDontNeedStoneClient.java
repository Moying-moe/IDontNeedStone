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
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import top.moyingmoe.idontneedstone.config.Config;
import top.moyingmoe.idontneedstone.config.ConfigManager;
import top.moyingmoe.idontneedstone.hotkey.HotkeyHandler;
import top.moyingmoe.idontneedstone.hotkey.HotkeyManager;
import top.moyingmoe.idontneedstone.network.ClientConfigSyncNetworkHelper;

@Environment(EnvType.CLIENT)
public class IDontNeedStoneClient implements ClientModInitializer {
    public static boolean inServer;     // 用于标记用户是否正在服务器中（正在游戏中） 只有当inServer为true时 从modmenu中修改设置才会引起同步

    @Override
    public void onInitializeClient() {
        inServer = false;

        // 仅在client端初始化config 并注册热键
        Config config = ConfigManager.initializeConfig();

        HotkeyManager.initial();
        HotkeyManager.addHotkey("filter", new HotkeyHandler(config.getFilterHotkey()) {
            @Override
            protected void onPress(MinecraftClient client) {
                Config config = ConfigManager.getConfig();
                boolean isOn = !config.getIsFilterOn();
                config.setIsFilterOn(isOn);

                // 通过热键修改设置以后 也需要进行config同步
                // 我不太确定热键是否会在游戏外触发 所以还是进行一下inServer判定
                if (IDontNeedStoneClient.inServer) {
                    ClientConfigSyncNetworkHelper.sendConfigToServer(config);
                }

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

        // 用户加入服务器的时候 就向服务器发起一次config同步请求
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            IDontNeedStoneClient.inServer = true;
            ClientConfigSyncNetworkHelper.sendConfigToServer(ConfigManager.getConfig());
        });
        // 退出时设置inServer为false
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            IDontNeedStoneClient.inServer = false;
        });
    }
}
