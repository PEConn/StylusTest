package dev.conn.stylustest;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

public class TextBox {
    private final Rect mBounds;

    public TextBox(int left, int top, int right, int bottom) {
        mBounds = new Rect(left, top, right, bottom);
    }

    public boolean contains(int x, int y) {
        return mBounds.contains(x, y);
    }

    public Path getOutline() {
        Path outline = new Path();
        outline.moveTo(mBounds.left, mBounds.top);
        outline.lineTo(mBounds.right, mBounds.top);
        outline.lineTo(mBounds.right, mBounds.bottom);
        outline.lineTo(mBounds.left, mBounds.bottom);
        outline.lineTo(mBounds.left, mBounds.top);
        return outline;
    }

    public RectF toRectF() {
        return new RectF(mBounds);
    }
}
