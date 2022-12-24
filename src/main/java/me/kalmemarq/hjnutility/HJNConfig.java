package me.kalmemarq.hjnutility;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.kalmemarq.hjnutility.util.Anchor;
import me.kalmemarq.hjnutility.util.HJNCrosshair;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.MathHelper;

import java.io.*;

public class HJNConfig {
    public General general = new General();
    public Modules modules = new Modules();
    public Themes themes = new Themes();
    public CrosshairMode crosshairs = new CrosshairMode();

    public static class General {
        public boolean shinyPotions = false;
        public boolean armorHud = false;
        public boolean mainHandSlot = false;
        public boolean offHandSlot = true;
        public boolean statusHud = false;
    }

    public static class Modules {
        public boolean showPaperdoll = false;
        public boolean hideMobEffects = false;
        public boolean showItemId = false;
        public boolean showCompass = false;
        public boolean hideBossBars = false;
        public boolean hideVignette = false;
        public boolean hideTooltips = false;

        public ModuleSettings itemIdSettings = new ModuleSettings();
        public ModuleSettings compassSettings = new ModuleSettings();
        public ModuleSettings armorHudSettings = new ModuleSettings();
        public ModuleSettings mainHandSettings = new ModuleSettings();
        public ModuleSettings statusSettings = new ModuleSettings();
        public ModuleSettings paperDollSettings = new PaperDollModuleSettings();
    }

    public enum CrosshairModifier {
        Inverted,
        Chroma,
        White,
        Red,
        Green,
        Blue,
        Yellow,
        Aqua,
        Custom
    }

    public static class CrosshairMode {
        public float scale = 1.0f;
        public int crosshairIndex = 0;
        public CrosshairModifier modifier = CrosshairModifier.Inverted;
        public int crosshairColor = 0xFF_FF_FF;
    }

    public enum Theme {
        Default,
        Custom,
        Bedrock
    }

    public static class Themes {
        public Theme theme = Theme.Default;
        public boolean hideScreenBackground = false;
    }

    public static class ModuleSettings {
        public int x;
        public int y;
        public Anchor anchor;
    }

    public static class PaperDollModuleSettings extends ModuleSettings {
        public float scale;
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setLenient().create();
    private static File configFile;

    public static HJNConfig load() {
        HJNConfig config = new HJNConfig();

        try {
            if (configFile == null) {
                configFile = new File(FabricLoader.getInstance().getConfigDir().toAbsolutePath().toString().replace('\\', '/'), HJNUtilityMod.MOD_ID + ".json");
            }

            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            config = GSON.fromJson(builder.toString(), HJNConfig.class);
            config.validate();
            return config;
        } catch (Exception ignored) {
            return config;
        }
    }

    public void save() {
        if (configFile == null) return;

        this.validate();

        String output = GSON.toJson(this);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
            writer.write(output);
            writer.close();

            if (!configFile.exists()) {
                configFile.createNewFile();
            }
        } catch (Exception ignored) {
        }
    }

    private void validate() {
        this.crosshairs.crosshairIndex = MathHelper.clamp(this.crosshairs.crosshairIndex, 0, HJNCrosshair.CROSSHAIRS.size() - 1);
        this.crosshairs.scale = MathHelper.clamp(this.crosshairs.scale, 0.0f, 2.5f);
    }
}
