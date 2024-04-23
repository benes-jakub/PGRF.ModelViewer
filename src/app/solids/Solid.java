package app.solids;

import lwjglutils.OGLBuffers;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Vec3D;

import static org.lwjgl.opengl.GL20.*;


public class Solid {
    protected int shader;
    protected int topology;
    protected OGLBuffers buffers;
    protected Mat4 model;

    public Solid(int shader) {
        this.shader = shader;
        this.model = new Mat4Identity();
    }

    public void draw(Light light, Vec3D cameraPosition) {
        setUniforms(light, cameraPosition);
        this.buffers.draw(topology, shader);
    }

    public int getShader() {
        return shader;
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
    };

    public void setModel(Mat4 model) {
        this.model = model;
    }
}
