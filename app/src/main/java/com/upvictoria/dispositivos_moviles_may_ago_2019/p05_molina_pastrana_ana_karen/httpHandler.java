package com.upvictoria.dispositivos_moviles_may_ago_2019.p05_molina_pastrana_ana_karen;

/**
 * Created by marco on 7/10/16.
 */
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;

public class httpHandler {
    String text;
    String line = "";

    //public String post(final String posturl, String ImagenC){
    //public String post(final String posturl, String id, String Nombre, String ApellidoP, String ApellidoM, String CadenaAdicional){

    public String post(String url, List<NameValuePair> datos){

        try {

            text = "contenido inicial";

            HttpClient httpclient = new DefaultHttpClient();
            //Creamos el objeto de HttpClient que nos permitira conectarnos mediante peticiones http
            HttpPost httppost = new HttpPost(url);
            //El objeto HttpPost permite que enviemos una peticion de tipo POST a una URL especificada

            //Una vez añadidos los parametros actualizamos la entidad de httppost, esto quiere decir en pocas palabras anexamos los parametros al objeto para que al enviarse al servidor envien los datos que hemos añadido
            httppost.setEntity(new UrlEncodedFormEntity(datos));

            //Finalmente ejecutamos enviando la info al server
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();//y obtenemos una respuesta

            text = EntityUtils.toString(ent);

            return text;

        }
        catch(Exception e) { return "error";}

    }
}
