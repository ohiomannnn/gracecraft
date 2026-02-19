package ohiomannnn.gracecraft.handler;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import ohiomannnn.gracecraft.config.GraceCraftConfig;
import ohiomannnn.gracecraft.network.GraceCraftNetwork;
import ohiomannnn.gracecraft.network.showEntity.ShowEntityPacket;

import java.util.Random;

public class EntitySpawner {

    private static final Random random = new Random();

    public static int DOZER_SPAWN = GraceCraftConfig.COMMON.DOZER_SPAWN.get();
    public static int LITANY_SPAWN = GraceCraftConfig.COMMON.LITANY_SPAWN.get();

    public static void rollTheDice(Level level) {
        if (!GraceCraftConfig.COMMON.SPAWN_ENTITY_RANDOM.get()) return;
        if (level.getGameTime() % (DOZER_SPAWN + random.nextInt(GraceCraftConfig.COMMON.RAND_VALUE.get())) == 0) {
            if (!level.players().isEmpty()) {
                Player player = level.players().get(level.random.nextInt(level.players().size()));

                PacketDistributor.sendToPlayer((ServerPlayer) player, new ShowEntityPacket("EntityDozer"));
            }
        }
        if (level.getGameTime() % (LITANY_SPAWN + random.nextInt(GraceCraftConfig.COMMON.RAND_VALUE.get())) == 0) {
            if (!level.players().isEmpty()) {
                Player player = level.players().get(level.random.nextInt(level.players().size()));

                PacketDistributor.sendToPlayer((ServerPlayer) player, new ShowEntityPacket("EntityLitany"));
            }
        }
    }
}
