package com.example.esmeralda.kyklosbotmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    private TextView nombre,apellido,usuario,contra,contra2,email,titulo;
    private Button iniciar, iniciar2,intentar;
    public static String Usuario;
    public static String Contra;
    public static int banderaPrimeraVez=0;
    public int banderaEmail = 0, banderaNombre = 0,banderaApellido = 0,banderaUsuario = 0, banderaContra = 0;
    public GifImageView gifImageView;
    public GifImageView gifImageView2;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        banderaPrimeraVez=0;
        final Context context = this;

        nombre = (TextView)findViewById(R.id.txtnombre);
        apellido = (TextView)findViewById(R.id.txtapellidos);
        usuario = (TextView)findViewById(R.id.txtusername);
        contra = (TextView)findViewById(R.id.txtcontraseña);
        contra2 = (TextView)findViewById(R.id.txtcontraseña2);
        email = (TextView)findViewById(R.id.txtemail);

        iniciar = (Button)findViewById(R.id.btnlisto2);
        intentar = (Button)findViewById(R.id.intentar);
        intentar.setVisibility(View.INVISIBLE);
        titulo = (TextView)findViewById(R.id.titulo);

        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        gifImageView2 = (GifImageView)findViewById(R.id.gifImageView);

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidNombre(nombre.getText().toString().trim())){
                    banderaNombre = 1;
                }else {
                    nombre.setError("Solo se permiten letras");
                    banderaNombre = 0;
                }
            }
        });

        apellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidNombre(apellido.getText().toString().trim())){
                    banderaApellido = 1;
                }else {
                    apellido.setError("Solo se permiten letras");
                    banderaApellido = 0;
                }
            }
        });

        usuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidUsuario(usuario.getText().toString().trim())){
                    banderaUsuario = 1;
                }else {
                    usuario.setError("Solo se permiten letras y números");
                    banderaUsuario = 0;
                }
            }
        });

        contra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isValidUsuario(contra.getText().toString().trim())){
                    banderaContra = 1;
                }else {
                    contra.setError("Solo se permiten letras y números");
                    banderaContra = 0;
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isValidEmailId(email.getText().toString().trim())){
                    banderaEmail = 1;
                }else{
                    email.setError("Formato de email incorrecto");
                }
            }
        });

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Finalizar(context);
            }
        });

    }

    public void Finalizar(final Context context){
        try {
            if (isOnline() == true) {
                String Nombre = nombre.getText().toString();
                String Apellido = apellido.getText().toString();
                String Usuario = usuario.getText().toString();
                String Contra = contra.getText().toString();
                String Contra2 = contra2.getText().toString();
                String Email = email.getText().toString();
                if (Nombre.length() != 0 &&
                        Apellido.length() != 0 &&
                        Usuario.length() != 0 &&
                        Contra.length() != 0 &&
                        Contra2.length() != 0 &&
                        Email.length() != 0) {
                    if (Contra.equals(Contra2))
                    {
                        if (banderaNombre == 1 &&
                                banderaApellido == 1 &&
                                banderaUsuario == 1 &&
                                banderaContra == 1 &&
                                banderaEmail == 1) {
                            banderaEmail = 0; banderaContra = 0; banderaUsuario = 0; banderaApellido = 0; banderaNombre = 0;
                            try {
                                InputStream inputStream = getAssets().open("loading.gif");
                                byte[] bytes = IOUtils.toByteArray(inputStream);
                                gifImageView.setBytes(bytes);
                                gifImageView.startAnimation();
                                gifImageView.setVisibility(View.VISIBLE);

                                iniciar.setVisibility(View.INVISIBLE);

                                nombre.setVisibility(View.INVISIBLE);
                                apellido.setVisibility(View.INVISIBLE);
                                usuario.setVisibility(View.INVISIBLE);
                                contra.setVisibility(View.INVISIBLE);
                                contra2.setVisibility(View.INVISIBLE);
                                email.setVisibility(View.INVISIBLE);
                                //titulo.setVisibility(View.INVISIBLE);
                            } catch (IOException ex) {

                            }
                            String url = "http://tunas.mztzone.com/tunas/apiJesus/agregar/add";
                            RequestQueue requestQueue = Volley.newRequestQueue(this);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.trim().equals("error")) {
                                        gifImageView.stopAnimation();
                                        gifImageView.setVisibility(View.INVISIBLE);

                                        iniciar.setVisibility(View.VISIBLE);

                                        nombre.setVisibility(View.VISIBLE);
                                        apellido.setVisibility(View.VISIBLE);
                                        usuario.setVisibility(View.VISIBLE);
                                        contra.setVisibility(View.VISIBLE);
                                        contra2.setVisibility(View.VISIBLE);
                                        email.setVisibility(View.VISIBLE);
                                        //titulo.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "Ya existe este nombre usuario", Toast.LENGTH_SHORT).show();
                                    } else {
                                        LoginActivity.idUsuario = response.trim();
                                        SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("id", LoginActivity.idUsuario);
                                        editor.commit();
                                        gifImageView.stopAnimation();
                                        gifImageView.setVisibility(View.INVISIBLE);

                                        iniciar.setVisibility(View.VISIBLE);

                                        nombre.setVisibility(View.VISIBLE);
                                        apellido.setVisibility(View.VISIBLE);
                                        usuario.setVisibility(View.VISIBLE);
                                        contra.setVisibility(View.VISIBLE);
                                        contra2.setVisibility(View.VISIBLE);
                                        email.setVisibility(View.VISIBLE);
                                        //titulo.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(), "Registro agregado " + LoginActivity.idUsuario, Toast.LENGTH_SHORT).show();
                                        banderaPrimeraVez=1;
                                        Intent intent = new Intent(context, howTo.class);
                                        finish();
                                        startActivity(intent);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    gifImageView.stopAnimation();
                                    gifImageView.setVisibility(View.INVISIBLE);

                                    iniciar.setVisibility(View.VISIBLE);

                                    nombre.setVisibility(View.VISIBLE);
                                    apellido.setVisibility(View.VISIBLE);
                                    usuario.setVisibility(View.VISIBLE);
                                    contra.setVisibility(View.VISIBLE);
                                    contra2.setVisibility(View.VISIBLE);
                                    email.setVisibility(View.VISIBLE);
                                    //titulo.setVisibility(View.VISIBLE);
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
                                    params.put("TipoUsuario_idTipoUsuario", "3");
                                    return params;
                                }
                            };
                            requestQueue.add(stringRequest);
                        }else {
                            banderaEmail = 0; banderaContra = 0; banderaUsuario = 0; banderaApellido = 0; banderaNombre = 0;
                            Toast.makeText(getApplicationContext(), "Formatos invalidos", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                        contra.setText("");
                        contra2.setText("");
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Llene todo los campos", Toast.LENGTH_SHORT).show();
                }
            }else{
                try{
                    /*scroller.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);*/
                    InputStream inputStream = getAssets().open("sin_internet.gif");
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    gifImageView2.setBytes(bytes);
                    gifImageView2.startAnimation();
                    gifImageView2.setVisibility(View.VISIBLE);
                    intentar.setVisibility(View.VISIBLE);

                    iniciar.setVisibility(View.INVISIBLE);

                    nombre.setVisibility(View.INVISIBLE);
                    apellido.setVisibility(View.INVISIBLE);
                    usuario.setVisibility(View.INVISIBLE);
                    contra.setVisibility(View.INVISIBLE);
                    contra2.setVisibility(View.INVISIBLE);
                    email.setVisibility(View.INVISIBLE);
                    //titulo.setVisibility(View.INVISIBLE);

                    /*iniciar.setVisibility(View.GONE);

                    nombre.setVisibility(View.GONE);
                    apellido.setVisibility(View.GONE);
                    usuario.setVisibility(View.GONE);
                    contra.setVisibility(View.GONE);
                    contra2.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    titulo.setVisibility(View.GONE);*/
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

                nombre.setVisibility(View.VISIBLE);
                apellido.setVisibility(View.VISIBLE);
                usuario.setVisibility(View.VISIBLE);
                contra.setVisibility(View.VISIBLE);
                contra2.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                //titulo.setVisibility(View.VISIBLE);
            }
            catch (IOException ex)
            {

            }
        }
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    private boolean isValidNombre(String nombre){

        return Pattern.compile("^[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+(\\s*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]*)*[a-zA-ZÀ-ÿ\\u00f1\\u00d1]+$").matcher(nombre).matches();
    }
    private boolean isValidUsuario(String usuario){

        return Pattern.compile("^[a-zA-Z 0-9]*$").matcher(usuario).matches();
    }
}
