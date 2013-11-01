/***
 * Excerpted from "OpenGL ES for Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/kbogla for more book information.
***/
package com.antics.objects;

import java.util.List;

import com.antics.data.VertexArray;
import com.antics.geometry.Cylinder;
import com.antics.geometry.Point3D;
import com.antics.programs.ColorShaderProgram;
import com.antics.programs.TextureShaderProgram;
import com.antics.util.ConfigFile;


public class SquareVector {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;
    public Point3D location;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT+TEXTURE_COORDINATES_COMPONENT_COUNT) * ConfigFile.BYTES_PER_FLOAT;
    private GeneratedData generatedData;
    private final VertexArray vertexArray;
    private final List<DrawCommand> drawList;
    
    private final float [] diggerArray =
    	{
        		//X    Y
        		.1125f, 0.5f, 	//middle
        		0.0f, 0.0f,		//bottom left
        		0.225f, 0.0f,		//bottom right
        		0.225f, 1f,			//top right
        		0f, 1.0f,		//top left
        		0.0f, 0.0f,	
    	};
    private final float [] queenArray =
    	{
        		//X    Y
    		.3f, 0.5f, 	//middle
    		0.225f, 0.0f,		//bottom left
    		0.412f, 0.0f,		//bottom right
    		0.412f, 1f,			//top right
    		0.225f, 1.0f,		//top left
    		0.225f, 0.0f,	
    	};
    private final float [] soldierArray =
    	{
        		//X    Y
    		.5f, 0.5f, 	//middle
    		0.41f, 0.0f,		//bottom left
    		0.6f, 0.0f,		//bottom right
    		0.6f, 1f,			//top right
    		0.41f, 1.0f,		//top left
    		0.41f, 0.0f,	
    	};
    private final float [] gathererArray =
    	{
        		//X    Y
    		.7f, 0.5f, 	//middle
    		0.6f, 0.0f,		//bottom left
    		0.8f, 0.0f,		//bottom right
    		0.8f, 1f,			//top right
    		0.6f, 1.0f,		//top left
    		0.6f, 0.0f,	
    	};
    private final float [] fullArray= //textureArray =
	{ 
    		//X    Y
            .5f, 0.5f,
            0.0f, 0.0f,
            1.0f, 0.0f,
            1f, 1f,
            0f, 1.0f,
            0.0f, 0.0f,	
		    
	};
           


    public SquareVector(float radius, float height, int numPointsAroundObject, int type) {
        generatedData = ObjectBuilder.createSquare(new Cylinder(new Point3D(0f, 0f, 0f), radius, height), numPointsAroundObject);
       // Log.d("SquareVector points", numPointsAroundObject+"");
        this.radius = radius;
        this.height = height;
        
        generatedTextureArray(chooseTextureArray(type));
        
        vertexArray = new VertexArray(generatedData.vertexData);
        
        drawList = generatedData.drawList;
    }
    
    public Point3D getLocation(){
    	return location;
    }
    public void setLocation(Point3D location0){
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
            POSITION_COMPONENT_COUNT,STRIDE);
         
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, 
            textureProgram.getTextureCoordinatesAttributeLocation(),
            TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        for (DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
    public void generatedTextureArray(float[] gathererArray){
        generatedData.vertexData[3] =  gathererArray[0];
        generatedData.vertexData[4] = gathererArray[1];
        
        generatedData.vertexData[8] = gathererArray[2];
        generatedData.vertexData[9] = gathererArray[3];
        
        generatedData.vertexData[13] = gathererArray[4];
        generatedData.vertexData[14] = gathererArray[5];
        
        generatedData.vertexData[18] = gathererArray[6];
        generatedData.vertexData[19] = gathererArray[7];
        
        generatedData.vertexData[23] = gathererArray[8];
        generatedData.vertexData[24] = gathererArray[9];
        
        generatedData.vertexData[28] = gathererArray[10];
        generatedData.vertexData[29] = gathererArray[11];
    }
    
    private float[] chooseTextureArray(int number){
        switch(number) {
        case(0):
            return diggerArray;   
        case(1):
            return gathererArray;
        case(2):
            return queenArray;
        case(3):
            return soldierArray;
        case(4):
            return fullArray;
        default:
            return diggerArray;
        }
    }
}