package ohiomannnn.gracecraft.entityLogic;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class EngineHandler {

    @SubscribeEvent
    public static void onLeave(ClientPlayerNetworkEvent.LoggingOut event) {
        EntityEngine.INSTANCE.clear();
    }

    @SubscribeEvent
    public static void registerGuis(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.parse("guis"), (guiGraphics, partialTick) -> {
            EntityEngine.INSTANCE.render(guiGraphics);
        });
    }

    @SubscribeEvent
    public static void onTick(ClientTickEvent.Pre event) {
        EntityEngine.INSTANCE.tick();
    }
}
