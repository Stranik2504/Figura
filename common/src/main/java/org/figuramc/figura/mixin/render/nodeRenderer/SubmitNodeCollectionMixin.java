package org.figuramc.figura.mixin.render.nodeRenderer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.feature.ModelPartFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.apache.commons.lang3.function.TriFunction;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.ducks.FlameSubmitExtension;
import org.figuramc.figura.model.rendering.nodeRenderer.FiguraSubmission;
import org.figuramc.figura.ducks.FiguraSubmitCallBackExtension;
import org.figuramc.figura.ducks.NodeCollectorExtension;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(SubmitNodeCollection.class)
public class SubmitNodeCollectionMixin implements NodeCollectorExtension {
    @Unique
    List<FiguraSubmission> figuraSubmissions = new ArrayList<>();

    public <S extends EntityRenderState> void submitFiguraModel(Avatar avatar, S renderState, TriFunction<Avatar, S, VertexConsumer, Void> renderer) {
        figuraSubmissions.add(new FiguraSubmission(avatar, renderState, (TriFunction<Avatar, EntityRenderState, VertexConsumer, Void>) renderer));
    }

    @Override
    public List<FiguraSubmission> getFiguraSubmissions() {
        return figuraSubmissions;
    }

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

    // et.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase
    @WrapOperation(method = "submitFlame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/phase/SimpleFeatureRenderPhase;submit(Lnet/minecraft/client/renderer/feature/submit/SubmitNode;)V"))
    private void figura$onSubmitFlame(SimpleFeatureRenderPhase instance, SubmitNode submit, Operation<Void> original) {
        var flameSubmit = (FlameFeatureRenderer.Submit) submit;

        FlameSubmitExtension itemSubmissionExtension = (FlameSubmitExtension) (Object) flameSubmit;
        Avatar avatar = AvatarManager.getAvatar(flameSubmit.entityRenderState());
        itemSubmissionExtension.figura$setAvatar(avatar);

        original.call(instance, submit);
    }

    @Inject(method = "clear", at = @At("HEAD"))
    private void figura$clearSubmissions(CallbackInfo ci) {
        figuraSubmissions.clear();
    }
}
