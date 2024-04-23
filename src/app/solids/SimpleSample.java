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
        float[] texCoords = ObjData.getTexCoordsArray(obj, 2);
        float[] normals = ObjData.getNormalsArray(obj);

        // Buffers
        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 3),
        };
        buffers = new OGLBuffers(vertices, attributes, indices);
    }

    @Override
    protected void setUniforms(Light light, Vec3D cameraPosition) {
        super.setUniforms(light, cameraPosition);
        int uColor = glGetUniformLocation(shader, "uColor");
        glUniform3f(uColor, 1.f, 0.f, 0.f);
    }
}
