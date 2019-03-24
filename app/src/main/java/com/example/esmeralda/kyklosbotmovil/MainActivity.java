package com.example.esmeralda.kyklosbotmovil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView verCupones = (CardView)findViewById(R.id.irVerCupones);
        CardView misCupones = (CardView)findViewById(R.id.irMisCupones);

        verCupones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaVerCupones = new Intent(getApplicationContext(), cupones.class);
                startActivity(pantallaVerCupones);
            }
        });

        misCupones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaMisCupones = new Intent(getApplicationContext(), misCupones.class);
                startActivity(pantallaMisCupones);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuopciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id==R.id.hPuntosObtenidos)
            {
                Intent pantallahPuntosObtenidos = new Intent(this, historialPuntosObtenidos.class);
                startActivity(pantallahPuntosObtenidos);
            }
            else if (id == R.id.hPuntosCanjeados)
            {
                Intent pantallahPuntosCanjeados = new Intent(this, historialPuntosCanjeados.class);
                startActivity(pantallahPuntosCanjeados);
            }
            else if (id == R.id.menuPrincipal)
            {
                Intent pantallaPrincipal = new Intent(this, MainActivity.class);
                startActivity(pantallaPrincipal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
}
