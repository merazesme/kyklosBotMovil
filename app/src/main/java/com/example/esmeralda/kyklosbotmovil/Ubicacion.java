package com.example.esmeralda.kyklosbotmovil;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Ubicacion extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Boolean actualPosition = true;
    JSONObject jso;
    Double longitudOrigen, latitudOrigen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
            dialog.show();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        try {
            mMap = googleMap;

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);

            // Instantiate the RequestQueue.
            RequestQueue queuePuntosUsuario = Volley.newRequestQueue(this);
            String url ="http://tunas.mztzone.com/tunas/apiJesus/maquinas";
            // Request a string response from the provided URL.
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray myJsonArray = response.getJSONArray("maquina");
                                for (int i=0; i<myJsonArray.length(); i++)
                                {
                                    JSONObject myJsonObject = myJsonArray.getJSONObject(i);
                                    Double longitud = Double.parseDouble(myJsonObject.getString("Longitud"));
                                    Double latitud = Double.parseDouble(myJsonObject.getString("Latitud"));
                                    String ubicacion = myJsonObject.getString("Ubicacion");

                                    //Crea un marker
                                    LatLng punto = new LatLng(latitud,longitud);

                                    //Lo agrega al mapa
                                    mMap.addMarker(new MarkerOptions().position(punto).title(ubicacion).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_kyklos)));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Toast.makeText(getApplicationContext(), "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    });

            // Add the request to the RequestQueue.
            queuePuntosUsuario.add(jsonObjectRequest);
            //mMap.addMarker(new MarkerOptions().position(punto4).title("KYKLOS").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));*/
            LatLng punto1 = new LatLng(23.265286, -106.373913);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(punto1));
            float zoomlevel=15;

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto1,zoomlevel));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
