package ohiomannnn.gracecraft.items.flashlight;

import net.minecraft.resources.ResourceLocation;
import ohiomannnn.gracecraft.GraceCraft;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class FlashlightModel extends GeoModel<FlashlightItem> {
    @Override
    public ResourceLocation getModelResource(FlashlightItem animatable, GeoRenderer<FlashlightItem> renderer) {
        return ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "geo/flashlight.geo.json");
    }

    @Override
    public ResourceLocation getModelResource(FlashlightItem animatable) {
        return null;
    }

    @Override
    public ResourceLocation getTextureResource(FlashlightItem animatable, GeoRenderer<FlashlightItem> renderer) {
        return ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/item/flashlight.png");
    }

    @Override
    public ResourceLocation getTextureResource(FlashlightItem animatable) {
        return null;
    }

    @Override
    public ResourceLocation getAnimationResource(FlashlightItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "animations/flashlight.animation.json");
    }

}