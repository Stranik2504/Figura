package org.figuramc.figura.model.rendering.nodeRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import org.joml.Matrix3x2fc;
import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;

public class ConstantColorVertexConsumer implements VertexConsumer {
    private final VertexConsumer delegate;
    private final int color;

    public ConstantColorVertexConsumer(VertexConsumer delegate, int color) {
        this.delegate = delegate;
        this.color = color;
    }
    @Override
    public @NonNull VertexConsumer addVertex(float x, float y, float z) {
        return delegate.addVertex(x, y, z);
    }

    @Override
    public @NonNull VertexConsumer setColor(int r, int g, int b, int a) {
        return delegate.setColor(
                (color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, (color >> 24) & 0xFF
        );
    }

    @Override
    public @NonNull VertexConsumer setColor(int color) {
        return delegate.setColor(
            (this.color >> 16) & 0xFF, (this.color >> 8) & 0xFF, this.color & 0xFF, (this.color >> 24) & 0xFF
        );
    }

    @Override
    public @NonNull VertexConsumer setUv(float u, float v) {
        return delegate.setUv(u, v);
    }

    @Override
    public @NonNull VertexConsumer setUv1(int u, int v) {
        return delegate.setUv1(u, v);
    }

    @Override
    public @NonNull VertexConsumer setUv2(int u, int v) {
        return delegate.setUv2(u, v);
    }

    @Override
    public @NonNull VertexConsumer setUv3(float u, float v) {
        return delegate.setUv3(u, v);
    }

    @Override
    public @NonNull VertexConsumer setNormal(float x, float y, float z) {
        return delegate.setNormal(x, y, z);
    }

    @Override
    public @NonNull VertexConsumer setLineWidth(float width) {
        return delegate.setLineWidth(width);
    }

    @Override
    public void addVertex(float x, float y, float z, int color, float u, float v, int overlayCoords, int lightCoords, float nx, float ny, float nz) {
        delegate.addVertex(x, y, z, color, u, v, overlayCoords, lightCoords, nx, ny, nz);
    }

    @Override
    public @NonNull VertexConsumer setColor(float r, float g, float b, float a) {
        return delegate.setColor(r, g, b, a);
    }

    @Override
    public @NonNull VertexConsumer setLight(int packedLightCoords) {
        return delegate.setLight(packedLightCoords);
    }

    @Override
    public @NonNull VertexConsumer setOverlay(int packedOverlayCoords) {
        return delegate.setOverlay(packedOverlayCoords);
    }

    @Override
    public void putBlockBakedQuad(float x, float y, float z, BakedQuad quad, QuadInstance instance) {
        delegate.putBlockBakedQuad(x, y, z, quad, instance);
    }

    @Override
    public void putBakedQuad(PoseStack.Pose pose, BakedQuad quad, QuadInstance instance) {
        delegate.putBakedQuad(pose, quad, instance);
    }

    @Override
    public void putBakedQuadWithGlint(PoseStack.Pose pose, BakedQuad quad, QuadInstance instance, PoseStack.Pose sheetedDecalPose) {
        delegate.putBakedQuadWithGlint(pose, quad, instance, sheetedDecalPose);
    }

    @Override
    public @NonNull VertexConsumer addVertex(Vector3fc position) {
        return delegate.addVertex(position);
    }

    @Override
    public @NonNull VertexConsumer addVertex(PoseStack.Pose pose, Vector3fc position) {
        return delegate.addVertex(pose, position);
    }

    @Override
    public @NonNull VertexConsumer addVertex(PoseStack.Pose pose, float x, float y, float z) {
        return delegate.addVertex(pose, x, y, z);
    }

    @Override
    public @NonNull VertexConsumer addVertex(Matrix4fc pose, float x, float y, float z) {
        return delegate.addVertex(pose, x, y, z);
    }

    @Override
    public @NonNull VertexConsumer addVertexWith2DPose(Matrix3x2fc pose, float x, float y) {
        return delegate.addVertexWith2DPose(pose, x, y);
    }

    @Override
    public @NonNull VertexConsumer setNormal(PoseStack.Pose pose, float x, float y, float z) {
        return delegate.setNormal(pose, x, y, z);
    }

    @Override
    public @NonNull VertexConsumer setNormal(PoseStack.Pose pose, Vector3fc normal) {
        return delegate.setNormal(pose, normal);
    }
}
