#version 330

in vec3 normal;
in vec3 lightVector;
in vec3 cameraVector;

uniform vec3 uBaseColor;

uniform vec3 uLightDiffuseColor;
uniform vec3 uLightSpecularColor;

out vec4 outColor;

void main() {
    vec3 unitNormal = normalize(normal);
    vec3 unitLightVector = normalize(lightVector);
    vec3 unitCameraVector = normalize(cameraVector);

    // Calculate diffuse
    float nDotL = max(0.f, dot(unitNormal, unitLightVector));

    // Calculate specula
    vec3 reflection = normalize(reflect(-unitLightVector, unitNormal));
    float rDotV = max(0.f, dot(reflection, unitCameraVector));

    // Set phong
    vec3 ambient = vec3(.1f, .1f, .1f);
    vec3 diffuse = nDotL * uLightDiffuseColor;
    vec3 specular = (pow(rDotV, 8.f)) * uLightSpecularColor;

    outColor = vec4((ambient + diffuse + specular) * uBaseColor, 1.f);
//    outColor = vec4(unitNormal, 1.f);
}
