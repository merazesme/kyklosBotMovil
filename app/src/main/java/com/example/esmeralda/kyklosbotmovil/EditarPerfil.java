package com.example.esmeralda.kyklosbotmovil;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public void ponerImagen(String text){
        String uri;
        if(imagen.equals("1") || imagen.equals("")) {
            uri = "@drawable/sinimagen";
        }
        else {
            uri = "@drawable/"+text+"";
        }

        //extraemos el drawable en un bitmap
        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
        Drawable originalDrawable = ContextCompat.getDrawable(getApplicationContext(), imageResource);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        if(imagen.equals("1") || imagen.equals("")) {
            //creamos el drawable redondeado
            RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);
            //asignamos el CornerRadius
            roundedDrawable.setCornerRadius(originalBitmap.getHeight());
            //asigamos al imageview
            foto.setImageDrawable(roundedDrawable);
        }
        else {
            foto.setImageBitmap(originalBitmap);
        }
    }

    public void actualizarPerfil(){
        //CONEXION A LA BD MEDIANTE WEB SERVICE
        try {
            //Actualizar perfil usuario
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url ="http://tunas.mztzone.com/tunas/apiAdriana/actualizarPerfil/"+idUsuario+"";
            StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                    new Response.Listener<String>()
                    {
                        @Override
                        public void onResponse(String response) {
                            // response
                            Toast.makeText(getApplicationContext(),"Se actualizaron tus datos exitosamente!", Toast.LENGTH_LONG).show();
                            finish();
                            //startActivity(ventana);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // error
                            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_LONG).show();
                        }
                    }
            ) {

                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String>  params = new HashMap<String, String>();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nombreUsuario(){
        try {
            //Agarrar maquianas disponibles
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
                                for (int i=0; i<a; i++) {
                                    JSONObject myJsonObject = myJsonArray.getJSONObject(i);
                                    nombreUsuarios = myJsonObject.getString("Usuario");
                                    nombreUsuariosFinal[i] = new String(nombreUsuarios);
                                }
                                lo=nombreUsuariosFinal.length;
                                Toast.makeText(getApplicationContext(),"Nombre usuarios: " + lo, Toast.LENGTH_LONG).show();
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
