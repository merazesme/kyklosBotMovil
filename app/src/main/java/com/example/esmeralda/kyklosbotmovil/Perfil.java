package com.example.esmeralda.kyklosbotmovil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.felipecsl.gifimageview.library.GifImageView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class Perfil extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    private TextView txtNombre;
    private TextView txtCorreo;
    private TextView txtUsuario;
    private TextView txtPuntos;
    private TextView txtCupones;
    private String nombre="", apellidos="", nickname="", imagen="", puntos="", correo="", contra="";
    private ImageView foto;
    private String idUsuario="3";
    public GifImageView gifImageView;
    public GifImageView gifImageView2;
    public CardView card;
    public LinearLayout layout2;
    public Button boton;
    public Button intentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txtNombre=(TextView)findViewById(R.id.txtNombre);
        txtCorreo=(TextView)findViewById(R.id.txtCorreo);
        txtUsuario=(TextView)findViewById(R.id.txtUsuario);
        txtPuntos=(TextView)findViewById(R.id.txtPuntos);
        txtCupones=(TextView)findViewById(R.id.txtCupones);
        foto=(ImageView) findViewById(R.id.imagenUsuario);
        card = (CardView)findViewById(R.id.card);
        layout2=(LinearLayout) findViewById(R.id.layout2);
        //relativeuno = (RelativeLayout)findViewById(R.id.relativeuno);
        boton = (Button) findViewById(R.id.boton);
        intentar = (Button)findViewById(R.id.intentar);
        intentar.setVisibility(View.INVISIBLE);
        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        gifImageView2 = (GifImageView)findViewById(R.id.gifImageView);

        gifImageView.setVisibility(View.GONE);
        gifImageView2.setVisibility(View.GONE);

        //Ocultar Bar
        //getSupportActionBar().hide();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        try {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Acá continuamos el procesos deseado a hacer
                } else {
                    // El usuario no necesitas explicación, puedes solicitar el permiso:
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    //
                }
            }

            this.CargarUsuario();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
            }else if (id == R.id.info)
            {
                Intent pantallaInformacion = new Intent(this, Informacion.class);
                startActivity(pantallaInformacion);
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


    public void CargarUsuario (){
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
                    txtUsuario.setVisibility(View.INVISIBLE);
                    txtPuntos.setVisibility(View.INVISIBLE);
                    txtCupones.setVisibility(View.INVISIBLE);
                    foto.setVisibility(View.INVISIBLE);
                    card.setVisibility(View.INVISIBLE);
                    layout2.setVisibility(View.INVISIBLE);
                    boton.setVisibility(View.INVISIBLE);

                } catch (IOException ex) {

                }

                //Agarrar datos usuario
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "http://tunas.mztzone.com/tunas/apiAdriana/usuario/" + idUsuario + "";

                // Request a string response from the provided URL.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray myJsonArray = response.getJSONArray("usuarios");
                                    JSONObject myJsonObject = myJsonArray.getJSONObject(0);
                                    nombre = myJsonObject.getString("Nombre");
                                    apellidos = myJsonObject.getString("Apellidos");
                                    nickname = myJsonObject.getString("Usuario");
                                    imagen = myJsonObject.getString("Imagen");
                                    puntos = myJsonObject.getString("Puntos");
                                    imagen = myJsonObject.getString("Imagen");
                                    correo = myJsonObject.getString("Correo");
                                    contra = myJsonObject.getString("Contra");
                                    String nombres = nombre + " " + apellidos;
                                    txtNombre.setText(String.valueOf(nombres));
                                    txtCorreo.setText(String.valueOf(correo));
                                    txtUsuario.setText(String.valueOf(nickname));
                                    txtPuntos.setText(String.valueOf(puntos));
                                    ponerImagen(imagen);

                                    //Agarrar cupones canjeados
                                    RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
                                    String url2 = "http://tunas.mztzone.com/tunas/apiAdriana/cuponesCanjeados/" + idUsuario + "";

                                    // Request a string response from the provided URL.
                                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                                            (Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        JSONArray myJsonArray = response.getJSONArray("usuarios");
                                                        txtCupones.setText(String.valueOf(myJsonArray.length()));

                                                        //Detiene loading
                                                        gifImageView.stopAnimation();
                                                        gifImageView.setVisibility(View.GONE);
                                                        gifImageView2.setVisibility(View.GONE);

                                                        txtNombre.setVisibility(View.VISIBLE);
                                                        txtCorreo.setVisibility(View.VISIBLE);
                                                        txtPuntos.setVisibility(View.VISIBLE);
                                                        txtUsuario.setVisibility(View.VISIBLE);
                                                        txtCupones.setVisibility(View.VISIBLE);
                                                        foto.setVisibility(View.VISIBLE);
                                                        card.setVisibility(View.VISIBLE);
                                                        layout2.setVisibility(View.VISIBLE);
                                                        boton.setVisibility(View.VISIBLE);


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
                                    queue2.add(jsonObjectRequest2);

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
            else {
                try{
                    InputStream inputStream = getAssets().open("sin_internet.gif");
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    gifImageView2.setBytes(bytes);
                    gifImageView2.startAnimation();
                    gifImageView2.setVisibility(View.VISIBLE);
                    intentar.setVisibility(View.VISIBLE);

                    txtNombre.setVisibility(View.INVISIBLE);
                    txtCorreo.setVisibility(View.INVISIBLE);
                    txtUsuario.setVisibility(View.INVISIBLE);
                    txtPuntos.setVisibility(View.INVISIBLE);
                    txtCupones.setVisibility(View.INVISIBLE);
                    foto.setVisibility(View.INVISIBLE);
                    card.setVisibility(View.INVISIBLE);
                    layout2.setVisibility(View.INVISIBLE);
                    boton.setVisibility(View.INVISIBLE);
                }
                catch (IOException ex)
                {

                }
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

                txtNombre.setVisibility(View.VISIBLE);
                txtCorreo.setVisibility(View.VISIBLE);
                txtPuntos.setVisibility(View.VISIBLE);
                txtUsuario.setVisibility(View.VISIBLE);
                txtCupones.setVisibility(View.VISIBLE);
                foto.setVisibility(View.VISIBLE);
                card.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                boton.setVisibility(View.VISIBLE);
            }
            catch (IOException ex)
            {

            }
        }
    }

    public void editarPerfil (View v){
        try {
            if(txtNombre.length()!=0 && txtCorreo.length()!=0 && txtUsuario.length()!=0 &&
                    txtCorreo.length()!=0 && txtPuntos.length()!=0 && txtCupones.length()!=0
                        && foto.getDrawable()!=null) {
                Intent ventana = new Intent(this, EditarPerfil.class);
                ventana.putExtra("idUsuario", idUsuario);
                ventana.putExtra("nombre", nombre);
                ventana.putExtra("apellidos", apellidos);
                ventana.putExtra("nickname", nickname);
                ventana.putExtra("imagen", imagen);
                ventana.putExtra("correo", correo);
                ventana.putExtra("contra", contra);
                startActivity(ventana);
            }
            else {
                Toast.makeText(this, "Espera a que cargen los datos", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        }
        else {*/
            foto.setImageBitmap(originalBitmap);
        //}
    }
}
