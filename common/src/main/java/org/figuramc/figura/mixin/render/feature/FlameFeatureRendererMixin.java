package org.figuramc.figura.mixin.render.feature;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.feature.FeatureFrameContext;
import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.ducks.FlameSubmitExtension;
import org.figuramc.figura.utils.RenderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FlameFeatureRenderer.class)
public class FlameFeatureRendererMixin {
    @Unique
    Avatar figura$avatar = null;

    @ModifyVariable(method = "prepare", at = @At("STORE"), name = "fire1", argsOnly = true)
    private TextureAtlasSprite firstFireTexture(TextureAtlasSprite fire1) {
        TextureAtlasSprite s = RenderUtils.firstFireLayer(figura$avatar);
        return s != null ? s : fire1;
    }

    @ModifyVariable(method = "prepare", at = @At("STORE"), name = "fire2", argsOnly = true)
    private TextureAtlasSprite secondFireTexture(TextureAtlasSprite fire2) {
        TextureAtlasSprite s = RenderUtils.secondFireLayer(figura$avatar);
        figura$avatar = null;
        return s != null ? s : fire2;
    }

    @Inject(method = "buildGroup", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer;prepare(Lnet/minecraft/client/renderer/feature/FlameFeatureRenderer$Submit;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V"))
    private void figura$preRender(FeatureFrameContext context, List<FlameFeatureRenderer.Submit> submits, CallbackInfo ci, @Local(name = "submit") FlameFeatureRenderer.Submit submit) {
        figura$avatar = ((FlameSubmitExtension)(Object)submit).figura$getAvatar();
    }
}
