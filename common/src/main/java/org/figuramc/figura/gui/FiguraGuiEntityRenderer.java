package org.figuramc.figura.gui;

import net.minecraft.client.gui.render.pip.GuiEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import org.jspecify.annotations.NonNull;

public class FiguraGuiEntityRenderer extends GuiEntityRenderer {

    public FiguraGuiEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher);
    }

    @Override
    protected @NonNull String getTextureLabel() {
        return "figura-entity";
    }
}
