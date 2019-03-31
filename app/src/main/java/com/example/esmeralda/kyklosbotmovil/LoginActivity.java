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
        setTheme(R.style.AppTheme);
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
                            if (response.trim().equals("bloqueado")){
                                Toast.makeText(getApplicationContext(), "Este usuario ha sido bloqueado", Toast.LENGTH_SHORT).show();
                            }else{
                                idUsuario = response;
                                //Toast.makeText(getApplicationContext(), "Id " + idUsuario, Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getApplicationContext(), "Iniciar sesión", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, MainActivity.class);
                                startActivity(intent);
                                //String[] cadenas = response.split("\\.");
                                //String id = cadenas[0];
                                //String status = cadenas[1];
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
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
}

