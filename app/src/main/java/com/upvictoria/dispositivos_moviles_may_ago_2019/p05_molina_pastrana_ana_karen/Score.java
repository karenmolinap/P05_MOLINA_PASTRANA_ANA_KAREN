package com.upvictoria.dispositivos_moviles_may_ago_2019.p05_molina_pastrana_ana_karen;

public class Score {
    private String id;
    private String nombre;
    private String aciertos;
    private String fecha;

    public Score(String id, String nombre, String aciertos, String fecha){
        this.setId(id);
        this.setNombre(nombre);
        this.setAciertos(aciertos);
        this.setFecha(fecha);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAciertos() {
        return aciertos;
    }

    public void setAciertos(String aciertos) {
        this.aciertos = aciertos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
