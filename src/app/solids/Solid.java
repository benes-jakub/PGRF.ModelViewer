package app.solids;

import lwjglutils.OGLBuffers;


public class Solid {
    protected int shader;
    protected int topology;
    protected OGLBuffers buffers;

    public Solid(int shader) {
        this.shader = shader;
    }

    public void draw() {
        this.buffers.draw(topology, shader);
    }

    public int getShader() {
        return shader;
    }
}
