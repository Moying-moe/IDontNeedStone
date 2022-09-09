/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.handler;


import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.stat.Stats;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.moyingmoe.idontneedstone.config.ServerConfig;
import top.moyingmoe.idontneedstone.config.ServerConfigCache;

import java.util.Objects;
import java.util.Set;

public class FabricItemPickupHandler {
    /**
     * 玩家拾取物品时触发事件
     * @param player 玩家对象
     * @param itemEntity 地面上的掉落物实体
     * @param callback 原版事件回调 调用callback.cancel()会提前中止原版拾取逻辑的执行
     */
    public void onEntityItemPickup(PlayerEntity player, ItemEntity itemEntity, CallbackInfo callback) {
        if (player.isCreative() || player.isSpectator()) {
            // 创造模式和旁观模式将不会生效
            return;
        }

        // 只有玩家可以拾取的时候才会检测
        if (!itemEntity.cannotPickup() && (itemEntity.getOwner() == null || itemEntity.getOwner().equals(player.getUuid()))) {
            // 尝试从configCache里获取玩家的config 如果为null的话 说明该玩家没有在本地安装此mod 那么就直接结束事件
            ServerConfig config = ServerConfigCache.getPlayerConfig(player.getUuid());
            if (Objects.isNull(config)) {
                return;
            }

            // 玩家是否开启过滤 如果没开启 那么所有功能都不会生效
            if (config.getIsFilterOn()) {
                ItemStack itemStack = itemEntity.getStack();
                Item item = itemStack.getItem();

                if (config.getBlacklistItem().contains(item)) {
                    // 如果物品在黑名单中 则对地面上的物品进行操作
                    if (config.getIsFilterDisappeared()) {
                        // 物品更快消失
                        NbtCompound nbt = new NbtCompound();
                        itemEntity.writeCustomDataToNbt(nbt);
                        int newAge = Math.max(nbt.getShort("Age"), 6000 - config.getDisappearedTicks());
                        nbt.putShort("Age", (short)newAge);
                        itemEntity.readCustomDataFromNbt(nbt);
                    }

                    // 重置物品拾取延迟 防止每一tick都触发本mod的逻辑
                    itemEntity.setToDefaultPickupDelay();

                    // 这里不取消原版逻辑的执行也是可以的 因为原版后续的逻辑会检查pickup delay
                    // 而我们已经将它设置为一个非0的值了 那么物品必定不会被拾取
                    // 这样做的好处是不容易和其他会注入此函数的mod起冲突
                    callback.cancel();
                } else if (config.getIsAutoDrop()) {
                    // 如果物品不在黑名单中 且玩家开启了自动丢弃功能
                    PlayerInventory inventory = player.getInventory();
                    if (inventory.insertStack(itemStack)) {
                        // 成功进行了拾取 这一部分的逻辑和原版原版逻辑是完全相同的
                        int i = itemStack.getCount();
                        player.sendPickup(itemEntity, i);
                        if (itemStack.isEmpty()) {
                            itemEntity.discard();
                            itemStack.setCount(i);
                        }
                        player.increaseStat(Stats.PICKED_UP.getOrCreateStat(item), i);
                        player.triggerItemPickedUpByEntityCriteria(itemEntity);

                        // 因为已经执行了原版逻辑 所以这里必须结束此函数 不再回到原版逻辑中进行执行
                        /* FIXME * 请注意! 这样做可能会和其他mod发生冲突
                                 * 因为它提前结束了原版函数的执行 如果其他mod在本注入点之后注入了逻辑
                                 * 那么那个mod的逻辑将不会被执行
                                 * 必须要结束事件的原因是: insertStack会完成向玩家背包加入物品的行为
                                 * 而我们实际上想要的 是检查玩家是否可以拾取此物品 而非真正去拾取
                                 * 因此 我们需要一个检查玩家是否可以拾取的逻辑来取代这一部分
                         */
                        callback.cancel();
                    } else {
                        // 未能成功拾取 则说明玩家背包已满 此时查找背包中是否存在黑名单物品 如果有 则丢出
                        int keepStackCount = config.getAutoDropKeepItemStacks();
                        Set<Item> blacklist = config.getBlacklistItem();

                        // TODO 找到黑名单物品中物品数量最少的一组
                        for (int i = 0; i < inventory.main.size(); ++i) {
                            ItemStack stack = inventory.main.get(i);
                            if (stack.isEmpty() || !blacklist.contains(stack.getItem())) {
                                continue;
                            }
                            if (keepStackCount == 0) {
                                ItemStack dropStack = inventory.getStack(i);
                                player.dropStack(dropStack);
                                inventory.removeStack(i);
                                break;
                            } else {
                                // 跳过前config.getAutoDropKeepItemStacks()组黑名单物品
                                // 这样的话 背包里就会至少留下这么多组黑名单物品不会被自动丢弃
                                keepStackCount--;
                            }
                        }

                        // 那么此时 玩家背包就会至少有一个空位了 我们可以返回到原版逻辑中 让原版逻辑处理这个物品的拾取动作
                    }

                }
            }
        }

    }
}