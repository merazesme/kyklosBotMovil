package com.example.esmeralda.kyklosbotmovil;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class howTo extends AppCompatActivity {

    private ViewPager canvaImagenes;
    private LinearLayout nDotLayout;
    private  sliderAdapter sliderAdapter;
    private Button btnIrApp;

    private TextView[] nDots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);

        canvaImagenes = (ViewPager)findViewById(R.id.viewImages);
        nDotLayout = (LinearLayout)findViewById(R.id.dots);

        sliderAdapter = new sliderAdapter(this);
        canvaImagenes.setAdapter(sliderAdapter);

        btnIrApp = (Button)findViewById(R.id.btnIrApp);

        if (Registro.banderaPrimeraVez==1) btnIrApp.setVisibility(View.VISIBLE);
        else btnIrApp.setVisibility(View.GONE);

        addDotsIndicator(0);
        canvaImagenes.addOnPageChangeListener(viewListener);
    }

    //CARGAR MENU '...'
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuopciones, menu);
        return true;
    }

    //OPCIONES DEL MENU '...'
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id==R.id.hPuntosObtenidos)
            {
                Intent pantallahPuntosObtenidos = new Intent(this, historialPuntosObtenidos.class);
                finish();
                startActivity(pantallahPuntosObtenidos);
            }
            else if (id == R.id.hPuntosCanjeados)
            {
                Intent pantallahPuntosCanjeados = new Intent(this, historialPuntosCanjeados.class);
                finish();
                startActivity(pantallahPuntosCanjeados);
            }
            else if (id == R.id.menuPrincipal)
            {
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    public  void addDotsIndicator(int position)
    {
        nDots =  new TextView[2];
        nDotLayout.removeAllViews();
        for (int i=0; i<nDots.length; i++)
        {
            nDots[i] = new TextView(this);
            nDots[i].setText(Html.fromHtml("&#8226;"));
            nDots[i].setTextSize(45);
            nDots[i].setTextColor(getResources().getColor(R.color.colorLightOrange));

            nDotLayout.addView(nDots[i]);
        }
        if(nDots.length > 0)
        {
            nDots[position].setTextColor(getResources().getColor(R.color.colorYellowAccent));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void irMenu(View v)
    {
        Registro.banderaPrimeraVez=0;
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

}
