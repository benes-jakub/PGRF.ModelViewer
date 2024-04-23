package app.solids;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import lwjglutils.OGLBuffers;
import transforms.Vec3D;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;

public class SimpleSample extends Solid{
    public SimpleSample(int shader) throws IOException {
        super(shader);
        this.topology = GL_TRIANGLES;

        // Read an OBJ file
        InputStream objInputStream =
                new FileInputStream("./res/obj/simpleSample/simpleSample.obj");
        Obj obj = ObjReader.read(objInputStream);
        obj = ObjUtils.convertToRenderable(obj);

        // Obtain the data from the OBJ, as direct buffers:
        int[] indices = ObjData.getFaceVertexIndicesArray(obj, 3);
        float[] vertices = ObjData.getVerticesArray(obj);
        float[] normals  = ObjData.getNormalsArray(obj);

        float[] vb = new float[vertices.length + normals.length];
        for(int i = 0; i < vb.length; i += 6) {
            // vertices
            vb[i] = vertices[i / 2];
            vb[i + 1] = vertices[i / 2 + 1];
            vb[i + 2] = vertices[i / 2 + 2];
            // normals
            vb[i + 3] = normals[i / 2];
            vb[i + 4] = normals[i / 2 + 1];
            vb[i + 5] = normals[i / 2 + 2];
        }

        // Buffers
        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 3),
                new OGLBuffers.Attrib("inNormal", 3),
        };
        buffers = new OGLBuffers(vb, attributes, indices);
    }
}
