package es.unizar.unoforall.api;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

public class Serializar {
    private static final boolean DEBUG = false;

    public static <T> String serializar(T dato){
        String mensaje;
        if(dato instanceof String){
            mensaje = (String) dato;
        }else{
            mensaje = new Gson().toJson(dato);
        }

        if(DEBUG){
            System.out.println("Mensaje enviado: " + mensaje);
        }

        return mensaje;
    }

    @SuppressWarnings("unchecked")
	public static <T> T deserializar(String mensaje, Type expectedClass){
        if(DEBUG){
            System.out.println("Mensaje recibido: " + mensaje);
        }
        
        if(mensaje.equals("null") || mensaje.equals("nulo")){
            return (T) null;
        }
        
        if(expectedClass.equals(String.class)){
            return (T) mensaje;
        }else{
            return new Gson().fromJson(mensaje, expectedClass);
        }
    }

    public static <T> T deserializar(InputStream inputStream, Class<T> expectedClass) throws IOException {
        StringBuilder mensajeBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesReaded;
        while((bytesReaded = inputStream.read(buffer)) > 0){
            mensajeBuilder.append(new String(buffer, 0, bytesReaded));
        }

        String mensaje = mensajeBuilder.toString();
        return deserializar(mensaje, expectedClass);
    }
    
}
