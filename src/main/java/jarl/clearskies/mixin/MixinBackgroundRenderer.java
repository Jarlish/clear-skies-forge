package jarl.clearskies.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;

@Mixin(FogRenderer.class)
public class MixinBackgroundRenderer {

	@Shadow private static float red;
	@Shadow private static float green;
	@Shadow private static float blue;
	@Shadow private static int lastWaterFogColor = -1;
	@Shadow private static int waterFogColor = -1;
	@Shadow private static long waterFogUpdateTime = -1L;

	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/CubicSampler;func_240807_a_(Lnet/minecraft/util/math/vector/Vector3d;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/util/math/vector/Vector3d;"), method = "updateFogColor", ordinal = 2, require = 1, allow = 1)
	private static Vector3d onSampleColor(Vector3d val) {
		final Minecraft mc = Minecraft.getInstance();
		
		final ClientWorld world = mc.world;

		if (world.getDimensionType().hasSkyLight()) {
			return world.getSkyColor(mc.gameRenderer.getActiveRenderInfo().getBlockPos(), mc.getTickLength());
		} else {
			return val;
		}
	}

	@ModifyVariable(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/vector/Vector3f;dot(Lnet/minecraft/util/math/vector/Vector3f;)F"), method = "updateFogColor", ordinal = 7, require = 1, allow = 1)
	private static float afterPlaneDot(float dotPrduct) {
		return 0;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getRainStrength(F)F"), method = "updateFogColor", require = 1, allow = 1)
	private static float onGetRainGradient(ClientWorld world, float tickDelta) {
		return 0;
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getThunderStrength(F)F"), method = "updateFogColor", require = 1, allow = 1)
	private static float onGetThunderGradient(ClientWorld world, float tickDelta) {
		return 0;
	}
}
