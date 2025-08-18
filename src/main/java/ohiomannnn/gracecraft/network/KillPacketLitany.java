package ohiomannnn.gracecraft.network;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;

import java.util.UUID;

public record KillPacketLitany(UUID targetId) implements CustomPacketPayload {

    public static final Type<KillPacketLitany> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "kill_litany"));

    public static final StreamCodec<FriendlyByteBuf, KillPacketLitany> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public KillPacketLitany decode(FriendlyByteBuf buf) {
                    return new KillPacketLitany(buf.readUUID());
                }

                @Override
                public void encode(FriendlyByteBuf buf, KillPacketLitany msg) {
                    buf.writeUUID(msg.targetId());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}