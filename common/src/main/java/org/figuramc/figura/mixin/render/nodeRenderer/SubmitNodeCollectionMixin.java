package org.figuramc.figura.mixin.render.nodeRenderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.client.renderer.SubmitNodeCollection;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.ducks.FlameSubmitExtension;
import org.figuramc.figura.ducks.FiguraSubmitCallBackExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SubmitNodeCollection.class)
public class SubmitNodeCollectionMixin {
    @WrapOperation(method = "submitModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/phase/SimpleFeatureRenderPhase;submit(Lnet/minecraft/client/renderer/feature/submit/SubmitNode;)V"))
    private <S> void figura$onSubmitModel(SimpleFeatureRenderPhase instance, SubmitNode submit, Operation<Void> original, @Local(argsOnly = true, name = "model") Model<? super S> model) {
        FiguraSubmitCallBackExtension modelSubmissionExtension = (FiguraSubmitCallBackExtension) submit;
        FiguraSubmitCallBackExtension modelExtension = (FiguraSubmitCallBackExtension) model;

        for (var callback : modelExtension.figura$getPreRenderingCallbacks()) {
            modelSubmissionExtension.figura$addPreRenderingCallback(callback);
        }

        for (var callback : modelExtension.figura$getPostRenderingCallbacks()) {
            modelSubmissionExtension.figura$addPostRenderingCallback(callback);
        }

        modelSubmissionExtension.figura$setPreventAnimSetup(modelExtension.figura$getPreventAnimSetup());
        modelExtension.figura$setPreventAnimSetup(false);

        modelExtension.figura$getPreRenderingCallbacks().clear();
        modelExtension.figura$getPostRenderingCallbacks().clear();
    }

    @WrapOperation(method = "submitItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/ItemFeatureRenderer$Submit;hasTranslucency()Z", ordinal = 0))
    private <E> boolean figura$onSubmitModelPart(ItemFeatureRenderer.Submit instance, Operation<Boolean> original) {
        FiguraSubmitCallBackExtension itemSubmissionExtension = (FiguraSubmitCallBackExtension) (Object) instance;
        FiguraSubmitCallBackExtension displayContextExtension = (FiguraSubmitCallBackExtension) (Object) instance.displayContext();

        for (var callback : displayContextExtension.figura$getPreRenderingCallbacks()) {
            itemSubmissionExtension.figura$addPreRenderingCallback(callback);
        }

        for (var callback : displayContextExtension.figura$getPostRenderingCallbacks()) {
            itemSubmissionExtension.figura$addPostRenderingCallback(callback);
        }
        itemSubmissionExtension.figura$setPreventAnimSetup(displayContextExtension.figura$getPreventAnimSetup());
        displayContextExtension.figura$setPreventAnimSetup(false);

        displayContextExtension.figura$getPreRenderingCallbacks().clear();
        displayContextExtension.figura$getPostRenderingCallbacks().clear();

        return original.call(instance);
    }

    @WrapOperation(method = "submitFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/phase/SimpleFeatureRenderPhase;submit(Lnet/minecraft/client/renderer/feature/submit/SubmitNode;)V"))
    private void figura$onSubmitFlame(SimpleFeatureRenderPhase instance, SubmitNode submit, Operation<Void> original) {
        var flameSubmit = (FlameFeatureRenderer.Submit) submit;

        FlameSubmitExtension itemSubmissionExtension = (FlameSubmitExtension) (Object) flameSubmit;
        Avatar avatar = AvatarManager.getAvatar(flameSubmit.entityRenderState());
        itemSubmissionExtension.figura$setAvatar(avatar);

        original.call(instance, submit);
    }
}
