package org.figuramc.figura.mixin.render.feature;

import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
import org.figuramc.figura.ducks.NameTagSubmitExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(NameTagFeatureRenderer.Submit.class)
public class NameTagFeatureRendererMixin implements NameTagSubmitExtension {
    /*@Inject(method = "renderTranslucent", at = @At(value = "TAIL"))
    private void renderOutlineTexts(CallbackInfo ci, @Local(argsOnly = true) MultiBufferSource.BufferSource bufferSource,
                                    @Local(argsOnly = true) Font font, @Local NameTagFeatureRenderer.Storage storage) {
        NameTagFeatureRenderer$StorageExtension storageExt = (NameTagFeatureRenderer$StorageExtension) storage;
        for (SubmitNodeStorage.NameTagSubmit outlineText : storageExt.getOutlineSubmits()) {
            font.drawInBatch8xOutline(outlineText.text().getVisualOrderText(), outlineText.x(), outlineText.y(), outlineText.color(),
                    outlineText.backgroundColor(), outlineText.pose(), bufferSource, outlineText.lightCoords());

            EntityRenderer.NAMETAG_SCALE
        }
    }*/

    @Unique
    private boolean figura$outline = false;
    @Unique
    private int figura$outlineColor = 0x202020;

    @Override
    public boolean figura$hasOutline() {
        return figura$outline;
    }

    @Override
    public void figura$setOutline(boolean outline, int color) {
        this.figura$outline = outline;
        this.figura$outlineColor = color;
    }

    @Override
    public int figura$getOutlineColor() {
        return figura$outlineColor;
    }

}
