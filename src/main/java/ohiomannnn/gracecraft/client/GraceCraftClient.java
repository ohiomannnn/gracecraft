package ohiomannnn.gracecraft.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.*;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.client.network.GraceCraftClientNetwork;
import ohiomannnn.gracecraft.entity.InitEntities;
import ohiomannnn.gracecraft.entityLogic.entities.EntityDozer;
import ohiomannnn.gracecraft.entityLogic.entities.EntityLitany;
import ohiomannnn.gracecraft.items.InitItems;
import ohiomannnn.gracecraft.render.SorrowRenderer;
import ohiomannnn.gracecraft.util.Clock;

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
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(InitEntities.SORROW.get(), SorrowRenderer::new);
    }

    public static final int redDuration = 950;
    public static long redTimeStamp;
    public static final int shakeDuration = 1_500;
    public static long shakeTimestamp;

    @SubscribeEvent
    public static void onFogDensity(ViewportEvent.RenderFog event) {
        long elapsed = Clock.getMs() - redTimeStamp;
        if (elapsed < redDuration) {
            float progress = elapsed / (float) redDuration;

            float startFar = event.getFarPlaneDistance();
            float endFar   = 20.0F;

            float far = startFar + (endFar - startFar) * progress;

            event.setNearPlaneDistance(1.0F);
            event.setFarPlaneDistance(far);

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        long elapsed = Clock.getMs() - redTimeStamp;
        if (elapsed < redDuration) {
            float progress = elapsed / (float) redDuration;
            if (progress > 1.0F) progress = 1.0F;

            float baseR = event.getRed();
            float baseG = event.getGreen();
            float baseB = event.getBlue();

            float targetR = 0.6F;
            float targetG = 0.0F;
            float targetB = 0.0F;

            float r = baseR + (targetR - baseR) * progress;
            float g = baseG + (targetG - baseG) * progress;
            float b = baseB + (targetB - baseB) * progress;

            event.setRed(r);
            event.setGreen(g);
            event.setBlue(b);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderGuiPre(RenderGuiEvent.Pre event) {
        long now = System.currentTimeMillis();
        long end = shakeTimestamp + shakeDuration;

        if (now < end) {
            float progress = (float) (now - shakeTimestamp) / shakeDuration;

            float fade = 1.0F - progress;

            float mult = 2.0F * fade;

            double horizontal = Mth.clamp(Math.sin(now * 0.02), -0.7, 0.7) * 15;
            double vertical   = Mth.clamp(Math.sin(now * 0.01 + 2), -0.7, 0.7) * 3;

            GuiGraphics graphics = event.getGuiGraphics();
            graphics.pose().translate(horizontal * mult, vertical * mult, 0);
        }
    }


    @SubscribeEvent
    public static void onRenderWorldLast(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            Clock.update();
        }
    }
}
