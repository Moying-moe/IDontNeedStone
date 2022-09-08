/*
 * Copyright 2022 moying All Rights Reserved.
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.moyingmoe.idontneedstone.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import top.moyingmoe.idontneedstone.config.ConfigScreen;

public class ModMenuApiEntry implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreen::getScreen;
    }
}
