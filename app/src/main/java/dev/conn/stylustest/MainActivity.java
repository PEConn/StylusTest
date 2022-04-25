package dev.conn.stylustest;

import android.os.Build.VERSION;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Peter", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        boolean isOnT = VERSION.SDK_INT > 33 ||
                VERSION.SDK_INT == 32 && VERSION.CODENAME.toLowerCase().equals("tiramisu");
        textView.setText("SDK_INT = " + VERSION.SDK_INT + ", SDK_NAME = " + VERSION.CODENAME);

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

        // drawView.requestFocus();

        if (isOnT) {
            // editText.setAutoHandwritingEnabled(false);
            button.setOnClickListener(view -> {
                InputMethodManager manager = (InputMethodManager)
                        view.getContext().getSystemService(INPUT_METHOD_SERVICE);

                editText.requestFocus();
                manager.startStylusHandwriting(editText);
            });

            drawView.setStartHandwritingCallback(() -> {
                InputMethodManager manager = (InputMethodManager)
                        getSystemService(INPUT_METHOD_SERVICE);

                drawView.requestFocus();
                // manager.showSoftInput(drawView, 0);

                manager.startStylusHandwriting(drawView);
            });
        }
    }
}