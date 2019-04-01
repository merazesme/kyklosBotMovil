package com.example.esmeralda.kyklosbotmovil;
//package com.example.jose_.agenda;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    private TextView usuario, contra, titulo, sesion, texto,texto2,nuevo,intentar;
    private Button iniciar, registrar;
    public static String idUsuario;
    public ProgressDialog pDialog;
    public GifImageView gifImageView;
    public GifImageView gifImageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        final Context context = this;
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        iniciar = (Button)findViewById(R.id.iniciar);
        registrar = (Button)findViewById(R.id.registrarse);
        intentar = (Button)findViewById(R.id.intentar);
        intentar.setVisibility(View.INVISIBLE);

        usuario = (TextView)findViewById(R.id.Usuario);
        contra = (TextView)findViewById(R.id.Contra);

        titulo = (TextView)findViewById(R.id.iniciar);
        texto2 = (TextView)findViewById(R.id.texto2);
        texto = (TextView)findViewById(R.id.texto);
        nuevo = (TextView)findViewById(R.id.nuevo);
        sesion = (TextView)findViewById(R.id.sesion);


        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        gifImageView2 = (GifImageView)findViewById(R.id.gifImageView);

        SharedPreferences prefs = getSharedPreferences("login",Context.MODE_PRIVATE);
        String id = prefs.getString("id", "");
        if (id.length() != 0){
            Intent intent = new Intent(context, Ubicacion.class);
            startActivity(intent);
        }


        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iniciarSesion(context);
            }
        });

    }
    public void iniciarSesion(final Context context){
        try {
            if(isOnline() == true){
                String User = usuario.getText().toString();
                String Contraa = contra.getText().toString();
                if(User.length() != 0 && Contraa.length() != 0){
                    try{
                        InputStream inputStream = getAssets().open("loading.gif");
                        byte[] bytes = IOUtils.toByteArray(inputStream);
                        gifImageView.setBytes(bytes);
                        gifImageView.startAnimation();
                        gifImageView.setVisibility(View.VISIBLE);

                        iniciar.setVisibility(View.INVISIBLE);
                        registrar.setVisibility(View.INVISIBLE);

                        usuario.setVisibility(View.INVISIBLE);
                        contra.setVisibility(View.INVISIBLE);
                        titulo.setVisibility(View.INVISIBLE);
                        texto2.setVisibility(View.INVISIBLE);
                        texto.setVisibility(View.INVISIBLE);
                        nuevo.setVisibility(View.INVISIBLE);
                        sesion.setVisibility(View.INVISIBLE);
                    }
                    catch (IOException ex)
                    {

                    }
                    String url = "http://tunas.mztzone.com/tunas/apiJesus/login/ad";
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.trim().equals("no")) {
                                //hidePDialog();
                                gifImageView.stopAnimation();
                                gifImageView.setVisibility(View.INVISIBLE);

                                iniciar.setVisibility(View.VISIBLE);
                                registrar.setVisibility(View.VISIBLE);

                                usuario.setVisibility(View.VISIBLE);
                                contra.setVisibility(View.VISIBLE);
                                titulo.setVisibility(View.VISIBLE);
                                texto2.setVisibility(View.VISIBLE);
                                texto.setVisibility(View.VISIBLE);
                                nuevo.setVisibility(View.VISIBLE);
                                sesion.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(), "Usuario/Contraseña incorrectos", Toast.LENGTH_SHORT).show();
                            } else {
                                if (response.trim().equals("bloqueado")){
                                    //hidePDialog();
                                    gifImageView.stopAnimation();
                                    gifImageView.setVisibility(View.INVISIBLE);

                                    iniciar.setVisibility(View.VISIBLE);
                                    registrar.setVisibility(View.VISIBLE);

                                    usuario.setVisibility(View.VISIBLE);
                                    contra.setVisibility(View.VISIBLE);
                                    titulo.setVisibility(View.VISIBLE);
                                    texto2.setVisibility(View.VISIBLE);
                                    texto.setVisibility(View.VISIBLE);
                                    nuevo.setVisibility(View.VISIBLE);
                                    sesion.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "Este usuario ha sido bloqueado", Toast.LENGTH_SHORT).show();
                                }else{
                                    //hidePDialog();
                                    gifImageView.stopAnimation();
                                    gifImageView.setVisibility(View.INVISIBLE);

                                    iniciar.setVisibility(View.VISIBLE);
                                    registrar.setVisibility(View.VISIBLE);

                                    usuario.setVisibility(View.VISIBLE);
                                    contra.setVisibility(View.VISIBLE);
                                    titulo.setVisibility(View.VISIBLE);
                                    texto2.setVisibility(View.VISIBLE);
                                    texto.setVisibility(View.VISIBLE);
                                    nuevo.setVisibility(View.VISIBLE);
                                    sesion.setVisibility(View.VISIBLE);
                                    idUsuario = response;
                                    SharedPreferences prefs = getSharedPreferences("login",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString("id", idUsuario);
                                    editor.commit();
                                    Intent intent = new Intent(context, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //hidePDialog();
                            gifImageView.stopAnimation();
                            gifImageView.setVisibility(View.INVISIBLE);

                            iniciar.setVisibility(View.VISIBLE);
                            registrar.setVisibility(View.VISIBLE);

                            usuario.setVisibility(View.VISIBLE);
                            contra.setVisibility(View.VISIBLE);
                            titulo.setVisibility(View.VISIBLE);
                            texto2.setVisibility(View.VISIBLE);
                            texto.setVisibility(View.VISIBLE);
                            nuevo.setVisibility(View.VISIBLE);
                            sesion.setVisibility(View.VISIBLE);
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
            }else{
                try{
                    InputStream inputStream = getAssets().open("sin_internet.gif");
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    gifImageView2.setBytes(bytes);
                    gifImageView2.startAnimation();
                    gifImageView2.setVisibility(View.VISIBLE);
                    intentar.setVisibility(View.VISIBLE);

                    iniciar.setVisibility(View.INVISIBLE);
                    registrar.setVisibility(View.INVISIBLE);

                    usuario.setVisibility(View.INVISIBLE);
                    contra.setVisibility(View.INVISIBLE);
                    titulo.setVisibility(View.INVISIBLE);
                    texto2.setVisibility(View.INVISIBLE);
                    texto.setVisibility(View.INVISIBLE);
                    nuevo.setVisibility(View.INVISIBLE);
                    sesion.setVisibility(View.INVISIBLE);
                }
                catch (IOException ex)
                {

                }
                Toast.makeText(getApplicationContext(), "No hay conexion a internet", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
    public void intentar(View view){
        if(isOnline() == true){
            try{
                InputStream inputStream = getAssets().open("sin_internet.gif");
                byte[] bytes = IOUtils.toByteArray(inputStream);
                gifImageView2.setBytes(bytes);
                gifImageView2.startAnimation();
                gifImageView2.setVisibility(View.INVISIBLE);
                intentar.setVisibility(View.INVISIBLE);

                iniciar.setVisibility(View.VISIBLE);
                registrar.setVisibility(View.VISIBLE);

                usuario.setVisibility(View.VISIBLE);
                contra.setVisibility(View.VISIBLE);
                titulo.setVisibility(View.VISIBLE);
                texto2.setVisibility(View.VISIBLE);
                texto.setVisibility(View.VISIBLE);
                nuevo.setVisibility(View.VISIBLE);
                sesion.setVisibility(View.VISIBLE);
            }
            catch (IOException ex)
            {

            }
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

