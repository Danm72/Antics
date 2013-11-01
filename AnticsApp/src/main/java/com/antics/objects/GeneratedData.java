package com.antics.objects;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: danmalone
 * Date: 05/10/2013
 * Time: 22:07
 * To change this template use File | Settings | File Templates.
 */
class GeneratedData {
    final float[] vertexData;
    final List<DrawCommand> drawList;

    GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
        this.vertexData = vertexData;
        int i = 0;
        for (Float flo : vertexData) {

            // Log.d("vertexData", "Object builder: " + i + " : " + flo);
            i++;
        }
        this.drawList = drawList;
    }
}
