package ohiomannnn.gracecraft.entityLogic.entityOverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.killOverlays.LitanyKillOverlay;
import ohiomannnn.gracecraft.network.GraceCraftNetwork;
import ohiomannnn.gracecraft.sounds.InitSounds;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static ohiomannnn.gracecraft.GraceCraft.isCrouchingLitany;

public class EntityLitany {

    public enum EyeStage {
        OPENING, OPEN
    }
    public enum EyePosition {
        LEFT, CENTER, RIGHT
    }

    private static final Map<EyePosition, Map<EyeStage, ResourceLocation>> EYE_TEXTURES = new HashMap<>();
    private static final ResourceLocation BASE =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany.png");

    static {
        EYE_TEXTURES.put(EyePosition.LEFT, Map.of(
                EyeStage.OPENING, ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_opening_left.png"),
                EyeStage.OPEN, ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_open_left.png")
        ));
        EYE_TEXTURES.put(EyePosition.CENTER, Map.of(
                EyeStage.OPENING, ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_opening_center.png"),
                EyeStage.OPEN, ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_open_center.png")
        ));
        EYE_TEXTURES.put(EyePosition.RIGHT, Map.of(
                EyeStage.OPENING, ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_opening_right.png"),
                EyeStage.OPEN, ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_open_right.png")
        ));
    }

    private static final int IMAGE_WIDTH = 165;
    private static final int IMAGE_HEIGHT = 165;
    private static final int DURATION_TICKS = 60;
    private static final int MID_SWITCH_TICKS = 44;
    private static final int FINAL_SWITCH_TICKS = 4;
    private static final int FINAL_SWITCH_TICKS_W_KILL = 1;
    private static final int CYCLES_TOTAL = 3;

    private static boolean active = false;
    private static long cycleStartTick = -1;
    private static int cyclesDone = 0;
    private static int statePlayed = 1;
    private static int SHAKE_AMPLITUDE = 2;
    private static ResourceLocation texture = BASE;
    private static final Random rng = new Random();

    public static int LitanyBaseX;
    public static int LitanyBaseY;

    public static boolean endOthersFromLitany = false;

    public static void start() {
        active = true;
        cycleStartTick = -1;
        cyclesDone = 0;
        statePlayed = 1;
        SHAKE_AMPLITUDE = 2;
        texture = BASE;
    }

    public static void RegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.parse("entity_litany"), (guiGraphics, partialTick) -> {
            if (!active) return;

            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level == null) return;
            Player player = mc.player;
            long t = level.getGameTime();

            if (cycleStartTick < 0) {
                cycleStartTick = t;
                chooseNewPosition(mc);
            }

            long ticksInCycle = t - cycleStartTick;

            if (EntityDozer.endOthersFromDozer) {
                active = false;
            }

            if (ticksInCycle >= DURATION_TICKS - FINAL_SWITCH_TICKS_W_KILL) {
                if (!isCrouchingLitany) {
                    playSoundEntity(player, 4);
                    assert player != null;
                    GraceCraftNetwork.sendKillToServerWLitany(player.getUUID());
                    mc.setOverlay(new LitanyKillOverlay());
                    active = false;
                    endOthersFromLitany = true;
                    return;
                }
            }

            if (ticksInCycle >= DURATION_TICKS - FINAL_SWITCH_TICKS) {
                SHAKE_AMPLITUDE = 1;
                texture = switch (cyclesDone) {
                    case 0 -> EYE_TEXTURES.get(EyePosition.CENTER).get(EyeStage.OPEN);
                    case 1 -> EYE_TEXTURES.get(EyePosition.LEFT).get(EyeStage.OPEN);
                    case 2 -> EYE_TEXTURES.get(EyePosition.RIGHT).get(EyeStage.OPEN);
                    default -> BASE;
                };
                if (statePlayed == 3) {
                    playSoundEntity(player, 3);
                    statePlayed = 1;
                }
            } else if (ticksInCycle >= MID_SWITCH_TICKS) {
                SHAKE_AMPLITUDE = 6;
                texture = switch (cyclesDone) {
                    case 0 -> EYE_TEXTURES.get(EyePosition.CENTER).get(EyeStage.OPENING);
                    case 1 -> EYE_TEXTURES.get(EyePosition.LEFT).get(EyeStage.OPENING);
                    case 2 -> EYE_TEXTURES.get(EyePosition.RIGHT).get(EyeStage.OPENING);
                    default -> BASE;
                };
                if (statePlayed == 2) {
                    playSoundEntity(player, 2);
                    statePlayed = 3;
                }
            } else {
                SHAKE_AMPLITUDE = 2;
                texture = BASE;
                if (statePlayed == 1) {
                    playSoundEntity(player, 1);
                    statePlayed = 2;
                }
            }

            int shakeX = LitanyBaseX + rng.nextInt(SHAKE_AMPLITUDE * 2 + 1) - SHAKE_AMPLITUDE;
            int shakeY = LitanyBaseY + rng.nextInt(SHAKE_AMPLITUDE * 2 + 1) - SHAKE_AMPLITUDE;

            guiGraphics.blit(texture, shakeX, shakeY, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

            if (ticksInCycle >= DURATION_TICKS) {
                cyclesDone++;
                if (cyclesDone >= CYCLES_TOTAL) {
                    active = false;
                    return;
                }
                cycleStartTick = t;
                chooseNewPosition(mc);
            }
        });
    }

    private static void chooseNewPosition(Minecraft mc) {
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();
        int maxX = Math.max(0, w - IMAGE_WIDTH);
        int maxY = Math.max(0, h - IMAGE_HEIGHT);
        LitanyBaseX = (maxX > 0) ? rng.nextInt(maxX + 1) : 0;
        LitanyBaseY = (maxY > 0) ? rng.nextInt(maxY + 1) : 0;
    }

    private static void playSoundEntity(Player player, int audio) {
        switch (audio) {
            case 1 -> player.playSound(InitSounds.LITANY_ATTACK.get(), 1.0F, 1.0F);
            case 2 -> player.playSound(InitSounds.LITANY_ATTACK_FAST.get(), 1.0F, 1.0F);
            case 3 -> player.playSound(InitSounds.LITANY_ATTACK_JUMPSCARE.get(), 1.0F, 1.0F);
            case 4 -> player.playSound(InitSounds.LITANY_ATTACK_KILL.get(), 1.0F, 1.0F);
        }
    }
}
