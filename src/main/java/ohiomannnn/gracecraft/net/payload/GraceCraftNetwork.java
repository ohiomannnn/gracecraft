package ohiomannnn.gracecraft.net.payload;

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

import java.util.UUID;

import static ohiomannnn.gracecraft.util.DamageTypes.DOZER_ATTACK;

public final class GraceCraftNetwork {

    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar(GraceCraft.MOD_ID);

        registrar.playToServer(
                KillPacket.TYPE,
                KillPacket.STREAM_CODEC,
                GraceCraftNetwork::handleKillDozerBound
        );
    }

    private static void handleKillDozerBound(final KillPacket msg, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer sender)) return;

            MinecraftServer server = sender.server;
            UUID target = msg.targetId();

            for (ServerLevel level : server.getAllLevels()) {
                var entity = level.getEntity(target);
                if (entity instanceof LivingEntity living) {
                    DamageSource src = new DamageSource(
                            // Получаем Holder нужного типа через world.registryAccess
                            level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DOZER_ATTACK)
                    );
                    living.hurt(src, Float.MAX_VALUE);
                }
            }
        });
    }

    public static void sendKillToServer(UUID target) {
        PacketDistributor.sendToServer(new KillPacket(target));
    }
    public static void sendOverlayToClient(ServerPlayer player, String overlayName) {
        PacketDistributor.sendToPlayer(player, new ShowOverlayPacket(overlayName));
    }
}