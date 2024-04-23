package app;

import app.solids.Axis;
import app.solids.Solid;
import lwjglutils.OGLBuffers;
import lwjglutils.ShaderUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4PerspRH;
import transforms.Vec3D;

import static org.lwjgl.opengl.GL20.*;

/**
 * @author PGRF FIM UHK
 * @version 2.0
 * @since 2019-09-02
 */
public class Renderer extends AbstractRenderer {
    private int vertexColorShader;
    private Camera camera;
    private Mat4 proj;

    private Axis axis;

    @Override
    public void init() {
        initShaders();
        initCamera();
        initSolids();
    }

    @Override
    public void display() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        renderSolid(axis);
    }

    private void renderSolid(Solid solid) {
        glUseProgram(solid.getShader());
        setGlobalUniforms(solid.getShader());
        solid.draw();
    }

    private void initSolids() {
        axis = new Axis(vertexColorShader);
    }

    private void initCamera() {
        camera = new Camera()
                .withPosition(new Vec3D(1f, -2f, 1.5f))
                .withAzimuth(Math.toRadians(120))
                .withZenith(Math.toRadians(-25))
                .withFirstPerson(false);

        proj = new Mat4PerspRH(Math.PI / 4, height / (float)width, 0.1f, 100.f);
    }

    private void initShaders() {
        vertexColorShader = ShaderUtils.loadProgram("/shaders/vertexColor");
    }

    private void setGlobalUniforms(int shader) {
        int uView = glGetUniformLocation(shader, "uView");
        glUniformMatrix4fv(uView, false, camera.getViewMatrix().floatArray());

        int uProj = glGetUniformLocation(shader, "uProj");
        glUniformMatrix4fv(uProj, false, proj.floatArray());
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