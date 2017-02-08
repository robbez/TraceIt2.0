package com.example.robert.traceit.shape;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Robert on 2/8/2017.
 */

public class ShapeGenerator {

    private Point mLowerBound;
    private Point mUpperBound;
    private int canvasWidth;
    private int canvasHeight;
    private Random randGenerator;

    private ArrayList<LineSegment> linesForPath;
    private Path shapePath;

    public ShapeGenerator(Point topLeft, Point bottomRight) {
        this.mLowerBound = topLeft;
        this.mUpperBound = bottomRight;
        this.canvasWidth = bottomRight.x - topLeft.x;
        this.canvasHeight = bottomRight.y - topLeft.y;

        this.randGenerator = new Random();

        this.linesForPath = new ArrayList<>();
        this.shapePath = new Path();
    }

    public Path getNextShape() {
        generateNextShape();

        return this.shapePath;
    }

    private void generateNextShape() {
        this.linesForPath.clear();
        this.shapePath.reset();

        PointF p1 = generateRandomPoint(new PointF(100, 700), 50);
        PointF p2 = generateRandomPoint(new PointF(400, 1000), 50);
        PointF p3 = generateRandomPoint(new PointF(900, 900), 50);

        this.shapePath.moveTo(p1.x, p1.y);
        this.linesForPath.add(new LineSegment(p1, p2));
        this.shapePath.lineTo(p2.x, p2.y);
        this.linesForPath.add(new LineSegment(p2, p3));
        this.shapePath.lineTo(p3.x, p3.y);
        this.linesForPath.add(new LineSegment(p3, p1));
        this.shapePath.lineTo(p1.x, p1.y);
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
