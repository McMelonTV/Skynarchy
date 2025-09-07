package ing.boykiss.skynarchy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import ing.boykiss.skynarchy.worldgen.island.IslandSize;
import ing.boykiss.skynarchy.worldgen.island.WorldIsland;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

public class IslandCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("island")
                .then(Commands.literal("generate")
                        .then(Commands.argument("size", IntegerArgumentType.integer(1, 3))
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1, 10))
                                        .executes(IslandCommand::generateIsland))))
                .then(Commands.literal("clear")
                        .executes(IslandCommand::clearArea)));
    }

    private static int generateIsland(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (source.getEntity() instanceof Player player) {
            ServerLevel level = source.getLevel();
            BlockPos pos = player.blockPosition();

            int sizeArg = IntegerArgumentType.getInteger(context, "size");
            int amount = IntegerArgumentType.getInteger(context, "amount");

            IslandSize size = switch (sizeArg) {
                case 1 -> IslandSize.SMALL;
                case 2 -> IslandSize.MEDIUM;
                case 3 -> IslandSize.LARGE;
                default -> IslandSize.MEDIUM;
            };

            WorldIsland.createIsland(new Random(), level, pos, size, amount);

            source.sendSuccess(() -> Component.literal("Generating island at " + pos), true);
            return 1;
        }

        source.sendFailure(Component.literal("Command must be run by a player"));
        return 0;
    }

    private static int clearArea(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();

        if (source.getEntity() instanceof Player player) {
            ServerLevel level = source.getLevel();
            BlockPos center = player.blockPosition();

            // Clear a 128x128x60 area
            for (int x = -64; x <= 64; x++) {
                for (int z = -64; z <= 64; z++) {
                    for (int y = -30; y <= 30; y++) {
                        level.setBlock(center.offset(x, y, z),
                                net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }

            source.sendSuccess(() -> Component.literal("Cleared area around " + center), true);
            return 1;
        }

        source.sendFailure(Component.literal("Command must be run by a player"));
        return 0;
    }
}
