package me.nabdev.companioncube.blocks;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.block.IModBlock;
import com.github.puzzle.game.block.generators.BlockEventGenerator;
import com.github.puzzle.game.block.generators.BlockGenerator;
import com.github.puzzle.game.block.generators.model.BlockModelGenerator;
import finalforeach.cosmicreach.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.util.Identifier;
import me.nabdev.companioncube.Constants;
import me.nabdev.physicsmod.utils.PhysicsUtils;

import java.util.List;

public class CompanionCube implements IModBlock {

    public static final Identifier BLOCK_ID = Identifier.of(Constants.MOD_ID, "companion_cube");

    public static final Identifier ALL_TEXTURE = Identifier.of(Constants.MOD_ID, "textures/blocks/companion_cube.png");

    @Override
    public Identifier getIdentifier() {
        return BLOCK_ID;
    }

    @Override
    public void onPlace(BlockEventArgs args) {
        PhysicsUtils.createBlockAt(new Vector3(args.blockPos.getGlobalX(), args.blockPos.getGlobalY(), args.blockPos.getGlobalZ()), args.srcBlockState, args.zone);
        args.blockPos.setBlockState(Block.AIR.getDefaultBlockState());
    }


    @Override
    public BlockGenerator getBlockGenerator() {
        BlockGenerator generator = new BlockGenerator(BLOCK_ID);
        BlockGenerator.State state = generator.createBlockState("default", "model", true, "base_block_model_generator", true);
        state.langKey = "Companion Cube";
        return generator;
    }

    @Override
    public List<BlockModelGenerator> getBlockModelGenerators(Identifier blockId) {
        BlockModelGenerator generator = new BlockModelGenerator(blockId, "model");
        generator.createTexture("all", ALL_TEXTURE);
        generator.createCuboid(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F, "all");
        return List.of(generator);
    }

    @Override
    public List<BlockEventGenerator> getBlockEventGenerators(Identifier blockId) {
        BlockEventGenerator generator = new BlockEventGenerator(blockId, "base_block_model_generator");
        return List.of(generator);
    }
}
