package app.deprecated;

import app.deprecated.solids.*;
import lwjglutils.ShaderUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import transforms.*;

import java.io.IOException;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;


public class Renderer extends AbstractRenderer {

    // Shaders
    private int vertexColorShader, flatColorShader, phongShader;

    // Camera
    private Camera camera;
    double ox, oy;
    boolean mouseButton1 = false;
    private Mat4 proj;

    // Solids
    private Axis axis;
    private Light light;
    private Quad floor;

    private Dragon dragon;
    private Cottage cottage;

    // Solid animation
    private float dragonRotation = 0.f;

    @Override
    public void init() {
        glEnable(GL_DEPTH_TEST);

        initShaders();
        initCamera();
        initSolids();
    }

    @Override
    public void display() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        renderSolid(axis);
        renderSolid(light);
        //renderSolid(floor);

        dragon.setModel(
                new Mat4Scale(0.1f, 0.1f, 0.1f)
                .mul(new Mat4RotX(Math.toRadians(90)))
                .mul(new Mat4RotZ(Math.toRadians(dragonRotation)))
        );
        //renderSolid(dragon);
        dragonRotation += 0.2f;

        cottage.setModel(
                new Mat4Scale(0.07f, 0.07f, 0.07f)
                        .mul(new Mat4RotX(Math.toRadians(90)))
        );
        renderSolid(cottage);
    }

    private void initSolids() {
        try {
            floor = new Quad(phongShader);
            dragon = new Dragon(phongShader);
            cottage = new Cottage(phongShader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        floor.setModel(new Mat4Scale(2.f).mul(new Mat4Transl(0, 0, -0.2f)));


        axis = new Axis(vertexColorShader);
        light = new Light(flatColorShader,
                new Vec3D(0.5f, 0.5f, 2.7f),
                new Vec3D(0.5, 0.5,0.5),
                new Vec3D(1., 1.,1.));
    }

    private void initShaders() {
        vertexColorShader = ShaderUtils.loadProgram("/shaders/vertexColor");
        flatColorShader = ShaderUtils.loadProgram("/shaders/flatColor");
        phongShader = ShaderUtils.loadProgram("/shaders/phong");
    }

    private void initCamera() {
        camera = new Camera()
                .withPosition(new Vec3D(0, 0, 0))
                .withAzimuth(Math.toRadians(45))
                .withZenith(Math.toRadians(-25))
                .withFirstPerson(false)
                .withRadius(3);

        proj = new Mat4PerspRH(Math.PI / 4, height / (float) width, 0.1f, 100.f);
    }

    private void renderSolid(Solid solid) {
        glUseProgram(solid.getShader());
        setGlobalUniforms(solid.getShader());
        solid.draw(light, camera.getEye());
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
            mouseButton1 = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;

            if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                mouseButton1 = true;
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xBuffer, yBuffer);
                ox = xBuffer.get(0);
                oy = yBuffer.get(0);
            }

            if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_RELEASE) {
                mouseButton1 = false;
                DoubleBuffer xBuffer = BufferUtils.createDoubleBuffer(1);
                DoubleBuffer yBuffer = BufferUtils.createDoubleBuffer(1);
                glfwGetCursorPos(window, xBuffer, yBuffer);
                double x = xBuffer.get(0);
                double y = yBuffer.get(0);
                camera = camera.addAzimuth((double) Math.PI * (ox - x) / width)
                        .addZenith((double) Math.PI * (oy - y) / width);
                ox = x;
                oy = y;
            }
        }

    };

    private GLFWCursorPosCallback cpCallbacknew = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double x, double y) {
            if (mouseButton1) {
                camera = camera.addAzimuth((double) Math.PI * (ox - x) / width)
                        .addZenith((double) Math.PI * (oy - y) / width);
                ox = x;
                oy = y;
            }
        }
    };

    private GLFWScrollCallback scrollCallback = new GLFWScrollCallback() {
        @Override
        public void invoke(long window, double dx, double dy) {
            if (dy < 0)
                camera = camera.mulRadius(0.9f);
            else
                camera = camera.mulRadius(1.1f);
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