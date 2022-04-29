package dev.conn.stylustest;

public class OngoingHandwritingStroke extends OngoingStroke {
    private boolean mTriggered;
    private final Runnable mStartHandwriting;

    public OngoingHandwritingStroke(float x, float y, Runnable startHandwriting) {
        super(x, y);
        mStartHandwriting = startHandwriting;
    }

    @Override
    public void move(float x, float y) {
        if (!moveExceedsTolerance(x, y)) return;

        if (!mTriggered) {
            mStartHandwriting.run();
            mTriggered = true;
        }

        super.move(x, y);
    }
}
