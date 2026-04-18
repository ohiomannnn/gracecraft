package ohiomannnn.gracecraft.entityLogic.entities;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.client.sprites.AnimatedSpriteSheet;
import ohiomannnn.gracecraft.client.sprites.SpriteSheet;
import ohiomannnn.gracecraft.entityLogic.ScreenEntity;

public class Litany extends ScreenEntity {

    private static final ResourceLocation TEX = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/lit.png");

    private final AnimatedSpriteSheet closedEye;
    private final AnimatedSpriteSheet openEye;

    private final int eyeCount;

    private int openEyeCount = -1;
    private long lastSwitchTime = 0;

    public Litany(int eyeCount) {
        this.eyeCount = eyeCount;
        SpriteSheet baseSpriteSheet = new SpriteSheet(
                ResourceLocation.fromNamespaceAndPath("gracecraft", "textures/entities/lit_eyes.png"),
                120, 90, 40, 90
        );

        this.closedEye = new AnimatedSpriteSheet(baseSpriteSheet, new long[] {1000, 1000, 1000});
        this.closedEye.setFrame(0);
        this.closedEye.pause();

        this.openEye = new AnimatedSpriteSheet(baseSpriteSheet, new long[] {1000, 1000, 1000});
        this.openEye.setFrame(2);
        this.openEye.pause();
    }

    private void updateAnimationState() {
        if (openEyeCount >= eyeCount) {
            this.remove();
            return;
        }

        long currentTime = System.currentTimeMillis();

        if (currentTime - lastSwitchTime > 1000) {
            openEyeCount++;
            lastSwitchTime = currentTime;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics) {
        updateAnimationState();

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int x = width / 2;
        int y = height / 2;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0F);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.65F, 0.65F, 1F);
        guiGraphics.blit(TEX, -256 / 2, -256 / 2, 256, 256, 0, 0, 256, 256, 256, 256);
        guiGraphics.pose().popPose();

        renderEyes(guiGraphics, eyeCount);

        guiGraphics.pose().popPose();

    }

    private void renderEyes(GuiGraphics guiGraphics, int n) {
        float angleStep = 360.0f / n;

        RenderSystem.disableCull();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0F, -27F, 0F);
        guiGraphics.pose().scale(0.5F, -0.5F, 1F);

        for (int i = 0; i < n; i++) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(i * angleStep));

            if (i == this.openEyeCount) {
                openEye.render(guiGraphics, -40 / 2, -90, 40, 90);
            } else {
                closedEye.render(guiGraphics, -40 / 2, -90, 40, 90);
            }

            guiGraphics.pose().popPose();
        }

        guiGraphics.pose().popPose();

        RenderSystem.enableCull();
    }
}
