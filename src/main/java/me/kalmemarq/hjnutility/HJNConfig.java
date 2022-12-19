package me.kalmemarq.hjnutility;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

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
    }

    public enum CrosshairModifier {
        Inverted,
        Chroma,
        White,
        Red,
        Green,
        Blue,
        Yellow,
        Aqua
    }

    public static class CrosshairMode {
        public float scale = 1.0f;
        public int crosshairIndex = 0;
        public CrosshairModifier modifier = CrosshairModifier.Inverted;
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
            return config;
        } catch (Exception ignored) {
            return config;
        }
    }

    public void save() {
        if (configFile == null) return;

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
}
