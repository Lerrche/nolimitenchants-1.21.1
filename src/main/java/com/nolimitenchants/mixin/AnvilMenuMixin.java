package com.nolimitenchants.mixin;

import com.nolimitenchants.config.MaxLevelConfig;

import net.minecraft.core.Holder;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Restores enchantment levels that vanilla's anvil calculation may have clamped. */
@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {
    @Inject(method = "createResult", at = @At("TAIL"))
    private void nolimitenchants$preserveOverleveledEnchantments(CallbackInfo ci) {
        ItemCombinerMenuAccessor accessor = (ItemCombinerMenuAccessor) (Object) this;
        Container inputSlots = accessor.nolimitenchants$getInputSlots();
        ResultContainer resultSlots = accessor.nolimitenchants$getResultSlots();

        ItemStack left = inputSlots.getItem(0);
        ItemStack right = inputSlots.getItem(1);
        ItemStack result = resultSlots.getItem(0);
        if (left.isEmpty() || right.isEmpty() || result.isEmpty()) {
            return;
        }

        ItemEnchantments leftEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(left);
        ItemEnchantments rightEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(right);
        ItemEnchantments resultEnchantments = EnchantmentHelper.getEnchantmentsForCrafting(result);
        ItemEnchantments.Mutable updated = new ItemEnchantments.Mutable(resultEnchantments);
        boolean changed = false;
        int configuredMaximum = MaxLevelConfig.getMaxLevel();

        // Only edit enchantments vanilla already placed on the result. This preserves normal
        // item compatibility and anvil rules while undoing a level-only downgrade.
        for (Holder<Enchantment> enchantment : resultEnchantments.keySet()) {
            int resultLevel = resultEnchantments.getLevel(enchantment);
            int leftLevel = leftEnchantments.getLevel(enchantment);
            int rightLevel = rightEnchantments.getLevel(enchantment);
            int desiredLevel;
            if (leftLevel > configuredMaximum) {
                // The left-hand item is the target: preserve its old level, but do not use the
                // sacrifice item to push that existing over-cap level higher.
                desiredLevel = leftLevel;
            } else {
                // New levels added from the sacrifice item/book obey the configured cap.
                desiredLevel = Math.max(leftLevel, Math.min(rightLevel, configuredMaximum));

                // Vanilla increments a level when equal levels are combined.
                if (leftLevel > 0 && leftLevel == rightLevel && leftLevel < configuredMaximum) {
                    desiredLevel = Math.max(desiredLevel, leftLevel + 1);
                }
                // Preserve the result if vanilla already produced a valid level, but keep this
                // value within the configured limit for an enchantment newly applied here.
                desiredLevel = Math.max(desiredLevel, Math.min(resultLevel, configuredMaximum));
            }
            desiredLevel = Math.min(MaxLevelConfig.MAX_LEVEL, desiredLevel);

            if (desiredLevel != resultLevel) {
                updated.set(enchantment, desiredLevel);
                changed = true;
            }
        }

        if (changed) {
            ItemStack fixedResult = result.copy();
            EnchantmentHelper.setEnchantments(fixedResult, updated.toImmutable());
            resultSlots.setItem(0, fixedResult);
        }
    }
}
