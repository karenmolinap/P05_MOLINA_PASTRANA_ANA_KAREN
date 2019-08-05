package com.upvictoria.dispositivos_moviles_may_ago_2019.p05_molina_pastrana_ana_karen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class Scores extends AppCompatActivity {

    private Button BT1;

    List<NameValuePair> datos;
    private leaderboard_listviewAdapter adapter;
    private ListView listview_leaderboard;
    private ArrayList<Score> scoreList;

    Context CX;

    private int TIPO_OPERACION = -1;

    /*
    1.- Select all
    2.- Insert registro
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
       // getSupportActionBar().hide();
        Scores.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        BT1 = (Button) findViewById(R.id.B_Volver); // Insertar


        BT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        });

        CX = this;

        listview_leaderboard = findViewById(R.id.leaderboard_listview);

        //Cargar la tabla
        cargarLeaderBoard();
    }

    public void sqlMagico(int tipo, String liga, List<NameValuePair> datos){

        //Cambia el tipo segun el parametro ingresado
        TIPO_OPERACION = tipo;

        OperacionesServidor ms = new OperacionesServidor();

        String result = "";

        //Realizar la consulta
        try {
            result = ms.execute(liga, datos).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (result.equals("error")){
            Toast.makeText(CX, "No tiene conexión a Internet.", Toast.LENGTH_SHORT).show();
        } else {
            if (!result.equals("0")){
                //Respuesta en tokens
                StringTokenizer tokens = ms.resultado_Token(result);

                if (TIPO_OPERACION ==1)
                {
                    if (tokens.hasMoreTokens()){

                        scoreList = new ArrayList<Score>();

                        while (tokens.hasMoreElements()){
                            String id = tokens.nextElement().toString();
                            //Log.wtf("ID:", id);

                            String nombre = tokens.nextElement().toString();
                            //Log.wtf("ID:", id);

                            String aciertos = tokens.nextElement().toString();
                            //Log.wtf("ID:", id);

                            String fecha = tokens.nextElement().toString();
                            //Log.wtf("ID:", id);

                            scoreList.add(new Score(id,nombre,aciertos,fecha));
                        }

                        actualizar_lista(listview_leaderboard, scoreList);

                    }

                }
            }

        }

    }


    public void cargarLeaderBoard(){
        datos = new ArrayList<NameValuePair>();

        datos.add(new BasicNameValuePair("orden", "cargarLeaderBoard"));

        //Cargar la lista de existencia
        sqlMagico(1, URLs.select, datos);
    }

    public void actualizar_lista(ListView listView, ArrayList<Score> array){
        adapter = new leaderboard_listviewAdapter(this, array);
        listView.setAdapter(adapter);
    }
}
