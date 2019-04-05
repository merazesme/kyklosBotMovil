package com.example.esmeralda.kyklosbotmovil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LectorQR extends AppCompatActivity {
    private SurfaceView surafeceView;
    private CameraSource cameraSource;
    private TextView textView;
    private BarcodeDetector barcodeDetector;
    private String series;
    private String serie="";
    private String seriesfinal[];
    private Intent ventana;
    private int lo=0;
    private String puntos;
    public GifImageView gifImageView;
    public GifImageView gifImageView2;
    public Button intentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);

        surafeceView = (SurfaceView) findViewById(R.id.camerapreview);
        textView = (TextView) findViewById(R.id.textView);
        gifImageView = (GifImageView)findViewById(R.id.gifImageView);
        gifImageView2 = (GifImageView)findViewById(R.id.gifImageView);
        intentar = (Button)findViewById(R.id.intentar);

        intentar.setVisibility(View.INVISIBLE);

        //Ocultar Bar
        //getSupportActionBar().hide();

        agarrarMaquinas();

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(640, 480).build();
        ventana = new Intent(this, Loading.class);

        surafeceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSource.start(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                try {
                    final SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                    if(qrCodes.size()!=0){
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(300);
                                textView.setText(qrCodes.valueAt(0).displayValue);
                                serie=(qrCodes.valueAt(0).displayValue);
                                for (int i=0; i<seriesfinal.length; i++)
                                {
                                    if(seriesfinal[i].equals(serie)){
                                        barcodeDetector.release();
                                        cameraSource.stop();
                                        //CONEXION A LA BD MEDIANTE WEB SERVICE
                                        try {
                                            //Agregar usuario a maquina
                                            RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
                                            String url ="http://tunas.mztzone.com/tunas/apiAdriana/maquinaUsuario/"+serie+"";
                                            StringRequest putRequest = new StringRequest(Request.Method.PUT, url,
                                                    new Response.Listener<String>()
                                                    {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            // response
                                                            //Toast.makeText(getApplicationContext(),"Se agrego el idUsuario a la maquina y puntos actuales: " + puntos, Toast.LENGTH_LONG).show();
                                                            finish();
                                                            ventana.putExtra("puntos", puntos);
                                                            startActivity(ventana);
                                                        }
                                                    },
                                                    new Response.ErrorListener()
                                                    {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            //Detiene loading
                                                            gifImageView.stopAnimation();
                                                            gifImageView.setVisibility(View.INVISIBLE);

                                                            surafeceView.setVisibility(View.VISIBLE);
                                                            textView.setVisibility(View.VISIBLE);
                                                            Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                            ) {

                                                @Override
                                                protected Map<String, String> getParams()
                                                {
                                                    Map<String, String>  params = new HashMap<String, String>();
                                                    params.put("idUsuario", LoginActivity.idUsuario);

                                                    return params;
                                                }

                                            };

                                            queue2.add(putRequest);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //Aqui iban
                                    }
                                    else {
                                        //Toast.makeText(getApplicationContext(),
                                        // "QR invalido, intenta de nuevo...",
                                        // Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

                surafeceView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
            }
            catch (IOException ex)
            {

            }
        }
    }

    public void agarrarMaquinas(){
        try {

            if (isOnline()) {
                try {
                    InputStream inputStream = getAssets().open("loading.gif");
                    byte[] bytes = IOUtils.toByteArray(inputStream);
                    gifImageView.setBytes(bytes);
                    gifImageView.startAnimation();
                    gifImageView.setVisibility(View.VISIBLE);

                    surafeceView.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    intentar.setVisibility(View.INVISIBLE);
                } catch (IOException ex) {

                }

                //Agarrar maquianas disponibles
                RequestQueue queue = Volley.newRequestQueue(this);
                String url = "http://tunas.mztzone.com/tunas/apiAdriana/maquinas";

                // Request a string response from the provided URL.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray myJsonArray = response.getJSONArray("maquinas");
                                    int a = myJsonArray.length();
                                    seriesfinal = new String[a];
                                    for (int i = 0; i < a; i++) {
                                        JSONObject myJsonObject = myJsonArray.getJSONObject(i);
                                        series = myJsonObject.getString("Serie");
                                        seriesfinal[i] = new String(series);

                                        //Segunda peticion
                                        //Agarrar puntos actuales usuario
                                        RequestQueue queue2 = Volley.newRequestQueue(getApplicationContext());
                                        String url = "http://tunas.mztzone.com/tunas/apiAdriana/puntosUsuario/" + LoginActivity.idUsuario + "";

                                        // Request a string response from the provided URL.
                                        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                                                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            JSONArray myJsonArray = response.getJSONArray("puntos");
                                                            JSONObject myJsonObject = myJsonArray.getJSONObject(0);
                                                            puntos = myJsonObject.getString("Puntos");
                                                            //Toast.makeText(getApplicationContext(), "Puntos: " + puntos, Toast.LENGTH_LONG).show();

                                                            //Detiene loading
                                                            gifImageView.stopAnimation();
                                                            gifImageView.setVisibility(View.INVISIBLE);

                                                            surafeceView.setVisibility(View.VISIBLE);
                                                            textView.setVisibility(View.VISIBLE);

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {

                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        //Detiene loading
                                                        gifImageView.stopAnimation();
                                                        gifImageView.setVisibility(View.INVISIBLE);

                                                        surafeceView.setVisibility(View.VISIBLE);
                                                        textView.setVisibility(View.VISIBLE);
                                                        Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        // Add the request to the RequestQueue.
                                        queue2.add(jsonObjectRequest2);
                                    }
                                    lo = seriesfinal.length;
                                    textView.setText("Longitud: " + lo);
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

                    surafeceView.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                }
                catch (IOException ex)
                {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
