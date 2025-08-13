package ohiomannnn.gracecraft.entityLogic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.net.payload.GraceCraftNetwork;
import ohiomannnn.gracecraft.sounds.InitSounds;

import java.util.Random;
import java.util.UUID;

public class EntityDozerOverlay extends Overlay {
    private static final ResourceLocation TEXTURE_START =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/gui/entity_dozer.png");
    private static final ResourceLocation TEXTURE_END =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/gui/entity_dozer_awake.png");

    private static final int END_PREVIEW_TICKS = 15;
    private static final int AUTO_CLOSE_TICKS = 120;

    private static final int IMAGE_WIDTH = 128;
    private static final int IMAGE_HEIGHT = 128;

    public int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
    public int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

    public static boolean isEntityDone;
    private static boolean isPlayed = false;

    private int ticksOpen = 0;
    private final Random rng = new Random();

    public static void killByUuidClient(UUID target) {
        GraceCraftNetwork.sendKillToServer(target);
    }

    public void tick() {
        ticksOpen++;
        isEntityDone = false;
        if (ticksOpen == 0) {
            isPlayed = false;
        }
        if (ticksOpen >= AUTO_CLOSE_TICKS) {
            Minecraft.getInstance().setOverlay(null);
            isEntityDone = true;
        }
    }
    private void playSoundEntity(Player player, boolean audio) {
        if (audio) {
            player.playSound(InitSounds.DOZY_ATTACK.get(), 1.0F, 1.0F);
        } else {
            player.playSound(InitSounds.DOZY_ATTACK_KILL.get(), 1.0F, 1.0F);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        int x = (this.screenWidth - IMAGE_WIDTH) / 2  + rng.nextInt(3);
        int y = (this.screenHeight - IMAGE_HEIGHT) / 2 + rng.nextInt(3);

        boolean showEnd = ticksOpen >= (AUTO_CLOSE_TICKS - END_PREVIEW_TICKS);
        ResourceLocation tex = showEnd ? TEXTURE_END : TEXTURE_START;

        guiGraphics.blit(tex, x, y, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        if (isEntityDone) {
            isPlayed = false;
        }

        tick();

        if (!isPlayed) {
            playSoundEntity(Minecraft.getInstance().player, true);
            isPlayed = true;
        }

        if (EntityDozerOverlay.isEntityDone && !GraceCraft.isCrouching) {
            playSoundEntity(Minecraft.getInstance().player, false);
            killByUuidClient(Minecraft.getInstance().player.getUUID());
            Minecraft.getInstance().setOverlay(new DozerKillOverlay());
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
