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
        Log.d("Peter", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView logView = findViewById(R.id.logView);
        logView.setMovementMethod(new ScrollingMovementMethod());
        boolean isOnT = VERSION.SDK_INT > 33 ||
                VERSION.SDK_INT == 32 && VERSION.CODENAME.toLowerCase().equals("tiramisu");
        logView.setText("SDK_INT = " + VERSION.SDK_INT + ", SDK_NAME = " + VERSION.CODENAME);
        Logger logger = line -> {
            Log.d("Peter", line);
            logView.append('\n' + line);
        };

        EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);

        DrawView drawView = findViewById(R.id.drawView);
        drawView.getViewTreeObserver()
                .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                drawView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = drawView.getMeasuredWidth();
                int height = drawView.getMeasuredHeight();
                drawView.init(height, width);
            }
        });

        drawView.setLogger(logger);

        if (isOnT) {
            // editText.setAutoHandwritingEnabled(false);
            button.setOnClickListener(view -> {
                InputMethodManager manager = (InputMethodManager)
                        view.getContext().getSystemService(INPUT_METHOD_SERVICE);

                editText.requestFocus();
                manager.startStylusHandwriting(editText);
            });
        }
    }
}