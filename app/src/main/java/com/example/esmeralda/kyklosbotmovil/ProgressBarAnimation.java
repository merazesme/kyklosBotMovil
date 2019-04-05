package com.example.esmeralda.kyklosbotmovil;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

public class ProgressBarAnimation extends Animation {

    private Context context;
    private ProgressBar progressBar;
    private TextView textView;
    private float from;
    private float to;
    private int valueF;
    private String puntos;
    private String idUsuario="3";
    private String resultado;

    public ProgressBarAnimation(Context context, ProgressBar progressBar, TextView textView, float from, float to, String puntos){
        this.context = context;
        this.progressBar = progressBar;
        this.textView = textView;
        this.from = from;
        this.to = to;
        this.puntos = puntos;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t){
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int)value);
        valueF=(int)value;
        //textView.setText(valueF+" %");
        textView.setText("Calculando puntos...");

        if(valueF == 50f){
            try {
                //Agarrar puntos actuales usuario
                RequestQueue queue = Volley.newRequestQueue(context);
                String url = "http://tunas.mztzone.com/tunas/apiAdriana/puntos";

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Response: " + response, Toast.LENGTH_LONG).show();
                        resultado=response;
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Error
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("idUsuario", idUsuario);
                        params.put("puntos", puntos);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);

                // Request a string response from the provided URL.
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(valueF == to){
            try {
                //Toast.makeText(context, "REGERSO: " + resultado, Toast.LENGTH_LONG).show();
                if(resultado.equals("-1")){
                    //Toast.makeText(context, "IGUALES", Toast.LENGTH_LONG).show();
                    //((Loading) context).finish();
                    Intent ventana = new Intent(context, Loading.class);
                    ventana.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ventana.putExtra("puntos", puntos);
                    context.startActivity(ventana);
                }
                else {
                    //Toast.makeText(context, "DIFERENTES", Toast.LENGTH_LONG).show();
                    ((Loading) context).finish();
                    Intent ventana = new Intent(context, LectorQRespuesta.class);
                    ventana.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    ventana.putExtra("puntos", resultado);
                    context.startActivity(ventana);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
