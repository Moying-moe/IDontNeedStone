/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import top.moyingmoe.idontneedstone.IDontNeedStone;
import top.moyingmoe.idontneedstone.config.Config;

/**
 * 用于向服务器发送本地的config信息
 * 服务器接受到后 将其缓存在ServerConfigCache类中
 */
public class ClientConfigSyncNetworkHelper {
    /**
     * 向服务器发送config数据，使服务器缓存并更新玩家的设置
     * @param config 向服务器发送的config数据对象
     */
    public static void sendConfigToServer(Config config) {
        PacketByteBuf configBuf = PacketByteBufs.create();
        configBuf.writeString(config.toServerConfig().toJsonString());
        ClientPlayNetworking.send(IDontNeedStone.CONFIG_SYNC_PACKET_ID, configBuf);
    }
}
