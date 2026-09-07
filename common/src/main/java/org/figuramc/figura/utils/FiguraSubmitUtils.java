package org.figuramc.figura.utils;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeStorage;
import org.figuramc.figura.model.rendering.nodeRenderer.FiguraSubmission;

public class FiguraSubmitUtils {
    public static void submit(SubmitNodeCollector collector, FiguraSubmission submission) {
        if (submission == null) return;
        if (collector instanceof SubmitNodeStorage storage) {
            storage.order(0).translucentCustomGeometry.submit(submission);
        }
    }
}
