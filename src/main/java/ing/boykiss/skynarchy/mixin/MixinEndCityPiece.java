package ing.boykiss.skynarchy.mixin;

import ing.boykiss.skynarchy.Skynarchy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.EndCityPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(EndCityPieces.EndCityPiece.class)
public class MixinEndCityPiece {
    @Unique
    private static final BlockState VAULT_BLOCK = Blocks.VAULT.defaultBlockState();

    @Unique
    private final EndCityPieces.EndCityPiece piece = (EndCityPieces.EndCityPiece) (Object) this;

    @Inject(method = "handleDataMarker", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/decoration/ItemFrame;<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)V"), cancellable = true)
    public void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box, CallbackInfo ci) {
        BlockPos blockPos = pos.below();
        if (box.isInside(blockPos)) {
            level.setBlock(blockPos, VAULT_BLOCK.rotate(piece.placeSettings().getRotation().getRotated(Rotation.CLOCKWISE_180)), Block.UPDATE_CLIENTS | Block.UPDATE_SKIP_ALL_SIDEEFFECTS);
            VaultBlockEntity vault = (VaultBlockEntity) level.getBlockEntity(blockPos);
            if (vault != null) {
                vault.setConfig(new VaultConfig(
                        ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Skynarchy.MOD_ID, "empty")),
                        4,
                        4.5,
                        Items.BARRIER.getDefaultInstance(),
                        Optional.of(ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Skynarchy.MOD_ID, "vaults/end_cities/elytra")))
                ));
            }
        }
        ci.cancel();
    }
}
