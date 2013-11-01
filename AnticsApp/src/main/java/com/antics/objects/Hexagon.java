/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
 ***/
package com.antics.objects;

import com.antics.data.VertexArray;
import com.antics.geometry.Cylinder;
import com.antics.geometry.Point3D;
import com.antics.programs.ColorShaderProgram;
import com.antics.programs.TextureShaderProgram;
import com.antics.util.ConfigFile;

import java.util.List;

public class Hexagon {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT)
            * ConfigFile.BYTES_PER_FLOAT;
    public final float radius, height;
    private final VertexArray vertexArray;
    private final List<DrawCommand> drawList;
    private final float[] textureArray = {0.5f, 0.5f, 0.25f, 0.0f, 0.75f,
            0.0f, 1.0f, 0.5f, 0.75f, 1.0f, 0.25f, 1.0f, 0.0f, 0.5f, 0.25f,
            0.0f,};
    public Point3D location;
    private GeneratedData generatedData;

    public Hexagon(float radius, float height, int numPointsAroundObject) {
        // GeneratedData
        generatedData = ObjectBuilder.createHexagon(new Cylinder(new Point3D(0f,
                0f, 0f), radius, height), numPointsAroundObject);
        this.radius = radius;
        this.height = height;

        // mess with vertexData here, add textures.
        generatedData.vertexData[3] = textureArray[0];
        generatedData.vertexData[4] = textureArray[1];

        generatedData.vertexData[8] = textureArray[2];
        generatedData.vertexData[9] = textureArray[3];

        generatedData.vertexData[13] = textureArray[4];
        generatedData.vertexData[14] = textureArray[5];

        generatedData.vertexData[18] = textureArray[6];
        generatedData.vertexData[19] = textureArray[7];

        generatedData.vertexData[23] = textureArray[8];
        generatedData.vertexData[24] = textureArray[9];

        generatedData.vertexData[28] = textureArray[10];
        generatedData.vertexData[29] = textureArray[11];

        generatedData.vertexData[33] = textureArray[12];
        generatedData.vertexData[34] = textureArray[13];

        generatedData.vertexData[38] = textureArray[14];
        generatedData.vertexData[39] = textureArray[15];


        vertexArray = new VertexArray(generatedData.vertexData);

        drawList = generatedData.drawList;
    }

    public Point3D getLocation() {
        return location;
    }

    public void setLocation(Point3D location0) {
        location = location0;
    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);

        vertexArray.setVertexAttribPointer(0,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);

    }

    public void draw() {
        for (DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
