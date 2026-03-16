package ohiomannnn.gracecraft.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.client.network.GraceCraftClientNetwork;
import ohiomannnn.gracecraft.network.killPackets.KillGeneric;
import ohiomannnn.gracecraft.network.showEntity.ShowEntityPacket;

public class GraceCraftNetwork {

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(GraceCraft.MOD_ID);

        registrar.playToClient(
                ShowEntityPacket.TYPE,
                ShowEntityPacket.STREAM_CODEC,
                GraceCraftClientNetwork::handleShowOverlayClientBound
        );
        registrar.playToServer(KillGeneric.TYPE, KillGeneric.STREAM_CODEC, KillGeneric::handleServer);
    }
}