package org.figuramc.figura.mixin.render.feature;

import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import net.minecraft.client.renderer.feature.FeatureRendererMap;
import org.figuramc.figura.model.rendering.nodeRenderer.FiguraFeatureRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FeatureRenderDispatcher.class)
public class FeatureRendererDispatcherMixin {
    @Final
    @Shadow
    private FeatureRendererMap featureRenderers;
    @Unique
    final FiguraFeatureRenderer noFigura$figuraFeatureRenderer = new FiguraFeatureRenderer();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void figura$renderFiguraFeatures(CallbackInfo ci) {
        featureRenderers.put(FiguraFeatureRenderer.TYPE, noFigura$figuraFeatureRenderer);
    }
}
