package ohiomannnn.gracecraft.entityLogic.killOverlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.entities.EntityLitany;

import java.util.ArrayList;
import java.util.List;

public class LitanyKillOverlay extends Overlay {

    private static final int IMAGE_WIDTH = 165;
    private static final int IMAGE_HEIGHT = 165;

    private static final long MS_FIRST = 1290;
    private static final long MS_SECOND = 1690;
    private static final long MS_THIRD = 1950;

    private static final long MS_END = 4700;

    private static final float MAX_SCALE_HEIGHT = 4.7f;
    private static final float MIN_SCALE_HEIGHT = 0.8f;

    private static final float MAX_SCALE_WIDTH = 5.5f;
    private static final float MIN_SCALE_WIDTH = 1.0f;

    private final Minecraft mc;

    public LitanyKillOverlay(Minecraft mc) {
        this.mc = mc;
    }

    private final List<AEntry> messages = new ArrayList<>();

    private boolean firstAdded = false;
    private boolean secondAdded = false;
    private boolean thirdAdded = false;
    private boolean fAdded = false;
    private boolean barrageStarted = false;

    private long startTimeMillis = -1;

    private final RandomSource random = RandomSource.create();

    private static final ResourceLocation LITANY_KILL = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_kill.png");
    private static final ResourceLocation WDYL = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/why_did_you_listen.png");

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {

        long now = System.currentTimeMillis();

        if (startTimeMillis < 0) {
            startTimeMillis = now;
        }

        long elapsedMillis = now - startTimeMillis;

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        guiGraphics.fill(0, 0, width, height, 0xFF000000);
        guiGraphics.blit(LITANY_KILL, EntityLitany.LitanyBaseX, EntityLitany.LitanyBaseY, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        if (!firstAdded) {
            messages.add(new AEntry(width / 2, height / 2, 2F, 1.5F));
            firstAdded = true;
        }

        if (!secondAdded && elapsedMillis >= MS_FIRST) {
            messages.add(new AEntry(width - 220, 40, 1.0F, 1.0F));
            secondAdded = true;
        }

        if (!thirdAdded && elapsedMillis >= MS_SECOND) {
            messages.add(new AEntry(width - 60, 52, 1.0F, 1.0F));
            thirdAdded = true;
        }

        if (!fAdded && elapsedMillis >= MS_THIRD) {
            messages.add(new AEntry(width - 400, 44, 1.0F, 1.0F));
            fAdded = true;
        }

//        if (!barrageStarted && elapsedMillis >= TICKS_BARRAGE * 50L) {
//            barrageStarted = true;
//        }
//
//        if (barrageStarted) {
//            int perTick = 1;
//            for (int i = 0; i < perTick; i++) {
//                float sx = randScaleWidth();
//                float sy = randScaleHeight();
//                int x = random.nextInt(Math.max(1, screenWidth));
//                int y = random.nextInt(Math.max(1, screenHeight));
//                messages.add(new AEntry(x, y, sx, sy));
//            }
//        }

        // end
        if (elapsedMillis >= MS_END) {
            mc.setOverlay(null);
        }

        // render vals
        PoseStack poseStack = guiGraphics.pose();
        int renderW = 200;
        int renderH = 100;
        for (AEntry entry : messages) {
            poseStack.pushPose();
            poseStack.translate(entry.x, entry.y, 0);
            poseStack.scale(entry.sx, entry.sy, 1.0f);
            guiGraphics.blit(WDYL, -renderW / 2, -renderH / 2, renderW, renderH, 0, 0, 327, 162, 327, 162);
            poseStack.popPose();
        }
    }
    private float randScaleHeight() {
        return MIN_SCALE_HEIGHT * (MAX_SCALE_HEIGHT - MIN_SCALE_HEIGHT);
    }
    private float randScaleWidth() {
        return MIN_SCALE_WIDTH * (MAX_SCALE_WIDTH - MIN_SCALE_WIDTH);
    }

    private static class AEntry {
        // pos
        int x, y;
        // scale
        float sx, sy;

        AEntry(int x, int y, float sx, float sy) {
            this.x = x;
            this.y = y;
            this.sx = sx;
            this.sy = sy;
        }
    }

    @Override public boolean isPauseScreen() { return false; }
}
