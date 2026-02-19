package ohiomannnn.gracecraft.network.killPackets;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.misc.InitDamageTypes;

import java.util.UUID;

public record KillGeneric(UUID targetId, String damageType) implements CustomPacketPayload {

    public static final Type<KillGeneric> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "kill_generic"));

    public static final StreamCodec<FriendlyByteBuf, KillGeneric> STREAM_CODEC =
            new StreamCodec<>() {
                @Override
                public KillGeneric decode(FriendlyByteBuf buf) {
                    return new KillGeneric(buf.readUUID(), buf.readUtf());
                }

                @Override
                public void encode(FriendlyByteBuf buf, KillGeneric msg) {
                    buf.writeUUID(msg.targetId);
                    buf.writeUtf(msg.damageType);
                }
            };

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handleServer(KillGeneric packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer sender)) return;

            MinecraftServer server = sender.server;
            UUID target = packet.targetId();

            for (ServerLevel level : server.getAllLevels()) {
                Entity entity = level.getEntity(target);
                if (entity instanceof LivingEntity living) {
                    switch (packet.damageType()) {
                        case "dozer" -> living.hurt(living.damageSources().source(InitDamageTypes.DOZER_ATTACK), Float.MAX_VALUE);
                        case "litany" -> living.hurt(living.damageSources().source(InitDamageTypes.LITANY_ATTACK), Float.MAX_VALUE);
                    }
                }
            }
        });
    }
}