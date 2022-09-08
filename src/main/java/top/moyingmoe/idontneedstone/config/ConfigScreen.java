/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public class ConfigScreen {
    public static Screen getScreen(Screen parent) {
        Config config = ConfigManager.getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("idontneedstone.config.menu"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // 通用
        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("idontneedstone.config.general"));

        general.addEntry(
                entryBuilder.startBooleanToggle(Text.translatable("idontneedstone.config.option.isfilteron"),config.getIsFilterOn())
                        .setDefaultValue(Config.Default.isFilterOn)
                        .setTooltip(Text.translatable("idontneedstone.config.option.isfilteron.tooltip"))
                        .setSaveConsumer(config::setIsFilterOn)
                        .build()
        );

        general.addEntry(
                entryBuilder.startStrList(Text.translatable("idontneedstone.config.option.blacklist"), config.getBlacklist())
                        .setDefaultValue(Config.Default.blacklist)
                        .setCreateNewInstance((entry) -> new StringListListEntry.StringListCell("minecraft:", entry))
                        .setCellErrorSupplier(itemName -> {
                            try {
                                String[] temp = itemName.split(":");
                                String namespace = temp[0];
                                String path = temp[1];
                                Optional<Item> newItem = Registry.ITEM.getOrEmpty(new Identifier(namespace, path));
                                if (newItem.isEmpty()) {
                                    throw new RuntimeException("can not find " + itemName);
                                }
                            } catch (Throwable e) {
                                return Optional.of(Text.of("无法查找到的物品:" + itemName));
                            }
                            return Optional.empty();
                        })
                        .setTooltip(Text.translatable("idontneedstone.config.option.blacklist.tooltip"))
                        .setSaveConsumer(config::setBlacklist)
                        .build()
        );

        // 快捷键
        ConfigCategory hotkey = builder.getOrCreateCategory(Text.translatable("idontneedstone.config.hotkey"));

        hotkey.addEntry(
                entryBuilder.startKeyCodeField(Text.translatable("idontneedstone.config.option.filterhotkey"), config.getFilterHotkey())
                        .setDefaultValue(Config.Default.filterHotkey)
                        .setTooltip(Text.translatable("idontneedstone.config.option.filterhotkey.tooltip"))
                        .setKeySaveConsumer(config::setFilterHotkey)
                        .build()
        );

        // 额外
        ConfigCategory extra = builder.getOrCreateCategory(Text.translatable("idontneedstone.config.extra"));

        extra.addEntry(
                entryBuilder.startBooleanToggle(Text.translatable("idontneedstone.config.option.isfilterdisappeared"),config.getIsFilterDisappeared())
                        .setDefaultValue(Config.Default.isFilterDisappeared)
                        .setTooltip(Text.translatable("idontneedstone.config.option.isfilterdisappeared.tooltip"))
                        .setSaveConsumer(config::setIsFilterDisappeared)
                        .build()
        );

        extra.addEntry(
                entryBuilder.startIntField(Text.translatable("idontneedstone.config.option.disappearedtick"), config.getDisappearedTicks())
                        .setDefaultValue(Config.Default.disappearedTicks)
                        .setMin(0)
                        .setMax(6000)
                        .setTooltip(Text.translatable("idontneedstone.config.option.disappearedtick.tooltip"))
                        .setSaveConsumer(config::setDisappearedTicks)
                        .build()
        );

        extra.addEntry(
                entryBuilder.startBooleanToggle(Text.translatable("idontneedstone.config.option.autodrop"), config.getIsAutoDrop())
                        .setDefaultValue(Config.Default.autoDrop)
                        .setTooltip(Text.translatable("idontneedstone.config.option.autodrop"))
                        .setSaveConsumer(config::setIsAutoDrop)
                        .build()
        );

        extra.addEntry(
                entryBuilder.startIntField(Text.translatable("idontneedstone.config.option.autodropkeep"), config.getAutoDropKeepItemStacks())
                        .setDefaultValue(Config.Default.autoDropKeepItemStacks)
                        .setMin(0).setMax(36)
                        .setTooltip(Text.translatable("idontneedstone.config.option.autodropkeep.tooltip"))
                        .setSaveConsumer(config::setAutoDropKeepItemStacks)
                        .build()
        );


        builder
                .setSavingRunnable(ConfigManager::save)
                .setTransparentBackground(true);
        builder.setGlobalized(true);
        builder.setGlobalizedExpanded(true);

        return builder.build();
    }
}
