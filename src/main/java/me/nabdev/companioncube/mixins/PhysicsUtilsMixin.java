package me.nabdev.companioncube.mixins;

import com.badlogic.gdx.math.Vector3;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import com.nikrasoff.seamlessportals.SeamlessPortals;
import com.nikrasoff.seamlessportals.portals.Portal;
import finalforeach.cosmicreach.entities.player.Player;
import me.nabdev.physicsmod.utils.PhysicsUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PhysicsUtils.class)
public class PhysicsUtilsMixin {
    @Inject(method = "applyMagnetForce", at = @At("HEAD"), cancellable = true)
    private static void applyMagnetForce(Player player, Vector3 position, PhysicsRigidBody body, CallbackInfo ci) {
        boolean isCubeInPortal = false;
        boolean isPlayerInPortal = false;
        for(Portal p :SeamlessPortals.portalManager.createdPortals.values()){
            if(p.globalBoundingBox.contains(position)) {
                isCubeInPortal = true;
            }
            if(p.globalBoundingBox.intersects(player.getEntity().globalBoundingBox)) {
                isPlayerInPortal = true;
            }
            if (isCubeInPortal && isPlayerInPortal) {
                break;
            }
        }
        if(isCubeInPortal && isPlayerInPortal && player.getPosition().dst(position) > 3f) {
            ci.cancel();
            body.setGravity(new Vector3f(0, 0, 0));
        } else {
            body.setGravity(new Vector3f(0, -9.81f, 0));
        }
    }
}
