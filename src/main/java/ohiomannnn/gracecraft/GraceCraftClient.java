package ohiomannnn.gracecraft;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import ohiomannnn.gracecraft.network.payload.GraceCraftClientNetwork;

@Mod(value = GraceCraft.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = GraceCraft.MOD_ID, value = Dist.CLIENT)
public class GraceCraftClient {
    public GraceCraftClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        GraceCraftClientNetwork.registerClientPayloads(event);
    }
    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {}
}
