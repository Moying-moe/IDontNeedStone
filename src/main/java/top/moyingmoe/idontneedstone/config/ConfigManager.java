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

/**
 * <p>在Client端管理和储存用户Config</p>
 * <p><b>这个类不能在Server环境中被调用!</b></p>
 */
public class ConfigManager {
    private static File file;
    private static Config config;

    /**
     * 准备config文件。如果不存在，则创建一个新的
     */
    private static void prepareConfigFile() {
        if (file != null) {
            return;
        }
        file = FabricLoader.getInstance().getConfigDir().resolve(IDontNeedStone.MOD_ID+".json").toFile();
    }

    /**
     * 初始化并读取配置文件中的config数据（如果有）
     * @return 读取到的config对象
     */
    public static Config initializeConfig() {
        if (config != null) {
            return config;
        }

        config = new Config();
        load();

        return config;
    }

    /**
     * 从本地配置文件中读取config的数据
     */
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

    /**
     * <p>将config保存至本地配置文件中</p>
     * <p>如果玩家已经登入了服务器，那么还会向服务器发起一次config同步请求</p>
     */
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

    /**
     * 获取当前的config
     * @return config
     */
    public static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }
}