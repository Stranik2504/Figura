package org.figuramc.figura.compat;

import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.figuramc.figura.compat.wrappers.ClassWrapper;
import org.figuramc.figura.compat.wrappers.FieldWrapper;
import org.figuramc.figura.compat.wrappers.MethodWrapper;
import java.util.Map;

public class GeckoLibCompat {
    // For 5.0 versions of GeckoLib
    private static ClassWrapper GLRenderUtil5;
    private static MethodWrapper getGeoModelForArmor5;

    // For newer versions of GeckoLib
    private static ClassWrapper GLRenderUtil;
    private static MethodWrapper getGeoModelForArmor;

    // For older versions of GeckoLib
    private static ClassWrapper GLGeoArmorRenderer;
    private static FieldWrapper renderers;


    public static void init() {
        // Version 5
        GLRenderUtil5 = new ClassWrapper("com.geckolib.util.RenderUtil");
        getGeoModelForArmor5 = GLRenderUtil5.getMethod("getGeckoLibArmorRenderer", ItemStack.class, EquipmentSlot.class);

        // Modern
        GLRenderUtil = new ClassWrapper("software.bernie.geckolib.util.RenderUtil");
        getGeoModelForArmor = GLRenderUtil.getMethod("getGeoModelForArmor", ItemStack.class, EquipmentSlot.class, EquipmentClientInfo.LayerType.class);

        // Legacy
        GLGeoArmorRenderer = new ClassWrapper("software.bernie.geckolib3.renderers.geo.GeoArmorRenderer");
        renderers = GLGeoArmorRenderer.getField("renderers");
    }

    public static boolean armorHasCustomModel(ItemStack stack, EquipmentSlot slot, EquipmentClientInfo.LayerType layerType) {
        if (GLRenderUtil5.isLoaded) {
            if (getGeoModelForArmor5.exists()) {
                return getGeoModelForArmor5.invoke(null, stack, slot) != null;
            }
            return false;
        }

        if (GLRenderUtil.isLoaded) {
            if (getGeoModelForArmor.exists()) {
                return getGeoModelForArmor.invoke(null, stack, slot, layerType) != null;
            }
            return false;
        }

        if (GLGeoArmorRenderer.isLoaded) {
            if (renderers.exists()) {
                if (renderers.getValue(null) instanceof Map<?, ?> map) {
                    return map.containsKey(stack.getItem().getClass());
                }
                renderers.markErrored();
            }

            return false;
        }


        return false;
    }
}
