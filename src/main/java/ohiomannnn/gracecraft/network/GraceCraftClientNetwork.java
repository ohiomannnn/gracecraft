package ohiomannnn.gracecraft.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.entityOverlay.EntityDozer;
import ohiomannnn.gracecraft.entityLogic.entityOverlay.EntityLitany;
import ohiomannnn.gracecraft.network.showOverlay.ShowEntityPacket;

public final class GraceCraftClientNetwork {
    public static void registerClientPayloads(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(GraceCraft.MOD_ID);

        registrar.playToClient(
                ShowEntityPacket.TYPE,
                ShowEntityPacket.STREAM_CODEC,
                GraceCraftClientNetwork::handleShowOverlayClientbound
        );
    }
    private static void handleShowOverlayClientbound(ShowEntityPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (msg.entityName().equals("EntityDozer")) {
                EntityDozer.start();
            }
            if (msg.entityName().equals("EntityLitany")) {
                EntityLitany.start();
            }
        });
    }
}