package org.figuramc.figura.mixin.render.feature;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
import net.minecraft.client.renderer.feature.phase.TranslucentFeatureRenderPhase;
import net.minecraft.client.renderer.feature.submit.SubmitNode;
import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.ducks.NameTagSubmitExtension;
import org.figuramc.figura.lua.api.nameplate.EntityNameplateCustomization;
import org.figuramc.figura.utils.TextUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(SubmitNodeCollection.class)
public class NameTagFeatureRenderer$StorageMixin {

    // i literally have to inject a new submission list for outlined text, *screams into void*
    /*@Unique
    final List<NameTagFeatureRenderer.Submit> figura$outlineSubmits = new ArrayList<>();

    @Unique
    Avatar figura$avatar;
    @Unique
    EntityNameplateCustomization figura$custom;
    @Unique
    List<Component> figura$textList;

    @Unique
    boolean figura$hasCustomNameplate;
    @Unique
    boolean figura$enabled;

    @Unique
    boolean figura$isRenderingName;*/

    /*@Inject(at = @At(value = "HEAD"), method = "add")
    private void setupAvatar(PoseStack poseStack, Vec3 vec3, int i, Component component, boolean bl, int j, double d, CameraRenderState cameraRenderState, CallbackInfo ci) {

        figura$avatar = ((CameraRenderStateExtension)cameraRenderState).figura$getAvatar();
        figura$isRenderingName = ((CameraRenderStateExtension)cameraRenderState).figura$isRenderingNameTag();
        ((CameraRenderStateExtension)cameraRenderState).figura$setAvatar(null);
        ((CameraRenderStateExtension)cameraRenderState).figura$setRenderingNameTag(false);

        if (figura$avatar == null)
            return;

        figura$custom = figura$avatar == null || figura$avatar.luaRuntime == null ? null : figura$avatar.luaRuntime.nameplate.ENTITY;
        figura$hasCustomNameplate = figura$custom != null && figura$avatar.permissions.get(Permissions.NAMEPLATE_EDIT) == 1;
        figura$enabled =  Configs.ENTITY_NAMEPLATE.value > 0 && !AvatarManager.panic && figura$hasCustomNameplate;

        figura$textList = TextUtils.splitText(component, "\n");
    }*/

    /*@Inject(method = "submitNameTag", at = @At("HEAD"))
    private void figura$setupAvatar(PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, CameraRenderState camera, CallbackInfo ci) {
        figura$avatar = ((CameraRenderStateExtension) camera).figura$getAvatar();
        ((CameraRenderStateExtension) camera).figura$setAvatar(null);

        if (figura$avatar == null) {
            figura$custom = null;
            figura$hasCustomNameplate = false;
            figura$enabled = false;
            return;
        }

        figura$custom = figura$avatar.luaRuntime == null ? null : figura$avatar.luaRuntime.nameplate.ENTITY;
        figura$hasCustomNameplate = figura$custom != null && figura$avatar.permissions.get(Permissions.NAMEPLATE_EDIT) == 1;
        figura$enabled = Configs.ENTITY_NAMEPLATE.value > 0 && !AvatarManager.panic && figura$hasCustomNameplate;

        figura$textList = TextUtils.splitText(name, "\n");
    }*/

    /*@Inject(at = @At(value = "TAIL"), method = "add")
    private void clearAvatar(PoseStack poseStack, Vec3 vec3, int i, Component component, boolean bl, int j, double d, CameraRenderState cameraRenderState, CallbackInfo ci) {
        figura$avatar = null;
        figura$isRenderingName = false;
        figura$custom = null;
        figura$hasCustomNameplate = false;
        figura$enabled =  false;
        figura$textList = null;
    }*/

    /*@Inject(method = "submitNameTag", at = @At("TAIL"))
    private void figura$clearAvatar(CallbackInfo ci) {
        figura$avatar = null;
        figura$custom = null;
        figura$hasCustomNameplate = false;
        figura$enabled = false;
    }*/

    // Push pivot transformations when the nametag is being pivoted (set to entity height in vanilla)
    /*@WrapOperation(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"), method = "add")
    private void modifyPivot(PoseStack instance, double x, double y, double z, Operation<Void> original) {
        FiguraVec3 pivot = FiguraVec3.of(x, y, z);
        if (figura$enabled && figura$avatar != null) {
            // pivot
            FiguraMod.pushProfiler("pivot");
            if (figura$hasCustomNameplate && figura$custom.getPivot() != null)
                pivot = figura$custom.getPivot();
        }
        original.call(instance, pivot.x, pivot.y, pivot.z);
    }*/

    /*@WrapOperation(method = "submitNameTag",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V"))
    private void figura$modifyPivot(PoseStack instance, double x, double y, double z, Operation<Void> original) {
        FiguraVec3 pivot = FiguraVec3.of(x, y, z);

        if (figura$enabled && figura$hasCustomNameplate && figura$custom.getPivot() != null) {
            FiguraMod.pushProfiler("pivot");
            pivot = figura$custom.getPivot();
        }

        original.call(instance, pivot.x, pivot.y, pivot.z);
    }*/


    // Push position transformations after the nametag has been rotated to face the camera
    /*@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", shift = At.Shift.AFTER), method = "add")
    private void modifyPos(PoseStack matrices, Vec3 vec3, int i, Component component, boolean bl, int j, double d, CameraRenderState cameraRenderState, CallbackInfo ci) {
        if (figura$enabled && figura$avatar != null) {
            // pos
            FiguraMod.popPushProfiler("position");
            if (figura$hasCustomNameplate && figura$custom.getPos() != null) {
                FiguraVec3 pos = figura$custom.getPos();
                matrices.translate(pos.x, pos.y, pos.z);
            }
        }
    }*/
    /*@Inject(method = "submitNameTag",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", shift = At.Shift.AFTER))
    private void figura$modifyPos(PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, CameraRenderState camera, CallbackInfo ci) {
        if (figura$enabled && figura$hasCustomNameplate && figura$custom.getPos() != null) {
            FiguraMod.popPushProfiler("position");
            FiguraVec3 pos = figura$custom.getPos();
            poseStack.translate(pos.x, pos.y, pos.z);
        }
    }*/

    // push the scale when vanilla does so
    /*@WrapOperation(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"), method = "add")
    private void modifyScale(PoseStack instance, float x, float y, float z, Operation<Void> original) {
        FiguraVec3 scaleVec = FiguraVec3.of(x, y, z);
        if (figura$enabled && figura$avatar != null) {
            // scale
            FiguraMod.popPushProfiler("scale");
            if (figura$hasCustomNameplate && figura$custom.getScale() != null)
                scaleVec.multiply(figura$custom.getScale());
        }
        original.call(instance, (float) scaleVec.x, (float) scaleVec.y, (float) scaleVec.z);
    }*/
    /*@WrapOperation(method = "submitNameTag",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V"))
    private void figura$modifyScale(PoseStack instance, float x, float y, float z, Operation<Void> original) {
        FiguraVec3 scaleVec = FiguraVec3.of(x, y, z);

        if (figura$enabled && figura$hasCustomNameplate && figura$custom.getScale() != null) {
            FiguraMod.popPushProfiler("scale");
            scaleVec.multiply(figura$custom.getScale());
        }

        original.call(instance, (float) scaleVec.x, (float) scaleVec.y, (float) scaleVec.z);
    }*/



    /*@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack$Pose;pose()Lorg/joml/Matrix4f;"), method = "add")
    private void setShadowMatrix(PoseStack matrices, Vec3 vec3, int i, Component component, boolean bl, int j, double d, CameraRenderState cameraRenderState, CallbackInfo ci, @Share("textMatrix") LocalRef<Matrix4f> textMatrix) {
        if (!figura$enabled || figura$avatar == null || !figura$hasCustomNameplate || !figura$custom.shadow)
            return;

        textMatrix.set(matrices.last().pose());
        if (figura$enabled && figura$avatar != null && figura$hasCustomNameplate && figura$custom.shadow) {
            matrices.pushPose();
            textMatrix.set(matrices.last().pose());
            matrices.popPose();
        }
    }*/
    /*@Inject(method = "submitNameTag",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack$Pose;pose()Lorg/joml/Matrix4f;"))
    private void setShadowMatrix(PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, CameraRenderState camera, CallbackInfo ci, @Share("textMatrix") LocalRef<Matrix4f> textMatrix) {
        if (!figura$enabled || figura$avatar == null || !figura$hasCustomNameplate || !figura$custom.shadow)
            return;

        textMatrix.set(poseStack.last().pose());

        if (figura$enabled && figura$avatar != null && figura$hasCustomNameplate && figura$custom.shadow) {
            poseStack.pushPose();
            textMatrix.set(poseStack.last().pose());
            poseStack.popPose();
        }
    }*/


    /*@WrapOperation(method = "add",
            at = @At(value = "INVOKE", target =
                    "Ljava/util/List;add(Ljava/lang/Object;)Z"
            , ordinal = 1))
    private <E> boolean drawWithColor(List<E> instance, E e, Operation<Boolean> original) {
        SubmitNodeStorage.NameTagSubmit submit = (SubmitNodeStorage.NameTagSubmit) e;

        Font font = Minecraft.getInstance().font;

        if (figura$enabled && figura$avatar != null && figura$hasCustomNameplate) {
            int light = figura$custom.light != null ? figura$custom.light : submit.lightCoords();
            int backgroundColor = figura$custom.background != null ? figura$custom.background : submit.backgroundColor();
            boolean deadmau = submit.text().getString().equals("deadmau5");

            // This renders the translucent part of the nametag you see when shifting, and the background
            if (figura$isRenderingName) {
                // If the player's name is being rendered, render by lines otherwise just render whatever component is being passed. Applies for the rest of the loops below
                for (int i = 0; i < figura$textList.size(); i++) {
                    Component text1 = figura$textList.get(i);

                    if (text1.getString().isEmpty())
                        continue;

                    int line = i - figura$textList.size() + 1;
                    float x = -font.width(text1) / 2f;
                    float y =  (deadmau ? -10f : 0f) + (font.lineHeight + 1) * line;

                    original.call(instance, new SubmitNodeStorage.NameTagSubmit(submit.pose(), x, y, text1, light, submit.color(), backgroundColor, submit.distanceToCameraSq()));
                }
                return true;
            }  else {
                return original.call(instance, new SubmitNodeStorage.NameTagSubmit(submit.pose(), submit.x(), submit.y(), submit.text(), light, submit.color(), backgroundColor, submit.distanceToCameraSq()));
            }
        }
        return original.call(instance, e);
    }*/
    /*@WrapOperation(method = "submitNameTag",
            at = @At(value = "INVOKE", target =
                    "Lnet/minecraft/client/renderer/feature/phase/TranslucentFeatureRenderPhase;submit(Lnet/minecraft/client/renderer/feature/submit/TranslucentSubmit;)V"))
    private void drawWithColor(TranslucentFeatureRenderPhase instance, TranslucentSubmit s, Operation<Void> original) {
        var submit = (NameTagFeatureRenderer.Submit) s;

        Font font = Minecraft.getInstance().font;

        if (figura$enabled && figura$avatar != null && figura$hasCustomNameplate) {
            int light = figura$custom.light != null ? figura$custom.light : submit.lightCoords();
            int backgroundColor = figura$custom.background != null ? figura$custom.background : submit.backgroundColor();
            boolean deadmau = submit.text().getString().equals("deadmau5");

            // This renders the translucent part of the nametag you see when shifting, and the background
            if (figura$isRenderingName) {
                // If the player's name is being rendered, render by lines otherwise just render whatever component is being passed. Applies for the rest of the loops below
                for (int i = 0; i < figura$textList.size(); i++) {
                    Component text1 = figura$textList.get(i);

                    if (text1.getString().isEmpty())
                        continue;

                    int line = i - figura$textList.size() + 1;
                    float x = -font.width(text1) / 2f;
                    float y =  (deadmau ? -10f : 0f) + (font.lineHeight + 1) * line;

                    original.call(instance, new NameTagFeatureRenderer.Submit(submit.pose(), x, y, text1, light, submit.color(), backgroundColor, Font.DisplayMode.NORMAL));
                }

                return;
            }  else {
                original.call(instance, new NameTagFeatureRenderer.Submit(submit.pose(), submit.x(), submit.y(), submit.text(), light, submit.color(), backgroundColor, Font.DisplayMode.NORMAL));
                return;
            }
        }

        original.call(instance, s);
    }*/


    /*@WrapOperation(method = "add",
            at = @At(value = "INVOKE", target =
                    "Ljava/util/List;add(Ljava/lang/Object;)Z"
                    , ordinal = 0))
    private <E> boolean drawWithOutline(List<E> instance, E e, Operation<Boolean> original, @Share("textMatrix") LocalRef<Matrix4f> textMatrix) {
        SubmitNodeStorage.NameTagSubmit submit = (SubmitNodeStorage.NameTagSubmit) e;

        Font font = Minecraft.getInstance().font;

        Matrix4f pose = new Matrix4f(submit.pose());
        int color = submit.color();
        boolean deadmau = submit.text().getString().equals("deadmau5");
        Matrix4f shadowMatrix = textMatrix.get() != null ? textMatrix.get() : pose;
        if (figura$enabled && figura$avatar != null && figura$hasCustomNameplate && figura$custom.outline) {
            // This renders the opaque text with an outline if the player has that enabled.
            int outlineColor = figura$custom.outlineColor != null ? figura$custom.outlineColor : 0x202020;

            if (figura$isRenderingName) {
                for (int i = 0; i < figura$textList.size(); i++) {
                    Component text1 = figura$textList.get(i);

                    if (text1.getString().isEmpty())
                        continue;

                    int line = i - figura$textList.size() + 1;
                    float x = -font.width(text1) / 2f;
                    float y = (deadmau ? -10f : 0f) + (font.lineHeight + 1) * line;
                    // yes i am using the bg color field to store the outline color, sue me
                    figura$outlineSubmits.add(new SubmitNodeStorage.NameTagSubmit(pose, x, y, text1,  submit.lightCoords(), color, outlineColor, submit.distanceToCameraSq()));
                }
            } else {
                figura$outlineSubmits.add(new SubmitNodeStorage.NameTagSubmit(pose, submit.x(), submit.y(), submit.text(),  submit.lightCoords(), color, outlineColor, submit.distanceToCameraSq()));
            }
            return original.call(instance, new SubmitNodeStorage.NameTagSubmit(shadowMatrix, submit.x(), submit.y(), Component.empty(),  submit.lightCoords(), color, submit.backgroundColor(), submit.distanceToCameraSq()));
        } else {
            if (figura$enabled && figura$avatar != null && figura$hasCustomNameplate && figura$isRenderingName) {
                // This renders the opaque part of the nametag, that is text
                for (int i = 0; i < figura$textList.size(); i++) {
                    Component text1 = figura$textList.get(i);

                    if (text1.getString().isEmpty())
                        continue;

                    int line = i - figura$textList.size() + 1;
                    float x = -font.width(text1) / 2f;
                    float y = (deadmau ? -10f : 0f) + (font.lineHeight + 1) * line;
                    original.call(instance, new SubmitNodeStorage.NameTagSubmit(shadowMatrix, x, y, text1, submit.lightCoords(), color, submit.backgroundColor(), submit.distanceToCameraSq()));
                }
                return true;
            } else {
                return original.call(instance, new SubmitNodeStorage.NameTagSubmit(shadowMatrix, submit.x(), submit.y(), submit.text(),  submit.lightCoords(), color, submit.backgroundColor(), submit.distanceToCameraSq()));
            }
        }
    }*/
    /*@WrapOperation(method = "submitNameTag",
            at = @At(value = "INVOKE", target =
                    "Lnet/minecraft/client/renderer/feature/phase/SimpleFeatureRenderPhase;submit(Lnet/minecraft/client/renderer/feature/submit/SubmitNode;)V"
                    , ordinal = 0))
    private void drawWithOutline(SimpleFeatureRenderPhase instance, SubmitNode s, Operation<Void> original, @Share("textMatrix") LocalRef<Matrix4f> textMatrix) {
        var submit = (NameTagFeatureRenderer.Submit) s;

        Font font = Minecraft.getInstance().font;

        Matrix4f pose = new Matrix4f(submit.pose());
        int color = submit.color();
        boolean deadmau = submit.text().getString().equals("deadmau5");

        Matrix4f shadowMatrix = textMatrix.get() != null ? textMatrix.get() : pose;

        if (figura$enabled && figura$avatar != null && figura$hasCustomNameplate && figura$custom.outline) {
            // This renders the opaque text with an outline if the player has that enabled.
            int outlineColor = figura$custom.outlineColor != null ? figura$custom.outlineColor : 0x202020;

            if (figura$isRenderingName) {
                for (int i = 0; i < figura$textList.size(); i++) {
                    Component text1 = figura$textList.get(i);

                    if (text1.getString().isEmpty())
                        continue;

                    int line = i - figura$textList.size() + 1;
                    float x = -font.width(text1) / 2f;
                    float y = (deadmau ? -10f : 0f) + (font.lineHeight + 1) * line;
                    // yes i am using the bg color field to store the outline color, sue me
                    figura$outlineSubmits.add(new NameTagFeatureRenderer.Submit(pose, x, y, text1,  submit.lightCoords(), color, outlineColor, Font.DisplayMode.NORMAL));
                }
            } else {
                figura$outlineSubmits.add(new NameTagFeatureRenderer.Submit(pose, submit.x(), submit.y(), submit.text(),  submit.lightCoords(), color, outlineColor, Font.DisplayMode.NORMAL));
            }

            original.call(instance, new NameTagFeatureRenderer.Submit(shadowMatrix, submit.x(), submit.y(), Component.empty(),  submit.lightCoords(), color, submit.backgroundColor(), Font.DisplayMode.NORMAL));
        } else {
            if (figura$enabled && figura$avatar != null && figura$hasCustomNameplate && figura$isRenderingName) {
                // This renders the opaque part of the nametag, that is text
                for (int i = 0; i < figura$textList.size(); i++) {
                    Component text1 = figura$textList.get(i);

                    if (text1.getString().isEmpty())
                        continue;

                    int line = i - figura$textList.size() + 1;
                    float x = -font.width(text1) / 2f;
                    float y = (deadmau ? -10f : 0f) + (font.lineHeight + 1) * line;
                    original.call(instance, new NameTagFeatureRenderer.Submit(shadowMatrix, x, y, text1, submit.lightCoords(), color, submit.backgroundColor(), Font.DisplayMode.NORMAL));
                }
            } else {
                original.call(instance, new NameTagFeatureRenderer.Submit(shadowMatrix, submit.x(), submit.y(), submit.text(),  submit.lightCoords(), color, submit.backgroundColor(), Font.DisplayMode.NORMAL));
            }
        }
    }*/

    /*@Inject(method = "clear", at = @At("HEAD"))
    private void clearOutlineSubmits(CallbackInfo ci) {
        figura$outlineSubmits.clear();
    }*/

    /*@Override
    public List<NameTagFeatureRenderer.Submit> figura$getOutlineSubmits() {
        return figura$outlineSubmits;
    }*/

    @WrapOperation(method = "submitNameTag",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/phase/SimpleFeatureRenderPhase;submit(Lnet/minecraft/client/renderer/feature/submit/SubmitNode;)V"))
    private void figura$redirectSimple(SimpleFeatureRenderPhase instance, SubmitNode submit, Operation<Void> original,
                                       PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component name,
                                       boolean seeThrough, int lightCoords, CameraRenderState camera) {
        figura$handle(instance, null, submit, original, camera);
    }

    @WrapOperation(method = "submitNameTag",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/feature/phase/TranslucentFeatureRenderPhase;submit(Lnet/minecraft/client/renderer/feature/submit/TranslucentSubmit;)V"))
    private void figura$redirectTranslucent(TranslucentFeatureRenderPhase instance, TranslucentSubmit submit, Operation<Void> original,
                                            PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component name,
                                            boolean seeThrough, int lightCoords, CameraRenderState camera) {
        figura$handle(null, instance, submit, original, camera);
    }

    private void figura$handle(SimpleFeatureRenderPhase simplePhase, TranslucentFeatureRenderPhase translucentPhase,
                               Object submitObj, Operation<Void> original, CameraRenderState camera) {
        NameTagFeatureRenderer.Submit submit = (NameTagFeatureRenderer.Submit) submitObj;

        org.figuramc.figura.ducks.CameraRenderStateExtension camExt = (org.figuramc.figura.ducks.CameraRenderStateExtension) camera;
        Avatar avatar = camExt.figura$getAvatar();

        EntityNameplateCustomization custom = (avatar != null && avatar.luaRuntime != null) ? avatar.luaRuntime.nameplate.ENTITY : null;
        boolean hasCustom = custom != null && avatar.permissions.get(org.figuramc.figura.permissions.Permissions.NAMEPLATE_EDIT) == 1;
        boolean enabled = org.figuramc.figura.config.Configs.ENTITY_NAMEPLATE.value > 0
                && !org.figuramc.figura.avatar.AvatarManager.panic && hasCustom;

        if (!enabled) {
            figura$callOriginal(simplePhase, translucentPhase, submit, original);
            return;
        }

        Font font = Minecraft.getInstance().font;
        int light = custom.light != null ? custom.light : submit.lightCoords();
        int backgroundColor = custom.background != null ? custom.background : submit.backgroundColor();
        boolean deadmau = submit.text().getString().equals("deadmau5");


        List<Component> lines = TextUtils.splitText(submit.text(), "\n");

        for (int i = 0; i < lines.size(); i++) {
            Component line = lines.get(i);
            if (line.getString().isEmpty())
                continue;

            int lineOffset = i - lines.size() + 1;
            float x = -font.width(line) / 2f;
            float y = (deadmau ? -10f : 0f) + (font.lineHeight + 1) * lineOffset;

            NameTagFeatureRenderer.Submit lineSubmit = new NameTagFeatureRenderer.Submit(
                    submit.pose(), x, y, line, light, submit.color(), backgroundColor, submit.displayMode());

            // outline
            if (custom.outline) {
                int outlineColor = custom.outlineColor != null ? custom.outlineColor : 0x202020;
                ((NameTagSubmitExtension) (Object) lineSubmit).figura$setOutline(true, outlineColor);
            }

            figura$callOriginal(simplePhase, translucentPhase, lineSubmit, original);
        }
    }

    @SuppressWarnings("unchecked")
    private void figura$callOriginal(SimpleFeatureRenderPhase simplePhase, TranslucentFeatureRenderPhase translucentPhase,
                                     NameTagFeatureRenderer.Submit submit, Operation<Void> original) {
        if (simplePhase != null) {
            ((Operation<Void>) original).call(simplePhase, submit);
        } else {
            ((Operation<Void>) original).call(translucentPhase, submit);
        }
    }
}
