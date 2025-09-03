package ohiomannnn.gracecraft.client.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entityLogic.entities.EntityDozer;
import ohiomannnn.gracecraft.entityLogic.entities.EntityLitany;
import ohiomannnn.gracecraft.network.showEntity.ShowEntityPacket;

public final class GraceCraftClientNetwork {
    public static void registerClientPackets(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(GraceCraft.MOD_ID);

        registrar.playToClient(
                ShowEntityPacket.TYPE,
                ShowEntityPacket.STREAM_CODEC,
                GraceCraftClientNetwork::handleShowOverlayClientBound
        );
    }
    private static void handleShowOverlayClientBound(ShowEntityPacket msg, IPayloadContext ctx) {
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