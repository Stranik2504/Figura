package org.figuramc.figura.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.rendertype.RenderType;

@FunctionalInterface
public interface FiguraVertexConsumerProvider {
    VertexConsumer getBuffer(RenderType type);
}
