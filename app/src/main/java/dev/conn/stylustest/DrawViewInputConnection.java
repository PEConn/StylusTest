package dev.conn.stylustest;

import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import dev.conn.stylustest.MainActivity.Logger;

/** An input connection that logs the text that gets committed. */
public class DrawViewInputConnection extends BaseInputConnection {
    private final Logger mLogger;
    private String mText = "";
    private final Runnable mInvalidate;

    public DrawViewInputConnection(View targetView, Logger logger, Runnable invalidate) {
        super(targetView, false);
        mLogger = logger;
        mInvalidate = invalidate;
    }

    public String getText() {
        return mText;
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        mLogger.log("commitText(" + text + ")");
        mText += text;

        mInvalidate.run();

        return super.commitText(text, newCursorPosition);
    }
}
