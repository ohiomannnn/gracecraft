package ohiomannnn.gracecraft.net.payload;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;

import java.util.UUID;

public record KillPacket(UUID targetId) implements CustomPacketPayload {

    public static final Type<KillPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "kill"));

    public static final StreamCodec<FriendlyByteBuf, KillPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public KillPacket decode(FriendlyByteBuf buf) {
                    return new KillPacket(buf.readUUID());
                }

                @Override
                public void encode(FriendlyByteBuf buf, KillPacket msg) {
                    buf.writeUUID(msg.targetId());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}