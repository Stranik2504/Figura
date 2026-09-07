package org.figuramc.figura.ducks;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.apache.commons.lang3.function.TriFunction;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.model.rendering.nodeRenderer.FiguraSubmission;

import java.util.List;

@FunctionalInterface
interface QuadFunction<A, B, C, D, R> {
    R apply(A a, B b, C c, D d);
}

public interface NodeCollectorExtension {
    <S extends EntityRenderState> void submitFiguraModel(Avatar avatar, S renderState, TriFunction<Avatar, S, VertexConsumer, Void> renderer);

    List<FiguraSubmission> getFiguraSubmissions();
}

