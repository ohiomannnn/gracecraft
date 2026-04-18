package ohiomannnn.gracecraft.network.showEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;
import org.jetbrains.annotations.NotNull;

public record ShowEntityPacket(String entityName, int addData) implements CustomPacketPayload {

    public static final Type<ShowEntityPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "show_entity"));

    public static final StreamCodec<FriendlyByteBuf, ShowEntityPacket> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public ShowEntityPacket decode(FriendlyByteBuf buf) {
                    return new ShowEntityPacket(buf.readUtf(), buf.readInt());
                }

                @Override
                public void encode(FriendlyByteBuf buf, ShowEntityPacket msg) {
                    buf.writeUtf(msg.entityName());
                    buf.writeInt(msg.addData);
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}