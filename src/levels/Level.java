package levels;

import graphics.Shader;
import graphics.Texture;
import graphics.VertexArray;
import math.Matrix;
import math.Point;
import models.Player;
import models.Spikes;

import java.util.Random;

public class Level {

    private Texture bgTexture;
    private VertexArray background;
    private Player player;
    private final int SIZE = 20;
    private Spikes[] spikes = new Spikes[SIZE];

    private int index = 0;
    private float prevDistance = 20.0f;
    private int xScroll = 0;
    private int map = 0;

    private Random rand = new Random();

        public Level() {
        float[] vertices = new float[]{
            -10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
            -10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
              0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
              0.0f, -10.0f * 9.0f / 16.0f, 0.0f
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

        bgTexture = new Texture("res/bg.jpeg");
        background = new VertexArray(vertices, indices, tcs);
        player = new Player();

        createSpikes();
    }

    private void createSpikes() {
        Spikes.create();

        for (int i = 0; i < SIZE; i++) {
            float distanceApart = 22f * ((rand.nextFloat() / 2f) + 0.5f) + prevDistance;
            spikes[i] = new Spikes(distanceApart);
            prevDistance = distanceApart;
        }
    }

    private void updateSpikes() {
        float distanceApart = 20f * ((rand.nextFloat() / 2f) + 0.3f) + prevDistance;
        spikes[index % SIZE] = new Spikes(distanceApart);
        prevDistance = distanceApart;
        index = (index + 1) % SIZE;
    }

    public void update() {
        xScroll--;
        System.out.println("Scroll: " + xScroll);
        System.out.println("prev Distance: " + prevDistance);
        if (-xScroll % 88 == 0) map++;
        if (-xScroll > 800 && -xScroll % 140 == 0)
            updateSpikes();

        player.update();
}
    public void render() {
        bgTexture.bind();
        Shader.BG.enable();
        background.bind();
        for (int i = map; i < map + 8; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix.translate(new Point(i * 10 + xScroll * 0.12f, 0.0f, 0.0f )));
            background.draw();
        }
        Shader.BG.disable();
        bgTexture.unbind();

        renderSpikes();
        player.render();
    }

    public void renderSpikes() {
        Shader.SPIKES.enable();
        Shader.SPIKES.setUniformMat4f("vw_matrix", Matrix.translate(new Point(xScroll * 0.12f, 0.0f, 0.0f)));
        Spikes.getTexture().bind();
        Spikes.getMesh().bind();

        for (int i = 0; i < SIZE; i++) {
            Shader.SPIKES.setUniformMat4f("ml_matrix", spikes[i].getModelMatrix());
            Spikes.getMesh().draw();
        }

        Spikes.getMesh().unbind();
        Spikes.getTexture().unbind();
    }
}
