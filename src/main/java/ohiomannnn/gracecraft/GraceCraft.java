package ohiomannnn.gracecraft;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import ohiomannnn.gracecraft.blocks.InitBlocks;
import ohiomannnn.gracecraft.entityLogic.entities.EntityDozer;
import ohiomannnn.gracecraft.entityLogic.entities.EntityLitany;
import ohiomannnn.gracecraft.items.InitItems;
import ohiomannnn.gracecraft.items.doombringer.DoombringerItem;
import ohiomannnn.gracecraft.misc.InitCreativeModeTabs;
import ohiomannnn.gracecraft.misc.InitCommands;
import ohiomannnn.gracecraft.network.GraceCraftNetwork;
import ohiomannnn.gracecraft.sounds.InitSounds;
import org.slf4j.Logger;

@Mod(GraceCraft.MOD_ID)
public class GraceCraft {
    public static final String MOD_ID = "gracecraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GraceCraft(IEventBus modEventBus) {
        modEventBus.addListener(GraceCraftNetwork::registerPayloads);
        modEventBus.addListener(EntityDozer::RegisterGuiLayers);
        modEventBus.addListener(EntityLitany::RegisterGuiLayers);

        InitItems.register(modEventBus);
        InitBlocks.register(modEventBus);
        InitSounds.register(modEventBus);
        InitCreativeModeTabs.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
    }
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        InitCommands.registerCommandSpawnEntity(event.getDispatcher());
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
    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getSource().is(DamageTypes.EXPLOSION)) {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (stack.getItem() instanceof DoombringerItem) {
                        player.getInventory().setItem(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }
}
