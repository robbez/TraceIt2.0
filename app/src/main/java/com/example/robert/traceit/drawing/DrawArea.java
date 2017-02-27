package com.example.robert.traceit.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.example.robert.traceit.shape.Shape;
import com.example.robert.traceit.shape.ShapeGenerator;

import java.util.ArrayList;

/**
 * Created by Robert on 2/7/2017.
 */

public class DrawArea extends View {

    private static final int SHAPE_STROKE = 60;
    private static final int TRACE_STROKE = 30;

    private Context context;
    public int width;
    public int height;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mCanvasPaint;
    private Paint mDrawPaint;
    private Paint mShapePaint;
    private Path mPath;
    private PathMeasure mTracePathMeasure;
    private Shape mShape;


    private double threshold;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private ShapeGenerator mShapeGenerator;

    private ArrayList<PointF> points;

    public DrawArea(Context c) {
        super(c);
        init(c);
    }

    public DrawArea(Context c, AttributeSet attrs) {
        super(c, attrs);
        init(c);
    }

    public DrawArea(Context c, AttributeSet attrs, int defStyle) {
        super(c, attrs, defStyle);
        init(c);
    }

    private void init(Context c) {
        this.context = c;
        this.mPath = new Path();
        this.mTracePathMeasure = new PathMeasure();
        setupCanvasPaint();
        setupDrawingPaint();
        setupShapePaint();

        this.threshold = (SHAPE_STROKE + TRACE_STROKE);
        this.points = new ArrayList<>();
    }

    private void setupShapePaint() {
        this.mShapePaint = new Paint();
        this.mShapePaint.setAntiAlias(true);
        this.mShapePaint.setDither(true);
        this.mShapePaint.setColor(Color.GREEN);
        this.mShapePaint.setStyle(Paint.Style.STROKE);
        this.mShapePaint.setStrokeJoin(Paint.Join.ROUND);
        this.mShapePaint.setStrokeCap(Paint.Cap.ROUND);
        this.mShapePaint.setStrokeWidth(SHAPE_STROKE);
    }

    private void setupDrawingPaint() {
        this.mDrawPaint = new Paint();
        this.mDrawPaint.setAntiAlias(true);
        this.mDrawPaint.setDither(true);
        this.mDrawPaint.setColor(Color.RED);
        this.mDrawPaint.setStyle(Paint.Style.STROKE);
        this.mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mDrawPaint.setStrokeWidth(TRACE_STROKE);
    }

    private void setupCanvasPaint() {
        this.mCanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mShapeGenerator = new ShapeGenerator(new Point(SHAPE_STROKE, SHAPE_STROKE), new Point(w - SHAPE_STROKE, h - SHAPE_STROKE), getResources().getDisplayMetrics());
        this.mShape = this.mShapeGenerator.getNextShape();
        this.mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, this.mCanvasPaint);
        canvas.drawPath(this.mShape.getPath(), this.mShapePaint);
        canvas.drawPath(this.mPath, this.mDrawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStarted(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMoved(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchEnded();
                invalidate();
                break;
        }

        return true;
    }

    private void touchEnded() {
        mPath.lineTo(this.mX, this.mY);
        this.mPath.reset();
        this.mCanvas.drawPath(this.mPath, this.mDrawPaint);

        if(this.mShape.getMatchRating() > 0.6 && this.mShape.passedAllCheckpoints()) {
            this.mShape = this.mShapeGenerator.getNextShape();
        } else {
            this.mShape.resetDetection();
        }

        this.points.clear();
    }

    private void touchMoved(float x, float y) {
        float dX = Math.abs(x - this.mX);
        float dY = Math.abs(y - this.mY);

        if(dX >= TOUCH_TOLERANCE || dY >= TOUCH_TOLERANCE) {
            float halfPointX = (x + this.mX) / 2;
            float halfPointY = (y + this.mY) / 2;
            mPath.quadTo(this.mX, this.mY, halfPointX, halfPointY);

            this.mX = x;
            this.mY = y;

            this.mShape.checkDetection(new PointF(x, y), this.threshold, getTraceLength());
            points.add(new PointF(x, y));
        }
    }

    private void touchStarted(float x, float y) {
        this.mPath.reset();
        mPath.moveTo(x, y);
        this.mX = x;
        this.mY = y;
    }

    public float getTraceLength(){
        this.mTracePathMeasure.setPath(this.mPath, false);
        return this.mTracePathMeasure.getLength();
    }
}
