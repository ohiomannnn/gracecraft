package ohiomannnn.gracecraft.entityLogic;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;

import java.util.ArrayList;
import java.util.List;

public class DozerKillOverlay extends Overlay {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_dozer_kill.png");

    private final List<TextEntry> messages = new ArrayList<>();

    private static final int TO_CLOSE_TICKS = 20;

    private int ticksOpen = 0;

    private static final int IMAGE_WIDTH = 128;
    private static final int IMAGE_HEIGHT = 128;

    public int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
    public int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

    private boolean firstMessageShown = false;

    private long startTick = -1;

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

        Font font = mc.font;
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int ximg = (this.screenWidth - IMAGE_WIDTH) / 2;
        int yimg = (this.screenHeight - IMAGE_HEIGHT) / 2;


        guiGraphics.fill(0, 0, screenWidth, screenHeight, 0xFF000000);

        guiGraphics.blit(TEXTURE, ximg, yimg, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        if (gameTicks >= TO_CLOSE_TICKS) {
            mc.setOverlay(null);
        }

        if (!firstMessageShown && gameTicks <= 5) {
            messages.add(new TextEntry("will you wake up tomorrow?", 10, screenHeight - 30));
            guiGraphics.drawString(font, "will you wake up tomorrow?", 15 , screenHeight - 30, 0xFFFFFF, false);
            firstMessageShown = true;
        }

        if (firstMessageShown && gameTicks >= 5 && gameTicks % 2 == 0) {
            TextEntry last = messages.getLast();
            messages.add(new TextEntry(
                    "will you wake up tomorrow?",
                    last.x + 10,
                    last.y - 15
            ));
        }

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.scale(4.0f, 6.0f, 1.0f);

        for (TextEntry entry : messages) {
            guiGraphics.drawString(font,
                    entry.text,
                    (int)(entry.x / 4.0f),
                    (int)(entry.y / 6.0f),
                    0xFFFFFF, false
            );
        }

        poseStack.popPose();
    }

    private static class TextEntry {
        String text;
        int x, y;

        TextEntry(String text, int x, int y) {
            this.text = text;
            this.x = x;
            this.y = y;
        }
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
