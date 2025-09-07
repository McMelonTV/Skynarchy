package ing.boykiss.skynarchy.mixin;

import net.minecraft.network.protocol.game.CommonPlayerSpawnInfo;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {
    @Unique
    private final ServerPlayer sp = (ServerPlayer) (Object) this;

    @Inject(method = "createCommonSpawnInfo", at = @At("HEAD"), cancellable = true)
    public void createCommonSpawnInfo(ServerLevel level, CallbackInfoReturnable<CommonPlayerSpawnInfo> cir) {
        cir.setReturnValue(new CommonPlayerSpawnInfo(
                level.dimensionTypeRegistration(),
                level.dimension(),
                BiomeManager.obfuscateSeed(level.getSeed()),
                sp.gameMode.getGameModeForPlayer(),
                sp.gameMode.getPreviousGameModeForPlayer(),
                level.isDebug(),
                level.dimensionTypeRegistration().is(BuiltinDimensionTypes.OVERWORLD) || level.isFlat(), // remove the black void skybox
                sp.getLastDeathLocation(),
                sp.getPortalCooldown(),
                level.getSeaLevel()
        ));
    }
}
