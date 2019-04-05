package com.example.esmeralda.kyklosbotmovil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private String idUsuario="3";
    private String puntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lector_qr);

        surafeceView = (SurfaceView) findViewById(R.id.camerapreview);
        textView = (TextView) findViewById(R.id.textView);

        //Ocultar Bar
        getSupportActionBar().hide();

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
                                                            Toast.makeText(getApplicationContext(),"Se agrego el idUsuario a la maquina y puntos actuales: " + puntos, Toast.LENGTH_LONG).show();
                                                            finish();
                                                            ventana.putExtra("puntos", puntos);
                                                            startActivity(ventana);
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
                                                    params.put("idUsuario", idUsuario);

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

    public void agarrarMaquinas(){
        try {
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
                                for (int i=0; i<a; i++) {
                                    JSONObject myJsonObject = myJsonArray.getJSONObject(i);
                                    series = myJsonObject.getString("Serie");
                                    seriesfinal[i] = new String(series);
                                    //textView.setText(String.valueOf(seriesfinal[i]));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        /****************************************************************************************/

        try {
            //Agarrar puntos actuales usuario
            RequestQueue queue2 = Volley.newRequestQueue(this);
            String url = "http://tunas.mztzone.com/tunas/apiAdriana/puntosUsuario/"+idUsuario+"";

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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Error
                        }
                    });

            // Add the request to the RequestQueue.
            queue2.add(jsonObjectRequest2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}