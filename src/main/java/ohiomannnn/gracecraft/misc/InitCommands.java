package ohiomannnn.gracecraft.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import ohiomannnn.gracecraft.network.GraceCraftNetwork;

public class InitCommands {
    private static final SuggestionProvider<CommandSourceStack> PLAYER_SUGGESTIONS =
            (context, builder) -> {
                for (ServerPlayer player : context.getSource().getServer().getPlayerList().getPlayers()) {
                    builder.suggest(player.getGameProfile().getName());
                }
                return builder.buildFuture();
            };
    private static final SuggestionProvider<CommandSourceStack> ENTITY_SUGGESTIONS =
            (context, builder) -> {
                builder.suggest("EntityDozer");
                builder.suggest("EntityLitany");
                return builder.buildFuture();
            };
    public static void registerCommandSpawnEntity(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("spawnentity")
                        .then(Commands.argument("hudName", StringArgumentType.string())
                                .suggests(ENTITY_SUGGESTIONS)
                                .then(Commands.argument("playerName", StringArgumentType.string())
                                        .suggests(PLAYER_SUGGESTIONS)
                                        .executes(context -> {
                                            ServerPlayer player = context.getSource().getPlayerOrException();
                                            if (!player.hasPermissions(4)) {
                                                player.sendSystemMessage(Component.translatable(
                                                        "commands.gracecraft.spawnentity.noAccess"));
                                                return 0;
                                            }
                                            String hudName = StringArgumentType.getString(context, "hudName");
                                            String playerName = StringArgumentType.getString(context, "playerName");
                                            return executeSpawnEntity(context.getSource(), hudName, playerName);
                                        })))
        );
    }

    private static int executeSpawnEntity(CommandSourceStack source, String hudName, String playerName) {
        ServerPlayer targetPlayer = source.getServer().getPlayerList().getPlayerByName(playerName);
        if (targetPlayer == null) {
            source.sendFailure(Component.translatable("commands.gracecraft.spawnentity.playernotfound"));
            return 0;
        }
        GraceCraftNetwork.sendEntityToClient(targetPlayer, hudName);
        source.sendSuccess(() -> Component.translatable("commands.gracecraft.spawnentity.sendtoplayer", hudName, playerName), true);
        return 1;
    }
}