#version 330

in vec2 inPosition;
in vec3 inColor;

uniform mat4 uView;
uniform mat4 uProj;

out vec3 color;

void main() {
    color = inColor;
    gl_Position = vec4(inPosition, 1., 1.0);
}
