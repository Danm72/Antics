package com.antics.programs;

import android.content.Context;

import com.antics.R;

import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class TextureShaderProgram extends ShaderProgram {
    // Attribute constants.
    private static final String[] ATTRIBUTES = { A_POSITION,
        A_TEXTURE_COORDINATES };

    // Attribute locations.
    private static final int A_POSITION_LOCATION = 
        getAttributeLocation(ATTRIBUTES, A_POSITION);
    private static final int A_TEXTURE_COORDINATES_LOCATION = 
        getAttributeLocation(ATTRIBUTES, A_TEXTURE_COORDINATES);

    // Uniform locations.
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader,
            R.raw.texture_fragment_shader, ATTRIBUTES);

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program,
            U_TEXTURE_UNIT);
    }

    public void setUniforms(float[] matrix, int textureId) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0);

        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId);

        // Tell the texture uniform sampler to use this texture in the shader by
        // telling it to read from texture unit 0.
        glUniform1i(uTextureUnitLocation, 0);
    }
    
    public int getPositionAttributeLocation() {
        return A_POSITION_LOCATION;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return A_TEXTURE_COORDINATES_LOCATION;
    }
}
