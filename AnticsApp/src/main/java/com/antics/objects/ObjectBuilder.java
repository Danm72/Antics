package com.antics.objects;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static android.opengl.GLES20.GL_SRC_ALPHA;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnable;

import java.util.ArrayList;
import java.util.List;

import android.util.FloatMath;
import com.antics.geometry.Circle;
import com.antics.geometry.Cylinder;

class ObjectBuilder {
	private static final int FLOATS_PER_VERTEX = 3;

    static GeneratedData createPuck(Cylinder puck, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints)
                        + sizeOfOpenCylinderInVertices(numPoints);

        ObjectBuilder builder = new ObjectBuilder(size, "CircleVector");

        Circle puckTop = new Circle(puck.center.translateY(puck.height / 2f),
                        puck.radius);

        builder.appendUnalteredCircle(puckTop, numPoints);
        // builder.appendOpenCylinder(puck, numPoints);

        return builder.build();
}

	static GeneratedData createSquare(Cylinder square, int numPoints) {
		int size = sizeOfCircleInVertices(numPoints);
		String objectType = "square";
		ObjectBuilder builder = new ObjectBuilder(size, objectType);

		Circle squareTop = new Circle(
				square.center.translateY(square.height / 2f), square.radius);

		builder.appendPointsCircle(squareTop, numPoints);

		return builder.build();
	}
	 private void appendUnalteredCircle(Circle circleVector, int numPoints) {
         final int startVertex = offset / FLOATS_PER_VERTEX;
         final int numVertices = sizeOfCircleInVertices(numPoints);

         // Center point of fan
         vertexData[offset++] = circleVector.center.x;
         vertexData[offset++] = circleVector.center.y;
         vertexData[offset++] = circleVector.center.z;

         // Fan around center point. <= is used because we want to generate
         // the point at the starting angle twice, to complete the fan.
         for (int i = 0; i <= numPoints; i++) {
                 float angleInRadians = ((float) i / (float) numPoints)
                                 * ((float) Math.PI * 2f);

                 vertexData[offset++] = circleVector.center.x + circleVector.radius
                                 * FloatMath.cos(angleInRadians);

                 vertexData[offset++] = circleVector.center.y;

                 vertexData[offset++] = circleVector.center.z + circleVector.radius
                                 * FloatMath.sin(angleInRadians);

         }

         drawList.add(new DrawCommand() {
                 @Override
                 public void draw() {
                         glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);

                 }
         });
}

	static GeneratedData createHexagon(Cylinder hex, int numPoints) {
		int size = sizeOfCircleInVertices(numPoints);

		ObjectBuilder builder = new ObjectBuilder(size);

		Circle hexTop = new Circle(hex.center.translateY(hex.height / 2f),
				hex.radius);

		builder.appendCircle(hexTop, numPoints);
		return builder.build();
	}

	private static int sizeOfCircleInVertices(int numPoints) {
		return 1 + (numPoints + 1);
	}

	private final float[] vertexData;
	private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();
	private int offset = 0;

	private ObjectBuilder(int sizeInVertices) {
		vertexData = new float[(sizeInVertices * FLOATS_PER_VERTEX) + 16];
	}

	private ObjectBuilder(int sizeInVertices, String type) {
		if (type.compareTo("CircleVector") == 0)
			vertexData = new float[(sizeInVertices * FLOATS_PER_VERTEX)];
		else
			vertexData = new float[(sizeInVertices * FLOATS_PER_VERTEX) + 12];
	}
    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
}

	private void appendCircle(Circle circleVector, int numPoints) {
		final int startVertex = offset / FLOATS_PER_VERTEX;
		final int numVertices = sizeOfCircleInVertices(numPoints);

		// Center point of fan
		vertexData[offset++] = circleVector.center.x;
		vertexData[offset++] = circleVector.center.y;
		vertexData[offset++] = circleVector.center.z;

		offset += 2;

		// Fan around center point. <= is used because we want to generate
		// the point at the starting angle twice, to complete the fan.
		for (int i = 0; i <= numPoints; i++) {
			float angleInRadians = ((float) i / (float) numPoints)
					* ((float) Math.PI * 2f);

			vertexData[offset++] = circleVector.center.x + circleVector.radius
					* FloatMath.cos(angleInRadians);

			vertexData[offset++] = circleVector.center.y;

			vertexData[offset++] = circleVector.center.z + circleVector.radius
					* FloatMath.sin(angleInRadians);
			offset++;
			offset++;

		}

		offset = 0;
		drawList.add(new DrawCommand() {
			@Override
			public void draw() {
				glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
			}
		});
	}

	private void appendPointsCircle(Circle circleVector, int numPoints) {
		final int startVertex = offset / FLOATS_PER_VERTEX;
		final int numVertices = sizeOfCircleInVertices(numPoints);

		// Center point of fan
		vertexData[offset++] = circleVector.center.x;
		vertexData[offset++] = circleVector.center.y;
		vertexData[offset++] = circleVector.center.z;

		offset += 2;

		// Fan around center point. <= is used because we want to generate
		// the point at the starting angle twice, to complete the fan.
		for (int i = 0; i <= numPoints; i++) {
			float angleInRadians = ((float) i / (float) numPoints)
					* ((float) Math.PI * 2f);

			vertexData[offset++] = circleVector.center.x + circleVector.radius
					* FloatMath.cos(angleInRadians);

			vertexData[offset++] = circleVector.center.y;

			vertexData[offset++] = circleVector.center.z + circleVector.radius
					* FloatMath.sin(angleInRadians);
			offset++;
			offset++;

		}
		drawList.add(new DrawCommand() {
			@Override
			public void draw() {
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices);
			}
		});
	}

	private GeneratedData build() {
		return new GeneratedData(vertexData, drawList);
	}

}
