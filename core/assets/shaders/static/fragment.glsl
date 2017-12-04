#ifdef GL_ES
    precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

float rand(vec2 co){
return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main() {
    vec4 color = texture2D(u_texture, v_texCoords);
	gl_FragColor = color;
	if(color.a > 0.0){
		vec4 noise = vec4(1.0);
		noise.xyz = vec3(rand(vec2(0.0, v_texCoords.y)));
		gl_FragColor = mix(color, noise, 0.25);
	}
}