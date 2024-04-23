package app.solids;

import lwjglutils.OGLBuffers;
import transforms.Mat4;
import transforms.Mat4Identity;

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

    public void draw(Light light) {
        setUniforms(light);
        this.buffers.draw(topology, shader);
    }

    public int getShader() {
        return shader;
    }

    protected void setUniforms(Light light){
        int uModel = glGetUniformLocation(shader, "uModel");
        glUniformMatrix4fv(uModel, false, model.floatArray());
        if(light == null) return;

        int uLightPosition = glGetUniformLocation(shader, "uLightPosition");
        glUniform3f(uLightPosition, (float)light.getPosition().getX(), (float)light.getPosition().getY(), (float)light.getPosition().getZ());

        int uLightColor = glGetUniformLocation(shader, "uLightColor");
        glUniform3f(uLightColor, (float)light.getColor().getX(), (float)light.getColor().getY(), (float)light.getColor().getZ());
    };

    public void setModel(Mat4 model) {
        this.model = model;
    }
}
