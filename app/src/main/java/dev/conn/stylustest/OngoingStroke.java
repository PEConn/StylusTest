package dev.conn.stylustest;

import android.graphics.Color;
import android.graphics.Path;

public class OngoingStroke {
    private static final float TOUCH_TOLERANCE = 4;

    private float mX;
    private float mY;

    private Path mPath = new Path();
    private Stroke mStroke = new Stroke(Color.YELLOW, 5, mPath);

    public OngoingStroke(float x, float y) {
        mPath.reset();  // Why do we need this?

        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    protected boolean moveExceedsTolerance(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        return dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE;
    }

    public void move(float x, float y) {
        if (!moveExceedsTolerance(x, y)) return;

        mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
        mX = x;
        mY = y;
    }

    public Stroke getStroke() {
        return mStroke;
    }

    public void finish() {
        mPath.lineTo(mX, mY);
    }

    public void cancel() {
        mStroke.color = Color.GRAY;
    }
}
