package app.showcase.solids;

import de.javagl.obj.Mtl;
import lwjglutils.OGLBuffers;
import transforms.Mat4;
import transforms.Mat4Identity;

public class SolidPart {
    private String name;
    private float[] vb;
    private int[] ib;
    private Mtl material;
    private Mat4 model;
    private OGLBuffers buffers;
    private boolean isTexture;

    public SolidPart(String name, float[] vb, int[] ib, Mtl material, boolean isTexture) {
        this.name = name;
        this.vb = vb;
        this.ib = ib;
        this.material = material;
        this.model = new Mat4Identity();
        this.isTexture = isTexture;

        // Buffers
        OGLBuffers.Attrib[] attributes = new OGLBuffers.Attrib[isTexture ? 3 : 2];

        attributes[0] = new OGLBuffers.Attrib("inPosition", 3);
        attributes[1] = new OGLBuffers.Attrib("inNormal", 3);
        if(isTexture)
            attributes[2] = new OGLBuffers.Attrib("inTexCoord", 2);

        buffers = new OGLBuffers(vb, attributes, ib);
    }

    public String getName() {
        return name;
    }

    public Mtl getMaterial() {
        return material;
    }

    public OGLBuffers getBuffers() {
        return buffers;
    }

    public Mat4 getModel() {
        return model;
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

    public boolean isTexture() {
        return isTexture;
    }
}
