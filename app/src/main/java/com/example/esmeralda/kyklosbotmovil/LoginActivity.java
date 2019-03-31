package com.example.esmeralda.kyklosbotmovil;
//package com.example.jose_.agenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    private TextView usuario, contra;
    private Button iniciar;
    public int bandera=0;
    public static String idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        iniciar = (Button)findViewById(R.id.iniciar);
        usuario = (TextView)findViewById(R.id.Usuario);
        contra = (TextView)findViewById(R.id.Contra);


        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarSesion(context);
            }
        });

    }
    public void iniciarSesion(final Context context){
        try {
            String User = usuario.getText().toString();
            String Contraa = contra.getText().toString();
            if(User.length() != 0 && Contraa.length() != 0){
                String url = "http://tunas.mztzone.com/tunas/apiJesus/login/ad";
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("no")) {
                            Toast.makeText(getApplicationContext(), "Usuario/Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Iniciar sesión", Toast.LENGTH_SHORT).show();
                            bandera = 1;
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            idUsuario = response.trim();
                            Toast.makeText(getApplicationContext(), "idUsuario " + idUsuario, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error " + error, Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Usuario", usuario.getText().toString().trim());
                        params.put("Contra", contra.getText().toString().trim());
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
        }else{
                Toast.makeText(getApplicationContext(), "Llene todo los campos", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Inicio(View view){
        try {
            Intent mapa = new Intent(this,Ubicacion.class);
            startActivity(mapa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Registro(View view){
        try {
            Intent mapa = new Intent(this,Registro.class);
            startActivity(mapa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

