#version 330

in vec3 normal;
in vec3 lightVector;

uniform vec3 uLightColor;

out vec4 outColor;

void main() {
    // Calculate brightness
    vec3 unitNormal = normalize(normal);
    vec3 unitLightVector = normalize(lightVector);
    float nDotL = dot(unitNormal, unitLightVector);
    float brightness = max(nDotL, 0.f);

    // Set phong
    vec3 ambient = vec3(.7f, .7f, .7f);
    vec3 diffuse = brightness * uLightColor;

    outColor = vec4((ambient + diffuse) * vec3(.7, .7f, .7f), 1.f);
}
