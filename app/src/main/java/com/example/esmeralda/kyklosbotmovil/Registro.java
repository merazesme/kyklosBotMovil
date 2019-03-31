package com.example.esmeralda.kyklosbotmovil;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class Registro extends AppCompatActivity {

    private TextView nombre,apellido,usuario,contra,contra2,email;
    private Button iniciar, iniciar2;
    public static String Usuario;
    public static String Contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        final Context context = this;
        nombre = (TextView)findViewById(R.id.txtnombre);
        apellido = (TextView)findViewById(R.id.txtapellidos);
        usuario = (TextView)findViewById(R.id.txtusername);
        contra = (TextView)findViewById(R.id.txtcontraseña);
        contra2 = (TextView)findViewById(R.id.txtcontraseña2);
        email = (TextView)findViewById(R.id.txtemail);

        iniciar = (Button)findViewById(R.id.btnlisto2);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Finalizar(context);
            }
        });

    }

    public void Finalizar(final Context context){
        try {
            String Nombre = nombre.getText().toString();
            String Apellido = apellido.getText().toString();
            final String Usuario = usuario.getText().toString();
            final String Contra = contra.getText().toString();
            String Contra2 = contra2.getText().toString();
            String Email = email.getText().toString();
            if(Nombre.length() != 0 &&
                    Apellido.length() != 0 &&
                    Usuario.length() != 0 &&
                    Contra.length() != 0 &&
                    Contra2.length() != 0 &&
                    Email.length() != 0)
            {
                String url = "http://tunas.mztzone.com/tunas/apiJesus/agregar/add";
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("error")) {
                            Toast.makeText(getApplicationContext(), "Error al crear la cuenta", Toast.LENGTH_SHORT).show();
                        } else {
                            LoginActivity.idUsuario = response.trim();
                            Toast.makeText(getApplicationContext(), "Registro agregado " + LoginActivity.idUsuario , Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
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
                        params.put("Nombre", nombre.getText().toString().trim());
                        params.put("Apellidos", apellido.getText().toString().trim());
                        params.put("Correo", email.getText().toString().trim());
                        params.put("Usuario", usuario.getText().toString().trim());
                        params.put("Contra", contra.getText().toString().trim());

                        params.put("Imagen", "1");
                        params.put("Estado", "1");
                        params.put("Puntos", "0");
                        params.put("TipoUsuario_idTipoUsuario", "1");
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
