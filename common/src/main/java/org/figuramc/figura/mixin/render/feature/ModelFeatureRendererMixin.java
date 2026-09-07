package org.figuramc.figura.mixin.render.feature;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import org.figuramc.figura.ducks.FiguraSubmitCallBackExtension;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// this method runs callbacks for Models before and after rendering, as well as before animation setup if they exist
@Mixin(ModelFeatureRenderer.class)
public class ModelFeatureRendererMixin {
    @Shadow
    @Final
    private PoseStack poseStack;

    @Inject(
        method = "prepareModel",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;setupAnim(Ljava/lang/Object;)V"),
        cancellable = true
    )
    private <S> void figura$beforeSetupAnim(ModelFeatureRenderer.Submit<S> submit, CallbackInfo ci) {
        FiguraSubmitCallBackExtension callBackExtension = (FiguraSubmitCallBackExtension) (Object) submit;
        if (callBackExtension.figura$getPreventAnimSetup()) {
            ci.cancel();
        }
    }


    @Inject(method = "prepareModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"), cancellable = true)
    private <S> void figura$preRender(ModelFeatureRenderer.Submit<S> submit, CallbackInfo ci, @Local(name = "buffer") VertexConsumer buffer) {
        FiguraSubmitCallBackExtension callBackExtension = (FiguraSubmitCallBackExtension) (Object) submit;

        assert callBackExtension != null;

        for (var callback : callBackExtension.figura$getPreRenderingCallbacks()) {
            if (!callback.apply(buffer, poseStack)) {
                ci.cancel();
            }
        }

        if (ci.isCancelled()) {
            for (var callback : callBackExtension.figura$getPostRenderingCallbacks())
                callback.run();

            callBackExtension.figura$getPostRenderingCallbacks().clear();
        }

        callBackExtension.figura$getPreRenderingCallbacks().clear();
    }

    @Inject(
            method = "prepareModel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V", shift = At.Shift.AFTER)
    )
    private <S> void figura$postRender(ModelFeatureRenderer.Submit<S> submit, CallbackInfo ci) {
        FiguraSubmitCallBackExtension callBackExtension = (FiguraSubmitCallBackExtension) (Object) submit;

        for (var callback : callBackExtension.figura$getPostRenderingCallbacks())
            callback.run();

        callBackExtension.figura$getPostRenderingCallbacks().clear();
    }
}
