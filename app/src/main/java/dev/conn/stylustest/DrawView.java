package dev.conn.stylustest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.EditorBoundsInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import dev.conn.stylustest.MainActivity.Logger;
import java.util.ArrayList;

@RequiresApi(api = 33)
public class DrawView extends View {
    private final Paint mPaint;
    private final ArrayList<Stroke> paths = new ArrayList<>();
    private Bitmap mBitmap;
    private Canvas mCanvas;

    private final Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private Logger mLogger;

    private Box mTextBoxBounds;
    private OngoingStroke mOngoingStroke;

    // Constructors to initialise all the attributes
    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);

        mPaint = new Paint();

        // the below methods smoothens
        // the drawings of the user
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        // 0xff=255 in decimal
        mPaint.setAlpha(0xff);
    }

    public void init(int height, int width) {
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        int quarterWidth = width / 4;
        int quarterHeight = height / 4;
        mTextBoxBounds = new Box(quarterWidth, quarterHeight,
                quarterWidth * 3, quarterHeight + 80);
        paths.add(new Stroke(Color.RED, 4, mTextBoxBounds.getOutline()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        int backgroundColor = Color.rgb(200, 200, 200);
        mCanvas.drawColor(backgroundColor);

        for (Stroke fp : paths) {
            mPaint.setColor(fp.color);
            mPaint.setStrokeWidth(fp.strokeWidth);
            mCanvas.drawPath(fp.path, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    private void touchStart(float x, float y) {
        if (mTextBoxBounds.contains((int) x, (int) y)) {
            mOngoingStroke = new OngoingHandwritingStroke(x, y, this::triggerHandwriting);
        } else {
            mOngoingStroke = new OngoingStroke(x, y);
        }

        paths.add(mOngoingStroke.getStroke());
    }

    private void triggerHandwriting() {
        InputMethodManager manager = getContext().getSystemService(InputMethodManager.class);

        mLogger.log("Triggering handwriting");

        requestFocus();
        manager.startStylusHandwriting(this);

        CursorAnchorInfo info = new CursorAnchorInfo.Builder()
                .setEditorBoundsInfo(new EditorBoundsInfo.Builder()
                        .setHandwritingBounds(mTextBoxBounds.toRectF())
                        .build()
                )
                .build();
        manager.updateCursorAnchorInfo(this, info);
    }

    private void touchMove(float x, float y) {
        mOngoingStroke.move(x, y);
    }

    private void touchUp() {
        mOngoingStroke.finish();
        mOngoingStroke = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLogger.log("ACTION_DOWN");
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                mLogger.log("ACTION_UP");
                touchUp();
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                mLogger.log("ACTION_CANCEL");
                mOngoingStroke.cancel();
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        mLogger.log("onCreateInputConnection");

        return new DrawViewInputConnection(this, mLogger);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        Log.w("Peter", "onCheckIsTextEditor");
        return true;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction,
            @Nullable Rect previouslyFocusedRect) {
        Log.d("Peter", "onFocusChanged(" + gainFocus + ")");
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        Log.d("Peter", "onWindowFocusChanged(" + hasWindowFocus + ")");
        super.onWindowFocusChanged(hasWindowFocus);
    }

    public void setLogger(Logger logger) {
        mLogger = logger;
    }
}
