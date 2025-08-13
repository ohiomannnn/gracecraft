package ohiomannnn.gracecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import ohiomannnn.gracecraft.blocks.InitBlocks;
import ohiomannnn.gracecraft.misc.CreativeModeTabs;
import ohiomannnn.gracecraft.items.InitItems;
import ohiomannnn.gracecraft.misc.ModCommands;
import ohiomannnn.gracecraft.net.payload.GraceCraftNetwork;
import ohiomannnn.gracecraft.sounds.InitSounds;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(GraceCraft.MOD_ID)
public class GraceCraft {
    public static final String MOD_ID = "gracecraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public GraceCraft(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(GraceCraftNetwork::registerPayloads);

        InitItems.register(modEventBus);
        InitBlocks.register(modEventBus);
        InitSounds.register(modEventBus);
        CreativeModeTabs.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (GraceCraft) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
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
    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {}

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}
