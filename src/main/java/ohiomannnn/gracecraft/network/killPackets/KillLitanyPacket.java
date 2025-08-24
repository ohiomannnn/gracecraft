package ohiomannnn.gracecraft.network.killPackets;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record KillLitanyPacket(UUID targetId) implements CustomPacketPayload {

    public static final Type<KillLitanyPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "kill_litany"));

    public static final StreamCodec<FriendlyByteBuf, KillLitanyPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public @NotNull KillLitanyPacket decode(FriendlyByteBuf buf) {
                    return new KillLitanyPacket(buf.readUUID());
                }

                @Override
                public void encode(FriendlyByteBuf buf, KillLitanyPacket msg) {
                    buf.writeUUID(msg.targetId());
                }
            };

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}