package com.antics.geometry;

/**
 * Created with IntelliJ IDEA.
 * User: danmalone
 * Date: 05/10/2013
 * Time: 21:56
 * To change this template use File | Settings | File Templates.
 */
public class Circle {
    public final Point3D center;
    public final float radius;

    public Circle(Point3D center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public Circle scale(float scale) {
        return new Circle(center, radius * scale);
    }
}