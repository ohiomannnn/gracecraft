package ohiomannnn.gracecraft.network;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.network.killPackets.KillDozerPacket;
import ohiomannnn.gracecraft.network.killPackets.KillLitanyPacket;
import ohiomannnn.gracecraft.network.showEntity.ShowEntityPacket;

import java.util.UUID;

import static ohiomannnn.gracecraft.misc.InitDamageTypes.DOZER_ATTACK;
import static ohiomannnn.gracecraft.misc.InitDamageTypes.LITANY_ATTACK;

public final class GraceCraftNetwork {

    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(GraceCraft.MOD_ID);

        registrar.playToServer(
                KillDozerPacket.TYPE,
                KillDozerPacket.STREAM_CODEC,
                GraceCraftNetwork::handleKillDozerBound
        );
        registrar.playToServer(
                KillLitanyPacket.TYPE,
                KillLitanyPacket.STREAM_CODEC,
                GraceCraftNetwork::handleKillLitanyBound
        );
    }

    private static void handleKillDozerBound(final KillDozerPacket msg, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer sender)) return;

            MinecraftServer server = sender.server;
            UUID target = msg.targetId();

            for (ServerLevel level : server.getAllLevels()) {
                var entity = level.getEntity(target);
                if (entity instanceof LivingEntity living) {
                    DamageSource src = new DamageSource(
                            level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DOZER_ATTACK)
                    );
                    living.hurt(src, Float.MAX_VALUE);
                }
            }
        });
    }
    private static void handleKillLitanyBound(final KillLitanyPacket msg, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer sender)) return;

            MinecraftServer server = sender.server;
            UUID target = msg.targetId();

            for (ServerLevel level : server.getAllLevels()) {
                var entity = level.getEntity(target);
                if (entity instanceof LivingEntity living) {
                    DamageSource src = new DamageSource(
                            level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(LITANY_ATTACK)
                    );
                    living.hurt(src, Float.MAX_VALUE);
                }
            }
        });
    }

    public static void sendKillToServerWDozer(UUID target) {
        PacketDistributor.sendToServer(new KillDozerPacket(target));
    }
    public static void sendKillToServerWLitany(UUID target) {
        PacketDistributor.sendToServer(new KillLitanyPacket(target));
    }
    public static void sendEntityToClient(ServerPlayer player, String overlayName) {
        PacketDistributor.sendToPlayer(player, new ShowEntityPacket(overlayName));
    }
}