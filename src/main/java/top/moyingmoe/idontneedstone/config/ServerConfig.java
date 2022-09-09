/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.config;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import top.moyingmoe.idontneedstone.IDontNeedStone;

import java.util.*;

public class ServerConfig {
    private boolean isFilterOn = false;
    private boolean isFilterDisappeared = false;
    private int disappearedTicks = 6000;
    private boolean autoDrop = false;
    private int autoDropKeepItemStacks = 0;
    private List<String> blacklist = new ArrayList<>();

    private final transient Set<Item> blacklistItem = new HashSet<>();      // 代码中使用的列表


    public ServerConfig() {
        initial();
    }

    /**
     * @return 对象的json字符串
     */
    public String toJsonString() {
        return IDontNeedStone.GSON.toJson(this);
    }

    /**
     * 从字符串创建对象
     * @param jsonString json字符串
     * @return 创建的新对象
     */
    public static ServerConfig fromJsonString(String jsonString) {
        ServerConfig config = IDontNeedStone.GSON.fromJson(jsonString, ServerConfig.class);
        config.setBlacklistItem();
        return config;
    }

    /**
     * 初始化对象
     */
    public void initial() {
        setBlacklistItem();
    }

    public boolean getIsFilterOn() {
        return isFilterOn;
    }

    public void setIsFilterOn(boolean filterOn) {
        isFilterOn = filterOn;
    }

    public boolean getIsFilterDisappeared() {
        return isFilterDisappeared;
    }

    public void setIsFilterDisappeared(boolean filterDisappeared) {
        isFilterDisappeared = filterDisappeared;
    }

    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist;
        setBlacklistItem();
    }

    public Set<Item> getBlacklistItem() {
        return blacklistItem;
    }

    private void setBlacklistItem() {
        Set<String> blacklistSet = new HashSet<>(blacklist);
        blacklistItem.clear();

        for (String itemName :
                blacklistSet) {
            try {
                String[] temp = itemName.split(":");
                String namespace = temp[0];
                String path = temp[1];
                Optional<Item> newItem = Registry.ITEM.getOrEmpty(new Identifier(namespace, path));
                if(newItem.isEmpty()) {
                    throw new RuntimeException();
                }
                blacklistItem.add(newItem.get());
            } catch (Throwable e) {
                IDontNeedStone.LOGGER.warn("无法查找到的物品:" + itemName);
            }
        }
    }

    public int getDisappearedTicks() {
        return disappearedTicks;
    }

    public void setDisappearedTicks(int disappearedTicks) {
        this.disappearedTicks = disappearedTicks;
    }

    public boolean getIsAutoDrop() {
        return autoDrop;
    }

    public void setIsAutoDrop(boolean autoDrop) {
        this.autoDrop = autoDrop;
    }

    public int getAutoDropKeepItemStacks() {
        return autoDropKeepItemStacks;
    }

    public void setAutoDropKeepItemStacks(int autoDropKeepItemStacks) {
        this.autoDropKeepItemStacks = autoDropKeepItemStacks;
    }
}
