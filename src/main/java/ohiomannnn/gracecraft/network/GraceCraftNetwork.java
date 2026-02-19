package ohiomannnn.gracecraft.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.network.killPackets.KillGeneric;

public class GraceCraftNetwork {

    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(GraceCraft.MOD_ID);

        registrar.playToServer(KillGeneric.TYPE, KillGeneric.STREAM_CODEC, KillGeneric::handleServer);
    }
}