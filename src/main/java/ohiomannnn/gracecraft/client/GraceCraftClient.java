package ohiomannnn.gracecraft.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.entities.EntityDozer;
import ohiomannnn.gracecraft.entityLogic.entities.EntityLitany;
import ohiomannnn.gracecraft.items.InitItems;
import ohiomannnn.gracecraft.client.network.GraceCraftClientNetwork;

@Mod(value = GraceCraft.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = GraceCraft.MOD_ID, value = Dist.CLIENT)
public class GraceCraftClient {
    public GraceCraftClient(IEventBus modEventBus) {
        modEventBus.addListener(GraceCraftClientNetwork::registerClientPackets);

        modEventBus.addListener(EntityDozer::RegisterGuiLayers);
        modEventBus.addListener(EntityLitany::RegisterGuiLayers);
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
        } else if (mainHand.is(InitItems.FLASHLIGHT)) {
            model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        } else if (offHand.is(InitItems.FLASHLIGHT)) {
            model.leftArmPose = HumanoidModel.ArmPose.EMPTY;
        }
    }
}
