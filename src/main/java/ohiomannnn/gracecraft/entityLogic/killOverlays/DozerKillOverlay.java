package ohiomannnn.gracecraft.entityLogic.killOverlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.network.killPackets.KillGeneric;
import ohiomannnn.gracecraft.sounds.InitSounds;

import java.util.ArrayList;
import java.util.List;

public class DozerKillOverlay extends Overlay {

    private static final ResourceLocation DOZER_KILL = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/doz_kill.png");
    private static final ResourceLocation DOZER_KILL_HA = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/doz_kill_ha.png");
    private static final ResourceLocation WILL = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/will.png");

    private final List<AEntry> messages = new ArrayList<>();

    private final int x;
    private final int y;

    private final Minecraft mc;

    public DozerKillOverlay(Minecraft mc, int x, int y) {
        this.mc = mc;
        this.x = x;
        this.y = y;
    }

    private static final int TO_CLOSE_MS = 1300;

    private long startTimeMillis = -1;

    private long lastMessageMillis = 0;
    private static final long MESSAGE_INTERVAL_MS = 33;

    // flag
    private boolean firstMessageShown = false;
    // flag
    private boolean soundPlayed = false;

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {

        long now = System.currentTimeMillis();

        if (startTimeMillis < 0) {
            startTimeMillis = now;
        }

        long elapsedMillis = now - startTimeMillis;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        guiGraphics.fill(0, 0, screenWidth, screenHeight, 0xFF000000);

        ResourceLocation texture = elapsedMillis <= 400 ? DOZER_KILL_HA : DOZER_KILL;
        guiGraphics.blit(texture, x, y, 200, 200, 0, 0, 300, 300, 300, 300);

        if (elapsedMillis >= TO_CLOSE_MS) {
            mc.setOverlay(null);
            PacketDistributor.sendToServer(new KillGeneric("dozer"));
        }

        if (!firstMessageShown) {
            messages.add(new AEntry(240, screenHeight - 40));
            firstMessageShown = true;
        }

        if (elapsedMillis >= 500) {
            if (!soundPlayed) {
                mc.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(InitSounds.DOZY_ATTACK_KILL.get(), 1.0F, 1.0F));
                soundPlayed = true;
            }
            if (now - lastMessageMillis >= MESSAGE_INTERVAL_MS) {
                lastMessageMillis = now;
                AEntry last = messages.getLast();
                messages.add(new AEntry(
                        last.x + 10,
                        last.y - 10
                ));
            }
        }

        PoseStack poseStack = guiGraphics.pose();
        int renderW = 500;
        int renderH = 100;
        for (AEntry entry : messages) {
            poseStack.pushPose();
            poseStack.translate(entry.x, entry.y, 0);
            guiGraphics.blit(WILL, -renderW / 2, -renderH / 2, renderW, renderH, 0, 0, 811, 307, 811, 307);
            poseStack.popPose();
        }
    }

    private static class AEntry {
        int x, y;

        AEntry( int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
