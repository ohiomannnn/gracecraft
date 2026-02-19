package ohiomannnn.gracecraft.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class ModRenderType extends RenderType {
    // constructor for no reason
    public ModRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    private static final TransparencyStateShard NO_DEPTH_TEST = new TransparencyStateShard(
            "no_depth_test",
            () -> {
                RenderSystem.disableDepthTest();
            },
            () -> {
                RenderSystem.enableDepthTest();
            });

    public static final Function<ResourceLocation, RenderType> MIME = Util.memoize(
            texture -> {
                RenderType.CompositeState state = RenderType.CompositeState.builder()
                        .setShaderState(RenderType.RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(NO_DEPTH_TEST)
                        .setCullState(RenderType.NO_CULL)
                        .setLightmapState(RenderType.LIGHTMAP)
                        .setOverlayState(RenderType.NO_OVERLAY)
                        .setWriteMaskState(RenderType.COLOR_WRITE)
                        .setOutputState(RenderType.TRANSLUCENT_TARGET)
                        .createCompositeState(false);
                return RenderType.create("haze", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 24325, false, true, state);
            }
    );
}
