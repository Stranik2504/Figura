package org.figuramc.figura.model.rendering.nodeRenderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FeatureRendererType;
import net.minecraft.client.renderer.feature.RenderTypeFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import org.figuramc.figura.model.rendering.ImmediateFiguraRenderer;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FiguraFeatureRenderer extends RenderTypeFeatureRenderer<FiguraSubmission> {
    public static final FeatureRendererType<FiguraSubmission> TYPE = FiguraSubmission.TYPE;

    @Override
    protected void buildGroup(@NonNull FeatureFrameContext context, List<FiguraSubmission> submits) {
        for (FiguraSubmission submit : submits) {
            drawBuffered(submit.primaryBuffers());
            drawBuffered(submit.secondaryBuffers());

            for (FiguraSubmission.QueuedRenderTask qt : submit.renderTasks()) {
                qt.task().render(qt.poseStack(), this::getVertexBuilder, qt.light(), qt.overlay());
            }

            for (FiguraSubmission.QueuedPivotBox qb : submit.pivotBoxes()) {
                VertexConsumer vc = this.getVertexBuilder(RenderTypes.LINES);

                ImmediateFiguraRenderer.renderLineBox(qb.pose(), vc,
                        qb.x1(), qb.y1(), qb.z1(), qb.x2(), qb.y2(), qb.z2(),
                        qb.r(), qb.g(), qb.b(), qb.a());
            }
        }
    }

    private void drawBuffered(Map<RenderType, List<Consumer<VertexConsumer>>> buffers) {
        for (Map.Entry<RenderType, List<Consumer<VertexConsumer>>> entry : buffers.entrySet()) {
            VertexConsumer vc = this.getVertexBuilder(entry.getKey());

            for (Consumer<VertexConsumer> action : entry.getValue())
                action.accept(vc);
        }
    }
}
