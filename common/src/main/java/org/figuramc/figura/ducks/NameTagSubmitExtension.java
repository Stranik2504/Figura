package org.figuramc.figura.ducks;

import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;

import java.util.List;

public interface NameTagSubmitExtension {
    boolean figura$hasOutline();
    void figura$setOutline(boolean outline, int color);
    int figura$getOutlineColor();
}
