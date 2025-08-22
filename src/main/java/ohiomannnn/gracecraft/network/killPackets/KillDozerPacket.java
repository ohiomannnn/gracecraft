package ohiomannnn.gracecraft.network.killPackets;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;

import java.util.UUID;

public record KillDozerPacket(UUID targetId) implements CustomPacketPayload {

    public static final Type<KillDozerPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "kill_dozer"));

    public static final StreamCodec<FriendlyByteBuf, KillDozerPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public KillDozerPacket decode(FriendlyByteBuf buf) {
                    return new KillDozerPacket(buf.readUUID());
                }

                @Override
                public void encode(FriendlyByteBuf buf, KillDozerPacket msg) {
                    buf.writeUUID(msg.targetId());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}