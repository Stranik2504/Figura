package org.figuramc.figura.mixin.gui;

import net.minecraft.client.gui.components.debug.DebugScreenEntries;
import net.minecraft.client.gui.components.debug.DebugScreenEntry;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DebugScreenEntries.class)
public interface DebugScreenEntriesAccessor {
    @Invoker("register")
    static Identifier figura$invokeRegister(Identifier resourceLocation, DebugScreenEntry debugScreenEntry) {
        throw new AssertionError();
    }
}
