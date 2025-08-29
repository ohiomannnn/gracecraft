package ohiomannnn.gracecraft.items.flashlight;

import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FlashlightRenderer extends GeoItemRenderer<FlashlightItem> {
    public FlashlightRenderer() {
        super(new FlashlightModel());
    }
}