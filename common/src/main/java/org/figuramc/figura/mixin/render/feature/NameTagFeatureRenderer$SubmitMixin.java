package org.figuramc.figura.mixin.render.feature;

import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
import org.figuramc.figura.ducks.NameTagSubmitExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NameTagFeatureRenderer.Submit.class)
public class NameTagFeatureRenderer$SubmitMixin implements NameTagSubmitExtension {
    @Unique
    private boolean figura$outline = false;
    @Unique
    private int figura$outlineColor = 0x202020;

    @Override
    public boolean figura$hasOutline() {
        return figura$outline;
    }

    @Override
    public void figura$setOutline(boolean outline, int color) {
        this.figura$outline = outline;
        this.figura$outlineColor = color;
    }

    @Override
    public int figura$getOutlineColor() {
        return figura$outlineColor;
    }
}
