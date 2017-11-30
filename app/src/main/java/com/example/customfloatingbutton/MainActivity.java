package com.example.customfloatingbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatButton2 floatButton = findViewById(R.id.floatButton);

        floatButton.setOnClickListener(new FloatButton2.OnClickListener() {
            @Override
            public void onClick(FloatButton2 sfb) {
                sfb.startScroll();
            }
        });

        floatButton.setFoldListener(new FloatButton2.FoldListener() {
            @Override
            public void onFold(boolean isIncrease, FloatButton2 fb) {

            }
        });
    }
}
