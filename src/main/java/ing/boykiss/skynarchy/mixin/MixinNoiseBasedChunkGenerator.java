package ing.boykiss.skynarchy.mixin;

import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(NoiseBasedChunkGenerator.class)
public class MixinNoiseBasedChunkGenerator {
    @Inject(method = "buildSurface*", at = @At("HEAD"), cancellable = true)
    public void buildSurface(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "applyCarvers", at = @At("HEAD"), cancellable = true)
    public void applyCarvers(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "fillFromNoise", at = @At("HEAD"), cancellable = true)
    public void fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir) {
        cir.setReturnValue(CompletableFuture.completedFuture(chunk));
    }
}
