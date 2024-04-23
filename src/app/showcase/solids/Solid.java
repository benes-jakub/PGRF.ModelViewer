package app.showcase.solids;

import de.javagl.obj.*;
import lwjglutils.OGLTexture;
import lwjglutils.OGLTexture2D;
import transforms.Mat4;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Solid {

    private List<SolidPart> parts = new ArrayList<>();
    private OGLTexture2D texture;

    public Solid() throws IOException {
        // Read an OBJ file
        InputStream objInputStream =
                new FileInputStream("./res/obj/cottage/cottage.obj");
        Obj originalObj = ObjReader.read(objInputStream);

        // Convert the OBJ into a "renderable" OBJ. 
        // (See ObjUtils#convertToRenderable for details)
        Obj obj = ObjUtils.convertToRenderable(originalObj);

        // The OBJ may refer to multiple MTL files using the "mtllib"
        // directive. Each MTL file may contain multiple materials.
        // Here, all materials (in form of Mtl objects) are collected.
        List<Mtl> allMtls = new ArrayList<>();
        for (String mtlFileName : obj.getMtlFileNames()) {
            InputStream mtlInputStream =
                    new FileInputStream("./res/obj/cottage/" + mtlFileName);
            allMtls.addAll(MtlReader.read(mtlInputStream));
        }

        // Split the OBJ into multiple parts. Each key of the resulting
        // map will be the name of one material. Each value will be 
        // an OBJ that contains the OBJ data that has to be rendered
        // with this material.
        Map<String, Obj> materialGroups = ObjSplitting.splitByMaterialGroups(obj);

        for (Map.Entry<String, Obj> entry : materialGroups.entrySet()) {
            String materialName = entry.getKey();
            Obj materialGroup = entry.getValue();
            materialGroup = ObjUtils.convertToRenderable(materialGroup);

            // Find the MTL that defines the material with the current name
            Mtl mtl = findMtlForName(allMtls, materialName);

            float[] texCoords = ObjData.getTexCoordsArray(materialGroup, 2);


            // Render the current material group with this material:
            parts.add(
                    new SolidPart(
                            materialName,
                            getVbFromObj(materialGroup),
                            getIbFromObj(materialGroup),
                            mtl,
                            texCoords.length > 0
                    )
            );
        }

        texture = new OGLTexture2D("./obj/cottage/cottage_diffuse.png");
    }

    public void draw(int shader) {
        int uColor = glGetUniformLocation(shader, "uColor");
        glUniform3f(uColor, 0.5f, 0, 0);

        for (SolidPart part : parts) {
            int uModel = glGetUniformLocation(shader, "uModel");
            glUniformMatrix4fv(uModel, false, part.getModel().floatArray());

            FloatTuple ambientColor = part.getMaterial().getKa();
            FloatTuple diffuseColor = part.getMaterial().getKd();
            FloatTuple specularColor = part.getMaterial().getKs();

            int uLightAmbientColor = glGetUniformLocation(shader, "uLightAmbientColor");
            glUniform3f(uLightAmbientColor, ambientColor.getX(), ambientColor.getY(), ambientColor.getZ());

            int uLightDiffuseColor = glGetUniformLocation(shader, "uLightDiffuseColor");
            glUniform3f(uLightDiffuseColor, diffuseColor.getX(), diffuseColor.getY(), diffuseColor.getZ());

            int uLightSpecularColor = glGetUniformLocation(shader, "uLightSpecularColor");
            glUniform3f(uLightSpecularColor, specularColor.getX(), specularColor.getY(), specularColor.getZ());

            int uIsTexture = glGetUniformLocation(shader, "uIsTexture");
            glUniform1i(uIsTexture, part.isTexture() ? 1 : 0);

            texture.bind();

            part.getBuffers().draw(GL_TRIANGLES, shader);
        }

    }

    public SolidPart getPartByName(String name) {
        for (SolidPart part : parts) {
            if (part.getName().equals(name))
                return part;
        }

        return null;
    }

    private float[] getVbFromObj(Obj obj) {
        float[] vertices = ObjData.getVerticesArray(obj);
        float[] normals = ObjData.getNormalsArray(obj);
        float[] texCoords = ObjData.getTexCoordsArray(obj, 2);

        float[] vb = new float[vertices.length + normals.length + texCoords.length];
        int step = texCoords.length > 0 ? 8 : 6;

        int verticesCounter = 0;
        int normalsCounter = 0;
        int texCoordsCounter = 0;
        for (int i = 0; i < vb.length; i += step) {
            // vertices
            vb[i] = vertices[verticesCounter++];
            vb[i + 1] = vertices[verticesCounter++];
            vb[i + 2] = vertices[verticesCounter++];
            // normals
            vb[i + 3] = normals[normalsCounter++];
            vb[i + 4] = normals[normalsCounter++];
            vb[i + 5] = normals[normalsCounter++];

            if(texCoords.length > 0) {
                // textures
                vb[i + 6] = texCoords[texCoordsCounter++];
                vb[i + 7] = texCoords[texCoordsCounter++];
            }
        }

        return vb;
    }

    private int[] getIbFromObj(Obj obj) {
        return ObjData.getFaceVertexIndicesArray(obj, 3);
    }

    private static Mtl findMtlForName(Iterable<? extends Mtl> mtls, String name) {
        for (Mtl mtl : mtls) {
            if (mtl.getName().equals(name)) {
                return mtl;
            }
        }
        return null;
    }

    public void setModel(Mat4 model) {
        for (SolidPart part : parts) {
            part.setModel(model);
        }
    }
}
