package com.example.esmeralda.kyklosbotmovil;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class cupones extends AppCompatActivity {

    Dialog myDialog;
    GridLayout cuerpo;
    TextView txterror;
    String puntos, puntosUsuario, idPremio,idPremioSelected;
    int u,p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupones);
        consultarPuntosUsuario();
        pantalla();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuopciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id==R.id.hPuntosObtenidos)
            {
                Intent pantallahPuntosObtenidos = new Intent(this, historialPuntosObtenidos.class);
                startActivity(pantallahPuntosObtenidos);
            }
            else if (id == R.id.hPuntosCanjeados)
            {
                Intent pantallahPuntosCanjeados = new Intent(this, historialPuntosCanjeados.class);
                startActivity(pantallahPuntosCanjeados);
            }
            else if (id == R.id.menuPrincipal)
            {
                Intent pantallaPrincipal = new Intent(this, MainActivity.class);
                startActivity(pantallaPrincipal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }
    public void consultarPuntosUsuario()
    {
        //CONEXION A LA BD MEDIANTE WEB SERVICE: OBTENER PUNTOS USUARIO

        // Instantiate the RequestQueue.
        RequestQueue queuePuntosUsuario = Volley.newRequestQueue(this);
        String urlUsuario ="http://tunas.mztzone.com/tunas/apiEsme/puntos/2";
        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequestPuntosUsuario = new JsonObjectRequest
                (Request.Method.GET, urlUsuario, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray myJsonArray = response.getJSONArray("puntosUsuario");
                            JSONObject myJsonObject = myJsonArray.getJSONObject(0);
                            puntosUsuario = myJsonObject.getString("Puntos");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(cupones.this, "Error al cargar puntos del usuario", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue.
        queuePuntosUsuario.add(jsonObjectRequestPuntosUsuario);

    }

    public void pantalla()
    {
        //PANTALA CUPONES DEL CINE

        //PARAMETROS PARA EL POP UP
        myDialog = new Dialog(this);
        final TextView txtClosePopup;
        myDialog.setContentView(R.layout.custompopup);
        txtClosePopup=(TextView)myDialog.findViewById(R.id.txtclose);
        final TextView txtpuntosDialog;
        txtpuntosDialog=(TextView)myDialog.findViewById(R.id.txtSubtitulo);
        final Button btnCajear;
        btnCajear=(Button) myDialog.findViewById(R.id.btnCanjear);

        //DIBUJAR PANTALLA

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
        miTextView.setText("CUPONES DISPONIBLES");
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
        String url ="http://tunas.mztzone.com/tunas/apiEsme/cupones";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray myJsonArray = response.getJSONArray("premios");
                            for (int i=0; i<myJsonArray.length(); i++)
                            {
                                JSONObject myJsonObject = myJsonArray.getJSONObject(i);
                                String titulo = myJsonObject.getString("Nombre");
                                String descripcion = myJsonObject.getString("Descripcion");
                                puntos = myJsonObject.getString("Puntos");
                                idPremio = myJsonObject.getString("idPremio");

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
                                imagen.setImageResource(R.mipmap.cupon_tallrect);
                                FrameLayout.LayoutParams margenImagen = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
                                margenImagen.setMargins(0,30,0,40);
                                imagen.setLayoutParams(margenImagen);

                                //BASE PARA EL TEXTO DE LA CARTA
                                LinearLayout contenedorTextoCarta = new LinearLayout(getApplicationContext());
                                contenedorTextoCarta.setOrientation(LinearLayout.VERTICAL);
                                contenedorCarta.setPadding(20,0,20,0);
                                FrameLayout.LayoutParams margenTexto = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                                margenTexto.setMargins(150,30,0,40);
                                contenedorTextoCarta.setLayoutParams(margenTexto);

                                //TITULO DE LA CARTA
                                TextView txtTitulo = new TextView(getApplicationContext());
                                txtTitulo.setTextColor(Color.BLACK);
                                txtTitulo.setTextSize(18);
                                txtTitulo.setText(titulo);

                                //ID OCULTO
                                final TextView txtID = new TextView(getApplicationContext());
                                txtID.setTextColor(Color.WHITE);
                                txtID.setTextSize(1);
                                txtID.setText(idPremio);

                                //PUNTOS DE LA CARTA
                                final TextView txtPuntos = new TextView(getApplicationContext());
                                txtPuntos.setTextColor(Color.BLUE);
                                txtPuntos.setTextSize(15);
                                txtPuntos.setText(puntos+" puntos");

                                //DESCRIPCION DE LA CARTA
                                TextView txtDescripcion = new TextView(getApplicationContext());
                                txtDescripcion.setTextColor(Color.BLACK);
                                txtDescripcion.setTextSize(12);
                                txtDescripcion.setText(descripcion);

                                //AGREGAR IMAGEN A LA CARTA
                                contenedorCarta.addView(imagen);
                                //AGREGAR TITULO A LA BASE DEL TEXTO DE LA CARTA
                                contenedorTextoCarta.addView(txtTitulo);
                                //AGREGAR ID A LA BASE DEL TEXTO DE LA CARTA
                                contenedorTextoCarta.addView(txtID);
                                //AGREGAR PUNTOS A LA BASE DEL TEXTO DE LA CARTA
                                contenedorTextoCarta.addView(txtPuntos);
                                //AGREGAR DESCRIPCION A LA BASE DEL TEXTO DE LA CARTA
                                contenedorTextoCarta.addView(txtDescripcion);
                                //AGREGAR BASE DEL TEXTO DE LA CARTA A LA CARTA
                                contenedorCarta.addView(contenedorTextoCarta);
                                //AGREGAR EL CONTENIDO A LA CARTA
                                carta.addView(contenedorCarta);
                                //AGREGAR CARTA A LA BASE CUERPO
                                cuerpo.addView(carta);

                                carta.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v) {
                                        u = Integer.parseInt(String.valueOf(puntosUsuario));
                                        String puntosU = String.valueOf(txtPuntos.getText());
                                        p = Integer.parseInt(puntosU.substring(0,3));
                                        //Si al usuario no le alcanzan sus puntos para canjear el cupon
                                        if (u<p)
                                        {
                                            txtpuntosDialog.setText("Puntos insuficientes");
                                            btnCajear.setEnabled(false);
                                        }else
                                        {
                                            txtpuntosDialog.setText(txtPuntos.getText());
                                            btnCajear.setEnabled(true);
                                            btnCajear.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    idPremioSelected = String.valueOf(txtID.getText());
                                                    añadirCuponUsuario();
                                                    consultarPuntosUsuario();
                                                    myDialog.dismiss();
                                                }
                                            });
                                        }

                                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        myDialog.show();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(cupones.this, "Error al cargar cupones", Toast.LENGTH_SHORT).show();
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

        //CERRAR POP UP
        txtClosePopup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

    }
    public void añadirCuponUsuario() {
        //CONEXION A LA BD MEDIANTE WEB SERVICE: CANJEAR CUPON

        // Instantiate the RequestQueue.
        RequestQueue queueCanjearPuntos = Volley.newRequestQueue(this);
        String urlCanjearPuntos = "http://tunas.mztzone.com/tunas/apiEsme/cuponesUsuario/add";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlCanjearPuntos,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                Toast.makeText(cupones.this, "Error al canjear cupón", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date date = new Date();
                String fecha = dateFormat.format(date);
                params.put("puntos", String.valueOf(p));
                params.put("fecha", fecha);
                params.put("idusuario", "2");
                params.put("idpremio", idPremioSelected);
                params.put("estado", "1");
                params.put("updated", fecha);
                return params;
            }
        };
        queueCanjearPuntos.add(stringRequest);
    }
}
