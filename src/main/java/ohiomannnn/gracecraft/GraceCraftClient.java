package ohiomannnn.gracecraft;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import ohiomannnn.gracecraft.items.InitItems;
import ohiomannnn.gracecraft.network.GraceCraftClientNetwork;

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
    public static void onRenderPlayer(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<?>> event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(event.getRenderer().getModel() instanceof PlayerModel<?> model)) return;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        if (mainHand.is(InitItems.DOOMBRINGER)) {
            model.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
        } else if (offHand.is(InitItems.DOOMBRINGER)) {
            model.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
        }
    }
}
