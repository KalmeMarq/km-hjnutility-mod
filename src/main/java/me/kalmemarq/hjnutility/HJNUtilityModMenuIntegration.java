package me.kalmemarq.hjnutility;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.kalmemarq.hjnutility.client.gui.HJNUtilityScreen;

public class HJNUtilityModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new HJNUtilityScreen(parent);
    }
}
