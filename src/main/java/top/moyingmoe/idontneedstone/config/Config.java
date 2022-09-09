/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.config;

import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import top.moyingmoe.idontneedstone.IDontNeedStone;
import top.moyingmoe.idontneedstone.hotkey.HotkeyManager;

import java.util.*;

/**
 * 玩家的设置类
 */
public class Config {
    /**
     * 所有配置选项的默认值
     */
    public static class Default {
        public static final boolean isFilterOn = true;
        public static final InputUtil.Key filterHotkey = InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_U);
        public static final boolean isFilterDisappeared = false;
        public static final int disappearedTicks = 200;
        public static final boolean autoDrop = false;
        public static final int autoDropKeepItemStacks = 2;
        public static final ArrayList<String> blacklist = new ArrayList<>(Arrays.asList(
                "minecraft:cobbled_deepslate",
                "minecraft:cobblestone",
                "minecraft:oak_sapling",
                "minecraft:spruce_sapling",
                "minecraft:birch_sapling",
                "minecraft:jungle_sapling",
                "minecraft:acacia_sapling",
                "minecraft:dark_oak_sapling",
                "minecraft:rotten_flesh"
        ));
    }
    private boolean isFilterOn = Default.isFilterOn;         // 是否开启过滤
    private transient InputUtil.Key filterHotkey = Default.filterHotkey;  // 切换开启过滤的快捷键
    private int filterHotkeyCode = Default.filterHotkey.getCode();
    private boolean isFilterDisappeared = Default.isFilterDisappeared;    // 是否让过滤的物品更快消失（减少卡顿）
    private int disappearedTicks = Default.disappearedTicks;    // 消失速度
    private boolean autoDrop = Default.autoDrop;    // 自动丢弃
    private int autoDropKeepItemStacks = Default.autoDropKeepItemStacks;    // 自动丢弃最低保留数量
    private List<String> blacklist = Default.blacklist; // 过滤名单

    private final transient Set<Item> blacklistItem = new HashSet<>();      // 代码中使用的列表


    public Config() {
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
    public static Config fromJsonString(String jsonString) {
        return IDontNeedStone.GSON.fromJson(jsonString, Config.class);
    }

    /**
     * 初始化对象
     */
    public void initial() {
        setBlacklistItem();

        filterHotkey = InputUtil.Type.KEYSYM.createFromCode(filterHotkeyCode);
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

    public List<String> getBlacklist() {
        return blacklist;
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
                assert newItem.isPresent();
                blacklistItem.add(newItem.get());
            } catch (Throwable e) {
                IDontNeedStone.LOGGER.warn("无法查找到的物品:" + itemName);
            }
        }
    }

    public InputUtil.Key getFilterHotkey() {
        return filterHotkey;
    }

    public void setFilterHotkey(InputUtil.Key filterHotkey) {
        this.filterHotkey = filterHotkey;
        setFilterHotkeyCode();
        HotkeyManager.modifyHotkey("filter", this.filterHotkey);
    }

    public void setFilterHotkeyCode() {
        filterHotkeyCode = filterHotkey.getCode();
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
