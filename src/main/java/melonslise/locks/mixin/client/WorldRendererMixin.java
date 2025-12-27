package melonslise.locks.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import melonslise.locks.client.event.LocksClientForgeEvents;
import melonslise.locks.client.util.LocksClientUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class WorldRendererMixin
{
	// Before first checkPoseStack call
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;checkPoseStack(Lcom/mojang/blaze3d/vertex/PoseStack;)V", ordinal = 0), method = "renderLevel")
	private void renderLevel(DeltaTracker deltaTracker, boolean renderOutline, Camera cam, GameRenderer gr, LightTexture lightTex, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci)
	{
		PoseStack mtx = new PoseStack();
		mtx.mulPose(frustumMatrix);
		float pt = deltaTracker.getGameTimeDeltaPartialTick(true);
		LocksClientForgeEvents.renderLocks(mtx, Minecraft.getInstance().renderBuffers().bufferSource(), LocksClientUtil.getFrustum(mtx, projectionMatrix), pt);
	}
}