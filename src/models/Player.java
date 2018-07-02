package models;

import graphics.Shader;
import graphics.Texture;
import graphics.VertexArray;
import inputs.Input;
import math.Matrix;
import math.Point;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class Player {

    private float SIZE = 1.0f;
    private VertexArray mesh;
    private Texture texture;

    private boolean midAir = true;
    private Point pos = new Point();
    private float delta = 0.0f;

    public Player() {
        float[] vertices = new float[]{
                -SIZE / 2.0f, -SIZE / 2.0f, 1.0f,
                -SIZE / 2.0f,  SIZE / 2.0f, 1.0f,
                 SIZE / 2.0f,  SIZE / 2.0f, 1.0f,
                 SIZE / 2.0f, -SIZE / 2.0f, 1.0f
        };

        byte[] indices = new byte[] {
                0, 1, 2,
                2, 3, 0
        };

        float[] tcs = new float[] {
                0, 1,
                0, 0,
                1, 0,
                1, 1
        };

        texture = new Texture("res/square.png");
        mesh = new VertexArray(vertices, indices, tcs);
    }

    public void update() {
        pos.y -= delta;

        if (Input.isKeyDown(GLFW_KEY_SPACE) && !midAir) {
            delta = -0.2f;
            midAir = true;
        }
        else
            gravity();

    }

    private void gravity() {
        if (pos.y >= -3.3f)
            delta += 0.01f;
        else {
            midAir = false;
            delta = 0;
        }
    }

    public void render() {
        Shader.PLAYER.enable();
        Shader.PLAYER.setUniformMat4f("ml_matrix", Matrix.translate(pos));
        texture.bind();
        mesh.render();
        Shader.PLAYER.disable();
    }

    public float getX() {
        return pos.x;
    }
}
