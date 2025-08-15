package ohiomannnn.gracecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import ohiomannnn.gracecraft.blocks.InitBlocks;
import ohiomannnn.gracecraft.misc.CreativeModeTabs;
import ohiomannnn.gracecraft.items.InitItems;
import ohiomannnn.gracecraft.misc.ModCommands;
import ohiomannnn.gracecraft.network.payload.GraceCraftNetwork;
import ohiomannnn.gracecraft.sounds.InitSounds;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

import static ohiomannnn.gracecraft.entityLogic.EntityLitanyOverlay.isCrouchingLitany;

@Mod(GraceCraft.MOD_ID)
public class GraceCraft {
    public static final String MOD_ID = "gracecraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GraceCraft(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(GraceCraftNetwork::registerPayloads);

        InitItems.register(modEventBus);
        InitBlocks.register(modEventBus);
        InitSounds.register(modEventBus);
        CreativeModeTabs.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }
    private void commonSetup(FMLCommonSetupEvent event) {}

    public static boolean isCrouching;
    @SubscribeEvent
    public void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        boolean shiftDown = mc.options.keyShift.isDown();

        boolean noOtherKeys =
                !mc.options.keyUp.isDown() &&
                        !mc.options.keyLeft.isDown() &&
                        !mc.options.keyDown.isDown() &&
                        !mc.options.keyRight.isDown() &&
                        !mc.options.keyJump.isDown() &&
                        !mc.options.keySprint.isDown();

        isCrouching = shiftDown && noOtherKeys;
        isCrouchingLitany = shiftDown;
    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}
