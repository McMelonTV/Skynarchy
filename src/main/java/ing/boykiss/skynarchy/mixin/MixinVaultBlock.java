package ing.boykiss.skynarchy.mixin;

import ing.boykiss.skynarchy.Skynarchy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(VaultBlock.class)
public class MixinVaultBlock {
    @Inject(method = "useItemOn", at = @At("HEAD"), cancellable = true)
    public void useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        VaultBlockEntity vault = (VaultBlockEntity) level.getBlockEntity(pos);
        if (vault == null) return;

        VaultConfig config = vault.getConfig();

        Optional<ResourceKey<LootTable>> optionalLootTable = config.overrideLootTableToDisplay();
        if (optionalLootTable.isEmpty() || optionalLootTable.get() != ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Skynarchy.MOD_ID, "vaults/end_cities/elytra")))
            return;

        if (!(level instanceof ServerLevel serverLevel)) return;

        try {
            if (vault.getServerData().hasRewardedPlayer(player)) {
                VaultBlockEntity.Server.playInsertFailSound(serverLevel, vault.getServerData(), pos, SoundEvents.VAULT_REJECT_REWARDED_PLAYER);
            } else {
                VaultBlockEntity.Server.unlock(serverLevel, state, pos, config, vault.getServerData(), vault.getSharedData(), List.of(Items.ELYTRA.getDefaultInstance()));
                vault.getServerData().rewardedPlayers.add(player.getUUID());
                vault.getServerData().markChanged();
                vault.getSharedData().updateConnectedPlayersWithinRange(serverLevel, pos, vault.getServerData(), config, config.deactivationRange());
            }
        } catch (Exception e) {
            Skynarchy.LOGGER.error(e.getMessage());
        }

        cir.cancel();
    }
}
