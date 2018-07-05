import graphics.Shader;
import inputs.Input;
import levels.Level;
import math.Matrix;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import javax.swing.*;

import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.CLOSED_OPTION;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class Game implements Runnable {
    private Thread thread;
    private boolean running = false;
    private static GLFWErrorCallback errorCallback;
    private static long window;
    private static int winWidth = 1280;
    private static int winHeight = 720;

    private Level level;

    /*
        Error Handler and creates the window.
    */

    public void start() {
        running = true;
        thread = new Thread(this, "Game");
        thread.start();
    }

    private void init() {
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errorCallback);

        if (!glfwInit())
            throw new IllegalStateException("GLFW Init Failed");

        window = glfwCreateWindow(
                winWidth, winHeight, "Display", MemoryUtil.NULL, MemoryUtil.NULL
        );

        if (window == MemoryUtil.NULL)
            throw new IllegalStateException("Window Failed");

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - winWidth) / 2, (vidmode.height() - winHeight) / 2 );

        glfwSetKeyCallback(window, new Input());
        glfwMakeContextCurrent(window);

        //  Forces a frame limit for 72 fps.
        glfwSwapInterval(2);
        glfwShowWindow(window);
        GL.createCapabilities();

        glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glActiveTexture(GL_TEXTURE1);

        System.out.println("OpenGL: " + glGetString(GL_VERSION));
        Shader.loadAll();

        Matrix pr_matrix = Matrix.orthographic(-10.0f, 10.0f, -10.0f * 9.0f / 16.0f, 10.0f * 9.0f / 16.0f, -1.0f, 1.0f);
        Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.BG.setUniform1i("tex", 1);

        Shader.PLAYER.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.PLAYER.setUniform1i("tex", 1);

        Shader.SPIKES.setUniformMat4f("pr_matrix", pr_matrix);
        Shader.SPIKES.setUniform1i("tex", 1);

        level = new Level();
    }

    public void run() {
        init();

        long lastTime = System.currentTimeMillis();
        double ns = 1000000000.0 / 60.0;
        double delta = 0.0;
        int updates = 0;
        int frames = 0;
        long timer = System.currentTimeMillis();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            render();
            if (delta >= 1.0) {
                running = update();
                if (!running) {
                    break;
                }
                updates++;
                delta--;
            }
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println(updates + " updates, " + frames + " fps");
                frames = 0;
                updates = 0;
            }

            if (glfwWindowShouldClose(window))
                running = false;
        }

        System.out.println("closing!!!");
        glfwDestroyWindow(window);
        glfwTerminate();
        System.exit(0);
    }

    private boolean update() {
        glfwPollEvents();
        int res = level.update();
        if (res == JOptionPane.OK_OPTION) {
            level = new Level();
        }
        else if (res == CLOSED_OPTION || res == CANCEL_OPTION) {
            return false;
        }

        return true;
    }

    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        level.render();

        int err = glGetError();
        if (err != GL_NO_ERROR)
            System.out.println(err);

        glfwSwapBuffers(window);
    }

    public static void main(String[] args) {
        new Game().start();
    }
}
