package com.antics.objects;

import java.util.List;

import com.antics.data.VertexArray;
import com.antics.geometry.Cylinder;
import com.antics.geometry.Point3D;
import com.antics.programs.ColorShaderProgram;

public class CircleVector {
	private static final int POSITION_COMPONENT_COUNT = 3;

	public final float radius, height;

	private final VertexArray vertexArray;
	private final List<DrawCommand> drawList;

	public CircleVector(float radius, float height, int numPointsAroundPuck) {
		GeneratedData generatedData = ObjectBuilder.createPuck(new Cylinder(
				new Point3D(0f, 0f, 0f), radius, height), numPointsAroundPuck);

		this.radius = radius;
		this.height = height;

		vertexArray = new VertexArray(generatedData.vertexData);
		drawList = generatedData.drawList;
	}

	public void bindData(ColorShaderProgram colorProgram) {
		vertexArray.setVertexAttribPointer(0,
				colorProgram.getPositionAttributeLocation(),
				POSITION_COMPONENT_COUNT, 0);
	}

	public void draw() {
		for (DrawCommand drawCommand : drawList) {
			drawCommand.draw();
		}
	}
}