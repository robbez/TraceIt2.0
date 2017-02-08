package com.example.robert.traceit.drawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.robert.traceit.shape.ShapeGenerator;

/**
 * Created by Robert on 2/7/2017.
 */

public class DrawArea extends View {
    private Context context;
    public int width;
    public int height;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mCanvasPaint;
    private Paint mDrawPaint;
    private Paint mShapePaint;
    private Path mPath;
    private Path mShapePath;
    
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private ShapeGenerator mShapeGenerator;

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
        this.mShapeGenerator = new ShapeGenerator(new Point(0, 0), new Point(width, height));
        setupCanvasPaint();
        setupDrawingPaint();
        setupShapePaint();

        this.mShapePath = this.mShapeGenerator.getNextShape();
    }

    private void setupShapePaint() {
        this.mShapePaint = new Paint();
        this.mShapePaint.setAntiAlias(true);
        this.mShapePaint.setDither(true);
        this.mShapePaint.setColor(Color.GREEN);
        this.mShapePaint.setStyle(Paint.Style.STROKE);
        this.mShapePaint.setStrokeJoin(Paint.Join.ROUND);
        this.mShapePaint.setStrokeCap(Paint.Cap.ROUND);
        this.mShapePaint.setStrokeWidth(40);
    }

    private void setupDrawingPaint() {
        this.mDrawPaint = new Paint();
        this.mDrawPaint.setAntiAlias(true);
        this.mDrawPaint.setDither(true);
        this.mDrawPaint.setColor(Color.RED);
        this.mDrawPaint.setStyle(Paint.Style.STROKE);
        this.mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mDrawPaint.setStrokeWidth(20);
    }

    private void setupCanvasPaint() {
        this.mCanvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, this.mCanvasPaint);
        canvas.drawPath(this.mShapePath, this.mShapePaint);
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
        this.mShapePath = this.mShapeGenerator.getNextShape();
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
        }
    }

    private void touchStarted(float x, float y) {
        this.mPath.reset();
        mPath.moveTo(x, y);
        this.mX = x;
        this.mY = y;
    }


}
