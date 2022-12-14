package me.kalmemarq.hjnutility;

import me.kalmemarq.hjnutility.gui.HJNUtilityScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class HJNUtilityClient implements ClientModInitializer {
    public static final KeyBinding UTILITY_KBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding("Open Utility Screen", InputUtil.Type.KEYSYM, InputUtil.GLFW_KEY_J, "HJN"));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (UTILITY_KBIND.isPressed()) {
                if (client != null && client.currentScreen instanceof HJNUtilityScreen) {
                    client.setScreen(null);
                } else {
                    client.setScreen(new HJNUtilityScreen(null));
                }
            }
        });
    }
}
