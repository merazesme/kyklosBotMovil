package com.example.esmeralda.kyklosbotmovil;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import java.util.Calendar;
import java.util.Date;

public class misCupones extends AppCompatActivity {

    GridLayout cuerpo;
    ConnectivityManager con;
    NetworkInfo networkInfo;
    ImageView noInternet;
    ScrollView base;
    private GifImageView gifLoading;
    private Button btnConectarInter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cupones);

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

    //CONEXION A LA BD MEDIANTE WEB SERVICE: OBTENER CUPONES DEL USUARIO
    public void pantalla()
    {
        //DIBUJAR PANTALLA
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
            miTextView.setText("MIS CUPONES");
            miTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            miTextView.setTextColor(Color.rgb(70,70,70));
            miTextView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            miTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            miTextView.setPadding(20,40,20,20);

            //AGREGAR TEXTO A LA BASE ENCABEZADO
            encabezado.addView(miTextView);

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
            String url ="http://tunas.mztzone.com/tunas/apiEsme/misCupones/"+LoginActivity.idUsuario;

            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                gifLoading.stopAnimation();
                                gifLoading.setVisibility(View.INVISIBLE);
                                JSONArray myJsonArray = response.getJSONArray("misCupones");
                                for (int i=0; i<myJsonArray.length(); i++)
                                {
                                    JSONObject myJsonObject = myJsonArray.getJSONObject(i);
                                    String premio = myJsonObject.getString("Nombre");
                                    String categoria = myJsonObject.getString("Titulo");
                                    String fechaExp = myJsonObject.getString("FechaExp");
                                    String idCategoria = myJsonObject.getString("idCategoria");

                                    //CREAR CARTAS
                                    CardView carta = new CardView(getApplicationContext());
                                    FrameLayout.LayoutParams margenCartas = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

                                    margenCartas.setMargins(50,10,50,30);
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
                                    switch (Integer.parseInt(idCategoria))
                                    {
                                        case 1: //dulceria
                                            imagen.setImageResource(R.drawable.dulceria_100px);
                                            break;
                                        case 2: //taquilla
                                            imagen.setImageResource(R.drawable.proyector_100px);
                                            break;
                                        case 3: //multiple
                                            imagen.setImageResource(R.drawable.multiple);
                                            break;
                                    }
                                    FrameLayout.LayoutParams margenImagen = new FrameLayout.LayoutParams(150, 150);
                                    margenImagen.setMargins(0,30,0,40);
                                    imagen.setLayoutParams(margenImagen);

                                    //BASE PARA EL TEXTO DE LA CARTA
                                    LinearLayout contenedorTextoCarta = new LinearLayout(getApplicationContext());
                                    contenedorTextoCarta.setOrientation(LinearLayout.VERTICAL);
                                    contenedorCarta.setPadding(20,0,20,0);
                                    FrameLayout.LayoutParams margenTexto = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                                    margenTexto.setMargins(170,30,0,40);
                                    contenedorTextoCarta.setLayoutParams(margenTexto);

                                    //TITULO DE LA CARTA
                                    TextView txtTitulo = new TextView(getApplicationContext());
                                    txtTitulo.setTextColor(Color.BLACK);
                                    txtTitulo.setTextSize(18);
                                    txtTitulo.setText(premio);

                                    //FECHA DE EXPIRACION DEL CUPON
                                    SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yy");
                                    SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    TextView txtFechaExp = new TextView(getApplicationContext());
                                    TextView txtDiasRestantes = new TextView(getApplicationContext());
                                    try {
                                        //FECHA DE EXPIRACION

                                        SimpleDateFormat fechaExpFormat = new SimpleDateFormat("yyyy-MM-dd");

                                        //OBTENER LA FECHA ACTUAL
                                        Calendar fechaActual = Calendar.getInstance();
                                        int anio = fechaActual.get(Calendar.YEAR);
                                        int mes = fechaActual.get(Calendar.MONTH)+1;
                                        int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
                                        String hoy = String.valueOf(anio)+"-"+String.valueOf(mes)+"-"+String.valueOf(dia);
                                        Date fechaInicial=fechaExpFormat.parse(hoy);

                                        //PASAR A FORMATO DATE LA FECHA DE EXPIRACION
                                        Date fechaFinal=fechaExpFormat.parse(fechaExp);

                                        //CALCULAR DIAS DE DIFERENCIA
                                        int diasRestantes=(int) ((fechaFinal.getTime()-fechaInicial.getTime())/86400000);

                                        //DARLE FORMATO A LA FECHA DE EXPIRACION
                                        txtFechaExp.setTextSize(13);
                                        if (diasRestantes<=2)
                                            txtFechaExp.setTextColor(getResources().getColor(R.color.colorAccent));
                                        else if (diasRestantes>2 && diasRestantes<=8)
                                            txtFechaExp.setTextColor(getResources().getColor(R.color.colorYellowAccent));
                                        else if (diasRestantes>8)
                                            txtFechaExp.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                                        txtFechaExp.setText(formateador.format(fechaFinal));

                                        //MOSTRAR LOS DIAS HABILES RESTANTES DEL CUPÓN
                                        txtDiasRestantes.setText(diasRestantes + " días para que expire el cupón");

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Toast.makeText(misCupones.this, "Error al obtener fecha de expiración", Toast.LENGTH_SHORT).show();
                                    }

                                    //AGREGAR IMAGEN A LA CARTA
                                    contenedorCarta.addView(imagen);
                                    //AGREGAR TITULO A LA BASE DEL TEXTO DE LA CARTA
                                    contenedorTextoCarta.addView(txtTitulo);
                                    //AGREGAR FECHA DE EXPIRACIÓN A LA BASE DEL TEXTO DE LA CARTA
                                    contenedorTextoCarta.addView(txtFechaExp);
                                    //AGREGAR DIAS RESTANTES DE EXPIRACIÓN A LA BASE DEL TEXTO DE LA CARTA
                                    contenedorTextoCarta.addView(txtDiasRestantes);
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
                            Toast.makeText(getApplicationContext(), "Error al cargar cupones", Toast.LENGTH_SHORT).show();
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
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            Toast.makeText(this, "Error al cargar cupones", Toast.LENGTH_SHORT).show();
        }

    }
}
