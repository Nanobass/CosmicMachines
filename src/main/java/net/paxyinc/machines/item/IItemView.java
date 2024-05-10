package net.paxyinc.machines.item;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;

public interface IItemView {

    void initialize();

    void render(Camera camera, Matrix4 projViewTrans);

}
