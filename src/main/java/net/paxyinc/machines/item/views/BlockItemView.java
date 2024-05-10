package net.paxyinc.machines.item.views;

import com.badlogic.gdx.graphics.Camera;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.MeshData;
import finalforeach.cosmicreach.rendering.RenderOrder;
import finalforeach.cosmicreach.rendering.SharedQuadIndexData;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import finalforeach.cosmicreach.rendering.meshes.IGameMesh;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import net.paxyinc.machines.item.IItemView;

public class BlockItemView implements IItemView {
    public final BlockState blockState;
    IGameMesh mesh;
    GameShader shader;

    public BlockItemView(BlockState blockState) {
        this.blockState = blockState;
    }

    @Override
    public void initialize() {
        MeshData meshData;
        if (blockState.isTransparent) {
            if (blockState.isFluid) {
                meshData = new MeshData(ChunkShader.WATER_BLOCK_SHADER, RenderOrder.TRANSPARENT);
            } else {
                meshData = new MeshData(ChunkShader.DEFAULT_BLOCK_SHADER, RenderOrder.TRANSPARENT);
            }
        } else {
            meshData = new MeshData(ChunkShader.DEFAULT_BLOCK_SHADER, RenderOrder.DEFAULT);
        }

        this.shader = meshData.getShader();
        blockState.addVertices(meshData, 0, 0, 0);
        if (BlockModelJson.useIndices) {
            this.mesh = meshData.toIntIndexedMesh(true);
        } else {
            this.mesh = meshData.toSharedIndexMesh(true);
            if (this.mesh != null) {
                int numIndices = this.mesh.getNumVertices() * 6 / 4;
                SharedQuadIndexData.allowForNumIndices(numIndices, false);
            }
        }
    }

    public void render(Camera camera) {
        if (this.mesh != null) {
            if (!BlockModelJson.useIndices) SharedQuadIndexData.bind();
            shader.bind(camera);
            shader.shader.setUniformMatrix("u_projViewTrans", camera.combined);
            mesh.bind(shader.shader);
            mesh.render(shader.shader, 4);
            mesh.unbind(shader.shader);
            shader.unbind();
            if (!BlockModelJson.useIndices) SharedQuadIndexData.unbind();
        }
    }

}
