package ohiomannnn.gracecraft;


import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import ohiomannnn.gracecraft.config.GraceCraftConfig;
import ohiomannnn.gracecraft.entity.InitEntities;
import ohiomannnn.gracecraft.handler.EntitySpawner;
import ohiomannnn.gracecraft.items.InitItems;
import ohiomannnn.gracecraft.misc.InitCommands;
import ohiomannnn.gracecraft.misc.InitCreativeModeTabs;
import ohiomannnn.gracecraft.network.GraceCraftNetwork;
import ohiomannnn.gracecraft.sounds.InitSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(GraceCraft.MOD_ID)
public class GraceCraft {
    public static final String MOD_ID = "gracecraft";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public GraceCraft(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(GraceCraftNetwork::registerPackets);

        InitItems.register(modEventBus);
        InitEntities.register(modEventBus);
        InitSounds.register(modEventBus);
        InitCreativeModeTabs.register(modEventBus);
        GraceCraftConfig.register(container);

        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        InitCommands.registerCommandSpawnEntity(event.getDispatcher());
    }
    @SubscribeEvent
    public void worldTick(LevelTickEvent.Pre event) {
        if (event.getLevel().isClientSide) return;
        EntitySpawner.rollTheDice(event.getLevel());
    }

    public static boolean isCrouchingLitany;
    public static boolean isCrouchingDozer;

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

        isCrouchingDozer = shiftDown && noOtherKeys;
        isCrouchingLitany = shiftDown;
    }
}
