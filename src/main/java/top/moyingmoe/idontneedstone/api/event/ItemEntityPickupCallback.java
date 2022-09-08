/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface ItemEntityPickupCallback {
    Event<ItemEntityPickupCallback> EVENT = EventFactory.createArrayBacked(ItemEntityPickupCallback.class,
            (listeners) -> (player, item, callback) -> {
        for (ItemEntityPickupCallback event : listeners) {
            event.onEntityItemPickup(player, item, callback);
        }
    });

    void onEntityItemPickup(PlayerEntity player, ItemEntity item, CallbackInfo callback);
}