package com.example.esmeralda.kyklosbotmovil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class Informacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                //emailIntent.setType("plain/text");
                emailIntent.setData(Uri.parse("empresa.tunas.mx@gmail.com"));
                emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");

                // Verify it resolves
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> activities = packageManager.queryIntentActivities(emailIntent, 0);
                boolean isIntentSafe = ((List) activities).size() > 0;

                // Start an activity if it's safe
                if (isIntentSafe) {
                    startActivity(emailIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No se encontro servicio",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
