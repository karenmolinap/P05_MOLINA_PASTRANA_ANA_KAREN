package com.upvictoria.dispositivos_moviles_may_ago_2019.p05_molina_pastrana_ana_karen;

/*
https://stackoverflow.com/questions/28460300/how-to-build-a-horizontal-listview-with-recyclerview
*/

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class MainJuego extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    final Context context = MainJuego.this;
    Button B1, B2, B3, B4;
    EditText ET1;
    TextView TV1,TV2;
    private MyRecyclerViewAdapter adapter;
    Map<String, String> map;
    private TextToSpeech tts;
    String[] items = {"...","....", "CCC"};
    int cont = 0;

    // Base de datos
    List<NameValuePair> datos;
    private ArrayList<Score> scoreList;
    Context CX;
    private int TIPO_OPERACION = -1;

    // alert dialog
    AlertDialog.Builder ADX; // Alert Builder
    AlertDialog AD; // ALert Dialog

    AlertDialog.Builder ADX2; // Alert Builder
    AlertDialog AD2; // ALert Dialog

    // Variables de cronometro
    private Chronometer chTiempo = null; // Etiqueta para mostrar el tiempo restante.
    private TextView txtTiempo = null; // Muestra el tiempo que se jugará.
    private long timeWhenStopped=0;
    private final MainJuego mainActivity = this; // Objeto de la misma clase.
    private boolean unaVez=false; // Controla que solo se ejecute una vez el sónido.
    private int X = 60; // Ayuda a la conversión de segundos a minutos.

    ////////////////////////////////////////////////////////////////////////////////////////////////
    private int tiempo_partida = 0; // VARIABLE DE TIEMPO QUE VIENE DE OTRA CLASE. *60 para convertirlo en minutos.
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private String nombre = "User";
    // Aqui terminan las variables del cronometro

    String palabra="";//Variable que almacena la palabra actual
    int puntuaje=0;//Variable que lleva el control del puntuaje
    ArrayList<String> A_words=null;//Lista de palabras del archivo TXT

    EditText ETT1;

    void LecturaLenta (String Cadena) {
        String Cadena2="";
        if (Cadena.length() > 0) {
            for (int i = 0; i < TV2.getText().toString().length(); i++) {
                String Caracter = Cadena.substring(i, i + 1);
                Cadena2 += (Caracter + "-");
            }
            tts.setSpeechRate((float) 0.5);
            tts.speak(Cadena2, TextToSpeech.QUEUE_FLUSH, null);
        }
        else
            Toast.makeText(getApplicationContext(),"You must write a word!",Toast.LENGTH_SHORT).show();

    }

    void LecturaRapida (String Cadena) {
        if (Cadena.length()==0) {
            Toast.makeText(getApplicationContext(),"You must write a TEXT !",Toast.LENGTH_SHORT).show();
        }
        else {
            tts.setSpeechRate((float) 1.0);
            tts.speak(Cadena, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        // AlertDialog de Ayuda
        ADX = new AlertDialog.Builder(this);
        AD = ADX.create();

        ADX2 = new AlertDialog.Builder(this);
        AD2 = ADX2.create();

        AD.setMessage("WELCOME! To start the game press the \"Click to hear the word to spell\" to " +
                "hear which word you will write on the text input, you can hear it as many times as you want " +
                "then write the word and press \"Next\" to set another word to spell, each correct answer gives " +
                "you 1 point, the game ends when the time is over.");
        AD.show();

        AD2.setMessage("¡BIENVENIDO! Para empezar el juego presiona el botón \"Click to hear the word to spell\"  para " +
                "saber que palabra deletrearas, puedes escucharla tantasa veces como desees, después escribe la palabra " +
                "en el cuadro de texto y presiona \"Next\" para generar otra palabra a deletrear, cada respuesta correcta " +
                "te da 1 punto, el juego termina cuando el tiempo acaba.");
        AD2.show();

        // Aqui empieza cronometro
        Toolbar tlMenu = findViewById(R.id.toolbar); // Barra de menú superior.
        setSupportActionBar(tlMenu); // Se agrega la barra de menú superior

        chTiempo = findViewById(R.id.chTiempo); // Etiqueta para mostrar el tiempo restante.
        txtTiempo = findViewById(R.id.txtTiempo); // Muestra el tiempo que se jugará.
        txtTiempo.setText("/ "+tiempo_partida/X+" ");
        // Aqui termina cronometro

        MainJuego.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        B1 = (Button) findViewById(R.id.B_Siguiente);
        B2 = (Button) findViewById(R.id.B_Salir);
        B3 = (Button) findViewById(R.id.B_Leer);
        B4 = (Button) findViewById(R.id.B_Leer2);
        ET1 = (EditText) findViewById(R.id.TV_Palabra);
        TV2 = (TextView) findViewById(R.id.TV_Adivinar);
        TV1 = (TextView) findViewById(R.id.TV_Score);
        map = new HashMap<>();
        GenerateLUT_Letters (map);


        tts=new TextToSpeech(MainJuego.this,new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }
        });


        // Tiempo de la Partida
        tiempo_partida =  getIntent().getIntExtra("MIN", 1);
        Toast.makeText(getApplicationContext(),tiempo_partida + " minutes",Toast.LENGTH_SHORT).show();

        /*INicio */
        final Context ctx = this;

        PalabraRandom(ctx);//Se carga la primera palabra
        /* fin*/

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String res = ET1.getText().toString().toUpperCase();
                    ET1.setText("");

                    Verificar(res,ctx);//Funcion para verificar la palabra
                }catch(Exception e){}
            }
        });

        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Scores.class);
                startActivity(intent);
            }
        });

        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cadena=TV2.getText().toString().toUpperCase();
                LecturaRapida(Cadena);
            }
        });

        B4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Cadena=TV2.getText().toString().toUpperCase();
                LecturaLenta(Cadena);
            }
        });


        //Al terminar el tiempo

        // Empiezan Listener de CRONOMETRO
        chTiempo.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                int elapsed = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);

                // Verifica si: se ha cumplido el tiempo total y que únicamente se entre 1 vez a esta
                // parte de código.
                if (elapsed >= tiempo_partida*X && !unaVez) {
                    chTiempo.stop(); // Se detiene el temporizador.

                    // Ventana emergente para pedir los datos del jugador.
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainJuego.this);
                    final AlertDialog alert = builder.create();
                    alert.setCancelable(false);

                    final LinearLayout linear = new LinearLayout(mainActivity); // Se genera un LinearLayout para añadir componentes.
                    linear.setOrientation(LinearLayout.VERTICAL); // Se coloca una orientación vertical al layout.
                    linear.setBackgroundColor(Color.rgb(255,152,0));

                    // Texto indicativo "El tiempo se ha acabado".
                    TextView txtViewTitulo = new TextView(mainActivity);
                    txtViewTitulo.setText("¡Time is Over!");
                    txtViewTitulo.setTextSize(30);
                    //txtViewTitulo.setTextColor(new ColorStateList(new int[][]{new int[0]}, new int[]{0x1E3644BB}));

                    // Texto indicativo "Ingrese su nombre".
                    TextView txtViewNombre = new TextView(mainActivity);
                    txtViewNombre.setText("Type your name");
                    txtViewNombre.setTextSize(15);

                    // EditText para añadir el nombre del jugador.
                    final EditText txtNombreJugador = new EditText(mainActivity);
                    txtNombreJugador.setFilters(new InputFilter[] { new InputFilter.LengthFilter(90) }); // Limita el número de carácteres de entrada.

                    // Botón de aceptar.
                    Button btnAcepar = new Button(mainActivity);
                    btnAcepar.setText("Accept");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btnAcepar.setBackgroundTintList(new ColorStateList(new int[][]{new int[0]}, new int[]{0xFFFFC107})); // Dorado.
                    }

                    // Acciones del botón, aquí se guarda el nombre del jugador.
                    btnAcepar.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            ////////////////////////////////////////////////////////////////////////////////////
                            nombre = txtNombreJugador.getText().toString(); // Se recibe el nombre del jugador.
                            // AQUI SE ENVIAN LOS RESULTADOS A LA BD.
                            ////////////////////////////////////////////////////////////////////////////////////
                            Log.d("Mensaje: ",nombre);

                            /*INSERTAR DATOS A LA BD*/

                            //Boton Insertar

                            String aciertos = TV1.getText().toString();
                            if(aciertos.equals("")){
                                aciertos = "0";
                            }

                            if (!nombre.equals("") && !aciertos.equals("")){
                                datos = new ArrayList<NameValuePair>();

                                datos.add(new BasicNameValuePair("orden", "agregarScore"));
                                datos.add(new BasicNameValuePair("nombre", nombre));
                                datos.add(new BasicNameValuePair("aciertos", aciertos));

                                //Agregar el prestamo en la BD
                                sqlMagico(2, URLs.insert, datos);

                                // CerrarAlert
                                alert.cancel();

                                // Detener Temporizador
                                // Reiniciar Variables y arreglos
                                nombre="User";
                                aciertos = "0";
                                ET1.setText("");
                                TV2.setText("0");
                                // Reiniciar Score
                                // Limpiar Campos
                                Intent intent = new Intent(getApplicationContext(), Scores.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(getApplicationContext(),  "Blank Spaces", Toast.LENGTH_SHORT).show();
                            }
                            /*FIN INSERTAR*/


                        }
                    });


                    linear.addView(txtViewTitulo); // Se añade el TextView descriptivo de titulo.
                    linear.addView(txtViewNombre); // Se añade el TextView descriptivo.
                    linear.addView(txtNombreJugador); // Se añade el TextView al Layout.
                    linear.addView(btnAcepar); // Se añade el botón al Layout.

                    alert.setView(linear); // Se añade el Layout de componentes al alert o ventana emegergente.

                    alert.show(); // Se muestra la ventana emergente.
                    unaVez=true;
                }
            }
        });

        chTiempo.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);

        chTiempo.start(); // Inicio de temporizador.

        // Terminan Listener de CRONOMETRO
    }//OnCreate

    void GenerateLUT_Letters (Map MiMap) {
        //MiMap = new HashMap<>();
        MiMap.put("A","ei");
        MiMap.put("B","bi");
        MiMap.put("C","si");
        MiMap.put("D","di");
        MiMap.put("E","i");
        MiMap.put("F","ef");
        MiMap.put("G","gi");
        MiMap.put("H","eich");
        MiMap.put("I","ai");
        MiMap.put("J","jei");
        MiMap.put("K","kei");
        MiMap.put("L","el");
        MiMap.put("M","em");
        MiMap.put("N","en");
        MiMap.put("O","ou");
        MiMap.put("P","pi");
        MiMap.put("Q","kiu");
        MiMap.put("R","ar");
        MiMap.put("S","es");
        MiMap.put("T","ti");
        MiMap.put("U","iu");
        MiMap.put("V","vi");
        MiMap.put("W","dobleiu");
        MiMap.put("X","exs");
        MiMap.put("Y","wai");
        MiMap.put("Z","set");
        MiMap.put(" ","espais");
        MiMap.put("-","jaifen");
        MiMap.put(".","dot");
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

        Log.wtf("Resultado:", result);

        if (result.equals("error")){
            Toast.makeText(getApplicationContext(), "No internet connection detected.", Toast.LENGTH_SHORT).show();
        } else {
            if (!result.equals("0")){
                //Respuesta en tokens
                StringTokenizer tokens = ms.resultado_Token(result);
            }

        }

    }

    /*FUNCIONES DE VALIDACION Y LECTURA DE PALABRAS*/
    public String readRawTextFile(Context ctx, int resId)//Funcion para leer el texto del archivo TXT almacenado en RAW
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while (( line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }

    public void palabras (Context ctx){//Funcnion que convierte en un ARAYLIST los datos del TXT

        String text = readRawTextFile(ctx,R.raw.palabras);

        A_words = new ArrayList<>();
        String bat[] = text.split(",");
        for(int i=0;i<bat.length;i++){
            A_words.add(bat[i]);
        }

    }

    public void PalabraRandom (Context ctx){//Funcion para obtener una palabra aleatoria

        if(A_words==null){
            palabras(ctx);
        }


        int min = 0;
        int max = A_words.size()-1;
        int random = new Random().nextInt((max - min) + 1) + min;//Se genera un numero aleatorio

        palabra = A_words.get(random);//Guardamos la palabra
        A_words.remove(random);//Eliminamos la palabra para evitar repetidos

        TextView palabraTXT;
        palabraTXT = findViewById(R.id.TV_Adivinar);
        palabraTXT.setText(palabra);//Mostramos la palabra al usuario


    }

    public void Verificar (String res, Context ctx){//Funcion para verificar si la palabra es correcta

        if(res.toUpperCase().equals(palabra.toUpperCase())){

            Toast.makeText(MainJuego.this,"Correct", Toast.LENGTH_SHORT).show();

            puntuaje++;

            TextView puntuajeTXT;//Mostramos el puntuaje del jugador
            puntuajeTXT = findViewById(R.id.TV_Score);
            puntuajeTXT.setText(String.valueOf(puntuaje));



            PalabraRandom(ctx);

        }
        else{

            Toast.makeText(MainJuego.this,"Incorrect", Toast.LENGTH_SHORT).show();

            PalabraRandom(ctx);
        }
    }
    /*FIN FUNCIONES DE VALIDACION Y LECTURA DE PALABRAS*/

    @Override
    public void onItemClick(View view, int position) {
        // No hace nada
    }
}