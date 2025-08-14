package ohiomannnn.gracecraft.network.payload;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;

public record ShowOverlayPacket(String overlayName) implements CustomPacketPayload {

    public static final Type<ShowOverlayPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "show_overlay"));

    public static final StreamCodec<FriendlyByteBuf, ShowOverlayPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public ShowOverlayPacket decode(FriendlyByteBuf buf) {
                    return new ShowOverlayPacket(buf.readUtf());
                }

                @Override
                public void encode(FriendlyByteBuf buf, ShowOverlayPacket msg) {
                    buf.writeUtf(msg.overlayName());
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}