/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.config;

import net.fabricmc.loader.api.FabricLoader;
import top.moyingmoe.idontneedstone.IDontNeedStone;
import top.moyingmoe.idontneedstone.client.IDontNeedStoneClient;
import top.moyingmoe.idontneedstone.network.ClientConfigSyncNetworkHelper;

import java.io.*;

public class ConfigManager {
    private static File file;
    private static Config config;

    private static void prepareConfigFile() {
        if (file != null) {
            return;
        }
        file = FabricLoader.getInstance().getConfigDir().resolve(IDontNeedStone.MOD_ID+".json").toFile();
    }

    public static Config initializeConfig() {
        if (config != null) {
            return config;
        }

        config = new Config();
        load();

        return config;
    }

    private static void load() {
        prepareConfigFile();

        try {
            if (!file.exists()) {
                save();
            }
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));

                Config parsed = IDontNeedStone.GSON.fromJson(br, Config.class);
                if (parsed != null) {
                    config = parsed;
                    config.initial();
                }
            }
        } catch (FileNotFoundException e) {
            IDontNeedStone.LOGGER.error("Couldn't load config. Reverting to defaults");
            e.printStackTrace();
        }
    }

    public static void save() {
        // 如果玩家正在服务器中游玩 通过modmenu修改设置 那么就还要向服务器发送同步请求
        if (IDontNeedStoneClient.inServer) {
            ClientConfigSyncNetworkHelper.sendConfigToServer(config);
        }
        prepareConfigFile();

        String jsonString = IDontNeedStone.GSON.toJson(config);

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            IDontNeedStone.LOGGER.error("Couldn't save config.");
            e.printStackTrace();
        }
    }

    public static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }
}