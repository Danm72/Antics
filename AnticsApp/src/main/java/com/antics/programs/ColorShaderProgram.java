package com.antics.programs;

import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;

import com.antics.R;

import android.content.Context;

public class ColorShaderProgram extends ShaderProgram {
	// Attribute constants.
	private static final String[] ATTRIBUTES = { A_POSITION, A_COLOR };
	private final int uColorLocation;

	// Atribute locations.
	private static final int A_POSITION_LOCATION = getAttributeLocation(
			ATTRIBUTES, A_POSITION);

	// Uniform locations.
	private final int uMatrixLocation;

	public ColorShaderProgram(Context context) {
		super(context, R.raw.vertex_shader, R.raw.fragment_shader, ATTRIBUTES);
		uColorLocation = glGetUniformLocation(program, U_COLOR);
		// Retrieve uniform locations for the shader program.
		uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
	}

	public void setUniforms(float[] matrix, float r, float g, float b) {
		// Pass the matrix into the shader program.
		glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
		glUniform4f(uColorLocation, r, g, b, 1f);
	}

	public int getPositionAttributeLocation() {
		return A_POSITION_LOCATION;
	}
}
