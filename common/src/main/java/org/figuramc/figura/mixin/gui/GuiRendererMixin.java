package org.figuramc.figura.mixin.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.client.renderer.state.gui.pip.GuiEntityRenderState;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
import org.figuramc.figura.ducks.GuiEntityRenderStateExtension;
import org.figuramc.figura.gui.FiguraGuiEntityRenderer;
import org.figuramc.figura.model.rendering.EntityRenderMode;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiRenderer.class)
public class GuiRendererMixin {
    @Shadow @Final
    private GuiRenderState renderState;

    @Shadow @Final
    private FeatureRenderDispatcher featureRenderDispatcher;

    @Unique
    FiguraGuiEntityRenderer figura$paperDollRenderer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initializePaperDollRenderer(GuiRenderState renderState, FeatureRenderDispatcher featureRenderDispatcher, List pictureInPictureRenderers, CallbackInfo ci) {
        figura$paperDollRenderer = new FiguraGuiEntityRenderer(Minecraft.getInstance().getEntityRenderDispatcher());
    }

    @Inject(method = "preparePictureInPictureState", at = @At("HEAD"), cancellable = true)
    private <T extends PictureInPictureRenderState> void renderPaperDoll(T pictureInPictureRenderState, int i, CallbackInfo ci) {
        if (pictureInPictureRenderState instanceof GuiEntityRenderStateExtension extension && (extension.getRenderMode() != null && extension.getRenderMode() == EntityRenderMode.PAPERDOLL)) {
            figura$paperDollRenderer.prepare((GuiEntityRenderState) pictureInPictureRenderState, this.renderState, featureRenderDispatcher, i);
            ci.cancel();
        }
    }


    @Inject(method = "close", at = @At(value = "INVOKE", target = "Ljava/util/Collection;forEach(Ljava/util/function/Consumer;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void onClose(CallbackInfo ci) {
        figura$paperDollRenderer.close();
    }
}
