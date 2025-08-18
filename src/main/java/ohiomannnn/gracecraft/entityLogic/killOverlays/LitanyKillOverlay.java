package ohiomannnn.gracecraft.entityLogic.killOverlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.entityOverlay.EntityLitanyOverlay;

import java.util.ArrayList;
import java.util.List;

public class LitanyKillOverlay extends Overlay {

    private static final int IMAGE_WIDTH = 165;
    private static final int IMAGE_HEIGHT = 165;

    private static final int TICKS_FIRST = 27;   // ~1.3 s
    private static final int TICKS_SECOND = 35;  // ~1.7 s
    private static final int TICKS_THIRD = 40;   // ~2.0 s
    private static final int TICKS_BARRAGE = 43; // ~2.2 s
    private static final int AUTO_CLOSE_TICKS = 94;

    private static final float MAX_SCALE_HEIGHT = 4.7f;
    private static final float MIN_SCALE_HEIGHT = 0.8f;

    private static final float MAX_SCALE_WIDTH = 5.5f;
    private static final float MIN_SCALE_WIDTH = 1.0f;


    private final List<TextEntry> messages = new ArrayList<>();

    private boolean firstAdded = false;
    private boolean secondAdded = false;
    private boolean thirdAdded = false;
    private boolean barrageStarted = false;

    private long startTick = -1;

    private final net.minecraft.util.RandomSource random = net.minecraft.util.RandomSource.create();

    private static final ResourceLocation TEX =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_kill.png");

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) {
            return;
        }

        if (startTick < 0) {
            startTick = mc.level.getGameTime();
        }

        long gameTicks = mc.level.getGameTime() - startTick;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        guiGraphics.fill(0, 0, screenWidth, screenHeight, 0xFF000000);
        guiGraphics.blit(TEX, EntityLitanyOverlay.baseX, EntityLitanyOverlay.baseY,
                0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        // 1) ~1.3 s
        if (!firstAdded && gameTicks >= TICKS_FIRST) {
            messages.add(new TextEntry("why did you listen?", random.nextInt(Math.max(1, screenWidth - 20)) + 10, random.nextInt(Math.max(1, screenHeight - 20)) + 10, 2.0f, 2.0f));
            firstAdded = true;
        }

        // 2) ~1.7 s
        if (!secondAdded && gameTicks >= TICKS_SECOND) {
            messages.add(new TextEntry("why did you listen?", screenWidth / 2, screenHeight / 2, 3.4f, 1.8f));
            messages.add(new TextEntry("why did you listen?", screenWidth - 220, 40, 1.6f, 2.4f));
            secondAdded = true;
        }

        // 3) ~2.0 s
        if (!thirdAdded && gameTicks >= TICKS_THIRD) {
            messages.add(new TextEntry("why did you listen?", 84, 99 / 2, 3.4f, 1.8f));
            messages.add(new TextEntry("why did you listen?", screenWidth - 60, 52, 1.6f, 2.4f));
            messages.add(new TextEntry("why did you listen?", 20, screenHeight - 44, 4.0f, 3.0f));
            thirdAdded = true;
        }
        // 4) ~2.2 s
        if (!barrageStarted && gameTicks >= TICKS_BARRAGE) {
            barrageStarted = true;
        }

        if (barrageStarted) {
            int perTick = 1;
            for (int i = 0; i < perTick; i++) {
                float sx = randScaleWidth();
                float sy = randScaleHeight();
                int x = random.nextInt(Math.max(1, screenWidth)) + 10;
                int y = random.nextInt(Math.max(1, screenHeight)) + 10;
                messages.add(new TextEntry("why did you listen?", x, y, sx, sy));
            }
        }

        if (gameTicks >= AUTO_CLOSE_TICKS) {
            mc.setOverlay(null);
        }

        PoseStack poseStack = guiGraphics.pose();
        Font font = mc.font;
        for (TextEntry entry : messages) {
            poseStack.pushPose();
            poseStack.scale(entry.sx, entry.sy, 1.0f);
            guiGraphics.drawString(font, entry.text,
                    (int) (entry.x / entry.sx),
                    (int) (entry.y / entry.sy),
                    0xFFFFFF,
                    false
            );
            poseStack.popPose();
        }
    }
    private float randScaleHeight() {
        return MIN_SCALE_HEIGHT + random.nextFloat() * (MAX_SCALE_HEIGHT - MIN_SCALE_HEIGHT);
    }
    private float randScaleWidth() {
        return MIN_SCALE_WIDTH + random.nextFloat() * (MAX_SCALE_WIDTH - MIN_SCALE_WIDTH);
    }

    private static class TextEntry {
        String text;
        int x, y;
        float sx, sy;

        TextEntry(String text, int x, int y, float sx, float sy) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.sx = sx;
            this.sy = sy;
        }
    }
    @Override public boolean isPauseScreen() {
        return false;
    }
}
