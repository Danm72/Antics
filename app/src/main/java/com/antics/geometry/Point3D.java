package com.antics.geometry;

/**
 * Created with IntelliJ IDEA.
 * User: danmalone
 * Date: 05/10/2013
 * Time: 21:54
 * To change this template use File | Settings | File Templates.
 */
public  class Point3D {
    public final float x, y, z;

    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D translateY(float distance) {
        return new Point3D(x, y + distance, z);
    }
    public String toString(){
        return ("positionX: " + x + "positionY: "+ y + "z: " + z);
    }
    public Point3D midPoint(Point3D distance) {
        float tmpX,tmpY,tmpZ;
        tmpX = (x + distance.x)/2;
        tmpY = (y + distance.y)/2;
        tmpZ = (z + distance.z)/2;

        return new Point3D(tmpX,tmpY,tmpZ);
    }
}