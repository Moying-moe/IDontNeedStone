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
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.moyingmoe.idontneedstone.api.event.ItemEntityPickupCallback;
import top.moyingmoe.idontneedstone.config.Config;
import top.moyingmoe.idontneedstone.config.ServerConfigCache;
import top.moyingmoe.idontneedstone.handler.FabricItemPickupHandler;

public class IDontNeedStone implements ModInitializer {
    public static final String MOD_ID = "idontneedstone";
    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    public static final Logger LOGGER = LogManager.getLogger("IDontNeedStone");
    public static final Identifier CONFIG_SYNC_PACKET_ID = new Identifier(MOD_ID, "config_sync_packet");

    @Override
    public void onInitialize() {
        // 注册拾取事件
        final FabricItemPickupHandler itemPickupHandler = new FabricItemPickupHandler();
        ItemEntityPickupCallback.EVENT.register(itemPickupHandler::onEntityItemPickup);

        // 注册接收事件 接收来自客户端的config同步请求
        ServerPlayNetworking.registerGlobalReceiver(IDontNeedStone.CONFIG_SYNC_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            String jsonString = buf.readString();
            Config config = Config.fromJsonString(jsonString);
            ServerConfigCache.updatePlayerConfig(player.getUuid(), config);
        });
    }
}
