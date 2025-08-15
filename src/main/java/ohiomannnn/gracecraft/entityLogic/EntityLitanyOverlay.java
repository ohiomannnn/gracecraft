package ohiomannnn.gracecraft.entityLogic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.network.payload.GraceCraftNetwork;
import ohiomannnn.gracecraft.sounds.InitSounds;

import java.util.Random;
import java.util.UUID;


public class EntityLitanyOverlay extends Overlay {
    private static final ResourceLocation TEX_BASE =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany.png");
    private static final ResourceLocation TEX_OPENING =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_opening.png");
    private static final ResourceLocation TEX_OPEN =
            ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/entity_litany_open.png");

    private static final int IMAGE_WIDTH  = 128;
    private static final int IMAGE_HEIGHT = 128;

    private static final int DURATION_TICKS      = 60;
    private static final int MID_SWITCH_TICKS    = 44;
    private static final int FINAL_SWITCH_TICKS  = 5;
    private static final int CYCLES_TOTAL        = 3;
    private static int SHAKE_AMPLITUDE = 2;

    private static int statePlayed = 1;

    public static boolean isCrouchingLit;

    private void playSoundEntity(Player player, int audio) {
        if (audio == 1) {
            player.playSound(InitSounds.LITANY_ATTACK.get(), 1.0F, 1.0F);
        } else if (audio == 2) {
            player.playSound(InitSounds.LITANY_ATTACK_FAST.get(), 1.0F, 1.0F);
        } else if (audio == 3) {
            player.playSound(InitSounds.LITANY_ATTACK_JUMPSCARE.get(), 1.0F, 1.0F);
        } else if (audio == 4) {
            player.playSound(InitSounds.LITANY_ATTACK_KILL.get(), 1.0F, 1.0F);
        }
    }
    private static void killByUuidClient(UUID target) {
        GraceCraftNetwork.sendKillToServerWLitany(target);
    }
    private void killOnNoCrouch(boolean crouch) {
        if (!crouch) {
            Minecraft mc = Minecraft.getInstance();
            Player self = mc.player;

            playSoundEntity(self, 4);
            killByUuidClient(self.getUUID());

            LitanyKillOverlay overlay = new LitanyKillOverlay();
            mc.setOverlay(overlay);
        }
    };

    private final Random rng = new Random();

    private long cycleStartTick = -1;
    private int cyclesDone = 0;

    public static int baseX;
    public static int baseY;

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null) return;
        Player player = Minecraft.getInstance().player;

        long t = level.getGameTime();

        if (cycleStartTick < 0) {
            cycleStartTick = t;
            chooseNewPosition(mc);
        }

        long ticksInCycle = t - cycleStartTick;

        ResourceLocation tex;
        if (ticksInCycle >= DURATION_TICKS - FINAL_SWITCH_TICKS) {
            tex = TEX_OPEN;
            SHAKE_AMPLITUDE = 1;
            killOnNoCrouch(isCrouchingLit);
            if (statePlayed == 3) {
                playSoundEntity(player, 3);
                statePlayed = 1;
            }
        } else if (ticksInCycle >= MID_SWITCH_TICKS) {
            tex = TEX_OPENING;
            SHAKE_AMPLITUDE = 6;
            if (statePlayed == 2) {
                playSoundEntity(player, 2);
                statePlayed = 3;
            }
        } else {
            tex = TEX_BASE;
            SHAKE_AMPLITUDE = 2;
            if (statePlayed == 1) {
                playSoundEntity(player, 1);
                statePlayed = 2;
            }
        }

        int shakeX = baseX + rng.nextInt(SHAKE_AMPLITUDE * 2 + 1) - SHAKE_AMPLITUDE;
        int shakeY = baseY + rng.nextInt(SHAKE_AMPLITUDE * 2 + 1) - SHAKE_AMPLITUDE;

        guiGraphics.blit(tex, shakeX, shakeY, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);

        if (ticksInCycle >= DURATION_TICKS) {
            cyclesDone++;
            if (cyclesDone >= CYCLES_TOTAL) {
                mc.setOverlay(null);
                return;
            }
            cycleStartTick = t;
            chooseNewPosition(mc);
        }
    }

    private void chooseNewPosition(Minecraft mc) {
        int w = mc.getWindow().getGuiScaledWidth();
        int h = mc.getWindow().getGuiScaledHeight();
        int maxX = Math.max(0, w - IMAGE_WIDTH);
        int maxY = Math.max(0, h - IMAGE_HEIGHT);
        baseX = (maxX > 0) ? rng.nextInt(maxX + 1) : 0;
        baseY = (maxY > 0) ? rng.nextInt(maxY + 1) : 0;
    }
    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
