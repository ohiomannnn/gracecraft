package ohiomannnn.gracecraft.client.network;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import ohiomannnn.gracecraft.entityLogic.EntityEngine;
import ohiomannnn.gracecraft.entityLogic.entities.Dozer;
import ohiomannnn.gracecraft.entityLogic.entities.EntityLitany;
import ohiomannnn.gracecraft.entityLogic.entities.Litany;
import ohiomannnn.gracecraft.network.showEntity.ShowEntityPacket;

public final class GraceCraftClientNetwork {
    public static void handleShowOverlayClientBound(ShowEntityPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (msg.entityName().equals("EntityDozer")) {
                EntityEngine.INSTANCE.add(new Dozer());
            }
            if (msg.entityName().equals("EntityLitany")) {
                EntityLitany.start();
            }
            if (msg.entityName().equals("test")) {
                EntityEngine.INSTANCE.add(new Litany());
            }
        });
    }
}