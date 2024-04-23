#version 330

uniform vec3 uColor;

uniform vec3 uLightColor;

out vec4 outColor;

void main() {
    outColor = vec4(uColor, 1.0);
}
