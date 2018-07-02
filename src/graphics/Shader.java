package graphics;

import math.Matrix;
import math.Point;
import utils.ShaderUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static final int VERTEX_ATTRIB = 0;
    public static final int TCOORD_ATTRIB = 1;

    public static Shader BG, PLAYER, SPIKES;

    private boolean enabled = false;
    private final int ID;
    private Map<String, Integer> locationMap = new HashMap<String, Integer>();

    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAll() {
        BG = new Shader("shaders/bg.vert", "shaders/bg.frag");
        PLAYER = new Shader("shaders/player.vert", "shaders/player.frag");
        SPIKES = new Shader("shaders/spikes.vert", "shaders/spikes.frag");
    }

    public int getUniform(String name) {
        if (locationMap.containsKey(name))
            return locationMap.get(name);

        int uniform = glGetUniformLocation(ID, name);

        if (uniform == -1)
            System.err.println("Couldn't find uniform variable '" + name + "'!");
        else
            locationMap.put(name, uniform);

        return uniform;
    }

    public void setUniform1i(String name, int value) {
        if (!enabled)
            enable();
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        if (!enabled)
            enable();
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float v1, float v2) {
        if (!enabled)
            enable();
        glUniform2f(getUniform(name), v1, v2);
    }

    public void setUniform3f(String name, Point point) {
        if (!enabled)
            enable();
        glUniform3f(getUniform(name), point.x, point.y, point.z);
    }

    public void setUniformMat4f(String name, Matrix matrix) {
        if (!enabled)
            enable();
        glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
    }

    public void enable(){
        glUseProgram(ID);
        enabled = true;
    }

    public void disable(){
        glUseProgram(0);
        enabled = false;
    }
}
