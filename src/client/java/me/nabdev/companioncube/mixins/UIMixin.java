package me.nabdev.companioncube.mixins;

import com.nikrasoff.seamlessportals.items.HandheldPortalGen;
import com.nikrasoff.seamlessportals.items.UnstableHandheldPortalGen;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.ui.UI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UI.class)
public class UIMixin {
    @Inject(method = "setInventoryOpen", at = @At("HEAD"), cancellable = true)
    private static void setInventoryOpen(boolean set, CallbackInfo ci){
        ItemStack heldItemStack = UI.hotbar.getSelectedItemStack();
        if (heldItemStack == null || (!heldItemStack.getItem().getID().equals(UnstableHandheldPortalGen.hpgID) && !heldItemStack.getItem().getID().equals(HandheldPortalGen.hpgID))) return;
        ci.cancel();
    }
}
