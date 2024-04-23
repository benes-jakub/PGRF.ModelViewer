package app.deprecated.solids;

import lwjglutils.OGLBuffers;
import transforms.Vec3D;

import java.io.IOException;
import static org.lwjgl.opengl.GL20.*;

public class Dragon extends Solid{
    public Dragon(int shader) throws IOException {
        super(shader);
        this.baseColor = new Vec3D(0.7f, 0.2f, 0.2f);
        this.topology = GL_TRIANGLES;

        loadObj("./res/obj/dragon/dragon.obj");
        float[] vb = getVbFromObj();
        int[] ib = getIbFromObj();

        // Buffers
        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 3),
                new OGLBuffers.Attrib("inNormal", 3),
        };
        buffers = new OGLBuffers(vb, attributes, ib);
    }
}
