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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.moyingmoe.idontneedstone.api.event.ItemEntityPickupCallback;
import top.moyingmoe.idontneedstone.handler.FabricItemPickupHandler;

public class IDontNeedStone implements ModInitializer {
    public static final String MOD_ID = "idontneedstone";
    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    public static final Logger LOGGER = LogManager.getLogger("IDontNeedStone");

    @Override
    public void onInitialize() {
        final FabricItemPickupHandler itemPickupHandler = new FabricItemPickupHandler();
        ItemEntityPickupCallback.EVENT.register(itemPickupHandler::onEntityItemPickup);
    }
}
