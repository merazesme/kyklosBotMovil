package com.example.esmeralda.kyklosbotmovil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView verCupones = (CardView)findViewById(R.id.irVerCupones);
        CardView misCupones = (CardView)findViewById(R.id.irMisCupones);
        CardView leerQR = (CardView)findViewById(R.id.irLeerQR);
        CardView perfil = (CardView)findViewById(R.id.irPerfil);
        CardView info = (CardView)findViewById(R.id.irInformacion);

        int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            }
        }

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

        leerQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaLectorQR = new Intent(getApplicationContext(), LectorQR.class);
                startActivity(pantallaLectorQR);
            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaPerfil = new Intent(getApplicationContext(), Perfil.class);
                startActivity(pantallaPerfil);
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaInformacion = new Intent(getApplicationContext(), howTo.class);
                startActivity(pantallaInformacion);
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
            else if (id == R.id.info)
            {
                Intent pantallaInformacion = new Intent(this, Informacion.class);
                startActivity(pantallaInformacion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
}
