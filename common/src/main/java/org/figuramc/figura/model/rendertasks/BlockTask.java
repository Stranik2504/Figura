package org.figuramc.figura.model.rendertasks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.api.world.BlockStateAPI;
import org.figuramc.figura.lua.docs.LuaMethodDoc;
import org.figuramc.figura.lua.docs.LuaMethodOverload;
import org.figuramc.figura.lua.docs.LuaTypeDoc;
import org.figuramc.figura.model.FiguraModelPart;
import org.figuramc.figura.model.FiguraVertexConsumerProvider;
import org.figuramc.figura.utils.LuaUtils;

import java.util.ArrayList;

@LuaWhitelist
@LuaTypeDoc(
        name = "BlockTask",
        value = "block_task"
)
public class BlockTask extends RenderTask {

    private BlockState block;
    private int cachedComplexity;

    public BlockTask(String name, Avatar owner, FiguraModelPart parent) {
        super(name, owner, parent);
    }

    @Override
    public boolean requiresDirectSubmit() {
        return true;
    }

    @Override
    public void render(PoseStack poseStack, FiguraVertexConsumerProvider buffer, int light, int overlay) { }

    @Override
    public void renderDirect(PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay) {
        poseStack.scale(16, 16, 16);

        int newLight = this.customization.light != null ? this.customization.light : light;
        int newOverlay = this.customization.overlay != null ? this.customization.overlay : overlay;

        var client = Minecraft.getInstance();

        var rendererState = new BlockModelRenderState();
        var resolver = new BlockModelResolver(client.getModelManager());
        resolver.update(rendererState, block, BlockDisplayContext.create());
        rendererState.submit(
            poseStack,
            collector,
            newLight,
            newOverlay,
            0
        );
    }

    @Override
    public int getComplexity() {
        return cachedComplexity;
    }

    @Override
    public boolean shouldRender() {
        return super.shouldRender() && block != null && !block.isAir();
    }

    // -- lua -- // 


    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaMethodOverload(
                            argumentTypes = String.class,
                            argumentNames = "block"
                    ),
                    @LuaMethodOverload(
                            argumentTypes = BlockStateAPI.class,
                            argumentNames = "block"
                    )
            },
            aliases = "block",
            value = "block_task.set_block"
    )
    public BlockTask setBlock(Object block) {
        this.block = LuaUtils.parseBlockState("block", block);
        Minecraft client = Minecraft.getInstance();
        RandomSource random = client.level != null ? client.level.getRandom() : RandomSource.create();

        var blockModel = client.getModelManager().getBlockStateModelSet().get(this.block);
        var parts = new ArrayList<BlockStateModelPart>();
        blockModel.collectParts(random, parts);

        cachedComplexity = parts.stream().mapToInt(p -> p.getQuads(null).size()).sum();

        for (Direction dir : Direction.values())
            cachedComplexity += parts.stream().mapToInt(p -> p.getQuads(dir).size()).sum();

        return this;
    }

    @LuaWhitelist
    public BlockTask block(Object block) {
        return setBlock(block);
    }

    @Override
    public String toString() {
        return name + " (Block Render Task)";
    }
}
