package app.showcase;


import app.showcase.solids.Light;
import app.showcase.solids.Solid;
import lwjglutils.ShaderUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import transforms.*;

import java.io.IOException;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;


public class Renderer extends AbstractRenderer {

    private int phongShader;

    // Camera
    private Camera camera;
    double ox, oy;
    boolean mouseButton1 = false;
    private Mat4 proj;

    private Solid solid;
    private Light light;

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

        glUseProgram(phongShader);
        setGlobalUniforms(phongShader);

        solid.setModel(
                new Mat4Scale(0.007f, 0.007f, 0.007f)
//                        .mul(new Mat4RotX(Math.toRadians(90)))
        );

        solid.draw(phongShader);
    }

    private void initSolids() {
        light = new Light(
                new Vec3D(0.5f, 0.5f, 2.7f),
                new Vec3D(0.5, 0.5, 0.5),
                new Vec3D(1., 1., 1.)
        );

        try {
            solid = new Solid("./res/obj/cottage/cottage.obj", "./res/obj/cottage/", "./obj/cottage/cottage_diffuse.png");
//            solid = new Solid("./res/obj/skull/12140_Skull_v3_L2.obj", "./res/obj/skull/", "./obj/skull/Skull.jpg");
//            solid = new Solid("./res/obj/airplane/11803_Airplane_v1_l1.obj", "./res/obj/airplane/", "./obj/airplane/11803_Airplane_body_diff.jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initShaders() {
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

    private void setGlobalUniforms(int shader) {
        int uView = glGetUniformLocation(shader, "uView");
        glUniformMatrix4fv(uView, false, camera.getViewMatrix().floatArray());

        int uProj = glGetUniformLocation(shader, "uProj");
        glUniformMatrix4fv(uProj, false, proj.floatArray());

        int uLightPosition = glGetUniformLocation(shader, "uLightPosition");
        glUniform3f(uLightPosition, (float)light.getPosition().getX(), (float)light.getPosition().getY(), (float)light.getPosition().getZ());

        int uLightDiffuseColor = glGetUniformLocation(shader, "uLightDiffuseColor");
        glUniform3f(uLightDiffuseColor, (float)light.getDiffuseColor().getX(), (float)light.getDiffuseColor().getY(), (float)light.getDiffuseColor().getZ());

        int uLightSpecularColor = glGetUniformLocation(shader, "uLightSpecularColor");
        glUniform3f(uLightSpecularColor, (float)light.getSpecularColor().getX(), (float)light.getSpecularColor().getY(), (float)light.getSpecularColor().getZ());

        int uCameraPosition = glGetUniformLocation(shader, "uCameraPosition");
        glUniform3f(uCameraPosition, (float)camera.getEye().getX(), (float)camera.getEye().getY(), (float)camera.getEye().getZ());

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