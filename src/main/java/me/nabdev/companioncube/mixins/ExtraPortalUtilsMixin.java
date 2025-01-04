package me.nabdev.companioncube.mixins;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.github.puzzle.game.items.data.DataTag;
import com.llamalad7.mixinextras.sugar.Local;
import com.nikrasoff.seamlessportals.SeamlessPortals;
import com.nikrasoff.seamlessportals.extras.ExtraPortalUtils;
import com.nikrasoff.seamlessportals.portals.Portal;
import com.nikrasoff.seamlessportals.portals.PortalManager;
import finalforeach.cosmicreach.entities.EntityUniqueId;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.world.Chunk;
import me.nabdev.physicsmod.utils.PhysicsWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExtraPortalUtils.class)
public class ExtraPortalUtilsMixin {
    @Inject(method = "fireHpg", at = @At(value = "TAIL"))
    private static void fireHPG(Player player, boolean isSecondPortal, ItemStack hpgItemStack, CallbackInfo ci,
                                @Local(ordinal = 0) DataTag<Vector3> primaryPortalChunkPos, @Local(ordinal = 1) DataTag<Integer> primaryPortalIdRand, @Local(ordinal = 2) DataTag<Long> primaryPortalIdTime,
                                @Local(ordinal = 3) DataTag<Integer> primaryPortalIdNum, @Local(ordinal = 4) DataTag<String> primaryPortalZone,
                                @Local(ordinal = 5) DataTag<Vector3> secondaryPortalChunkPos, @Local(ordinal = 6) DataTag<Integer> secondaryPortalIdRand, @Local(ordinal = 7) DataTag<Long> secondaryPortalIdTime,
                                @Local(ordinal = 8) DataTag<Integer> secondaryPortalIdNum, @Local(ordinal = 9) DataTag<String> secondaryPortalZone) {
        PortalManager pm = SeamlessPortals.portalManager;
        EntityUniqueId id1 = new EntityUniqueId();
        id1.set(primaryPortalIdTime.getValue(), primaryPortalIdRand.getValue(), primaryPortalIdNum.getValue());
        EntityUniqueId id2 = new EntityUniqueId();
        id2.set(secondaryPortalIdTime.getValue(), secondaryPortalIdRand.getValue(), secondaryPortalIdNum.getValue());
        Portal portal1 = pm.getPortalWithGen(id1, primaryPortalChunkPos.getValue(), primaryPortalZone.getValue());
        Portal portal2 = pm.getPortalWithGen(id2, secondaryPortalChunkPos.getValue(), secondaryPortalZone.getValue());
        Array<Chunk> adjacentChunks = new Array<>();
        if (portal1 != null) {
            Chunk chunk1 = player.getZone().getChunkAtPosition(portal1.position);
            chunk1.getAdjacentChunks(player.getZone(), adjacentChunks);
            adjacentChunks.add(chunk1);
        }
        if (portal2 != null) {
            Chunk chunk2 = player.getZone().getChunkAtPosition(portal2.position);
            chunk2.getAdjacentChunks(player.getZone(), adjacentChunks);
            adjacentChunks.add(chunk2);
        }
        for (Chunk c : adjacentChunks) {
            //c.fill(Block.WATER.getDefaultBlockState());
            PhysicsWorld.invalidateChunk(c);
        }
    }
}
