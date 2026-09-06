package org.figuramc.figura.mixin.compat;

// TODO: uncomment on version != 26.1.1
/*
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.entity.EquipmentSlot;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.ducks.GeckolibGeoArmorAccessor;
import org.figuramc.figura.lua.api.vanilla_model.VanillaPart;
import org.figuramc.figura.model.ParentType;
import org.figuramc.figura.permissions.Permissions;
import org.figuramc.figura.utils.RenderUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.RenderPassInfo;

import java.util.Optional;

 @Pseudo
@Mixin(value = GeoRenderer.class, remap = false)
public interface GeckolibGeoRendererMixin<R extends GeoRenderState> {
    @Inject(method = "submitRenderTasks", at = @At("HEAD"), cancellable = true)
    default void figura$onSubmitRenderTasks(RenderPassInfo<@NotNull R> renderPassInfo, OrderedSubmitNodeCollector renderTasks, RenderType renderType, CallbackInfo ci) {
        if (!(this instanceof GeoArmorRenderer<?, ?> armorRenderer)) return;

        var avatar = ((GeckolibGeoArmorAccessor) armorRenderer).figura$getAvatar();

        if (avatar == null) return;

        figura$renderPivots(renderPassInfo, renderTasks, renderType, ci);
    }

    @Unique
    default void figura$renderPivots(RenderPassInfo<@NotNull R> renderPassInfo, OrderedSubmitNodeCollector renderTasks, RenderType renderType, CallbackInfo ci) {
        var armorRenderer = (GeoArmorRenderer) this;
        Avatar avatar = ((GeckolibGeoArmorAccessor) armorRenderer).figura$getAvatar();

        if (avatar.permissions.get(Permissions.VANILLA_MODEL_EDIT) < 1) return;

        R renderState = renderPassInfo.renderState();
        EquipmentSlot slot = renderState.getGeckolibData(software.bernie.geckolib.constant.DataTickets.EQUIPMENT_SLOT);
        if (slot == null) return;

        switch (slot) {
            case HEAD:
                figura$processSegment(renderPassInfo, renderTasks, armorRenderer, avatar, ParentType.HelmetPivot, GeoArmorRenderer.ArmorSegment.HEAD, renderType);
                break;
            case CHEST:
                figura$processSegment(renderPassInfo, renderTasks, armorRenderer, avatar, ParentType.ChestplatePivot, GeoArmorRenderer.ArmorSegment.CHEST, renderType);
                figura$processSegment(renderPassInfo, renderTasks, armorRenderer, avatar, ParentType.LeftShoulderPivot, GeoArmorRenderer.ArmorSegment.LEFT_ARM, renderType);
                figura$processSegment(renderPassInfo, renderTasks, armorRenderer, avatar, ParentType.RightShoulderPivot, GeoArmorRenderer.ArmorSegment.RIGHT_ARM, renderType);
                break;
            case LEGS:
                figura$processSegment(renderPassInfo, renderTasks, armorRenderer, avatar, ParentType.LeftLeggingPivot, GeoArmorRenderer.ArmorSegment.LEFT_LEG, renderType);
                figura$processSegment(renderPassInfo, renderTasks, armorRenderer, avatar, ParentType.RightLeggingPivot, GeoArmorRenderer.ArmorSegment.RIGHT_LEG, renderType);
                break;
            case FEET:
                figura$processSegment(renderPassInfo, renderTasks, armorRenderer, avatar, ParentType.LeftBootPivot, GeoArmorRenderer.ArmorSegment.LEFT_FOOT, renderType);
                figura$processSegment(renderPassInfo, renderTasks, armorRenderer, avatar, ParentType.RightBootPivot, GeoArmorRenderer.ArmorSegment.RIGHT_FOOT, renderType);
                break;
        }
        ci.cancel();
    }

    @Unique
    default void figura$processSegment(RenderPassInfo<@NotNull R> renderPassInfo, OrderedSubmitNodeCollector renderTasks, GeoArmorRenderer<?,?> armorRenderer, Avatar avatar, ParentType parentType, GeoArmorRenderer.ArmorSegment segment, RenderType renderType) {
        String boneName = ((GeoArmorRenderer) armorRenderer).getBoneNameForSegment((HumanoidRenderState) renderPassInfo.renderState(), segment);
        Optional<GeoBone> boneOpt = renderPassInfo.model().getBone(boneName);

        if (boneOpt.isEmpty()) return;
        GeoBone geoBone = boneOpt.get();

        boolean failed = figura$renderPivot(renderPassInfo, renderTasks, avatar, parentType, geoBone, renderType);
        if (failed) {
            figura$submitStandardBone(renderPassInfo, renderTasks, geoBone, renderType);
        }
    }

    @Unique
    default boolean figura$renderPivot(RenderPassInfo<@NotNull R> renderPassInfo, OrderedSubmitNodeCollector renderTasks, Avatar avatar, ParentType parentType, GeoBone geoBone, RenderType renderType) {
        if (renderType == null) return true;

        int armorEditPermission = avatar.permissions.get(Permissions.VANILLA_MODEL_EDIT);
        VanillaPart part = RenderUtils.pivotToPart(avatar, parentType);
        if (armorEditPermission == 1 && part != null && !part.checkVisible()) return false;
        if (armorEditPermission != 1) return true;

        return !avatar.pivotPartRender(parentType, stack -> {
            stack.pushPose();
            figura$prepareArmorRender(stack);
            figura$transformBasedOnType(stack, parentType);

            stack.pushPose();

            renderTasks.submitCustomGeometry(stack, renderType, (pose, vertexConsumer) -> {
                PoseStack renderStack = renderPassInfo.poseStack();
                renderStack.pushPose();
                renderStack.last().set(pose);

                geoBone.render(renderPassInfo, renderStack, vertexConsumer, renderPassInfo.packedLight(), renderPassInfo.packedOverlay(), renderPassInfo.renderColor());

                renderStack.popPose();
            });

            stack.popPose();
            stack.popPose();
        });
    }

    @Unique
    default void figura$submitStandardBone(RenderPassInfo<@NotNull R> renderPassInfo, OrderedSubmitNodeCollector renderTasks, GeoBone geoBone, RenderType renderType) {
        if (renderType == null) return;
        renderTasks.submitCustomGeometry(renderPassInfo.poseStack(), renderType, (pose, vertexConsumer) -> {
            PoseStack renderStack = renderPassInfo.poseStack();
            renderStack.pushPose();
            renderStack.last().set(pose);
            geoBone.render(renderPassInfo, renderStack, vertexConsumer, renderPassInfo.packedLight(), renderPassInfo.packedOverlay(), renderPassInfo.renderColor());
            renderStack.popPose();
        });
    }

    @Unique
    default void figura$transformBasedOnType(PoseStack poseStack, ParentType parentType) {
        if (parentType == ParentType.LeftShoulderPivot || parentType == ParentType.RightShoulderPivot) {
            poseStack.translate(parentType == ParentType.LeftShoulderPivot ? -6 / 16f : 6 / 16f, 0f, 0f);
        } else if (parentType == ParentType.LeggingsPivot) {
            poseStack.translate(0, -12 / 16f, 0);
        } else if (parentType == ParentType.LeftLeggingPivot || parentType == ParentType.RightLeggingPivot) {
            poseStack.translate(parentType == ParentType.LeftLeggingPivot ? -2 / 16f : 2 / 16f, -12 / 16f, 0);
        } else if (parentType == ParentType.LeftBootPivot || parentType == ParentType.RightBootPivot) {
            poseStack.translate(parentType == ParentType.LeftBootPivot ? -2 / 16f : 2 / 16f, -24 / 16f, 0);
        }
    }

    @Unique
    default void figura$prepareArmorRender(PoseStack stack) {
        stack.scale(16, 16, 16);
        stack.mulPose(Axis.XP.rotationDegrees(180f));
        stack.mulPose(Axis.YP.rotationDegrees(180f));
    }
}
*/
