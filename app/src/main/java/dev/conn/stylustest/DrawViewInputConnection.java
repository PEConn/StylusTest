package dev.conn.stylustest;

import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import dev.conn.stylustest.MainActivity.Logger;

public class DrawViewInputConnection extends BaseInputConnection {
    private final Logger mLogger;

    public DrawViewInputConnection(View targetView, Logger logger) {
        this(targetView, false, logger);
    }

    public DrawViewInputConnection(View targetView, boolean fullEditor,
            Logger logger) {
        super(targetView, fullEditor);
        mLogger = logger;
    }

    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        mLogger.log("commitText(" + text + ")");

        return super.commitText(text, newCursorPosition);
    }
}
