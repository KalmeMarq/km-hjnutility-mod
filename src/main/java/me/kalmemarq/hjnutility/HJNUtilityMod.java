package me.kalmemarq.hjnutility;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class HJNUtilityMod implements ModInitializer {
	public static final String MOD_ID = "kmhjnutility";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static HJNConfig config;

	@Override
	public void onInitialize() {
		AutoConfig.register(HJNConfig.class, Toml4jConfigSerializer::new);
		config = AutoConfig.getConfigHolder(HJNConfig.class).getConfig();

		AutoConfig.getConfigHolder(HJNConfig.class).save();
	}
}
