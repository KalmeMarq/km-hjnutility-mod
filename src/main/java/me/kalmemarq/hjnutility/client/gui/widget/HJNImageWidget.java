package me.kalmemarq.hjnutility.client.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.kalmemarq.hjnutility.util.RenderUtil;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class HJNImageWidget extends ClickableWidget implements HJNWidget {
    private int u;
    private int v;
    private int us = -1;
    private int vs = -1;
    private ImageInfo info;
    @Nullable
    private RenderUtil.NinesliceInfo ninesliceInfo;
    private int tintColor = 0xFFFFFF;
    private boolean grayscale;

    public HJNImageWidget() {
        super(0, 0, 0, 0, Text.empty());
    }

    public void setInfo(ImageInfo info) {
        this.info = info;
    }

    public void setGrayscale(boolean grayscale) {
        this.grayscale = grayscale;
    }

    public void setNinesliceInfo(RenderUtil.NinesliceInfo ninesliceInfo) {
        this.ninesliceInfo = ninesliceInfo;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    public void setUV(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public void setUVSize(int us, int vs) {
        this.us = us;
        this.vs = vs;
    }

    @Override
    public int getDefaultSize() {
        if (info != null) {
            int s = info.getTextureWidth();
            if (info.getTextureHeight() > s) {
                s = info.getTextureHeight();
            }

            return s;
        }
        return 0;
    }

    private static final RenderUtil.NinesliceInfo DEFAULT_NS_INFO = new RenderUtil.NinesliceInfo(0, 0, 0, 0, 256, 256);

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (info == null) return;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        boolean colored = this.tintColor != 0xFFFFFF;

        if (this.grayscale) {
            RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        }

        RenderSystem.setShaderTexture(0, info.getTexture());
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        int tW = getWidth();
        int tH = getHeight();

        if (this.ninesliceInfo == null) {

        } else {
//            RenderUtil.drawNinesliceTexture(matrices, getX(), getY(), info);
        }

        RenderSystem.defaultBlendFunc();
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    public static class ImageInfo {
        private Identifier texture;
        private int textureWidth;
        private int textureHeight;

        public ImageInfo() {
            this(TextureManager.MISSING_IDENTIFIER, 256);
        }

        public ImageInfo(Identifier texture) {
            this(texture, 256);
        }

        public ImageInfo(Identifier texture, int textureSize) {
            this(texture, textureSize, textureSize);
        }

        public ImageInfo(Identifier texture, int textureWidth, int textureHeight) {
            this.texture = texture;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }

        public void setTexture(Identifier texture) {
            this.texture = texture;
        }

        public void setTextureSize(int textureWidth, int textureHeight) {
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }

        public Identifier getTexture() {
            return texture;
        }

        public int getTextureWidth() {
            return textureWidth;
        }

        public int getTextureHeight() {
            return textureHeight;
        }
    }
}
