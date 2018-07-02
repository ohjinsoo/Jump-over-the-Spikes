package models;

import graphics.Texture;
import graphics.VertexArray;
import math.Matrix;
import math.Point;

public class Spikes {

    private Point pos = new Point();
    private static float width = 3f, height = 1.0f;

    private static Texture texture;
    private Matrix ml_matrix;
    private static VertexArray mesh;

    public static void create() {
        float[] vertices = new float[]{
                0.0f , 0.0f  , 0.1f,
                0.0f , height, 0.1f,
                width, height, 0.1f,
                width, 0.0f  , 0.1f
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

        texture = new Texture("res/spikes.png");
        mesh = new VertexArray(vertices, indices, tcs);
    }

    public Spikes(float x) {
        pos.x = x;
        pos.y = -3.75f;
        ml_matrix = Matrix.translate(pos);
    }

    public float getX() {
        return pos.x;
    }

    public float getY() {
        return pos.y;
    }

    public static VertexArray getMesh() {
        return mesh;
    }

    public static Texture getTexture() {
        return texture;
    }

    public static float getWidth() {
        return width;
    }

    public static float getHeight() {
        return height;
    }

    public Matrix getModelMatrix() {
        return ml_matrix;
    }
}
