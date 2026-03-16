package ohiomannnn.gracecraft.entityLogic.entities;

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

    private boolean soundPlayed = false;

    private static final RandomSource rng = RandomSource.create();

    public Dozer() {
        this.lifetime = DURATION_TICKS;
    }

    @Override
    public void render(GuiGraphics guiGraphics) {

        if (mc.player.isDeadOrDying()) return;

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();
        int x = (width - 200) / 2 + rng.nextInt(2);
        int y = (height - 200) / 2 + rng.nextInt(2);

        boolean showEnd = age >= (DURATION_TICKS - AWAKE_W_NO_KILL);
        ResourceLocation texture = showEnd ? DOZER_AWAKE : DOZER_SLEEP;

        guiGraphics.blit(texture, x, y, 180, 180, 0, 0, 400, 400, 400, 400);

        if (!soundPlayed) {
            mc.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(InitSounds.DOZY_ATTACK.get(), 1.0F, 1.0F));
            soundPlayed = true;
        }

        if (age >= (DURATION_TICKS - AWAKE_W_KILL) && !GraceCraft.isCrouchingDozer && !kill) {
            this.remove();
            mc.getSoundManager().stop();
            mc.setOverlay(new DozerKillOverlay(x, y));
            this.kill = true;
        }
    }
}
