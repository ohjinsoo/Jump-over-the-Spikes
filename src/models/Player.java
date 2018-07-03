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

    private VertexArray mesh;
    private Texture texture;

    private boolean dead = false;
    private boolean midAir = true;
    private Point pos = new Point();
    private float delta = 0.0f;
    private final float JUMP_HEIGHT = 0.3f;
    private final float GRAVITY_ACCEL = 0.016f;
    private static float width = 0.79f;
    private static float height = 1.2f;

    public Player() {
        float[] vertices = new float[]{
                -width / 2.0f, -height / 2.0f, 0.2f,
                -width / 2.0f,  height / 2.0f, 0.2f,
                 width / 2.0f,  height / 2.0f, 0.2f,
                 width / 2.0f, -height / 2.0f, 0.2f
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

        texture = new Texture("res/player.png");
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

    public static float getWidth() {
        return width;
    }

    public static float getHeight() {
        return height;
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }
}
