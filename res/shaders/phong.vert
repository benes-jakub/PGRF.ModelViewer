#version 330

in vec3 inPosition;
in vec3 inNormal;
in vec2 inTexCoord;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProj;

uniform vec3 uLightPosition;
uniform vec3 uCameraPosition;

out vec3 normal;
out vec3 lightVector;
out vec3 cameraVector;

out vec2 texCoord;

void main() {
    vec4 worldPosition = uModel * vec4(inPosition, 1.0);

    // Calculate light vector in world space
    lightVector = uLightPosition - worldPosition.xyz;
    // Calculate vector in view space
    cameraVector = uCameraPosition - worldPosition.xyz;


    // Transform normal - model
    normal = inverse(transpose(mat3(uModel))) * inNormal;

    // Final position of vertex
    gl_Position = uProj * uView * worldPosition;

    texCoord = inTexCoord;
}
