package com.upvictoria.dispositivos_moviles_may_ago_2019.p05_molina_pastrana_ana_karen;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;

import java.util.List;
import java.util.StringTokenizer;

public class OperacionesServidor extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            httpHandler handler = new httpHandler();

            String liga = params[0].toString();
            List<NameValuePair> datos = (List<NameValuePair>) params[1];

            String Respuesta = handler.post(liga, datos);

            return Respuesta;
        }

        //Metodo para limpiar el resultado devuelto por el servidor
        public StringTokenizer resultado_Token(String respuesta){
            //Eliminar los espacios en blanco
            String datos_limpio = respuesta.replace("\n", ",").replace("\r", ",");

            if (datos_limpio.length() > 1){
                datos_limpio = datos_limpio.substring(0, datos_limpio.length() - 1);
            }

            //Eliminar las commas
            StringTokenizer tokens = new StringTokenizer(datos_limpio, ",");

            return tokens;
        }
}
