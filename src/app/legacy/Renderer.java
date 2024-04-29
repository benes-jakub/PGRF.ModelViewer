package app.legacy;


import app.showcase.solids.SolidPart;
import de.javagl.obj.*;
import lwjglutils.OGLTexture2D;
import org.lwjgl.glfw.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static app.legacy.utils.GluUtils.gluLookAt;
import static app.legacy.utils.GluUtils.gluPerspective;
import static org.lwjgl.opengl.GL20.*;


public class Renderer extends AbstractRenderer {
    private Obj obj;
    private List<Mtl> allMtls = new ArrayList<>();
    private OGLTexture2D texture;

    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);

        try {
            // Obj
            obj = ObjReader.read(new FileInputStream("./res/obj/airplane/11803_Airplane_v1_l1.obj"));
            allMtls.addAll(MtlReader.read(new FileInputStream("./res/obj/airplane/11803_Airplane_v1_l1.mtl")));

            // Textures
            texture = new OGLTexture2D("./obj/airplane/11803_Airplane_body_diff.jpg");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        obj = ObjUtils.convertToRenderable(obj);
    }

    @Override
    public void display() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluPerspective(45, width / (float) height, 0.1f, 100.0f);

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        gluLookAt(2, -2, 2,0,0,0,0,0, 1);
        glPushMatrix();

        drawAxis();

        // Modelovací matice tělesa
        glScalef(0.001f, 0.001f, 0.001f);

        drawObj();
    }

    private void drawObj() {
        glColor3f(0.5f, 0.5f, 0.5f);
        texture.bind();

        Map<String, Obj> materialGroups = ObjSplitting.splitByMaterialGroups(obj);
        for (Map.Entry<String, Obj> entry : materialGroups.entrySet()) {
            String materialName = entry.getKey();
            Obj materialGroup = entry.getValue();

            Mtl mtl = findMtlForName(allMtls, materialName);
            if(mtl == null) continue;

            for(int i = 0; i < materialGroup.getNumFaces(); i += 1) {
                ObjFace face = materialGroup.getFace(i);
                // Position
                FloatTuple position_a = materialGroup.getVertex(face.getVertexIndex(0));
                FloatTuple position_b = materialGroup.getVertex(face.getVertexIndex(1));
                FloatTuple position_c = materialGroup.getVertex(face.getVertexIndex(2));
                // uv
                FloatTuple texCoord_a = materialGroup.getTexCoord(face.getTexCoordIndex(0));
                FloatTuple texCoord_b = materialGroup.getTexCoord(face.getTexCoordIndex(1));
                FloatTuple texCoord_c = materialGroup.getTexCoord(face.getTexCoordIndex(2));

                glBegin(GL_TRIANGLES);

                glTexCoord2f(texCoord_a.getX(), texCoord_a.getY());
                glVertex3f(position_a.getX(), position_a.getY(), position_a.getZ());

                glTexCoord2f(texCoord_b.getX(), texCoord_b.getY());
                glVertex3f(position_b.getX(), position_b.getY(), position_b.getZ());

                glTexCoord2f(texCoord_c.getX(), texCoord_c.getY());
                glVertex3f(position_c.getX(), position_c.getY(), position_c.getZ());

                glEnd();
            }
        }
    }

    private void drawAxis() {
        glBegin(GL_LINES);
        // x
        glColor3f(1, 0,0);
        glVertex3f(0, 0, 0);
        glVertex3f(1, 0, 0);
        // y
        glColor3f(0, 1,0);
        glVertex3f(0, 0, 0);
        glVertex3f(0, 1, 0);
        // z
        glColor3f(0, 0,1);
        glVertex3f(0, 0, 0);
        glVertex3f(0, 0, 1);
        glEnd();
    }

    private Mtl findMtlForName(Iterable<? extends Mtl> mtls, String name) {
        for (Mtl mtl : mtls) {
            if (mtl.getName().equals(name)) {
                return mtl;
            }
        }
        return null;
    }

    private GLFWKeyCallback keyCallback = new GLFWKeyCallback() {
        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {

        }
    };

    private GLFWWindowSizeCallback wsCallback = new GLFWWindowSizeCallback() {
        @Override
        public void invoke(long window, int w, int h) {
        }
    };

    private GLFWMouseButtonCallback mbCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {

        }

    };

    private GLFWCursorPosCallback cpCallbacknew = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double x, double y) {

        }
    };

    private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double dx, double dy) {

        }
    };


    @Override
    public GLFWKeyCallback getKeyCallback() {
        return keyCallback;
    }

    @Override
    public GLFWWindowSizeCallback getWsCallback() {
        return wsCallback;
    }

    @Override
    public GLFWMouseButtonCallback getMouseCallback() {
        return mbCallback;
    }

    @Override
    public GLFWCursorPosCallback getCursorCallback() {
        return cpCallbacknew;
    }

    @Override
    public GLFWScrollCallback getScrollCallback() {
        return scrollCallback;
    }
}