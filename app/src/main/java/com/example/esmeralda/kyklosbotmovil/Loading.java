package com.example.esmeralda.kyklosbotmovil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Loading extends AppCompatActivity {
    ProgressBar progressBar;
    TextView textView;
    private String puntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_loading);

            try {
                Bundle bundle = getIntent().getExtras();
                puntos = bundle.getString("puntos");

                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

                progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                textView = (TextView) findViewById(R.id.textView);

                //Ocultar Tolbar
                getSupportActionBar().hide();

                progressBar.setMax(100);
                progressBar.setScaleY(3f);

                progressAnimation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void progressAnimation(){
        try {
            ProgressBarAnimation anim = new ProgressBarAnimation(this, progressBar, textView, 0f, 100f, puntos);
            anim.setDuration(8000);
            progressBar.setAnimation(anim);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
