package ohiomannnn.gracecraft.entityLogic.killOverlays;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.entities.EntityLitany;
import ohiomannnn.gracecraft.network.killPackets.KillGeneric;

import java.util.*;
import java.util.Map.Entry;

public class LitanyKillOverlay extends Overlay {

    public static final Map<Integer, Long> screamers = new HashMap<>();
    private final Set<Integer> triggeredScreamers = new HashSet<>();
    private final Set<Integer> modifiedScreamers = new HashSet<>();
    private final Map<Integer, AEntry> createdEntries = new HashMap<>();

    static {
        screamers.put(1, 1290L);
        screamers.put(2, 1690L);
        screamers.put(3, 1950L);
        screamers.put(4, 2167L);
        screamers.put(5, 2341L);
        screamers.put(6, 2485L);
        screamers.put(7, 2600L);
        screamers.put(8, 2700L);
        screamers.put(9, 2800L);
    }

    private static final long MS_GLITCH = 2900;

    private static final int IMAGE_WIDTH = 165;
    private static final int IMAGE_HEIGHT = 165;

    private static final long MS_END = 4700;

    private final Minecraft mc;

    private final int x;
    private final int y;

    public LitanyKillOverlay(int x, int y) {
        this.mc = Minecraft.getInstance();
        this.x = x;
        this.y = y;
    }

    private final List<AEntry> messages = new ArrayList<>();
    private final List<AEntry> savedM = new ArrayList<>();

    // flag
    private boolean firstAdded = false;

    private long startTimeMillis = -1;

    private long lastMessageMillis = 0;
    private static final long MESSAGE_INTERVAL_MS = 33;

    private static final Random RANDOM = new Random();

    private static final ResourceLocation LITANY_KILL = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_kill.png");
    private static final ResourceLocation WDYL = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/why.png");

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
        guiGraphics.blit(LITANY_KILL, x, y, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        if (!firstAdded) {
            messages.add(new AEntry(width / 2, height / 2, 2F, 1.5F));
            firstAdded = true;
        }

        for (Entry<Integer, Long> longEntry : screamers.entrySet()) {
            int key = longEntry.getKey();
            long triggerTime = longEntry.getValue();

            if (!triggeredScreamers.contains(key) && elapsedMillis >= triggerTime) {
                AEntry e = new AEntry(RANDOM.nextInt(width), RANDOM.nextInt(height), 1.0F, 1.0F);
                messages.add(e);
                savedM.add(e);
                triggeredScreamers.add(key);
                createdEntries.put(key, e);
            }

            if (!modifiedScreamers.contains(key) && elapsedMillis >= triggerTime + 99L) {
                AEntry e = createdEntries.get(key);
                if (e != null) {
                    e.sx = RANDOM.nextFloat(0.2F, 3F);
                    e.sy = RANDOM.nextFloat(0.2F, 3F);
                }
                modifiedScreamers.add(key);
            }
        }

        if (elapsedMillis >= MS_GLITCH) {
            if (now - lastMessageMillis >= MESSAGE_INTERVAL_MS) {
                lastMessageMillis = now;

                savedM.replaceAll(aEntry -> {
                    AEntry e = new AEntry(aEntry.x, aEntry.y + 5, aEntry.sx, aEntry.sy);
                    messages.add(e);
                    return e;
                });
            }
        }

        // end
        if (elapsedMillis >= MS_END) {
            mc.setOverlay(null);
            PacketDistributor.sendToServer(new KillGeneric("litany"));
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

        if (elapsedMillis >= MS_END - 535) {
            guiGraphics.fill(0, 0, width, height, 0xFF000000);
        }
    }

    private static class AEntry {
        // pos
        public int x, y;
        // scale
        public float sx, sy;

        AEntry(int x, int y, float sx, float sy) {
            this.x = x;
            this.y = y;
            this.sx = sx;
            this.sy = sy;
        }
    }

    @Override public boolean isPauseScreen() { return false; }
}
