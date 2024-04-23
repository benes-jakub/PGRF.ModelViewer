package app.solids;

import lwjglutils.OGLBuffers;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform3f;

public class Axis extends Solid{

    public Axis(int shader) {
        super(shader);
        this.topology = GL_LINES;

        // Vb
        float[] vb = {
                // x
                0.f, 0.f, 0.f,   1.0f, 0.0f, 0.0f,
                1.f, 0.f, 0.f,   1.0f, 0.0f, 0.0f,
                // y
                0.f, 0.f, 0.f,   0.0f, 1.0f, 0.0f,
                0.f, 1.f, 0.f,   0.0f, 1.0f, 0.0f,
                // z
                0.f, 0.f, 0.f,   0.0f, 0.0f, 1.0f,
                0.f, 0.f, 1.f,   0.0f, 0.0f, 1.0f,
        };

        // Ib
        int[] ib = {
                0, 1,
                2, 3,
                4, 5
        };

        // Buffers
        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 3),
                new OGLBuffers.Attrib("inColor", 3),
        };
        buffers = new OGLBuffers(vb, attributes, ib);
    }

    @Override
    protected void setUniforms() {
        int uView = glGetUniformLocation(shader, "uColor");
        glUniform3f(uView, 1.f, 0.f, 0.f);
    }
}
