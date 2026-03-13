package ohiomannnn.gracecraft.network.killPackets;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.misc.InitDamageTypes;

public record KillGeneric(String damageType) implements CustomPacketPayload {

    public static final Type<KillGeneric> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "kill_generic"));

    public static final StreamCodec<FriendlyByteBuf, KillGeneric> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public KillGeneric decode(FriendlyByteBuf buf) {
                    return new KillGeneric(buf.readUtf());
                }

                @Override
                public void encode(FriendlyByteBuf buf, KillGeneric msg) {
                    buf.writeUtf(msg.damageType);
                }
            };

    public static void handleServer(KillGeneric packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();

            switch (packet.damageType()) {
                case "dozer" -> player.hurt(player.damageSources().source(InitDamageTypes.DOZER_ATTACK), Float.MAX_VALUE);
                case "litany" -> player.hurt(player.damageSources().source(InitDamageTypes.LITANY_ATTACK), Float.MAX_VALUE);
            }
        });
    }

    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}