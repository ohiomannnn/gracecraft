package ohiomannnn.gracecraft.entityLogic.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.killOverlays.DozerKillOverlay;
import ohiomannnn.gracecraft.network.GraceCraftNetwork;
import ohiomannnn.gracecraft.sounds.InitSounds;

import java.util.Random;
import java.util.UUID;

import static ohiomannnn.gracecraft.GraceCraft.isCrouchingDozer;

public class EntityDozer {
    private static final ResourceLocation DOZER_SLEEP =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_dozer.png");
    private static final ResourceLocation DOZER_AWAKE =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_dozer_awake.png");

    private static final int DURATION_TICKS = 35;
    private static final int AWAKE_W_NO_KILL = 5;
    private static final int AWAKE_W_KILL = 2;
    private static final int IMAGE_WIDTH = 128;
    private static final int IMAGE_HEIGHT = 128;

    private static boolean soundPlayed = false;
    private static long startTick = -1;
    private static final Random rng = new Random();

    public static boolean endOthersFromDozer = false;

    public static void start() {
        assert Minecraft.getInstance().level != null;
        startTick = Minecraft.getInstance().level.getGameTime();
        soundPlayed = false;
    }

    public static void RegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.parse("entity_dozer"), (guiGraphics, partialTick) -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null || startTick < 0) return;

            long gameTicks = mc.level.getGameTime() - startTick;

            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            int x = (screenWidth - IMAGE_WIDTH) / 2 + rng.nextInt(2);
            int y = (screenHeight - IMAGE_HEIGHT) / 2 + rng.nextInt(2);

            boolean showEnd = gameTicks >= (DURATION_TICKS - AWAKE_W_NO_KILL);
            ResourceLocation texture = showEnd ? DOZER_AWAKE : DOZER_SLEEP;

            guiGraphics.blit(texture, x, y, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

            if (EntityLitany.endOthersFromLitany) {
                startTick = -1;
            }

            if (!soundPlayed) {
                playSoundEntity(mc.player, 1);
                soundPlayed = true;
            }

            if (gameTicks >= DURATION_TICKS) {
                startTick = -1;
                soundPlayed = false;
            }

            if (gameTicks >= (DURATION_TICKS - AWAKE_W_KILL) && !isCrouchingDozer) {
                playSoundEntity(mc.player, 2);
                assert mc.player != null;
                killByUuid(mc.player.getUUID());
                mc.setOverlay(new DozerKillOverlay());
                startTick = -1;
                endOthersFromDozer = true;
                soundPlayed = false;
            }
        });
    }

    private static void playSoundEntity(Player player, int audio) {
        if (audio == 1) {
            player.playSound(InitSounds.DOZY_ATTACK.get(), 1.0F, 1.0F);
        } else if (audio == 2) {
            player.playSound(InitSounds.DOZY_ATTACK_KILL.get(), 1.0F, 1.0F);
        }
    }

    private static void killByUuid(UUID target) {
        GraceCraftNetwork.sendKillToServerWDozer(target);
    }
}
