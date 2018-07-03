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

    private static final float SPEEDLIMIT = 0.155f;
    private static final float HITBOX_SHRINK = 0.1f;
    private Texture bgTexture;
    private VertexArray background;
    private Player player;
    private final int SIZE = 20;
    private Spikes[] spikes = new Spikes[SIZE];

    private int index = 0;
    private float prevDistance = 0.0f;
    private int xScroll = 0;
    private int map = 0;

    private float speedMultiplier = 0.11f;
    private int updateMultiplier = 50;

    private boolean gameOver = false;
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
            float distanceApart = 10f * ((rand.nextFloat() / 4.0f) + 0.6f) + prevDistance;
            spikes[i] = new Spikes(distanceApart);
            prevDistance = distanceApart;
        }
    }

    private void updateSpikes() {
        float distanceApart = 10f * ((rand.nextFloat() / 4.0f) + 0.6f) + prevDistance;
        spikes[index % SIZE] = new Spikes(distanceApart);
        prevDistance = distanceApart;
        index = (index + 1) % SIZE;

    }

    private void speedUp() {
        speedMultiplier /= 0.9999;
        if (xScroll % 20 == 0) {
        }
    }

    public int update() {
        if (!gameOver && !collision()) {
            System.out.println("Scroll: " + xScroll);
            System.out.println("speed: " + speedMultiplier);

            xScroll--;
            if (speedMultiplier < SPEEDLIMIT) {
                speedUp();
            }

            if (-xScroll % 120 == 0) map++;
            if (-xScroll > 400 && -xScroll % updateMultiplier == 0)
                updateSpikes();
        }
        else {
            if (!gameOver)
                deathAnimation();
            gameOver = true;
            if (player.getY() <= -10.0f)
               return UI.displayOptions("Your score was " + -xScroll + "! Would you like to restart?");
        }
        player.update();
        return 1;
}

    private void deathAnimation() {
        try {
            Thread.sleep(500);
        } catch(InterruptedException e) {
            System.out.println("Sleep got interrupted for some reason.");
        }

        player.die();
    }

    public void render() {
        bgTexture.bind();
        Shader.BG.enable();
        background.bind();
        for (int i = map; i < map + 8; i++) {
            Shader.BG.setUniformMat4f("vw_matrix", Matrix.translate(new Point(i * 10 + xScroll * 0.09f, 0.0f, 0.0f )));
            background.draw();
        }
        Shader.BG.disable();
        bgTexture.unbind();

        renderSpikes();
        player.render();
    }

    public void renderSpikes() {
        Shader.SPIKES.enable();
        Shader.SPIKES.setUniformMat4f("vw_matrix", Matrix.translate(new Point(xScroll * speedMultiplier, 0.0f, 0.0f)));
        Spikes.getTexture().bind();
        Spikes.getMesh().bind();

        for (int i = 0; i < SIZE; i++) {
            Shader.SPIKES.setUniformMat4f("ml_matrix", spikes[i].getModelMatrix());
            Spikes.getMesh().draw();
        }

        Spikes.getMesh().unbind();
        Spikes.getTexture().unbind();
    }

    private boolean collision() {
        for (int i = 0; i < 20; i++) {
            float playerX = -xScroll * speedMultiplier;
            float playerY = player.getY();

            float spikesX = spikes[i].getX();
            float spikesY = spikes[i].getY();

            float playerX0 = playerX - player.getSize() / 2;
            float playerX1 = playerX + player.getSize() / 2;
            float playerY0 = playerY - player.getSize() / 2;
            float playerY1 = playerY + player.getSize() / 2;

            float spikesX0 = spikesX;
            float spikesX1 = spikesX + Spikes.getWidth();
            float spikesY0 = spikesY;
            float spikesY1 = spikesY + Spikes.getHeight();

            if (playerX1 > spikesX0 + HITBOX_SHRINK && playerX0 < spikesX1 - HITBOX_SHRINK) {
                if (playerY1 > spikesY0 + HITBOX_SHRINK && playerY0 < spikesY1 - HITBOX_SHRINK) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void restart() {

    }
}
