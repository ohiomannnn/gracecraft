package ohiomannnn.gracecraft.entityLogic.entityOverlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;

public class EntityKooKooOverlay extends Overlay {
    ResourceLocation SPRITE_KOOKOO =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/koo_koo.png");
    ResourceLocation KOOKOO_STATIC =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/koo_koo_no_anim.png");
    ResourceLocation SPRITE_HAND =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/koo_koo_hand.png");
    ResourceLocation SPRITE_KOOKOO_END =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/koo_koo_end.png");

    private static final int TOTAL_FRAMES_TIMER = 6;
    private static final int TOTAL_FRAMES_HAND = 17;
    private static final int TOTAL_FRAMES_END = 5;
    private static final int FRAME_DURATION = 1;
    private static final int PAUSE_TICKS = 6;

    private static boolean showed = false;

    private long lastCycle = -1;
    private int timer = 0;

    private int minus = 1;

    private long startTick = -1;

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        if (startTick < 0) {
            startTick = mc.level.getGameTime();
        }

        int frameIndexTimer = getFrameIndexTimer(mc);
        int frameIndexHand = getFrameIndexHand(mc);
        int frameIndexEnd = getFrameIndexEnd(mc);

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        Font font = mc.font;

        int x = (screenWidth - 128) / 2;
        int y = (screenHeight - 128) / 2;

        int xhand = (screenWidth - 128) / 2 - 60;
        int yhand = (screenHeight - 128) / 2 + 35;

        int frameWidth = 128;
        int frameHeight = 128;

        int textureWidth = 128;
        int textureHeight = 896;

        int textureWidthHand = 128;
        int textureHeightHand = 2176;

        int textureWidthEnd = 128;
        int textureHeightEnd = 640;

        int u = 0;
        int v = frameIndexTimer * frameHeight;
        int uv = frameIndexHand * frameHeight;
        int uvv = frameIndexEnd * frameHeight;

        float scale = 3.0F;

        if (!showed) {
            guiGraphics.blit(KOOKOO_STATIC, x, y, 0, 0, 128, 128, 128, 128);
        } else {
            if (timer >= 12) {
                guiGraphics.blit(SPRITE_KOOKOO_END, x, y, u, uvv, frameWidth, frameHeight, textureWidthEnd, textureHeightEnd);
            } else {
                guiGraphics.blit(SPRITE_KOOKOO, x, y, u, v, frameWidth, frameHeight, textureWidth, textureHeight);
            }
        }

        if (!showed) {
            guiGraphics.blit(SPRITE_HAND, xhand, yhand, u, uv, frameWidth, frameHeight, textureWidthHand, textureHeightHand);
        }

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.scale(scale, scale, 1.0F);

        if (timer >= 10) {
            minus = 4;
        }
        if (timer != 12) {
            guiGraphics.drawString(
                    font,
                    String.valueOf(timer),
                    (int) (screenWidth / 2.0F - (scale * scale) / 2.0F) / scale - minus,
                    (int) (screenHeight / 2.0F - (scale * scale) / 2.0F) / scale,
                    0x4100FE,
                    true
            );
        }

        poseStack.popPose();
        if (timer  == 13) {
            mc.setOverlay(null);
            showed = false;
        }
    }

    private int getFrameIndexTimer(Minecraft mc) {
        int frameIndex = 0;
        if (showed) {
            long elapsed = mc.level.getGameTime() - startTick;

            int cycleTicks = TOTAL_FRAMES_TIMER * FRAME_DURATION + PAUSE_TICKS;

            long currentCycle = elapsed / cycleTicks;
            long tickInCycle = elapsed % cycleTicks;

            if (currentCycle != lastCycle) {
                if (lastCycle != -1) {
                    timer++;
                }
                lastCycle = currentCycle;
            }

            if (tickInCycle < TOTAL_FRAMES_TIMER * FRAME_DURATION) {
                frameIndex = (int) (tickInCycle / FRAME_DURATION);
            } else {
                frameIndex = TOTAL_FRAMES_TIMER - 1;
            }
        }
        return frameIndex;
    }
    private int getFrameIndexHand(Minecraft mc) {
        long elapsed = mc.level.getGameTime() - startTick;

        int cycleTicks = TOTAL_FRAMES_HAND * FRAME_DURATION + PAUSE_TICKS;

        long tickInCycle = elapsed % cycleTicks;

        // Определяем кадр
        int frameIndex;
        if (tickInCycle < TOTAL_FRAMES_HAND * FRAME_DURATION) {
            frameIndex = (int)(tickInCycle / FRAME_DURATION);
        } else {
            frameIndex = TOTAL_FRAMES_HAND - 1;
            showed = true;
        }
        return frameIndex;
    }
    private int getFrameIndexEnd(Minecraft mc) {
        long elapsed = mc.level.getGameTime() - startTick;

        final int frameDuration = 5;

        long totalAnimationTicks = TOTAL_FRAMES_END * frameDuration;

        if (elapsed >= totalAnimationTicks) {
            return TOTAL_FRAMES_END - 1;
        } else {
            return (int) (elapsed / frameDuration);
        }
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
