package com.example.myapplication.Entidades;

import java.util.Date;

public class LMensaje {
    private String key;
    private Mensaje_Individual mensaje;
    private LUsuario lUsuario;

    public LMensaje(String key, Mensaje_Individual mensaje) {
        this.key = key;
        this.mensaje = mensaje;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Mensaje_Individual getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje_Individual mensaje) {
        this.mensaje = mensaje;
    }

    //public long getCreatedTimestampLong(){
        //return (long) mensaje.getCreatedTimestamp();
    //}

    public LUsuario getlUsuario() {
        return lUsuario;
    }

    public void setlUsuario(LUsuario lUsuario) {
        this.lUsuario = lUsuario;
    }

    //public String fechaDeCreacionDelMensaje(){
        //Date date = new Date(getCreatedTimestampLong());
        //PrettyTime prettyTime = new PrettyTime(new Date(),Locale.getDefault());
        //return prettyTime.format(date);
        /*Date date = new Date(getCreatedTimestampLong());
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());//a pm o am
        return sdf.format(date);*/
    //}

}
