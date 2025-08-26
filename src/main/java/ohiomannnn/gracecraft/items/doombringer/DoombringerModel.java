package ohiomannnn.gracecraft.items.doombringer;

import ohiomannnn.gracecraft.GraceCraft;
import software.bernie.geckolib.model.GeoModel;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoRenderer;

public class DoombringerModel extends GeoModel<DoombringerItem> {

    @Override
    public ResourceLocation getModelResource(DoombringerItem animatable, GeoRenderer<DoombringerItem> renderer) {
        return ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "geo/doombringer.geo.json");
    }

    @Override
    public ResourceLocation getModelResource(DoombringerItem animatable) {
        return null;
    }

    @Override
    public ResourceLocation getTextureResource(DoombringerItem animatable, GeoRenderer<DoombringerItem> renderer) {
        return ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/item/doombringer.png");
    }

    @Override
    public ResourceLocation getTextureResource(DoombringerItem animatable) {
        return null;
    }

    @Override
    public ResourceLocation getAnimationResource(DoombringerItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "animations/doombringer.animation.json");
    }
}