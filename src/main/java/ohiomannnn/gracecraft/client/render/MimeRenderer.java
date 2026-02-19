package ohiomannnn.gracecraft.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import ohiomannnn.gracecraft.GraceCraft;
import ohiomannnn.gracecraft.entity.Mime;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Objects;

public class MimeRenderer extends EntityRenderer<Mime> {

    private static final ResourceLocation MIME = ResourceLocation.fromNamespaceAndPath(GraceCraft.MOD_ID, "textures/entities/mime.png");

    public MimeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(Mime mime, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        if (!Objects.equals(Minecraft.getInstance().player.getGameProfile().getName().toString(), mime.getTargetUser())) return;

        SingleQuadParticle.FacingCameraMode cameraMode = SingleQuadParticle.FacingCameraMode.LOOKAT_Y;

        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

        Quaternionf quaternionf = new Quaternionf();
        cameraMode.setRotation(quaternionf, camera, partialTicks);

        Vec3 vec3 = camera.getPosition();
        float x = (float)(Mth.lerp(partialTicks, mime.xo, mime.getX()) - vec3.x);
        float y = (float)(Mth.lerp(partialTicks, mime.yo, mime.getY()) - vec3.y) + 1F;
        float z = (float)(Mth.lerp(partialTicks, mime.zo, mime.getZ()) - vec3.z);

        VertexConsumer buffer = bufferSource.getBuffer(ModRenderType.MIME.apply(this.getTextureLocation(mime)));

        this.renderVertex(buffer, quaternionf, x, y, z, 1.0F, -1.0F, 1, 1, 1, 240);
        this.renderVertex(buffer, quaternionf, x, y, z, 1.0F, 1.0F, 1, 1, 0, 240);
        this.renderVertex(buffer, quaternionf, x, y, z, -1.0F, 1.0F, 1, 0, 0, 240);
        this.renderVertex(buffer, quaternionf, x, y, z, -1.0F, -1.0F, 1, 0, 1, 240);
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = new Vector3f(xOffset, yOffset, 0.0F).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setColor(1F, 1F, 1F, 1F).setNormal(0.0F, 1.0F, 0.0F).setLight(packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Mime mime) {
        return MIME;
    }
}
