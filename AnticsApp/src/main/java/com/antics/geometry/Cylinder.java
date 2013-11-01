package com.antics.geometry;

/**
 * Created with IntelliJ IDEA.
 * User: danmalone
 * Date: 05/10/2013
 * Time: 21:56
 * To change this template use File | Settings | File Templates.
 */
public class Cylinder {
    public final Point3D center;
    public final float radius;
    public final float height;

    public Cylinder(Point3D center, float radius, float height) {
        this.center = center;
        this.radius = radius;
        this.height = height;
    }
}