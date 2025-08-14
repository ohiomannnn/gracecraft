package ohiomannnn.gracecraft.network.payload;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;

import java.util.UUID;

public record KillPacketDozer(UUID targetId) implements CustomPacketPayload {

    public static final Type<KillPacketDozer> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "kill_dozer"));

    public static final StreamCodec<FriendlyByteBuf, KillPacketDozer> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public KillPacketDozer decode(FriendlyByteBuf buf) {
                    return new KillPacketDozer(buf.readUUID());
                }

                @Override
                public void encode(FriendlyByteBuf buf, KillPacketDozer msg) {
                    buf.writeUUID(msg.targetId());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}