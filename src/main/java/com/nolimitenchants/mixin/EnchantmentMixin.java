package com.nolimitenchants.mixin;

import com.nolimitenchants.config.MaxLevelConfig;
import com.nolimitenchants.util.RomanNumerals;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    /**
     * Applies one configurable max level to all vanilla and data-driven/modded enchantments.
     * ItemEnchantments in Minecraft 1.21.1 has a hard storage/codec ceiling of 255.
     */
    @Inject(method = "getMaxLevel", at = @At("RETURN"), cancellable = true)
    private void nolimitenchants$overrideMaxLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(MaxLevelConfig.getMaxLevel());
    }

    /** Render levels above X as Roman numerals instead of missing enchantment.level.N keys. */
    @Inject(method = "getFullname", at = @At("HEAD"), cancellable = true)
    private static void nolimitenchants$formatHighLevels(Holder<Enchantment> enchantment, int level,
            CallbackInfoReturnable<Component> cir) {
        if (level > 10) {
            MutableComponent name = enchantment.value().description().copy();
            name.append(Component.literal(" " + RomanNumerals.format(level)).withStyle(ChatFormatting.GRAY));
            cir.setReturnValue(name);
        }
    }
}
