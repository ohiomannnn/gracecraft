package ohiomannnn.gracecraft.util.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.client.sound.GuiLoopingSoundInstance;
import ohiomannnn.gracecraft.sounds.InitSounds;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelLoadingScreen.class)
public class LevelLoadingScreenMixin extends Screen {

    @Unique private GuiLoopingSoundInstance loopSound;

    @Unique private static final ResourceLocation GOD = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/gui/god.png");

    protected LevelLoadingScreenMixin(Component title) { super(title); }

    @Override
    protected void init() {
        loopSound = new GuiLoopingSoundInstance(
                InitSounds.SINE.get(),
                10.0F,
                1.0F,
                () -> false
        );
        Minecraft.getInstance().getSoundManager().play(loopSound);
    }

    @Override
    public void onClose() {
        if (loopSound != null) {
            Minecraft.getInstance().getSoundManager().stop(loopSound);
            loopSound = null;
        }
        super.onClose();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {

        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int x = (width - 50) / 2;
        int y = (height - 50) / 2;

        double time = System.currentTimeMillis() / 1000.0;
        int oy = (int) (Math.sin(time * 6) * 2);

        guiGraphics.fill(0, 0, screenWidth, screenHeight, 0xFF000000);
        guiGraphics.blit(GOD, x, y + oy, 50, 50, 0, 0, 256, 256, 256, 256);

        double speed = 6.0;

        double rangeX = 15.0;
        double rangeY = 5.0;

        int offsetX = (int) (Math.sin(time * speed) * rangeX);
        int offsetY = (int) (Math.sin(time * speed * 2) * rangeY);

        guiGraphics.drawCenteredString(this.font, "It's almost time.", (x + offsetX) + 20, (y + offsetY) + 60, 0xFFFFFFFF);
    }
}