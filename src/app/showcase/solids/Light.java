package app.showcase.solids;

import transforms.Vec3D;

public class Light {
    private Vec3D position;
    private Vec3D diffuseColor;
    private Vec3D specularColor;

    public Light(Vec3D position, Vec3D diffuseColor, Vec3D specularColor) {
        this.position = position;
        this.diffuseColor = diffuseColor;
        this.specularColor = specularColor;
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
