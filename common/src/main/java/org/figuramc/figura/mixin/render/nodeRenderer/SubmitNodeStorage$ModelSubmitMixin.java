package org.figuramc.figura.mixin.render.nodeRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import org.figuramc.figura.ducks.FiguraSubmitCallBackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Mixin(ModelFeatureRenderer.Submit.class)
public class SubmitNodeStorage$ModelSubmitMixin implements FiguraSubmitCallBackExtension {
    @Unique
    private final List<BiFunction<VertexConsumer, PoseStack, Boolean>> figura$preRenderingCallback = new ArrayList<>();
    @Unique
    private final List<Runnable> figura$postRenderingCallback = new ArrayList<>();
    @Unique
    private boolean figura$preventAnimSetup = false;

    @Override
    public void figura$addPreRenderingCallback(BiFunction<VertexConsumer, PoseStack, Boolean> callback) {
        this.figura$preRenderingCallback.add(callback);
    }

    @Override
    public void figura$addPostRenderingCallback(Runnable callback) {
        this.figura$postRenderingCallback.add(callback);
    }

    @Override
    public List<BiFunction<VertexConsumer, PoseStack, Boolean>> figura$getPreRenderingCallbacks() {
        return figura$preRenderingCallback;
    }

    @Override
    public List<Runnable> figura$getPostRenderingCallbacks() {
        return figura$postRenderingCallback;
    }

    @Override
    public boolean figura$getPreventAnimSetup() {
        return figura$preventAnimSetup;
    }

    @Override
    public void figura$setPreventAnimSetup(boolean prevent) {
        this.figura$preventAnimSetup = prevent;
    }
}
