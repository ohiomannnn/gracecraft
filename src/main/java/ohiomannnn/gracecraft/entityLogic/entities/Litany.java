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

    private final AnimatedSpriteSheet lit;

    public Litany() {
        lit = new AnimatedSpriteSheet(
                new SpriteSheet(ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/lit_eyes.png"), 120, 90, 40, 90),
                new long[] {1000, 1000, 1000}
        );
        //lit.pause();
        lit.setFrame(0);
        //lit.setLooping(false);
    }

    @Override
    public void render(GuiGraphics guiGraphics) {

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int x = width / 2;
        int y = height / 2;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0F);

        guiGraphics.blit(TEX, -170 / 2, -180 / 2, 170, 180, 0, 0, 300, 350, 300, 350);

        guiGraphics.pose().popPose();

        render(guiGraphics, 3);
    }

    private void render(GuiGraphics guiGraphics, int n) {
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int x = width / 2;
        int y = (height / 2) - 10;

        float angleStep = 360.0f / n;

        RenderSystem.disableCull();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 0F);
        guiGraphics.pose().scale(1F, -1F, 1F);

        for (int i = 0; i < n; i++) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(i * angleStep));

            lit.render(guiGraphics, -40 / 2, -90, 40, 90);

            //guiGraphics.blit(TEX, -40 / 2, -90, 40, 90, 0, 0, 40, 90, 40, 90);

            guiGraphics.pose().popPose();
        }

        guiGraphics.pose().popPose();

        RenderSystem.enableCull();
    }
}
