package ohiomannnn.gracecraft.network.payload;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.EntityDozerOverlay;
import ohiomannnn.gracecraft.entityLogic.EntityLitanyOverlay;

public final class GraceCraftClientNetwork {
    public static void registerClientPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(GraceCraft.MOD_ID);

        registrar.playToClient(
                ShowOverlayPacket.TYPE,
                ShowOverlayPacket.STREAM_CODEC,
                GraceCraftClientNetwork::handleShowOverlayClientbound
        );
    }

    private static void handleShowOverlayClientbound(ShowOverlayPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if ("EntityDozer".equals(msg.overlayName())) {
                Minecraft.getInstance().setOverlay(new EntityDozerOverlay());
            }
            if ("EntityLitany".equals(msg.overlayName())) {
                Minecraft.getInstance().setOverlay(new EntityLitanyOverlay());
            }
            // add other overlayes (entities) here
        });
    }
}