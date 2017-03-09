package com.example.robert.traceit.shape;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Robert on 2/8/2017.
 */

public class ShapeGenerator {

    private  static  final int MAX_NUMBER_OF_DISTRIBUTED_POINTS = 50;
    private static final float MIN_ZONE_RATIO = 0.20f;

    private Point mLowerBound;
    private Point mUpperBound;
    private int canvasWidth;
    private int canvasHeight;

    private EllipticallPointGenerator mPointGenerator;

    private Random randGenerator;
    private DisplayMetrics dm;

    public ShapeGenerator(Point topLeft, Point bottomRight, DisplayMetrics displayMetrics) {
        this.mLowerBound = topLeft;
        this.mUpperBound = bottomRight;
        this.canvasWidth = bottomRight.x - topLeft.x;
        this.canvasHeight = bottomRight.y - topLeft.y;

        this.randGenerator = new Random();

        this.mPointGenerator = new EllipticallPointGenerator(new PointF(topLeft.x + canvasWidth / 2, topLeft.y + canvasHeight / 2), canvasWidth / 2, canvasHeight / 2,  MAX_NUMBER_OF_DISTRIBUTED_POINTS);
        this.dm = displayMetrics;
    }

    public Shape getNextShape() {
        return generateNextShape();
    }

    private Shape generateNextShape() {

        try {
            PointF[] randomPoints = mPointGenerator.generateRandomPoints(randGenerator.nextInt(3) + 3, MIN_ZONE_RATIO); // randGenerator.nextInt(7) + 3, MIN_ZONE_RATIO);
            return new Shape(randomPoints, 0.005f, 0.75, this.dm);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private PointF generateRandomPoint(PointF pivot, int delta) {
        int minX = (int)(pivot.x - delta);
        int minY = (int)(pivot.y - delta);
        int maxX = (int)(pivot.x + delta);
        int maxY = (int)(pivot.y + delta);
        float x = this.randGenerator.nextInt( maxX - minX  + 1) + minX;
        float y = this.randGenerator.nextInt( maxY - minY  + 1) + minY;

        return new PointF(x, y);
    }

}
