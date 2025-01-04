package me.nabdev.companioncube.mixins;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.nikrasoff.seamlessportals.items.HandheldPortalGen;
import com.nikrasoff.seamlessportals.items.UnstableHandheldPortalGen;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.world.Zone;
import me.nabdev.physicsmod.entities.Cube;
import me.nabdev.physicsmod.utils.PhysicsWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGame.class)
public abstract class InGameMixin {
    @Unique
    private final Ray companionCube$ray = new Ray();
    @Unique
    private final BoundingBox companionCube$tmpBoundingBox = new BoundingBox();
    @Unique
    private final Vector3 companionCube$intersectionPoint = new Vector3();
    @Unique
    private final float companionCube$maximumRaycastDist = 6.0F;


    @Shadow
    private static Player localPlayer;

    @Shadow private static PerspectiveCamera rawWorldCamera;

    @Inject(method = "render", at = @At("HEAD"))
    public void render0(CallbackInfo ci) {
        ItemStack heldItemStack = UI.hotbar.getSelectedItemStack();
        if (heldItemStack == null) return;
        Entity lookingAt = companionCube$raycastForEntities(localPlayer.getZone(), rawWorldCamera);
        if(lookingAt instanceof Cube cube) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                if (heldItemStack.getItem().getID().equals(UnstableHandheldPortalGen.hpgID) || heldItemStack.getItem().getID().equals(HandheldPortalGen.hpgID)) {
                    if (cube.magnetPlayer != null) {
                        if (cube.magnetPlayer.getAccount().getUniqueId().equals(localPlayer.getAccount().getUniqueId()))
                            PhysicsWorld.dropMagnet(localPlayer);
                    } else {
                        PhysicsWorld.magnet(localPlayer, cube);
                    }
                }
            }
        }
    }

    @Unique
    private Entity companionCube$raycastForEntities(Zone zone, Camera worldCamera) {
        this.companionCube$ray.set(worldCamera.position, worldCamera.direction);
        Entity playerEntity = InGame.getLocalPlayer().getEntity();

        for (Entity e : zone.getAllEntities()) {
            if (e != playerEntity && e instanceof Cube) {
                e.getBoundingBox(this.companionCube$tmpBoundingBox);
                if (Intersector.intersectRayBounds(this.companionCube$ray, this.companionCube$tmpBoundingBox, this.companionCube$intersectionPoint)) {
                    float distance = this.companionCube$intersectionPoint.dst(worldCamera.position);
                    if (!(distance > this.companionCube$maximumRaycastDist)) {
                        return e;
                    }
                }
            }
        }
        return null;
    }
}
