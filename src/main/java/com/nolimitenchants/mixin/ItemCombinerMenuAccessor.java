package com.nolimitenchants.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ResultContainer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Accesses the input and result containers declared by ItemCombinerMenu. */
@Mixin(ItemCombinerMenu.class)
public interface ItemCombinerMenuAccessor {
    @Accessor("inputSlots")
    Container nolimitenchants$getInputSlots();

    @Accessor("resultSlots")
    ResultContainer nolimitenchants$getResultSlots();
}
