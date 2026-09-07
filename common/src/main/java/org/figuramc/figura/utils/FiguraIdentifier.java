package org.figuramc.figura.utils;

import net.minecraft.util.Util;
import net.minecraft.resources.Identifier;
import org.figuramc.figura.FiguraMod;

public final class FiguraIdentifier {
    private FiguraIdentifier() {
    }

    public static Identifier of(String path) {
        return Identifier.fromNamespaceAndPath(FiguraMod.MOD_ID, formatPath(path));
    }

    public static Identifier of(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, formatPath(path));
    }

    public static String formatPath(String path) {
        return Util.sanitizeName(path, Identifier::validPathChar);
    }
}
