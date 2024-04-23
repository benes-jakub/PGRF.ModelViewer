package app.deprecated.solids;

import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjReader;
import de.javagl.obj.ObjUtils;
import lwjglutils.OGLBuffers;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Vec3D;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL20.*;


public class Solid {
    protected int shader;
    protected int topology;
    protected OGLBuffers buffers;
    protected Mat4 model;
    protected Vec3D baseColor;

    private Obj obj;

    public Solid(int shader) {
        this.shader = shader;
        this.model = new Mat4Identity();
    }

    public void draw(Light light, Vec3D cameraPosition) {
        setUniforms(light, cameraPosition);
        this.buffers.draw(topology, shader);
    }

    protected void setUniforms(Light light, Vec3D cameraPosition){
        int uModel = glGetUniformLocation(shader, "uModel");
        glUniformMatrix4fv(uModel, false, model.floatArray());
        if(light == null) return;

        int uLightPosition = glGetUniformLocation(shader, "uLightPosition");
        glUniform3f(uLightPosition, (float)light.getPosition().getX(), (float)light.getPosition().getY(), (float)light.getPosition().getZ());

        int uLightDiffuseColor = glGetUniformLocation(shader, "uLightDiffuseColor");
        glUniform3f(uLightDiffuseColor, (float)light.getDiffuseColor().getX(), (float)light.getDiffuseColor().getY(), (float)light.getDiffuseColor().getZ());

        int uLightSpecularColor = glGetUniformLocation(shader, "uLightSpecularColor");
        glUniform3f(uLightSpecularColor, (float)light.getSpecularColor().getX(), (float)light.getSpecularColor().getY(), (float)light.getSpecularColor().getZ());

        int uCameraPosition = glGetUniformLocation(shader, "uCameraPosition");
        glUniform3f(uCameraPosition, (float)cameraPosition.getX(), (float)cameraPosition.getY(), (float)cameraPosition.getZ());

        int uBaseColor = glGetUniformLocation(shader, "uBaseColor");
        glUniform3f(uBaseColor, (float)baseColor.getX(), (float)baseColor.getY(), (float)baseColor.getZ());
    };

    protected void loadObj(String path) throws IOException  {
        InputStream objInputStream = new FileInputStream(path);
        this.obj = ObjReader.read(objInputStream);
        this.obj = ObjUtils.convertToRenderable(obj);
    }

    protected float[] getVbFromObj(){
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

        return vb;
    }

    protected int[] getIbFromObj() {
        return ObjData.getFaceVertexIndicesArray(obj, 3);
    }

    public int getShader() {
        return shader;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }
}
