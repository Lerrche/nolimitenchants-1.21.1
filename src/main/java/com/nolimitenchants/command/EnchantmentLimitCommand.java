package com.nolimitenchants.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.nolimitenchants.config.MaxLevelConfig;
import com.nolimitenchants.network.EnchantmentLimitNetworking;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/** Registers the operator-only command used to configure the universal enchantment cap. */
public final class EnchantmentLimitCommand {
    private EnchantmentLimitCommand() {
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("nolimitenchants")
                .requires(source -> source.hasPermission(2))
                .executes(context -> showCurrentLimit(context.getSource()))
                .then(Commands.literal("max")
                        .executes(context -> showCurrentLimit(context.getSource()))
                        .then(Commands.argument("level", IntegerArgumentType.integer(MaxLevelConfig.MIN_LEVEL, MaxLevelConfig.MAX_LEVEL))
                                .executes(context -> setMaxLevel(context.getSource(), IntegerArgumentType.getInteger(context, "level"))))));
    }

    private static int showCurrentLimit(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Universal enchantment max level: " + MaxLevelConfig.getMaxLevel()
                + ". Set it with /nolimitenchants max <1-255> (operators only)."), false);
        return 1;
    }

    private static int setMaxLevel(CommandSourceStack source, int level) {
        boolean saved = MaxLevelConfig.setMaxLevel(level);
        EnchantmentLimitNetworking.syncToAllPlayers();
        String message = "Universal enchantment max level set to " + level + ".";
        if (saved) {
            message += " Saved to config/nolimitenchants.properties; no restart required.";
        } else {
            message += " Warning: the setting is active now but could not be saved to disk.";
        }
        final String response = message;
        source.sendSuccess(() -> Component.literal(response), true);
        return 1;
    }
}
