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

    private boolean dead = false;
    private boolean midAir = true;
    private Point pos = new Point();
    private float delta = 0.0f;
    private final float JUMP_HEIGHT = 0.27f;
    private final float GRAVITY_ACCEL = 0.016f;

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
    public void die() {
        delta = -JUMP_HEIGHT;
        dead = true;
    }
    public void update() {
        pos.y -= delta;

        if (Input.isKeyDown(GLFW_KEY_SPACE) && !midAir) {
            delta = -JUMP_HEIGHT;
            midAir = true;
        }
        else
            gravity();

    }

    private void gravity() {
        if (pos.y >= -3.10f)
            delta += GRAVITY_ACCEL;
        else if (!dead) {
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

    public float getSize() {
        return SIZE;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }
}
