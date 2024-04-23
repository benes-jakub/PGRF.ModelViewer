package app.solids;

import lwjglutils.OGLBuffers;
import transforms.Mat4Transl;
import transforms.Vec3D;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform3f;

public class Light  extends Solid {
    private Vec3D position;
    private Vec3D diffuseColor;
    private Vec3D specularColor;

    public Light(int shader, Vec3D position, Vec3D diffuseColor, Vec3D specularColor) {
        super(shader);
        this.position = position;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
        this.topology = GL_TRIANGLES;

        // Model matrix - light position
        model = new Mat4Transl(position);

        // Vb
        float[] vb = {
                 .1f,  .1f, 0.f,
                -.1f, -.1f, 0.f,
                 .1f, -.1f, 0.f,

                 .1f,  .1f, 0.f,
                -.1f,  .1f, 0.f,
                -.1f, -.1f, 0.f,
        };

        // Ib
        int[] ib = {
                0, 1, 2,
                3, 4, 5
        };

        // Buffers
        OGLBuffers.Attrib[] attributes = {
                new OGLBuffers.Attrib("inPosition", 3)
        };
        buffers = new OGLBuffers(vb, attributes, ib);
    }

    @Override
    protected void setUniforms(Light light, Vec3D cameraPosition) {
        super.setUniforms(light, cameraPosition);
        int uColor = glGetUniformLocation(shader, "uColor");
        glUniform3f(uColor, (float)diffuseColor.getX(), (float)diffuseColor.getY(), (float)diffuseColor.getZ());
    }

    public Vec3D getPosition() {
        return position;
    }

    public Vec3D getDiffuseColor() {
        return diffuseColor;
    }

    public Vec3D getSpecularColor() {
        return specularColor;
    }
}
