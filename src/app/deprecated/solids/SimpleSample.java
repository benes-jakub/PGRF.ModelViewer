package app.deprecated.solids;

import lwjglutils.OGLBuffers;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

public class SimpleSample extends Solid{
    public SimpleSample(int shader) throws IOException {
        super(shader);
        this.topology = GL_TRIANGLES;

        loadObj("./res/obj/simpleSample/simpleSample.obj");
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
