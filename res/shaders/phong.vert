#version 330

in vec3 inPosition;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProj;

void main() {
    gl_Position = uProj * uView * uModel * vec4(inPosition, 1.0);
}
