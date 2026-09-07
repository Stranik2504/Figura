package org.figuramc.figura.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.figuramc.figura.compat.wrappers.ClassWrapper;
import org.figuramc.figura.compat.wrappers.FieldWrapper;
import org.figuramc.figura.compat.wrappers.MethodWrapper;

public class SimpleVCCompat {

    private static ClassWrapper ClientManager;
    private static FieldWrapper renderEventsField;
    private static MethodWrapper onRenderName;
    public static void init() {
        ClientManager = new ClassWrapper("de.maxhenkel.voicechat.voice.client.ClientManager");
        renderEventsField = ClientManager.getField("renderEvents");

        var renderEvents = new ClassWrapper("de.maxhenkel.voicechat.voice.client.RenderEvents");
        onRenderName = renderEvents.getMethod("onRenderName", EntityRenderState.class, CameraRenderState.class, PoseStack.class, SubmitNodeCollector.class);
    }


    // Accesses SimpleVC's onRenderName method through reflection so that no dependency is actually needed
    public static void renderSimpleVCIcon(EntityRenderState state, CameraRenderState renderState, PoseStack stack, SubmitNodeCollector submitNodeCollector) {
        if (ClientManager.isLoaded && renderEventsField.exists() && onRenderName.exists()) {
            onRenderName.invoke(renderEventsField.getValue(ClientManager.getMethod("instance").invoke(null)), state, renderState, stack, submitNodeCollector);
        }
    }
}
