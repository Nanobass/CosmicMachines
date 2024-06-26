package net.paxyinc.item.views;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.MeshData;
import finalforeach.cosmicreach.rendering.RenderOrder;
import finalforeach.cosmicreach.rendering.SharedQuadIndexData;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import finalforeach.cosmicreach.rendering.meshes.IGameMesh;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.rendering.shaders.GameShader;
import net.paxyinc.item.IItemView;

public class BlockItemView implements IItemView {
    public final BlockState blockState;
    public IGameMesh mesh;
    public GameShader shader;

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

    public void render(Camera camera, Matrix4 projViewTrans) {
        if (this.mesh != null) {
            if (!BlockModelJson.useIndices) SharedQuadIndexData.bind();
            shader.bind(camera);
            shader.shader.setUniformMatrix("u_projViewTrans", projViewTrans);
            mesh.bind(shader.shader);
            mesh.render(shader.shader, 4);
            mesh.unbind(shader.shader);
            shader.unbind();
            if (!BlockModelJson.useIndices) SharedQuadIndexData.unbind();
        }
    }

}
