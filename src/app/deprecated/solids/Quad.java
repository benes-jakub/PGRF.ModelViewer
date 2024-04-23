package app.deprecated.solids;

import lwjglutils.OGLBuffers;
import transforms.Vec3D;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class Quad extends Solid{
    public Quad(int shader) {
        super(shader);
        this.topology = GL_TRIANGLES;
        this.baseColor = new Vec3D(0.7f, 0.7f, 0.7f);

        // Vb
        float[] vb = {
                1.f,  1.f, 0.f,  0.f, 0.f, 1.f,
                -1.f, -1.f, 0.f, 0.f, 0.f, 1.f,
                1.f, -1.f, 0.f,  0.f, 0.f, 1.f,

                1.f,  1.f, 0.f,  0.f, 0.f, 1.f,
                -1.f,  1.f, 0.f, 0.f, 0.f, 1.f,
                -1.f, -1.f, 0.f, 0.f, 0.f, 1.f,
        };

        // Ib
        int[] ib = {
                0, 1, 2,
                3, 4, 5
        };

        // Buffers
        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 3),
                new OGLBuffers.Attrib("inNormal", 3),
        };
        buffers = new OGLBuffers(vb, attributes, ib);
    }
}
