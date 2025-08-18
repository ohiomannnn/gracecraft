package ohiomannnn.gracecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import ohiomannnn.gracecraft.network.GraceCraftClientNetwork;
import ohiomannnn.gracecraft.util.ItemCheck;

@Mod(value = GraceCraft.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = GraceCraft.MOD_ID, value = Dist.CLIENT)
public class GraceCraftClient {
    public GraceCraftClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
//    @SubscribeEvent
//    public static void onPlayerTick(PlayerTickEvent.Post event) {
//        if (Minecraft.getInstance().level == null) {
//            return;
//        }
//        Player player = Minecraft.getInstance().player;
//
//        assert player != null;
//        if (ItemCheck.isHoldingItem(player)) {
//            player.sendSystemMessage(Component.literal("У вас предмет!"));
//        }
//    }
    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        GraceCraftClientNetwork.registerClientPayloads(event);
    }
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {}
}
