package me.kalmemarq.hjnutility.client.gui.page;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.HJNUtilityMod;
import me.kalmemarq.hjnutility.client.gui.widget.HJNIconButton;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.WrapperWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class HJNMusicConfigPage extends HJNConfigPage {
    private static MusicSound[] musics = {
        new MusicSound(SoundEvents.MUSIC_GAME, 0, 0, true),
        new MusicSound(SoundEvents.MUSIC_MENU, 0, 0, true),
        new MusicSound(SoundEvents.MUSIC_CREDITS, 0, 0, true),
        new MusicSound(SoundEvents.MUSIC_END, 0, 0, true),
        new MusicSound(SoundEvents.MUSIC_UNDER_WATER, 0, 0, true),
        new MusicSound(SoundEvents.MUSIC_DRAGON, 0, 0, true),
        new MusicSound(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST, 0, 0, true),
        new MusicSound(SoundEvents.MUSIC_NETHER_NETHER_WASTES, 0, 0, true),
        new MusicSound(SoundEvents.MUSIC_NETHER_WARPED_FOREST, 0, 0, true),
        new MusicSound(SoundEvents.MUSIC_NETHER_BASALT_DELTAS, 0, 0, true),
        new MusicSound(SoundEvents.MUSIC_CREATIVE, 0, 0, true)
    };
    private static String[] musicNames = {
        "Music: Game",
        "Music: Menu",
        "Music: Credits",
        "Music: End",
        "Music: Underwater",
        "Music: Dragon",
        "Music: Crimson Forest",
        "Music: Nether Wastes",
        "Music: Warped Forest",
        "Music: Basalt Deltas",
        "Music: Creative"
    };

    private int currentPage = 0;
    private int totalPages;

    private HJNIconButton prevBtn;
    private HJNIconButton nextBtn;

    private List<HJNMusicTrackerItem> items;

    public HJNMusicConfigPage(MinecraftClient client, TextRenderer textRenderer, int x, int y, int width, int height) {
        super(client, textRenderer, x, y, width, height, Text.translatable("kmhjnutility.config.music.title"));
        this.totalPages = (int)Math.ceil((float)musics.length / 4.0f);
    }

    @Override
    protected void init() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }

        this.items.clear();

        for (int i = 0; i < musics.length; i++) {
            this.items.add(new HJNMusicTrackerItem(getX() + 20, getY() + 20 + (i % 3) * 22, 200, 20, Text.literal(musicNames[i])));
        }

        this.items.forEach(this::add);

        this.prevBtn = new HJNIconButton(getX() + getWidth() / 2 - titleWidth / 2 - 11 - 4, getY() + 6, 11, 11, 160 - 16 - 16, 48, 16, 16, button -> {
           setCurrentPage(currentPage - 1);
        });

        this.nextBtn = new HJNIconButton(getX() + getWidth() / 2 + titleWidth / 2 + 4, getY() + 6, 11, 11, 160 - 16 - 16, 32, 16, 16, button -> {
            setCurrentPage(currentPage + 1);
        });

        this.add(this.prevBtn);
        this.add(this.nextBtn);

        setCurrentPage(currentPage);
    }

    public void setCurrentPage(int page) {
        currentPage = page;
        if (page < 0) {
            currentPage = 0;
        }
        if (page >= totalPages) {
            currentPage = totalPages - 1;
        }
//
//        if (this.prevBtn != null) this.prevBtn.visible = currentPage > 0;
//        if (this.nextBtn != null) this.nextBtn.visible = currentPage < this.totalPages - 1;

        for (int i = 0; i < items.size(); i++) {
            int pg = (int)(Math.ceil((float)i / 4.0f));

            items.get(i).visible = pg == page;
        }
    }

    @Override
    protected void renderPage(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderPage(matrices, mouseX, mouseY, delta);

        RenderUtil.drawCenteredText(matrices, textRenderer, Text.literal("Page " + (this.currentPage + 1) + "/" + this.totalPages), getX() + getWidth() / 2, getY() + 112, 0xFF_FFFFFF);
    }

    private static class HJNMusicTrackerItem extends WrapperWidget {
        private final List<HJNIconButton> buttons;

        public HJNMusicTrackerItem(int i, int j, int k, int l, Text text) {
            super(i, j, k, l, text);
            this.buttons = new ArrayList<>();
            this.buttons.add(new HJNIconButton(getX() + getWidth() - 22, getY() , 20, 20, 160, 32, 16, 16, button -> {

            }));
        }

        @Override
        protected List<? extends ClickableWidget> wrappedWidgets() {
            return this.buttons;
        }

        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderTexture(0, HJNUtilityMod.HJN_TEXTURE);

            RenderUtil.drawColoredNinesliceTexture(matrices, getX(), getY(), getWidth(), getHeight(), 0, 0, 16, 16, HJNUtilityMod.HJN_PANEL_INFO, 0xFF_303030);

            RenderUtil.drawText(matrices, MinecraftClient.getInstance().textRenderer, getMessage(), getX() + 4, getY() + (getHeight() - 8) / 2 + 1, 0xFF_FFFFFF);

            super.render(matrices, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!this.visible) return false;
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }
}
