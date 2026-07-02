package org.figuramc.figura.mixin.compat;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.ducks.GeckolibGeoArmorAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

@Pseudo
@Mixin(value = GeoArmorRenderer.class, remap = false)
public abstract class GeckolibGeoArmorRendererMixin<T extends Item & GeoItem> implements GeckolibGeoArmorAccessor {
    @Unique
    private Avatar figura$avatar;

    @Inject(method = "captureDefaultRenderState*", at = @At(value = "HEAD"))
    private <R extends HumanoidRenderState & GeoRenderState> void figura$prepAvatar(T animatable, GeoArmorRenderer.RenderData renderData, R renderState, float partialTick, CallbackInfo ci){
        Entity entity = renderData.entity();
        figura$avatar = AvatarManager.getAvatar(entity);
    }

    @Override
    @Unique
    public Avatar figura$getAvatar() {
        return figura$avatar;
    }
}