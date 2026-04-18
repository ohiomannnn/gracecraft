package ohiomannnn.gracecraft.entityLogic.entities;

import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.ScreenEntity;
import ohiomannnn.gracecraft.entityLogic.killOverlays.DozerKillOverlay;
import ohiomannnn.gracecraft.sounds.InitSounds;

public class Dozer extends ScreenEntity {

    private static final ResourceLocation DOZER_SLEEP = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/doz.png");
    private static final ResourceLocation DOZER_AWAKE = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/doz_awake.png");

    private static final int DURATION_TICKS = 35;
    private static final int AWAKE_W_NO_KILL = 5;
    private static final int AWAKE_W_KILL = 1;

    private static final int SIZE = 180;

    private boolean soundPlayed = false;

    private static final RandomSource rng = RandomSource.create();

    boolean mirrored = false;
    long lastSwitch = 0;

    public Dozer() {
        this.lifetime = DURATION_TICKS;
    }

    @Override
    public void render(GuiGraphics guiGraphics) {

        if (mc.player.isDeadOrDying()) return;

        long now = System.currentTimeMillis();
        if (now - lastSwitch >= 100) {
            mirrored = !mirrored;
            lastSwitch = now;
        }

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int x = (width - SIZE) / 2;
        int y = (height - SIZE) / 2;

        boolean showEnd = age >= (DURATION_TICKS - AWAKE_W_NO_KILL);
        ResourceLocation texture = showEnd ? DOZER_AWAKE : DOZER_SLEEP;

        long steppedTime = (System.currentTimeMillis() / 300) * 300;
        double time = steppedTime / 1000.0;
        int ox = (int) (Math.cos(time * 3) * 5.0) + rng.nextInt(3);
        int oy = (int) (Math.sin(time * 3) * 3.0) + rng.nextInt(4);

        float angle = 1F + rng.nextFloat() * 2F;
        if (mirrored) angle = -angle;

        guiGraphics.pose().pushPose();

        float cx = x + ox + (SIZE / 2F);
        float cy = y + oy + (SIZE / 2F);

        guiGraphics.pose().translate(cx, cy, 0);
        guiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(angle));

        guiGraphics.blit(texture, -SIZE / 2, -SIZE / 2, SIZE, SIZE, 0, 0, mirrored ? -400 : 400, 400, 400, 400);

        guiGraphics.pose().popPose();

        if (!soundPlayed) {
            mc.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(InitSounds.DOZY_ATTACK.get(), 1.0F, 1.0F));
            soundPlayed = true;
        }

        if (age >= (DURATION_TICKS - AWAKE_W_KILL) && !kill) {
            this.remove();
            if (!this.mc.player.isSpectator() && !GraceCraft.isCrouchingDozer) {
                mc.getSoundManager().stop();
                mc.setOverlay(new DozerKillOverlay(x + ox, y + oy, angle));
            }
            this.kill = true;
        }
    }
}
