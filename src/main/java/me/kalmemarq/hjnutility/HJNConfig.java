package me.kalmemarq.hjnutility;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

@Config(name = HJNUtilityMod.MOD_ID)
public class HJNConfig implements ConfigData {
    public General general = new General();
    public Modules modules = new Modules();
    public Themes themes = new Themes();

    public enum CrosshairModifier {
        Normal,
        White,
        Chroma,
        Red,
        Green,
        Blue,
        Yellow,
        Aqua
    }
    public enum Theme {
        Default,
        Custom,
        Bedrock
    }

    public static class General {
        public boolean hideMsg = false;
        public boolean shinyPotions = false;
        public boolean armorHud = false;
        public boolean mainHandSlot = false;
        public boolean offHandSlot = true;
    }

    public static class Modules {
        public boolean showPaperdoll = false;
        public boolean hideMobEffects = false;
        public boolean showItemID = false;
        public boolean showCompass = false;
        public boolean hideBossBars = false;
        public boolean hideVignette = false;
    }

    public static class Themes {
        public Theme theme = Theme.Default;
        public boolean hideScreenBackground = false;
    }

    public static class CrosshairMode {
        public float scale = 1.0f;
        public int crosshairIndex = 0;
        public CrosshairModifier modifier = CrosshairModifier.Normal;
    }
}
