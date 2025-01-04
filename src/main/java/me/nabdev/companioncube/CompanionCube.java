package me.nabdev.companioncube;

import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.block.DataModBlock;
import com.github.puzzle.game.events.OnRegisterBlockEvent;
import finalforeach.cosmicreach.util.Identifier;
import meteordevelopment.orbit.EventHandler;

public class CompanionCube implements ModInitializer {
    @Override
    public void onInit() {
        PuzzleRegistries.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    public void onEvent(OnRegisterBlockEvent event) {
        event.registerBlock(me.nabdev.companioncube.blocks.CompanionCube::new);
    }
}
