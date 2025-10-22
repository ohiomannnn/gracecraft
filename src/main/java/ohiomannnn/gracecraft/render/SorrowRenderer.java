package ohiomannnn.gracecraft.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.client.GraceCraftClient;
import ohiomannnn.gracecraft.entity.Sorrow;

import java.util.Random;

public class SorrowRenderer extends EntityRenderer<Sorrow> {

    private static final ResourceLocation SORROW = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/sorrow.png");
    private static final ResourceLocation SORROW_HEAVY = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/sorrow_heavy.png");

    private final Random random = new Random();
    private float[] rainXCoords;
    private float[] rainYCoords;
    private long lastTime = System.nanoTime();

    public SorrowRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(Sorrow entity) {
        return SORROW;
    }

    @Override
    public void render(Sorrow entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        LivingEntity camera = mc.player;
        ClientLevel world = mc.level;
        if (camera == null || world == null) return;
        long time = System.nanoTime();
        float dt = (time - lastTime) / 50_000_000f;
        float interp = Math.min(dt, 1.0f);
        lastTime = time;

        if (entity.tickCount > 140 && System.currentTimeMillis() - GraceCraftClient.redTimeStamp > 1_000) GraceCraftClient.redTimeStamp = System.currentTimeMillis();
        if (entity.tickCount > 150 && !entity.didShake && System.currentTimeMillis() - GraceCraftClient.shakeTimestamp > 1_000) {
            GraceCraftClient.shakeTimestamp = System.currentTimeMillis();
            entity.didShake = true;
            Player player = Minecraft.getInstance().player;
            player.hurtTime = 15;
            player.hurtDuration = 1;
        }

        PoseStack globalPose = new PoseStack();
        renderRainSnow(interp, globalPose, bufferSource, world, camera, entity);
    }

    @Override
    public boolean shouldRender(Sorrow livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    public void renderRainSnow(float interp, PoseStack pose, MultiBufferSource buffers, ClientLevel world, LivingEntity camera, Sorrow entity) {

        int timer = camera.tickCount;
        float intensity = 1.0F;
        if (intensity <= 0.0F) return;

        if (this.rainXCoords == null) {
            this.rainXCoords = new float[1024];
            this.rainYCoords = new float[1024];
            for (int i = 0; i < 32; ++i) {
                for (int j = 0; j < 32; ++j) {
                    float f2 = j - 16;
                    float f3 = i - 16;
                    float f4 = Mth.sqrt(f2 * f2 + f3 * f3);
                    this.rainXCoords[i << 5 | j] = -f3 / f4;
                    this.rainYCoords[i << 5 | j] = f2 / f4;
                }
            }
        }

        var graphics = Minecraft.getInstance().options.graphicsMode().get();
        boolean fancy = graphics == GraphicsStatus.FANCY || graphics == GraphicsStatus.FABULOUS;
        int renderLayerCount = fancy ? 10 : 5;

        double camX = camera.xOld + (camera.getX() - camera.xOld) * interp;
        double camY = camera.yOld + (camera.getY() - camera.yOld) * interp;
        double camZ = camera.zOld + (camera.getZ() - camera.zOld) * interp;

        int playerX = Mth.floor(camera.getX());
        int playerY = Mth.floor(camera.getY());
        int playerZ = Mth.floor(camera.getZ());

        pose.pushPose();
        pose.translate(-camX, -camY, -camZ);

        VertexConsumer consumer = entity.tickCount > 125 ? buffers.getBuffer(RenderType.entityTranslucent(SORROW_HEAVY)) : buffers.getBuffer(RenderType.entityTranslucent(SORROW));
        int overlay = OverlayTexture.NO_OVERLAY;

        for (int layerZ = playerZ - renderLayerCount; layerZ <= playerZ + renderLayerCount; ++layerZ) {
            for (int layerX = playerX - renderLayerCount; layerX <= playerX + renderLayerCount; ++layerX) {

                int rainCoord = (layerZ - playerZ + 16) * 32 + layerX - playerX + 16;
                float rainCoordX = this.rainXCoords[rainCoord] * 0.5F;
                float rainCoordY = this.rainYCoords[rainCoord] * 0.5F;

                int rainHeight = world.getHeight(Heightmap.Types.WORLD_SURFACE, layerX, layerZ);
                int minHeight = rainHeight - 2;
                int maxHeight = playerY + renderLayerCount;
                if (maxHeight <= minHeight) continue;

                this.random.setSeed(layerX * layerX * 3121L + layerX * 45238971L ^ layerZ * layerZ * 418711L + layerZ * 13761L);

                float swayLoop = ((timer & 511) + interp) / 8.0F;
                float fallVariation = 0.4F + this.random.nextFloat() * 0.2F;
                float swayVariation = this.random.nextFloat();

                double distX = layerX + 0.5D - camera.getX();
                double distZ = layerZ + 0.5D - camera.getZ();
                float intensityMod = Mth.sqrt((float) (distX * distX + distZ * distZ)) / renderLayerCount;

                float alpha = ((1.0F - intensityMod * intensityMod) * 0.3F + 0.5F) * intensity;

                float u0 = 0.0F + fallVariation;
                float u1 = 1.0F + fallVariation;
                float vMin = minHeight / 4.0F + swayLoop + swayVariation;
                float vMax = maxHeight / 4.0F + swayLoop + swayVariation;

                int argb = FastColor.ARGB32.color((int) (alpha * 255), 255, 255, 255);

                float vx0 = (float) (layerX - rainCoordX + 0.5D);
                float vz0 = (float) (layerZ - rainCoordY + 0.5D);
                float vx1 = (float) (layerX + rainCoordX + 0.5D);
                float vz1 = (float) (layerZ + rainCoordY + 0.5D);

                consumer.addVertex(pose.last().pose(), vx0, minHeight, vz0)
                        .setColor(argb).setUv(u0, vMin).setOverlay(overlay).setLight(240).setNormal(0, 1, 0);
                consumer.addVertex(pose.last().pose(), vx1, minHeight, vz1)
                        .setColor(argb).setUv(u1, vMin).setOverlay(overlay).setLight(240).setNormal(0, 1, 0);
                consumer.addVertex(pose.last().pose(), vx1, maxHeight, vz1)
                        .setColor(argb).setUv(u1, vMax).setOverlay(overlay).setLight(240).setNormal(0, 1, 0);
                consumer.addVertex(pose.last().pose(), vx0, maxHeight, vz0)
                        .setColor(argb).setUv(u0, vMax).setOverlay(overlay).setLight(240).setNormal(0, 1, 0);
            }
        }

        pose.popPose();
    }
}
