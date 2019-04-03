package com.example.esmeralda.kyklosbotmovil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class historialPuntosCanjeados extends AppCompatActivity {

    GridLayout cuerpo;
    ImageView noInternet;
    ConnectivityManager con;
    NetworkInfo networkInfo;
    ScrollView base;
    private GifImageView gifLoading;
    private Button btnConectarInter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_puntos_canjeados);
        //Internet
        noInternet = (ImageView)findViewById(R.id.noInternet);
        noInternet.setVisibility(View.INVISIBLE);
        btnConectarInter = (Button)findViewById(R.id.btnConectar);

        //CHECAR LA CONEXION A INTERNET
        con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = con.getActiveNetworkInfo();
        //ELEMENTOS DE LA VISTA
        base = (ScrollView)findViewById(R.id.viewScroll);

        gifLoading = (GifImageView)findViewById(R.id.gifImageView);

        conectarInternet(null);

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

    public void conectarInternet(View v)
    {
        //CHECAR LA CONEXION A INTERNET
        con = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = con.getActiveNetworkInfo();
        noInternet.setVisibility(View.INVISIBLE);
        btnConectarInter.setVisibility(View.INVISIBLE);
        if(networkInfo!=null && networkInfo.isConnected())
        {
            noInternet.setVisibility(View.GONE);
            btnConectarInter.setVisibility(View.GONE);
            pantalla();
        }else
        {
            gifLoading.stopAnimation();
            gifLoading.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
            btnConectarInter.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Conectese a internet", Toast.LENGTH_SHORT).show();
        }
    }

    //CONEXION A LA BD MEDIANTE WEB SERVICE: HISTORIAL - OBTENER LOS PUNTOS QUE EL USUARIO HA CANJEADO
    public void pantalla()
    {
        try {
            InputStream inputStream = null;
            inputStream = getAssets().open("loading.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gifLoading.setBytes(bytes);
            gifLoading.setVisibility(View.VISIBLE);
            gifLoading.startAnimation();
        } catch (IOException e) {

        }
        try {
            //BASE PRINCIPAL
            ScrollView scrollView = new ScrollView(getApplicationContext());

            //BASE SECUNDARIA LINEAR
            LinearLayout contenedor = new LinearLayout(getApplicationContext());
            contenedor.setOrientation(LinearLayout.VERTICAL);

            //BASE ENCABEZADO
            RelativeLayout encabezado = new RelativeLayout(getApplicationContext());
            encabezado.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

            //TEXTO ENCABEZADO
            TextView miTextView = new TextView(getApplicationContext());
            miTextView.setText("PUNTOS CANJEADOS");
            miTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            miTextView.setTextColor(Color.rgb(70,70,70));
            miTextView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            miTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            miTextView.setPadding(20,40,20,20);

            //TEXTO ENCABEZADO
            TextView miTextView2 = new TextView(getApplicationContext());
            miTextView2.setText("HISTORIAL");
            miTextView2.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            miTextView2.setTextColor(Color.rgb(70,70,70));
            miTextView2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            miTextView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            miTextView2.setPadding(20,140,20,20);

            //AGREGAR TEXTO A LA BASE ENCABEZADO
            encabezado.addView(miTextView);
            encabezado.addView(miTextView2);

            //AGREGAR BASE ENCABEZADO A BASE SECUNDARIA
            contenedor.addView(encabezado);

            //BASE CUERPO
            cuerpo = new GridLayout(getApplicationContext());
            FrameLayout.LayoutParams parametros = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cuerpo.setLayoutParams(parametros);
            cuerpo.setColumnCount(1);
            cuerpo.setAlignmentMode(GridLayout.ALIGN_MARGINS);
            cuerpo.setColumnOrderPreserved(false);

            //CONEXION A LA BD MEDIANTE WEB SERVICE: OBTENER CUPONES DEL CINE

            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://tunas.mztzone.com/tunas/apiEsme/historialPuntosCanjeados/"+LoginActivity.idUsuario;

            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                gifLoading.stopAnimation();
                                gifLoading.setVisibility(View.INVISIBLE);
                                JSONArray myJsonArray = response.getJSONArray("historialPuntosCanjeados");
                                for (int i=0; i<myJsonArray.length(); i++)
                                {
                                    JSONObject myJsonObject = myJsonArray.getJSONObject(i);
                                    String cantidad = myJsonObject.getString("Cantidad");
                                    String fecha = myJsonObject.getString("Fecha");
                                    String nombre = myJsonObject.getString("Nombre");

                                    //CREAR CARTAS
                                    CardView carta = new CardView(getApplicationContext());
                                    FrameLayout.LayoutParams margenCartas = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

                                    margenCartas.setMargins(50,30,50,30);
                                    carta.setLayoutParams(margenCartas);
                                    carta.setCardElevation(15);
                                    carta.setRadius(1500);
                                    carta.setBackgroundColor(Color.WHITE);

                                    //BASE DE LA CARTA
                                    RelativeLayout contenedorCarta = new RelativeLayout(getApplicationContext());
                                    contenedorCarta.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                                    contenedorCarta.setPadding(20,10,20,10);

                                    //CONTENIDO DE LA CARTA

                                    //IMAGEN
                                    ImageView imagen = new ImageView(getApplicationContext());
                                    imagen.setImageResource(R.drawable.canjear_tickets_100px);
                                    FrameLayout.LayoutParams margenImagen = new FrameLayout.LayoutParams(150, 150);
                                    margenImagen.setMargins(0,30,0,40);
                                    imagen.setLayoutParams(margenImagen);

                                    //BASE PARA EL TEXTO DE LA CARTA
                                    LinearLayout contenedorTextoCarta = new LinearLayout(getApplicationContext());
                                    contenedorTextoCarta.setOrientation(LinearLayout.VERTICAL);
                                    contenedorCarta.setPadding(20,0,20,0);
                                    FrameLayout.LayoutParams margenTexto = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                                    margenTexto.setMargins(150,30,0,40);
                                    contenedorTextoCarta.setLayoutParams(margenTexto);

                                    //PUNTOS OBTENIDOS
                                    TextView txtPuntos = new TextView(getApplicationContext());
                                    txtPuntos.setTextColor(Color.BLACK);
                                    txtPuntos.setTextSize(18);
                                    txtPuntos.setText(cantidad+" puntos canjeados");

                                    //FECHA
                                    SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yy");
                                    SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    TextView txtFecha = new TextView(getApplicationContext());
                                    try {
                                        Date date = parseador.parse(fecha);
                                        txtFecha.setTextColor(Color.BLACK);
                                        txtFecha.setTextSize(15);
                                        txtFecha.setText(formateador.format(date));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    //UBICACION
                                    TextView txtUbicacion = new TextView(getApplicationContext());
                                    txtUbicacion.setTextColor(Color.BLACK);
                                    txtUbicacion.setTextSize(14);
                                    txtUbicacion.setText(nombre);


                                    //AGREGAR IMAGEN A LA CARTA
                                    contenedorCarta.addView(imagen);
                                    //AGREGAR TITULO A LA BASE DEL TEXTO DE LA CARTA
                                    contenedorTextoCarta.addView(txtPuntos);
                                    //AGREGAR FECHA A LA BASE DEL TEXTO DE LA CARTA
                                    contenedorTextoCarta.addView(txtFecha);
                                    //AGREGAR UBICACION A LA BASE DEL TEXTO DE LA CARTA
                                    contenedorTextoCarta.addView(txtUbicacion);
                                    //AGREGAR BASE DEL TEXTO DE LA CARTA A LA CARTA
                                    contenedorCarta.addView(contenedorTextoCarta);
                                    //AGREGAR EL CONTENIDO A LA CARTA
                                    carta.addView(contenedorCarta);
                                    //AGREGAR CARTA A LA BASE CUERPO
                                    cuerpo.addView(carta);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Toast.makeText(getApplicationContext(), "Error los datos", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);

            //AGREGAR EL CUERPO A LA BASE SECUNDARIA
            contenedor.addView(cuerpo);

            //AGREGAR BASE SECUNDARIA A LA BASE PRINCIPAL
            scrollView.addView(contenedor);

            //AGREGAR LA BASE PRINCIPAL A LA PANTALLA
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addContentView(scrollView, params);
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar historial", Toast.LENGTH_SHORT).show();
        }

    }
}
