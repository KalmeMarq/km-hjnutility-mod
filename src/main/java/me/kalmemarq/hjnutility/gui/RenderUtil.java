package me.kalmemarq.hjnutility.gui;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class RenderUtil {
    public static void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, int centerX, int y, int color) {
        OrderedText orderedText = text.asOrderedText();
        textRenderer.draw(matrices, orderedText, (float)(centerX - textRenderer.getWidth(orderedText) / 2), (float)y, color);
    }

    public static void drawText(MatrixStack matrices, TextRenderer textRenderer, Text text, int x, int y, int color) {
        textRenderer.draw(matrices, text.asOrderedText(), (float)x, (float)y, color);
    }

    public static void drawNinesliceTexture(MatrixStack matrices, int x, int y, int width, int height, int u, int v, int us, int vs, NinesliceInfo ninesliceInfo) {
        var matrix = matrices.peek().getPositionMatrix();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        int nx0 = ninesliceInfo.ninesliceX0;
        int ny0 = ninesliceInfo.ninesliceY0;
        int nx1 = ninesliceInfo.ninesliceX1;
        int ny1 = ninesliceInfo.ninesliceY1;
        int nBW = us;
        int nBH = vs;
        int nTW = ninesliceInfo.textureWidth;
        int nTH = ninesliceInfo.textureHeight;

        if (nx0 + nx1 >= nTW) {
            nx0 = 0;
            ny0 = 0;
        }

        if (nx0 == 0 && ny0 == 0 && nx1 == 0 && ny1 == 0) {
            drawNinesliceTextureRegion(matrix, bufferBuilder, x, y, width, height, u, v, nBW, nBH, nTW, nTH);
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            return;
        }

        // TopLeft
        drawNinesliceTextureRegion(matrix, bufferBuilder, x, y, nx0, ny0, u, v, nx0, ny0, nTW, nTH);
        // TopMiddle
        drawNinesliceTextureRegion(matrix, bufferBuilder, x + nx0, y, width - nx0 - nx1, ny0, u + nx0, v, nBW - nx0 - nx1, ny0, nTW, nTH);
        // TopRight
        drawNinesliceTextureRegion(matrix, bufferBuilder, x + width - nx1, y, nx1, ny0, u + nBW - nx1, v, nx1, ny0, nTW, nTH);
        // LeftMiddle
        drawNinesliceTextureRegion(matrix, bufferBuilder, x, y + ny0, nx0, height - ny0 - ny1, u, v + ny0, nx0, nBH - ny0 - ny1, nTW, nTH);
        // Center
        drawNinesliceTextureRegion(matrix, bufferBuilder, x + nx0, y + ny0, width - nx0 - nx1, height - ny0 - ny1, u + nx0, v + ny0, nBW - nx0 - nx1, nBH - ny0 - ny1, nTW, nTH);
        // RightMiddle
        drawNinesliceTextureRegion(matrix, bufferBuilder, x + width - nx1, y + ny0, nx1, height - ny0 - ny1, u + nBW - nx1, v + ny0, nx1, nBH - ny0 - ny1, nTW, nTH);
        // BottomLeft
        drawNinesliceTextureRegion(matrix, bufferBuilder, x, y + height - ny1, nx0, ny1, u, v + nBH - ny1, nx0, ny1, nTW, nTH);
        // BottomMiddle
        drawNinesliceTextureRegion(matrix, bufferBuilder, x + nx0, y + height - ny1, width - nx0 - nx1, ny1, u + nx0, v + nBH - ny1, nBW - nx0 - nx1, ny1, nTW, nTH);
        // BottomRight
        drawNinesliceTextureRegion(matrix, bufferBuilder, x + width - nx1, y + height - ny1, nx1, ny1, u + nBW - nx1, v + nBH - ny1, nx1, ny1, nTW, nTH);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void drawColoredNinesliceTexture(MatrixStack matrices, int x, int y, int width, int height, int u, int v, int us, int vs, NinesliceInfo ninesliceInfo, int color) {
        var matrix = matrices.peek().getPositionMatrix();

        RenderSystem.setShader(GameRenderer::getPositionColorTexProgram);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);

        int nx0 = ninesliceInfo.ninesliceX0;
        int ny0 = ninesliceInfo.ninesliceY0;
        int nx1 = ninesliceInfo.ninesliceX1;
        int ny1 = ninesliceInfo.ninesliceY1;
        int nBW = us;
        int nBH = vs;
        int nTW = ninesliceInfo.textureWidth;
        int nTH = ninesliceInfo.textureHeight;

        if (nx0 + nx1 >= nTW) {
            nx0 = 0;
            ny0 = 0;
        }

        if (nx0 == 0 && ny0 == 0 && nx1 == 0 && ny1 == 0) {
            drawNinesliceTextureRegion(matrix, bufferBuilder, x, y, width, height, u, v, nBW, nBH, nBW, nBH);
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
            RenderSystem.disableBlend();
            return;
        }

        // TopLeft
        drawColoredNinesliceTextureRegion(matrix, bufferBuilder, x, y, nx0, ny0, u, v, nx0, ny0, nTW, nTH, color);
        // TopMiddle
        drawColoredNinesliceTextureRegion(matrix, bufferBuilder, x + nx0, y, width - nx0 - nx1, ny0, u + nx0, v, nBW - nx0 - nx1, ny0, nTW, nTH, color);
        // TopRight
        drawColoredNinesliceTextureRegion(matrix, bufferBuilder, x + width - nx1, y, nx1, ny0, u + nBW - nx1, v, nx1, ny0, nTW, nTH, color);
        // LeftMiddle
        drawColoredNinesliceTextureRegion(matrix, bufferBuilder, x, y + ny0, nx0, height - ny0 - ny1, u, v + ny0, nx0, nBH - ny0 - ny1, nTW, nTH, color);
        // Center
        drawColoredNinesliceTextureRegion(matrix, bufferBuilder, x + nx0, y + ny0, width - nx0 - nx1, height - ny0 - ny1, u + nx0, v + ny0, nBW - nx0 - nx1, nBH - ny0 - ny1, nTW, nTH, color);
        // RightMiddle
        drawColoredNinesliceTextureRegion(matrix, bufferBuilder, x + width - nx1, y + ny0, nx1, height - ny0 - ny1, u + nBW - nx1, v + ny0, nx1, nBH - ny0 - ny1, nTW, nTH, color);
        // BottomLeft
        drawColoredNinesliceTextureRegion(matrix, bufferBuilder, x, y + height - ny1, nx0, ny1, u, v + nBH - ny1, nx0, ny1, nTW, nTH, color);
        // BottomMiddle
        drawColoredNinesliceTextureRegion(matrix, bufferBuilder, x + nx0, y + height - ny1, width - nx0 - nx1, ny1, u + nx0, v + nBH - ny1, nBW - nx0 - nx1, ny1, nTW, nTH, color);
        // BottomRight
        drawColoredNinesliceTextureRegion(matrix, bufferBuilder, x + width - nx1, y + height - ny1, nx1, ny1, u + nBW - nx1, v + nBH - ny1, nx1, ny1, nTW, nTH, color);

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    private static void drawNinesliceTextureRegion(Matrix4f matrix, BufferBuilder builder, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        int x0 = x;
        int x1 = x + width;
        int y0 = y;
        int y1 = y + height;

        float u0 = (u + 0.0f) / (float)textureWidth;
        float v0 = (v + 0.0f) / (float)textureHeight;
        float u1 = (u + (float)regionWidth) / (float)textureWidth;
        float v1 = (v + (float)regionHeight) / (float)textureHeight;

        builder.vertex(matrix, x0, y1, 0).texture(u0, v1).next();
        builder.vertex(matrix, x1, y1, 0).texture(u1, v1).next();
        builder.vertex(matrix, x1, y0, 0).texture(u1, v0).next();
        builder.vertex(matrix, x0, y0, 0).texture(u0, v0).next();
    }

    private static void drawColoredNinesliceTextureRegion(Matrix4f matrix, BufferBuilder builder, int x, int y, int width, int height, int u, int v, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color) {
        int x0 = x;
        int x1 = x + width;
        int y0 = y;
        int y1 = y + height;

        float u0 = (u + 0.0f) / (float)textureWidth;
        float v0 = (v + 0.0f) / (float)textureHeight;
        float u1 = (u + (float)regionWidth) / (float)textureWidth;
        float v1 = (v + (float)regionHeight) / (float)textureHeight;

        int a = color >> 24 & 0xFF;
        if (a == 0) a = 255;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;

        builder.vertex(matrix, x0, y1, 0).color(r, g, b, a).texture(u0, v1).next();
        builder.vertex(matrix, x1, y1, 0).color(r, g, b, a).texture(u1, v1).next();
        builder.vertex(matrix, x1, y0, 0).color(r, g, b, a).texture(u1, v0).next();
        builder.vertex(matrix, x0, y0, 0).color(r, g, b, a).texture(u0, v0).next();
    }

    public record NinesliceInfo(int ninesliceX0, int ninesliceY0, int ninesliceX1, int ninesliceY1, int textureWidth, int textureHeight) {}
}
