package com.example.esmeralda.kyklosbotmovil;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LectorQRespuesta extends AppCompatActivity {
    private String puntos;
    private TextView txtPuntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qrespuesta);

        txtPuntos=(TextView)findViewById(R.id.puntos);

        try {
            Bundle bundle = getIntent().getExtras();
            puntos = bundle.getString("puntos");


            txtPuntos.setText(puntos);

            //Ocultar Bar
            getSupportActionBar().hide();

            cerrar();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cerrar(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // acciones que se ejecutan tras los milisegundos
                finish();
            }
        }, 3000);
    }
}
