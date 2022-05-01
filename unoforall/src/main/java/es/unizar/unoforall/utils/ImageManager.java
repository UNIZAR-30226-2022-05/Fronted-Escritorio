package es.unizar.unoforall.utils;

import java.util.HashMap;

import es.unizar.unoforall.App;
import es.unizar.unoforall.model.partidas.Carta;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class ImageManager {

    private static final HashMap<Carta, Image> defaultCardsMap = new HashMap<>();
    private static final HashMap<Carta, Image> altCardsMap = new HashMap<>();

    public static ImageView setImagenCarta(Carta carta, boolean defaultMode, boolean isVisible){
        if(defaultCardsMap.isEmpty() || altCardsMap.isEmpty()){
            for(Carta.Color color : Carta.Color.values()){
                for(Carta.Tipo tipo : Carta.Tipo.values()){
                    Carta aux = new Carta(tipo, color);
                    
                    ImageView resourceIDdefault = getResourceCarta(aux, true);
                    ImageView resourceIDalt = getResourceCarta(aux, false);
                    if(resourceIDdefault != null){
                        defaultCardsMap.put(aux, resourceIDdefault);
                    }
                    if(resourceIDalt != null){
                        altCardsMap.put(aux, resourceIDalt);
                    }
                }
            }
        }

        if(isVisible){
            if(defaultMode){
                return new ImageView(defaultCardsMap.get(carta));    
            } else {
                return new ImageView(altCardsMap.get(carta));
            }
        } else {
            return new ImageView(getResourceRevesCarta(defaultMode));
        }
    }
    
    private static Image getResourceCarta(Carta carta, boolean defaultMode){
        switch(carta.getColor()){
            case comodin: return getResourceComodin(carta.getTipo(), defaultMode);
            case rojo: return getResourceRojo(carta.getTipo(), defaultMode);
            case azul: return getResourceAzul(carta.getTipo(), defaultMode);
            case verde: return getResourceVerde(carta.getTipo(), defaultMode);
            case amarillo: return getResourceAmarillo(carta.getTipo(), defaultMode);
            default: return null;
        }
    }
    
    
    
    private static Image getResourceRevesCarta(boolean defaultMode){
        if(defaultMode){
            return cargarImagen("images/cartas/set-1/negras/tacoVacio.png");
        }else{
            return cargarImagen("images/cartas/set-2/negras/tacoVacio.png");
        }
    }

    private static Image getResourceMazoCartas(boolean defaultMode){
        if(defaultMode){
            return R.drawable.carta_mazo;
        }else{
            return R.drawable.carta_alt_mazo;
        }
    }

    private static Image getResourceComodin(Carta.Tipo tipo, boolean defaultMode){
        if(defaultMode){
            switch (tipo){
                case cambioColor: return R.drawable.comodin_cambio_color;
                case mas4: return R.drawable.comodin_mas4;
                default: return INVALID_RESOURCE_ID;
            }
        }else{
            switch (tipo){
                case cambioColor: return R.drawable.comodin_alt_cambio_color;
                case mas4: return R.drawable.comodin_alt_mas4;
                default: return INVALID_RESOURCE_ID;
            }
        }
    }

    private static Image getResourceRojo(Carta.Tipo tipo, boolean defaultMode){
        if(defaultMode){
            switch (tipo){
                case n0: return R.drawable.rojo_0;
                case n1: return R.drawable.rojo_1;
                case n2: return R.drawable.rojo_2;
                case n3: return R.drawable.rojo_3;
                case n4: return R.drawable.rojo_4;
                case n5: return R.drawable.rojo_5;
                case n6: return R.drawable.rojo_6;
                case n7: return R.drawable.rojo_7;
                case n8: return R.drawable.rojo_8;
                case n9: return R.drawable.rojo_9;
                case mas2: return R.drawable.rojo_mas2;
                case salta: return R.drawable.rojo_saltar;
                case reversa: return R.drawable.rojo_cambio_sentido;
                case rayosX: return R.drawable.rojo_rayosx;
                case intercambio: return R.drawable.rojo_intercambio;
                case x2: return R.drawable.rojo_x2;
                case cambioColor: return R.drawable.rojo_cambio_color;
                case mas4: return R.drawable.rojo_mas4;
                default: return INVALID_RESOURCE_ID;
            }
        }else{
            switch (tipo){
                case n0: return R.drawable.rojo_alt_0;
                case n1: return R.drawable.rojo_alt_1;
                case n2: return R.drawable.rojo_alt_2;
                case n3: return R.drawable.rojo_alt_3;
                case n4: return R.drawable.rojo_alt_4;
                case n5: return R.drawable.rojo_alt_5;
                case n6: return R.drawable.rojo_alt_6;
                case n7: return R.drawable.rojo_alt_7;
                case n8: return R.drawable.rojo_alt_8;
                case n9: return R.drawable.rojo_alt_9;
                case mas2: return R.drawable.rojo_alt_mas2;
                case salta: return R.drawable.rojo_alt_saltar;
                case reversa: return R.drawable.rojo_alt_cambio_sentido;
                case rayosX: return R.drawable.rojo_alt_rayosx;
                case intercambio: return R.drawable.rojo_alt_intercambio;
                case x2: return R.drawable.rojo_alt_x2;
                case cambioColor: return R.drawable.rojo_alt_cambio_color;
                case mas4: return R.drawable.rojo_alt_mas4;
                default: return INVALID_RESOURCE_ID;
            }
        }
    }

    private static Image getResourceAzul(Carta.Tipo tipo, boolean defaultMode){
        if(defaultMode){
            switch (tipo){
                case n0: return R.drawable.azul_0;
                case n1: return R.drawable.azul_1;
                case n2: return R.drawable.azul_2;
                case n3: return R.drawable.azul_3;
                case n4: return R.drawable.azul_4;
                case n5: return R.drawable.azul_5;
                case n6: return R.drawable.azul_6;
                case n7: return R.drawable.azul_7;
                case n8: return R.drawable.azul_8;
                case n9: return R.drawable.azul_9;
                case mas2: return R.drawable.azul_mas2;
                case salta: return R.drawable.azul_saltar;
                case reversa: return R.drawable.azul_cambio_sentido;
                case rayosX: return R.drawable.azul_rayosx;
                case intercambio: return R.drawable.azul_intercambio;
                case x2: return R.drawable.azul_x2;
                case cambioColor: return R.drawable.azul_cambio_color;
                case mas4: return R.drawable.azul_mas4;
                default: return INVALID_RESOURCE_ID;
            }
        }else{
            switch (tipo){
                case n0: return R.drawable.azul_alt_0;
                case n1: return R.drawable.azul_alt_1;
                case n2: return R.drawable.azul_alt_2;
                case n3: return R.drawable.azul_alt_3;
                case n4: return R.drawable.azul_alt_4;
                case n5: return R.drawable.azul_alt_5;
                case n6: return R.drawable.azul_alt_6;
                case n7: return R.drawable.azul_alt_7;
                case n8: return R.drawable.azul_alt_8;
                case n9: return R.drawable.azul_alt_9;
                case mas2: return R.drawable.azul_alt_mas2;
                case salta: return R.drawable.azul_alt_saltar;
                case reversa: return R.drawable.azul_alt_cambio_sentido;
                case rayosX: return R.drawable.azul_alt_rayosx;
                case intercambio: return R.drawable.azul_alt_intercambio;
                case x2: return R.drawable.azul_alt_x2;
                case cambioColor: return R.drawable.azul_alt_cambio_color;
                case mas4: return R.drawable.azul_alt_mas4;
                default: return INVALID_RESOURCE_ID;
            }
        }
    }

    private static Image getResourceVerde(Carta.Tipo tipo, boolean defaultMode){
        if(defaultMode){
            switch (tipo){
                case n0: return R.drawable.verde_0;
                case n1: return R.drawable.verde_1;
                case n2: return R.drawable.verde_2;
                case n3: return R.drawable.verde_3;
                case n4: return R.drawable.verde_4;
                case n5: return R.drawable.verde_5;
                case n6: return R.drawable.verde_6;
                case n7: return R.drawable.verde_7;
                case n8: return R.drawable.verde_8;
                case n9: return R.drawable.verde_9;
                case mas2: return R.drawable.verde_mas2;
                case salta: return R.drawable.verde_saltar;
                case reversa: return R.drawable.verde_cambio_sentido;
                case rayosX: return R.drawable.verde_rayosx;
                case intercambio: return R.drawable.verde_intercambio;
                case x2: return R.drawable.verde_x2;
                case cambioColor: return R.drawable.verde_cambio_color;
                case mas4: return R.drawable.verde_mas4;
                default: return INVALID_RESOURCE_ID;
            }
        }else{
            switch (tipo){
                case n0: return R.drawable.verde_alt_0;
                case n1: return R.drawable.verde_alt_1;
                case n2: return R.drawable.verde_alt_2;
                case n3: return R.drawable.verde_alt_3;
                case n4: return R.drawable.verde_alt_4;
                case n5: return R.drawable.verde_alt_5;
                case n6: return R.drawable.verde_alt_6;
                case n7: return R.drawable.verde_alt_7;
                case n8: return R.drawable.verde_alt_8;
                case n9: return R.drawable.verde_alt_9;
                case mas2: return R.drawable.verde_alt_mas2;
                case salta: return R.drawable.verde_alt_saltar;
                case reversa: return R.drawable.verde_alt_cambio_sentido;
                case rayosX: return R.drawable.verde_alt_rayosx;
                case intercambio: return R.drawable.verde_alt_intercambio;
                case x2: return R.drawable.verde_alt_x2;
                case cambioColor: return R.drawable.verde_alt_cambio_color;
                case mas4: return R.drawable.verde_alt_mas4;
                default: return INVALID_RESOURCE_ID;
            }
        }
    }

    private static Image getResourceAmarillo(Carta.Tipo tipo, boolean defaultMode){
        if(defaultMode){
            switch (tipo){
                case n0: return R.drawable.amarillo_0;
                case n1: return R.drawable.amarillo_1;
                case n2: return R.drawable.amarillo_2;
                case n3: return R.drawable.amarillo_3;
                case n4: return R.drawable.amarillo_4;
                case n5: return R.drawable.amarillo_5;
                case n6: return R.drawable.amarillo_6;
                case n7: return R.drawable.amarillo_7;
                case n8: return R.drawable.amarillo_8;
                case n9: return R.drawable.amarillo_9;
                case mas2: return R.drawable.amarillo_mas2;
                case salta: return R.drawable.amarillo_saltar;
                case reversa: return R.drawable.amarillo_cambio_sentido;
                case rayosX: return R.drawable.amarillo_rayosx;
                case intercambio: return R.drawable.amarillo_intercambio;
                case x2: return R.drawable.amarillo_x2;
                case cambioColor: return R.drawable.amarillo_cambio_color;
                case mas4: return R.drawable.amarillo_mas4;
                default: return INVALID_RESOURCE_ID;
            }
        }else{
            switch (tipo){
                case n0: return R.drawable.amarillo_alt_0;
                case n1: return R.drawable.amarillo_alt_1;
                case n2: return R.drawable.amarillo_alt_2;
                case n3: return R.drawable.amarillo_alt_3;
                case n4: return R.drawable.amarillo_alt_4;
                case n5: return R.drawable.amarillo_alt_5;
                case n6: return R.drawable.amarillo_alt_6;
                case n7: return R.drawable.amarillo_alt_7;
                case n8: return R.drawable.amarillo_alt_8;
                case n9: return R.drawable.amarillo_alt_9;
                case mas2: return R.drawable.amarillo_alt_mas2;
                case salta: return R.drawable.amarillo_alt_saltar;
                case reversa: return R.drawable.amarillo_alt_cambio_sentido;
                case rayosX: return R.drawable.amarillo_alt_rayosx;
                case intercambio: return R.drawable.amarillo_alt_intercambio;
                case x2: return R.drawable.amarillo_alt_x2;
                case cambioColor: return R.drawable.amarillo_alt_cambio_color;
                case mas4: return R.drawable.amarillo_alt_mas4;
                default: return INVALID_RESOURCE_ID;
            }
        }
    }
    
    ///////////////////////////////////////PRIVADAS//////////////////
    private static Image cargarImagen(String path) {
    	try {
    		return new Image(App.class.getResourceAsStream(path));
    	} catch (Exception e) {
    		return null;
    	}
    }
}
