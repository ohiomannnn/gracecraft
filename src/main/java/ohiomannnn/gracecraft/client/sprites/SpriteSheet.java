package ohiomannnn.gracecraft.client.sprites;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class SpriteSheet {

    private final ResourceLocation texture;
    private final int textureWidth;
    private final int textureHeight;
    private final int spriteWidth;
    private final int spriteHeight;
    private final int columns;
    private final int rows;

    public SpriteSheet(ResourceLocation texture, int textureWidth, int textureHeight, int spriteWidth, int spriteHeight) {
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
        this.columns = textureWidth / spriteWidth;
        this.rows = textureHeight / spriteHeight;
    }

    public void renderSprite(GuiGraphics guiGraphics, int x, int y, int index, int renderWidth, int renderHeight) {
        int col = index % columns;
        int row = index / columns;
        renderSprite(guiGraphics, x, y, col, row, renderWidth, renderHeight);
    }

    public void renderSprite(GuiGraphics guiGraphics, int x, int y, int col, int row, int renderWidth, int renderHeight) {
        int u = col * spriteWidth;
        int v = row * spriteHeight;

        guiGraphics.blit(texture, x, y, renderWidth, renderHeight, u, v, spriteWidth, spriteHeight, textureWidth, textureHeight);
    }

    public void renderSprite(GuiGraphics guiGraphics, int x, int y, int index) {
        renderSprite(guiGraphics, x, y, index, spriteWidth, spriteHeight);
    }

    public void renderRegion(GuiGraphics guiGraphics, int x, int y, int u, int v, int regionWidth, int regionHeight, int renderWidth, int renderHeight) {
        guiGraphics.blit(texture, x, y, renderWidth, renderHeight, u, v, regionWidth, regionHeight, textureWidth, textureHeight);
    }

    public ResourceLocation getTexture() { return texture; }
    public int getTextureWidth() { return textureWidth; }
    public int getTextureHeight() { return textureHeight; }
    public int getSpriteWidth() { return spriteWidth; }
    public int getSpriteHeight() { return spriteHeight; }
    public int getColumns() { return columns; }
    public int getRows() { return rows; }
    public int getTotalSprites() { return columns * rows; }
}