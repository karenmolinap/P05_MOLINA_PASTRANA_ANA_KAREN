package com.upvictoria.dispositivos_moviles_may_ago_2019.p05_molina_pastrana_ana_karen;

/*
https://stackoverflow.com/questions/28460300/how-to-build-a-horizontal-listview-with-recyclerview
*/

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Menu extends AppCompatActivity {

    final Context context = Menu.this;
    Button B1, B3, B5;
    EditText ET1;
    private MyRecyclerViewAdapter adapter;
    Map<String, String> map;
    private TextToSpeech tts;
    String[] items = {"...","....", "CCC"};
    AlertDialog.Builder ADX; // Alert Builder
    AlertDialog AD; // ALert Dialog

    ArrayAdapter<String> adapterSP;
    List<String> objects;

    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION = 200;

    Spinner SP1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        askPermissionOnly();

//        getSupportActionBar().hide();

        ADX = new AlertDialog.Builder(this);
        AD = ADX.create();

        Menu.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        B1 = (Button) findViewById(R.id.B_UNO);
        B3 = (Button) findViewById(R.id.B_SCORES);
        B5 = (Button) findViewById(R.id.B_ACERCA);
        SP1 = (Spinner) findViewById(R.id.SP_MINUTOS);


        objects = new ArrayList<String>();

        for(int i=1;i<=10;i++){
            objects.add(""+i);
        }
        adapterSP = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, objects);
        adapterSP.notifyDataSetChanged();
        SP1.setAdapter(adapterSP);
        adapterSP.notifyDataSetChanged();
        SP1.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int minutos = Integer.parseInt(SP1.getSelectedItem().toString());
                Intent intent = new Intent(getApplicationContext(), MainJuego.class);
                intent.putExtra("MIN", minutos);
                startActivity(intent);
            }
        });

        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Scores.class);
                startActivity(intent);
            }
        });

        B5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AD.setMessage("This app was developed by \n"+
                    "Jesús Alfredo Cárdenas Castillo, \n" +
                    "Ana Karen Molina Pastrana, \n" +
                    "Genaro Juan Sánchez Gallegos,\n" +
                    "Yu Hsiang Wang \n" +
                    "Karla Vanessa Balderrama de la Rosa \n" +
                    "Universidad Politécnica de Victoria \n" +
                    "July 2019.");
            AD.show();
            }
        });
    }
    private void askPermissionOnly() {
        this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        this.askPermission(REQUEST_ID_READ_PERMISSION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);

    }


    // A partir de Nivel de Android mayor >= 23, se tienen que preguntar los permisos al ussuario, leer, escribir
    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // Revisar si hay permisos
            int permission = android.support.v4.app.ActivityCompat.checkSelfPermission(this, permissionName);


            if (permission != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Si no hay permisos se solicitan al usuario.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            Toast.makeText(parent.getContext(),
                    parent.getItemAtPosition(pos).toString() + " minutes",
                    Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
            Toast.makeText(getApplicationContext(),
                    " ",
                    Toast.LENGTH_SHORT).show();
        }
    }
}