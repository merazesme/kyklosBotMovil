package com.example.esmeralda.kyklosbotmovil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class EditarPerfil extends AppCompatActivity implements BottomSheetMenuFragment.BottomSheetListener {
    private TextView txtNombre;
    private TextView txtApellidos;
    private TextView txtNombreUsuario;
    private TextView txtCorreo;
    private TextView txtContra;
    private ImageView foto;
    private TextView salir;
    private String idUsuario="", nombre="", apellidos="", nickname="", imagen="", correo="", contra="";
    private String nombreUsuarios;
    private String nombreUsuariosFinal[];
    private int lo=0;
    private Intent ventana;
    public GifImageView gifImageView;
    public GifImageView gifImageView2;
    public Button intentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        txtNombre=(TextView)findViewById(R.id.txtNombre);
        txtApellidos=(TextView)findViewById(R.id.txtApellidos);
        txtNombreUsuario=(TextView)findViewById(R.id.txtNombreUsuario);
        txtCorreo=(TextView)findViewById(R.id.txtCorreo);
        txtContra=(TextView)findViewById(R.id.txtContra);
        foto=(ImageView) findViewById(R.id.imagenUsuario);
        salir=(TextView)findViewById(R.id.salir);

        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        gifImageView2 = (GifImageView)findViewById(R.id.gifImageView);

        ventana = new Intent(this, Perfil.class);

        intentar = (Button)findViewById(R.id.intentar);

        intentar.setVisibility(View.GONE);

        try {
            Bundle bundle = getIntent().getExtras();
            idUsuario = bundle.getString("idUsuario");
            nombre = bundle.getString("nombre");
            apellidos = bundle.getString("apellidos");
            nickname = bundle.getString("nickname");
            imagen = bundle.getString("imagen");
            correo = bundle.getString("correo");
            contra = bundle.getString("contra");

            //Toast.makeText(this, idUsuario, Toast.LENGTH_LONG).show();

            txtNombre.setText(String.valueOf(nombre));
            txtApellidos.setText(String.valueOf(apellidos));
            txtNombreUsuario.setText(String.valueOf(nickname));
            txtCorreo.setText(String.valueOf(correo));
            txtContra.setText(String.valueOf(contra));

            ponerImagen(imagen);

            //Editar Toolbar
            ActionBar actionBar = getSupportActionBar();
            if(actionBar!=null){
                //actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("Editar perfil");
            }

            nombreUsuario();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //@OnClick(R.id.imagenUsuario)
    public void onClickFoto(View v) {
        BottomSheetMenuFragment bottomSheet = new BottomSheetMenuFragment();
        bottomSheet.show(getSupportFragmentManager(), "BottomSheetMenuFragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editarperfil,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id=item.getItemId();

            if(id==R.id.guardar){
                //Toast.makeText(this,"Nickname: " + nickname, Toast.LENGTH_LONG).show();
                if(txtNombre.length()!=0 && txtApellidos.length()!=0 && txtNombreUsuario.length()!=0 && txtCorreo.length()!=0 && txtContra.length()!=0) {
                    if(nickname.equals(txtNombreUsuario.getText().toString())){
                        //Toast.makeText(this,"DE LA CAJA: " + txtNombreUsuario.getText().toString(), Toast.LENGTH_LONG).show();
                        //txtNombreUsuario.setFocusable(true);
                        actualizarPerfil();
                    }
                    else {
                        int b=0;
                        for (int i=0; i<nombreUsuariosFinal.length; i++) {
                            if (nombreUsuariosFinal[i].equals(txtNombreUsuario.getText().toString())) {
                                b=b+1;
                            }
                        }
                        if(b==1){
                            Toast.makeText(this,"Ya existe ese nombre de usuario, intenta con otro..", Toast.LENGTH_LONG).show();
                        }
                        else{
                            actualizarPerfil();
                        }
                    }

                }
                else {
                    Toast.makeText(this, "Favor de llenar todos los campos!", Toast.LENGTH_LONG).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onButtonClicked(String text) {
        try {
            if(text.equals("eliminar")){
                imagen="1";
                ponerImagen(imagen);
            }
            else {
                imagen=text;
                ponerImagen(text);
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

                txtNombre.setVisibility(View.INVISIBLE);
                txtCorreo.setVisibility(View.INVISIBLE);
                txtApellidos.setVisibility(View.INVISIBLE);
                txtNombreUsuario.setVisibility(View.INVISIBLE);
                txtContra.setVisibility(View.INVISIBLE);
                foto.setVisibility(View.INVISIBLE);
                salir.setVisibility(View.INVISIBLE);
            }
            catch (IOException ex)
            {

            }
        }
    }

    public void ponerImagen(String text){
        String uri;
        if(imagen.equals("1") || imagen.equals("")) {
            uri = "@drawable/logo";
        }
        else {
            uri = "@drawable/"+text+"";
        }

        //extraemos el drawable en un bitmap
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable originalDrawable = ContextCompat.getDrawable(getApplicationContext(), imageResource);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        /*if(imagen.equals("1") || imagen.equals("")) {
            //creamos el drawable redondeado
            RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);
            //asignamos el CornerRadius
            roundedDrawable.setCornerRadius(originalBitmap.getHeight());
            //asigamos al imageview
            foto.setImageDrawable(roundedDrawable);
        }*/
        //else {
            foto.setImageBitmap(originalBitmap);
        //}
    }

    public void actualizarPerfil(){
        //CONEXION A LA BD MEDIANTE WEB SERVICE
        try {
            if (isOnline()) {
                try {
                    InputStream inputStream = getAssets().open("loading.gif");
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    gifImageView.setBytes(bytes);
                    gifImageView.startAnimation();
                    gifImageView.setVisibility(View.VISIBLE);

                    txtNombre.setVisibility(View.INVISIBLE);
                    txtCorreo.setVisibility(View.INVISIBLE);
                    txtApellidos.setVisibility(View.INVISIBLE);
                    txtContra.setVisibility(View.INVISIBLE);
                    txtNombreUsuario.setVisibility(View.INVISIBLE);
                    foto.setVisibility(View.INVISIBLE);
                    salir.setVisibility(View.INVISIBLE);

                } catch (IOException ex) {

                }

                //Actualizar perfil usuario
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://tunas.mztzone.com/tunas/apiAdriana/actualizarPerfil/" + idUsuario + "";
                StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // response

                                //Detiene loading
                                gifImageView.stopAnimation();
                                gifImageView.setVisibility(View.INVISIBLE);

                                txtNombre.setVisibility(View.INVISIBLE);
                                txtCorreo.setVisibility(View.INVISIBLE);
                                txtApellidos.setVisibility(View.INVISIBLE);
                                txtNombreUsuario.setVisibility(View.INVISIBLE);
                                txtContra.setVisibility(View.INVISIBLE);
                                foto.setVisibility(View.INVISIBLE);
                                salir.setVisibility(View.INVISIBLE);

                                Toast.makeText(getApplicationContext(), "Se actualizaron tus datos exitosamente!", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(ventana);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                            }
                        }
                ) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Nombre", txtNombre.getText().toString());
                        params.put("Apellidos", txtApellidos.getText().toString());
                        params.put("Correo", txtCorreo.getText().toString());
                        params.put("Usuario", txtNombreUsuario.getText().toString());
                        params.put("Contra", txtContra.getText().toString());
                        params.put("Imagen", imagen);

                        return params;
                    }

                };

                queue.add(putRequest);
            }
            else{
                try{
                    InputStream inputStream = getAssets().open("sin_internet.gif");
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    gifImageView2.setBytes(bytes);
                    gifImageView2.startAnimation();
                    gifImageView2.setVisibility(View.VISIBLE);
                    intentar.setVisibility(View.VISIBLE);

                    txtNombre.setVisibility(View.INVISIBLE);
                    txtCorreo.setVisibility(View.INVISIBLE);
                    txtApellidos.setVisibility(View.INVISIBLE);
                    txtNombreUsuario.setVisibility(View.INVISIBLE);
                    txtContra.setVisibility(View.INVISIBLE);
                    foto.setVisibility(View.INVISIBLE);
                    salir.setVisibility(View.INVISIBLE);
                }
                catch (IOException ex)
                {

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nombreUsuario(){
        try {
            if (isOnline()) {
                try {
                    InputStream inputStream = getAssets().open("loading.gif");
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    gifImageView.setBytes(bytes);
                    gifImageView.startAnimation();
                    gifImageView.setVisibility(View.VISIBLE);

                    txtNombre.setVisibility(View.INVISIBLE);
                    txtCorreo.setVisibility(View.INVISIBLE);
                    txtApellidos.setVisibility(View.INVISIBLE);
                    txtContra.setVisibility(View.INVISIBLE);
                    txtNombreUsuario.setVisibility(View.INVISIBLE);
                    foto.setVisibility(View.INVISIBLE);
                    salir.setVisibility(View.INVISIBLE);

                } catch (IOException ex) {

                }

                //Agarrar maquinas disponibles
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "http://tunas.mztzone.com/tunas/apiAdriana/nombreUsuario";

                // Request a string response from the provided URL.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray myJsonArray = response.getJSONArray("nombres");
                                    int a = myJsonArray.length();
                                    nombreUsuariosFinal = new String[a];
                                    for (int i = 0; i < a; i++) {
                                        JSONObject myJsonObject = myJsonArray.getJSONObject(i);
                                        nombreUsuarios = myJsonObject.getString("Usuario");
                                        nombreUsuariosFinal[i] = new String(nombreUsuarios);
                                    }
                                    lo = nombreUsuariosFinal.length;
                                    //Toast.makeText(getApplicationContext(),"Nombre usuarios: " + lo, Toast.LENGTH_LONG).show();

                                    //Detiene loading
                                    gifImageView.stopAnimation();
                                    gifImageView.setVisibility(View.INVISIBLE);

                                    txtNombre.setVisibility(View.VISIBLE);
                                    txtCorreo.setVisibility(View.VISIBLE);
                                    txtApellidos.setVisibility(View.VISIBLE);
                                    txtNombreUsuario.setVisibility(View.VISIBLE);
                                    txtContra.setVisibility(View.VISIBLE);
                                    foto.setVisibility(View.VISIBLE);
                                    salir.setVisibility(View.VISIBLE);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                            }
                        });

                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);
            }
            else{
                try{
                    InputStream inputStream = getAssets().open("sin_internet.gif");
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    gifImageView2.setBytes(bytes);
                    gifImageView2.startAnimation();
                    gifImageView2.setVisibility(View.VISIBLE);
                    intentar.setVisibility(View.VISIBLE);

                    txtNombre.setVisibility(View.INVISIBLE);
                    txtCorreo.setVisibility(View.INVISIBLE);
                    txtApellidos.setVisibility(View.INVISIBLE);
                    txtNombreUsuario.setVisibility(View.INVISIBLE);
                    txtContra.setVisibility(View.INVISIBLE);
                    foto.setVisibility(View.INVISIBLE);
                    salir.setVisibility(View.INVISIBLE);
                }
                catch (IOException ex)
                {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void salir(View view) {
        try {
            finish();
        } catch (Exception e) {
        }
    }
}
