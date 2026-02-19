package ohiomannnn.gracecraft.entityLogic.entities;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.Entity;
import ohiomannnn.gracecraft.entityLogic.killOverlays.DozerKillOverlay;
import ohiomannnn.gracecraft.network.killPackets.KillGeneric;
import ohiomannnn.gracecraft.sounds.InitSounds;

import java.util.Random;

public class Dozer extends Entity {

    private static final ResourceLocation DOZER_SLEEP = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_dozer.png");
    private static final ResourceLocation DOZER_AWAKE = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_dozer_awake.png");

    private static final int DURATION_TICKS = 35;
    private static final int AWAKE_W_NO_KILL = 5;
    private static final int AWAKE_W_KILL = 2;
    private static final int IMAGE_WIDTH = 128;
    private static final int IMAGE_HEIGHT = 128;

    private boolean soundPlayed = false;
    private static final Random rng = new Random();

    public Dozer() {
        this.lifetime = DURATION_TICKS;
    }

    @Override
    public void render(GuiGraphics graphics) {

        if (mc.player.isDeadOrDying()) return;

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int x = (screenWidth - IMAGE_WIDTH) / 2 + rng.nextInt(2);
        int y = (screenHeight - IMAGE_HEIGHT) / 2 + rng.nextInt(2);

        boolean showEnd = age >= (DURATION_TICKS - AWAKE_W_NO_KILL);
        ResourceLocation texture = showEnd ? DOZER_AWAKE : DOZER_SLEEP;

        graphics.blit(texture, x, y, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        if (!soundPlayed) {
            playSoundEntity(1);
            soundPlayed = true;
        }

        if (age >= (DURATION_TICKS - AWAKE_W_KILL) && !GraceCraft.isCrouchingDozer && !kill) {
            playSoundEntity(2);
            this.remove();
            PacketDistributor.sendToServer(new KillGeneric(mc.player.getUUID(), "dozer"));
            mc.setOverlay(new DozerKillOverlay(mc, x, y));
            this.kill = true;
        }
    }

    private void playSoundEntity(int audio) {
        if (audio == 1) {
            mc.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(InitSounds.DOZY_ATTACK.get(), 1.0F, 1.0F));
        } else if (audio == 2) {
            mc.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(InitSounds.DOZY_ATTACK_KILL.get(), 1.0F, 1.0F));
        }
    }
}
