package me.kalmemarq.hjnutility.client;

import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.client.gui.HJNUtilityScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HJNUtilityClientMod implements ClientModInitializer {
    public static final KeyBinding UTILITY_KBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding("kmhjnutility.key.openUtilityScreen", InputUtil.Type.KEYSYM, InputUtil.GLFW_KEY_J, "HJN"));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (UTILITY_KBIND.isPressed()) {
                client.setScreen(new HJNUtilityScreen(null));
            }
        });
    }
}
