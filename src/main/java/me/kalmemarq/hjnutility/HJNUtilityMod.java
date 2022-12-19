package me.kalmemarq.hjnutility;

import me.kalmemarq.hjnutility.util.RenderUtil;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

import java.util.Optional;

public class HJNUtilityMod implements ModInitializer {
	public static final Identifier HJN_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_utility.png");
	public static final Identifier HJN_THEME_TEXTURE = Identifier.of(HJNUtilityMod.MOD_ID, "textures/gui/hjn_themes.png");
	public static final RenderUtil.NinesliceInfo HJN_PANEL_INFO = new RenderUtil.NinesliceInfo(3, 3, 3, 3, 256, 256);

	public static final String MOD_ID = "kmhjnutility";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static HJNConfig config;

	@Override
	public void onInitialize() {
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(HJNUtilityMod.MOD_ID);
		modContainer.ifPresent(container -> ResourceManagerHelper.registerBuiltinResourcePack(
			new Identifier("hjn_utility_programmer_art"),
			container,
			Text.literal("HJN Utility (Programmer Art)"),
			ResourcePackActivationType.NORMAL
		));

		config = HJNConfig.load();
	}
}
