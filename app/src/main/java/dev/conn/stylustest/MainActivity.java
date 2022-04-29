package dev.conn.stylustest;

import android.os.Build.VERSION;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    public interface Logger {
        void log(String line);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView logView = findViewById(R.id.logView);
        logView.setMovementMethod(new ScrollingMovementMethod());
        Logger logger = line -> {
            Log.d("Peter", line);
            logView.append('\n' + line);
        };

        boolean isOnT = VERSION.SDK_INT > 33 ||
                VERSION.SDK_INT == 32 && VERSION.CODENAME.toLowerCase().equals("tiramisu");
        logger.log("SDK_INT = " + VERSION.SDK_INT + ", SDK_NAME = " + VERSION.CODENAME);
        logger.log("You can draw by scribbling in the box above.");
        logger.log("If you start a stroke in the red box, #startStylusHandwriting will be called.");
        logger.log("If a stroke gets cancelled, it turns grey.");

        DrawView drawView = findViewById(R.id.drawView);
        drawView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                drawView.init(drawView.getMeasuredWidth(), drawView.getMeasuredHeight(), logger);
            }
        });
    }
}