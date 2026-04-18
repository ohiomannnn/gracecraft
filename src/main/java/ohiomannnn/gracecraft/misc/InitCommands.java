package ohiomannnn.gracecraft.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.KillCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import ohiomannnn.gracecraft.network.showEntity.ShowEntityPacket;

import java.util.Collection;

public class InitCommands {

    private static final SuggestionProvider<CommandSourceStack> ENTITY_SUGGESTIONS =
            (context, builder) -> {
                builder.suggest("Dozer");
                builder.suggest("Sorrow");
                builder.suggest("Litany");
                return builder.buildFuture();
            };

    public static void registerCommandSpawnEntity(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("spawnentity")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("entity", StringArgumentType.string())
                                .suggests(ENTITY_SUGGESTIONS)
                                .then(Commands.argument("target", EntityArgument.players())
                                        .executes(context ->
                                            spawnEntity(
                                                    context,
                                                    EntityArgument.getPlayers(context, "target"),
                                                    StringArgumentType.getString(context, "entity"),
                                                    0
                                            )
                                        )
                                        .then(Commands.argument("data", IntegerArgumentType.integer())
                                                .executes(context ->
                                                        spawnEntity(
                                                                context,
                                                                EntityArgument.getPlayers(context, "target"),
                                                                StringArgumentType.getString(context, "entity"),
                                                                IntegerArgumentType.getInteger(context, "data")
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }

    private static int spawnEntity(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players, String name, int data) {

        for (ServerPlayer serverplayer : players) {
            PacketDistributor.sendToPlayer(serverplayer, new ShowEntityPacket(name, data));
        }

        if (players.size() == 1) {
            context.getSource().sendSuccess(() -> Component.translatable("commands.gracecraft.spawnentity.sendtoplayer.single", name, players.iterator().next().getDisplayName()), true);
        } else {
            context.getSource().sendSuccess(() -> Component.translatable("commands.gracecraft.spawnentity.sendtoplayer", name, players.size()), true);
        }

        return 1;
    }
}