#version 330

in vec3 inPosition;
in vec3 inNormal;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProj;

uniform vec3 uLightPosition;
uniform vec3 uCameraPosition;

out vec3 normal;
out vec3 lightVector;

void main() {
    vec4 worldPosition = uModel * vec4(inPosition, 1.0);
    gl_Position = uProj * uView * worldPosition;

    // Calculate light vector in world space
    lightVector = uLightPosition - worldPosition.xyz;

    // Transform normal - model
    normal = transpose(inverse(mat3(uModel))) * inNormal;
}
