package ohiomannnn.gracecraft.entityLogic.entityOverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.killOverlays.DozerKillOverlay;
import ohiomannnn.gracecraft.network.GraceCraftNetwork;
import ohiomannnn.gracecraft.sounds.InitSounds;

import java.util.Random;
import java.util.UUID;

public class EntityDozerOverlay extends Overlay {
    private static final ResourceLocation TEXTURE_START =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_dozer.png");
    private static final ResourceLocation TEXTURE_END =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_dozer_awake.png");

    private static final int OPEN_WITH_NO_KILL = 5;
    private static final int OPEN_WITH_KILL = 2;
    private static final int TO_CLOSE_TICKS = 35;

    private static final int IMAGE_WIDTH = 128;
    private static final int IMAGE_HEIGHT = 128;

    private static boolean soundPlayed = false;

    private final Random rng = new Random();

    private long startTick = -1;

    private static void killByUuid(UUID target) {
        GraceCraftNetwork.sendKillToServerWDozer(target);
    }

    private void playSoundEntity(Player player,int audio) {
        if (audio == 1) {
            player.playSound(InitSounds.DOZY_ATTACK.get(), 1.0F, 1.0F);
        } else if (audio == 2) {
            player.playSound(InitSounds.DOZY_ATTACK_KILL.get(), 1.0F, 1.0F);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) {
            return;
        }
        if (startTick < 0) {
            startTick = mc.level.getGameTime();
        }

        long gameTicks = mc.level.getGameTime() - startTick;
        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int x = (screenWidth - IMAGE_WIDTH) / 2  + rng.nextInt(2);
        int y = (screenHeight - IMAGE_HEIGHT) / 2 + rng.nextInt(2);

        boolean showEnd = gameTicks >= (TO_CLOSE_TICKS - OPEN_WITH_NO_KILL);
        ResourceLocation tex = showEnd ? TEXTURE_END : TEXTURE_START;

        guiGraphics.blit(tex, x, y, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        if (!soundPlayed) {
            playSoundEntity(Minecraft.getInstance().player, 1);
            soundPlayed = true;
        }

        if (gameTicks >= TO_CLOSE_TICKS) {
            mc.setOverlay(null);
            soundPlayed = false;
        }

        if (gameTicks >= (TO_CLOSE_TICKS - OPEN_WITH_KILL) && !GraceCraft.isCrouchingDozer) {
            playSoundEntity(Minecraft.getInstance().player, 2);
            killByUuid(Minecraft.getInstance().player.getUUID());
            Minecraft.getInstance().setOverlay(new DozerKillOverlay());
            soundPlayed = false;
        }
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
