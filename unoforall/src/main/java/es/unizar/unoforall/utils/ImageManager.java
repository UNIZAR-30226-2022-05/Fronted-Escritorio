package es.unizar.unoforall.utils;

import java.util.HashMap;

import es.unizar.unoforall.App;
import es.unizar.unoforall.model.partidas.Carta;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.image.Image;

public class ImageManager {

	//Imagenes privadas para cachearlas y que sean cargadas una única vez en memoria.
	private static final HashMap<Carta, Image> defaultCardsMap = new HashMap<>();
    private static final HashMap<Carta, Image> altCardsMap = new HashMap<>();
    
    public static final int IA_IMAGE_ID = -1;
    public static final int DEFAULT_IMAGE_ID = -2;
    public static final int IMAGEN_PERFIL_0_ID = 0;
    public static final int IMAGEN_PERFIL_1_ID = 1;
    public static final int IMAGEN_PERFIL_2_ID = 2;
    public static final int IMAGEN_PERFIL_3_ID = 3;
    public static final int IMAGEN_PERFIL_4_ID = 4;
    public static final int IMAGEN_PERFIL_5_ID = 5;
    public static final int IMAGEN_PERFIL_6_ID = 6;
    
    public static final int IMAGEN_FONDO_0_ID = 0;
    public static final int IMAGEN_FONDO_1_ID = 1;
    public static final int IMAGEN_FONDO_2_ID = 2;

    public static final int IMAGEN_EMOJI_O_ID = 0;
    public static final int IMAGEN_EMOJI_1_ID = 1;
    public static final int IMAGEN_EMOJI_2_ID = 2;
    public static final int IMAGEN_EMOJI_3_ID = 3;
    public static final int IMAGEN_EMOJI_4_ID = 4;
    
    private static Image imageSentidoHorario = null;
    private static Image imageSentidoAntihorario = null;
    private static Image imageRevesCartaDefault = null;
    private static Image imageRevesCartaAlt = null;
    private static Image imageMazoCartaDefault = null;
    private static Image imageMazoCartaAlt = null;
    
    private static Image iaImage = null;
    private static Image defaultImage = null;
    private static Image imagenPerfil0 = null;
    private static Image imagenPerfil1 = null;
    private static Image imagenPerfil2 = null;
    private static Image imagenPerfil3 = null;
    private static Image imagenPerfil4 = null;
    private static Image imagenPerfil5 = null;
    private static Image imagenPerfil6 = null;
    
/////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////PUBLICAS//////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
    
//La primera vez que se llama, carga todas las cartas como imagen en el hashmap.
//Dada una carta que queremos encontrar, el booleano que dice si es del set 1 o del set 2
//y el booleano que decide si la carta es visible o no para el jugador, devuelve un ImageView
//de la carta que estamos buscando.
    public static void setImagenCarta(ImageView imageView, Carta carta, boolean defaultMode, boolean isVisible){
        if(defaultCardsMap.isEmpty() || altCardsMap.isEmpty()){
            for(Carta.Color color : Carta.Color.values()){
                for(Carta.Tipo tipo : Carta.Tipo.values()){
                    Carta aux = new Carta(tipo, color);
                    
                    Image resourceIDdefault = getResourceCarta(aux, true);
                    Image resourceIDalt = getResourceCarta(aux, false);
                    if(resourceIDdefault != null){
                        defaultCardsMap.put(aux, resourceIDdefault);
                    }
                    if(resourceIDalt != null){
                        altCardsMap.put(aux, resourceIDalt);
                    }
                }
            }
        }
        //estandarizar la imagen
        /*
        ImageView imageview = new ImageView;
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);*/
        if(isVisible) {
            if(defaultMode) {
                imageView.setImage(defaultCardsMap.get(carta));    
            } else {
            	imageView.setImage(altCardsMap.get(carta));
            }
        } else {
        	imageView.setImage(getResourceRevesCarta(defaultMode));
        }
    }
    public static void setImagenPerfil(ImageView imageview, int imageID) {
        if(imageID < -2 || imageID > 6){
            throw new IllegalArgumentException("ID de imagen inválido: " + imageID + ". Debe estar entre -2 y 6");
        }
        if(iaImage == null || defaultImage == null || imagenPerfil0 == null
        		 || imagenPerfil1 == null || imagenPerfil2 == null || imagenPerfil3 == null
        		 || imagenPerfil4 == null || imagenPerfil5 == null || imagenPerfil6 == null){
        	iaImage = cargarImagen("images/avatares/-2-IA.png");
        	defaultImage = cargarImagen("images/avatares/-2-IA.png");
        	imagenPerfil0 = cargarImagen("images/avatares/0-cero.png");
        	imagenPerfil1 = cargarImagen("images/avatares/1-uno.png");
        	imagenPerfil2 = cargarImagen("images/avatares/2-dos.png");
        	imagenPerfil3 = cargarImagen("images/avatares/3-tres.png");
        	imagenPerfil4 = cargarImagen("images/avatares/4-cuatro.png");
        	imagenPerfil5 = cargarImagen("images/avatares/5-cinco.png");
        	imagenPerfil6 = cargarImagen("images/avatares/6-seis.png");
        }
        
        switch(imageID){
        case IA_IMAGE_ID:
            imageview.setImage(iaImage);
            break;
        case DEFAULT_IMAGE_ID:
        	imageview.setImage(defaultImage);
            break;
        case IMAGEN_PERFIL_0_ID:
        	imageview.setImage(imagenPerfil0);
            break;
        case IMAGEN_PERFIL_1_ID:
        	imageview.setImage(imagenPerfil1);
            break;
        case IMAGEN_PERFIL_2_ID:
        	imageview.setImage(imagenPerfil2);
            break;
        case IMAGEN_PERFIL_3_ID:
        	imageview.setImage(imagenPerfil3);
            break;
        case IMAGEN_PERFIL_4_ID:
        	imageview.setImage(imagenPerfil4);
            break;
        case IMAGEN_PERFIL_5_ID:
        	imageview.setImage(imagenPerfil5);
            break;
        case IMAGEN_PERFIL_6_ID:
        	imageview.setImage(imagenPerfil6);
            break;
        }
    }
//Devuelve el ImageView del mazo de cartas correspondiente.
    public static ImageView setImagenMazoCartas(ImageView imageView, boolean defaultMode) {
    	return new ImageView(getResourceMazoCartas(defaultMode));
    }
    
    public static void setImagenSentidoPartida(ImageView imageView, boolean sentidoHorario) {
    	//Inicializar animaciones y cache si es la primera vez
    	if(imageSentidoHorario == null || imageSentidoAntihorario == null){
    		imageSentidoHorario = cargarImagen("images/sentidoHorario.png");
    		imageSentidoAntihorario = cargarImagen("images/sentidoAntihorario.png");
    		
    		AnimationManager.inicializarAnimacionesSentido(imageView, sentidoHorario);
    		//Guardar la imagen correspondiente en el imageView
    		if(sentidoHorario) {
    			imageView.setImage(imageSentidoHorario);
    			imageView.setUserData(imageSentidoHorario);
    		} else {
    			imageView.setImage(imageSentidoAntihorario);
    			imageView.setUserData(imageSentidoAntihorario);
    		}

    	}
    	
    	if (sentidoHorario) {
    		if ((Image)imageView.getUserData() == imageSentidoAntihorario) {

    			imageView.setUserData(imageSentidoHorario);
        		imageView.setImage(imageSentidoHorario);
        		//Transicionar a sentido Horario
        		AnimationManager.AntihorarioAHorario(imageView);
    		}
    		
    	} else {
    		if((Image)imageView.getUserData() == imageSentidoHorario) {
    			
    			imageView.setUserData(imageSentidoAntihorario);
    			imageView.setImage(imageSentidoAntihorario);
    			//Transicionar a sentido Antihorario
        		AnimationManager.HorarioAAntihorario(imageView);
    		}
    	}
    }
    
//En este método se declaran las dimensiones de las imágenes.
    public static Image cargarImagen(String path) {
    	try {
    		//return new Image(App.class.getResourceAsStream(path), 200, 150, true, false);
    		return new Image(App.class.getResourceAsStream(path));
    	} catch (Exception e) {
    		return null;
    	}
    }
/////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////PRIVADAS//////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
///////HAY QUE PONER LA URL DE LA IMAGEN TOMANDO COMO ORIGEN EL PATH DEL FICHERO FXML
///////NO EMPEZAR CON "/"CARPETAQUESEA PORQUE PETA, NO USAR CONTRABARRA AL PRINCIPIO.
    private static Image getResourceCarta(Carta carta, boolean defaultMode) {
        switch(carta.getColor()){
            case comodin: return getResourceComodin(carta.getTipo(), defaultMode);
            case rojo: return getResourceRojo(carta.getTipo(), defaultMode);
            case azul: return getResourceAzul(carta.getTipo(), defaultMode);
            case verde: return getResourceVerde(carta.getTipo(), defaultMode);
            case amarillo: return getResourceAmarillo(carta.getTipo(), defaultMode);
            default: return null;
        }
    }
    
    private static Image getResourceRevesCarta(boolean defaultMode) {
        if(imageRevesCartaDefault == null || imageRevesCartaAlt == null){
            imageRevesCartaDefault = cargarImagen("images/cartas/set-1/negras/tacoVacio.png");
            imageRevesCartaAlt = cargarImagen("images/cartas/set-2/negras/tacoVacio.png");
        }
        
        if(defaultMode) {
            return imageRevesCartaDefault;
        } else {
            return imageRevesCartaAlt;
        }
    }

    private static Image getResourceMazoCartas(boolean defaultMode) {
        if(imageMazoCartaDefault == null || imageMazoCartaAlt == null){
            imageMazoCartaDefault = cargarImagen("images/cartas/set-1/negras/tacoLleno.png");
            imageMazoCartaAlt = cargarImagen("images/cartas/set-2/negras/tacoLleno.png");
        }
        
        if(defaultMode) {
            return imageMazoCartaDefault;
        } else {
            return imageMazoCartaAlt;
        }
    }
    

    private static Image getResourceComodin(Carta.Tipo tipo, boolean defaultMode){
        if(defaultMode){
            switch (tipo) {
                case cambioColor: return cargarImagen("images/cartas/set-1/negras/cambioColor.png");
                case mas4: return cargarImagen("images/cartas/set-1/negras/mas4.png");
                default: return null;
            }
        } else {
            switch (tipo) {
                case cambioColor: return cargarImagen("images/cartas/set-2/negras/cambioColor.png");
                case mas4: return cargarImagen("images/cartas/set-2/negras/mas4.png");
                default: return null;
            }
        }
    }

    private static Image getResourceRojo(Carta.Tipo tipo, boolean defaultMode){
        if(defaultMode) {
            switch (tipo) { 
                case n0: return cargarImagen("images/cartas/set-1/rojas/0-rojo.png");
                case n1: return cargarImagen("images/cartas/set-1/rojas/1-rojo.png");
                case n2: return cargarImagen("images/cartas/set-1/rojas/2-rojo.png");
                case n3: return cargarImagen("images/cartas/set-1/rojas/3-rojo.png");
                case n4: return cargarImagen("images/cartas/set-1/rojas/4-rojo.png");
                case n5: return cargarImagen("images/cartas/set-1/rojas/5-rojo.png");
                case n6: return cargarImagen("images/cartas/set-1/rojas/6-rojo.png");
                case n7: return cargarImagen("images/cartas/set-1/rojas/7-rojo.png");
                case n8: return cargarImagen("images/cartas/set-1/rojas/8-rojo.png");
                case n9: return cargarImagen("images/cartas/set-1/rojas/9-rojo.png");
                case mas2: return cargarImagen("images/cartas/set-1/rojas/mas2-rojo.png");
                case salta: return cargarImagen("images/cartas/set-1/rojas/bloqueo-rojo.png");
                case reversa: return cargarImagen("images/cartas/set-1/rojas/cambioSentido-rojo.png");
                case rayosX: return cargarImagen("images/cartas/set-1/rojas/rayosX-rojo.png");
                case intercambio: return cargarImagen("images/cartas/set-1/rojas/intercambio-rojo.png");
                case x2: return cargarImagen("images/cartas/set-1/rojas/por2-rojo.png");
                case cambioColor: return cargarImagen("images/cartas/set-1/rojas/cambioColor-rojo.png");
                case mas4: return cargarImagen("images/cartas/set-1/rojas/mas4-rojo.png");
                default: return null;
            }
        } else {
            switch (tipo) {
	            case n0: return cargarImagen("images/cartas/set-2/rojas/0-rojo.png");
	            case n1: return cargarImagen("images/cartas/set-2/rojas/1-rojo.png");
	            case n2: return cargarImagen("images/cartas/set-2/rojas/2-rojo.png");
	            case n3: return cargarImagen("images/cartas/set-2/rojas/3-rojo.png");
	            case n4: return cargarImagen("images/cartas/set-2/rojas/4-rojo.png");
	            case n5: return cargarImagen("images/cartas/set-2/rojas/5-rojo.png");
	            case n6: return cargarImagen("images/cartas/set-2/rojas/6-rojo.png");
	            case n7: return cargarImagen("images/cartas/set-2/rojas/7-rojo.png");
	            case n8: return cargarImagen("images/cartas/set-2/rojas/8-rojo.png");
	            case n9: return cargarImagen("images/cartas/set-2/rojas/9-rojo.png");
	            case mas2: return cargarImagen("images/cartas/set-2/rojas/mas2-rojo.png");
	            case salta: return cargarImagen("images/cartas/set-2/rojas/bloqueo-rojo.png");
	            case reversa: return cargarImagen("images/cartas/set-2/rojas/cambioSentido-rojo.png");
	            case rayosX: return cargarImagen("images/cartas/set-2/rojas/rayosX-rojo.png");
	            case intercambio: return cargarImagen("images/cartas/set-2/rojas/intercambio-rojo.png");
	            case x2: return cargarImagen("images/cartas/set-2/rojas/por2-rojo.png");
	            case cambioColor: return cargarImagen("images/cartas/set-2/rojas/cambioColor-rojo.png");
	            case mas4: return cargarImagen("images/cartas/set-2/rojas/mas4-rojo.png");
                default: return null;
            }
        }
    }

    private static Image getResourceAzul(Carta.Tipo tipo, boolean defaultMode){
        if(defaultMode){
            switch (tipo){
	            case n0: return cargarImagen("images/cartas/set-1/azules/0-azul.png");
	            case n1: return cargarImagen("images/cartas/set-1/azules/1-azul.png");
	            case n2: return cargarImagen("images/cartas/set-1/azules/2-azul.png");
	            case n3: return cargarImagen("images/cartas/set-1/azules/3-azul.png");
	            case n4: return cargarImagen("images/cartas/set-1/azules/4-azul.png");
	            case n5: return cargarImagen("images/cartas/set-1/azules/5-azul.png");
	            case n6: return cargarImagen("images/cartas/set-1/azules/6-azul.png");
	            case n7: return cargarImagen("images/cartas/set-1/azules/7-azul.png");
	            case n8: return cargarImagen("images/cartas/set-1/azules/8-azul.png");
	            case n9: return cargarImagen("images/cartas/set-1/azules/9-azul.png");
	            case mas2: return cargarImagen("images/cartas/set-1/azules/mas2-azul.png");
	            case salta: return cargarImagen("images/cartas/set-1/azules/bloqueo-azul.png");
	            case reversa: return cargarImagen("images/cartas/set-1/azules/cambioSentido-azul.png");
	            case rayosX: return cargarImagen("images/cartas/set-1/azules/rayosX-azul.png");
	            case intercambio: return cargarImagen("images/cartas/set-1/azules/intercambio-azul.png");
	            case x2: return cargarImagen("images/cartas/set-1/azules/por2-azul.png");
	            case cambioColor: return cargarImagen("images/cartas/set-1/azules/cambioColor-azul.png");
	            case mas4: return cargarImagen("images/cartas/set-1/azules/mas4-azul.png");
                default: return null;
            }
        } else {
            switch (tipo){
	            case n0: return cargarImagen("images/cartas/set-2/azules/0-azul.png");
	            case n1: return cargarImagen("images/cartas/set-2/azules/1-azul.png");
	            case n2: return cargarImagen("images/cartas/set-2/azules/2-azul.png");
	            case n3: return cargarImagen("images/cartas/set-2/azules/3-azul.png");
	            case n4: return cargarImagen("images/cartas/set-2/azules/4-azul.png");
	            case n5: return cargarImagen("images/cartas/set-2/azules/5-azul.png");
	            case n6: return cargarImagen("images/cartas/set-2/azules/6-azul.png");
	            case n7: return cargarImagen("images/cartas/set-2/azules/7-azul.png");
	            case n8: return cargarImagen("images/cartas/set-2/azules/8-azul.png");
	            case n9: return cargarImagen("images/cartas/set-2/azules/9-azul.png");
	            case mas2: return cargarImagen("images/cartas/set-2/azules/mas2-azul.png");
	            case salta: return cargarImagen("images/cartas/set-2/azules/bloqueo-azul.png");
	            case reversa: return cargarImagen("images/cartas/set-2/azules/cambioSentido-azul.png");
	            case rayosX: return cargarImagen("images/cartas/set-2/azules/rayosX-azul.png");
	            case intercambio: return cargarImagen("images/cartas/set-2/azules/intercambio-azul.png");
	            case x2: return cargarImagen("images/cartas/set-2/azules/por2-azul.png");
	            case cambioColor: return cargarImagen("images/cartas/set-2/azules/cambioColor-azul.png");
	            case mas4: return cargarImagen("images/cartas/set-2/azules/mas4-azul.png");
                default: return null;
            }
        }
    }

    private static Image getResourceVerde(Carta.Tipo tipo, boolean defaultMode){
        if(defaultMode){
            switch (tipo){
	            case n0: return cargarImagen("images/cartas/set-1/verdes/0-verde.png");
	            case n1: return cargarImagen("images/cartas/set-1/verdes/1-verde.png");
	            case n2: return cargarImagen("images/cartas/set-1/verdes/2-verde.png");
	            case n3: return cargarImagen("images/cartas/set-1/verdes/3-verde.png");
	            case n4: return cargarImagen("images/cartas/set-1/verdes/4-verde.png");
	            case n5: return cargarImagen("images/cartas/set-1/verdes/5-verde.png");
	            case n6: return cargarImagen("images/cartas/set-1/verdes/6-verde.png");
	            case n7: return cargarImagen("images/cartas/set-1/verdes/7-verde.png");
	            case n8: return cargarImagen("images/cartas/set-1/verdes/8-verde.png");
	            case n9: return cargarImagen("images/cartas/set-1/verdes/9-verde.png");
	            case mas2: return cargarImagen("images/cartas/set-1/verdes/mas2-verde.png");
	            case salta: return cargarImagen("images/cartas/set-1/verdes/bloqueo-verde.png");
	            case reversa: return cargarImagen("images/cartas/set-1/verdes/cambioSentido-verde.png");
	            case rayosX: return cargarImagen("images/cartas/set-1/verdes/rayosX-verde.png");
	            case intercambio: return cargarImagen("images/cartas/set-1/verdes/intercambio-verde.png");
	            case x2: return cargarImagen("images/cartas/set-1/verdes/por2-verde.png");
	            case cambioColor: return cargarImagen("images/cartas/set-1/verdes/cambioColor-verde.png");
	            case mas4: return cargarImagen("images/cartas/set-1/verdes/mas4-verde.png");
                default: return null;
            }
        } else {
            switch (tipo){
	            case n0: return cargarImagen("images/cartas/set-2/verdes/0-verde.png");
	            case n1: return cargarImagen("images/cartas/set-2/verdes/1-verde.png");
	            case n2: return cargarImagen("images/cartas/set-2/verdes/2-verde.png");
	            case n3: return cargarImagen("images/cartas/set-2/verdes/3-verde.png");
	            case n4: return cargarImagen("images/cartas/set-2/verdes/4-verde.png");
	            case n5: return cargarImagen("images/cartas/set-2/verdes/5-verde.png");
	            case n6: return cargarImagen("images/cartas/set-2/verdes/6-verde.png");
	            case n7: return cargarImagen("images/cartas/set-2/verdes/7-verde.png");
	            case n8: return cargarImagen("images/cartas/set-2/verdes/8-verde.png");
	            case n9: return cargarImagen("images/cartas/set-2/verdes/9-verde.png");
	            case mas2: return cargarImagen("images/cartas/set-2/verdes/mas2-verde.png");
	            case salta: return cargarImagen("images/cartas/set-2/verdes/bloqueo-verde.png");
	            case reversa: return cargarImagen("images/cartas/set-2/verdes/cambioSentido-verde.png");
	            case rayosX: return cargarImagen("images/cartas/set-2/verdes/rayosX-verde.png");
	            case intercambio: return cargarImagen("images/cartas/set-2/verdes/intercambio-verde.png");
	            case x2: return cargarImagen("images/cartas/set-2/verdes/por2-verde.png");
	            case cambioColor: return cargarImagen("images/cartas/set-2/verdes/cambioColor-verde.png");
	            case mas4: return cargarImagen("images/cartas/set-2/verdes/mas4-verde.png");
                default: return null;
            }
        }
    }

    private static Image getResourceAmarillo(Carta.Tipo tipo, boolean defaultMode){
        if(defaultMode){
            switch (tipo){
	            case n0: return cargarImagen("images/cartas/set-1/amarillas/0-amarillo.png");
	            case n1: return cargarImagen("images/cartas/set-1/amarillas/1-amarillo.png");
	            case n2: return cargarImagen("images/cartas/set-1/amarillas/2-amarillo.png");
	            case n3: return cargarImagen("images/cartas/set-1/amarillas/3-amarillo.png");
	            case n4: return cargarImagen("images/cartas/set-1/amarillas/4-amarillo.png");
	            case n5: return cargarImagen("images/cartas/set-1/amarillas/5-amarillo.png");
	            case n6: return cargarImagen("images/cartas/set-1/amarillas/6-amarillo.png");
	            case n7: return cargarImagen("images/cartas/set-1/amarillas/7-amarillo.png");
	            case n8: return cargarImagen("images/cartas/set-1/amarillas/8-amarillo.png");
	            case n9: return cargarImagen("images/cartas/set-1/amarillas/9-amarillo.png");
	            case mas2: return cargarImagen("images/cartas/set-1/amarillas/mas2-amarillo.png");
	            case salta: return cargarImagen("images/cartas/set-1/amarillas/bloqueo-amarillo.png");
	            case reversa: return cargarImagen("images/cartas/set-1/amarillas/cambioSentido-amarillo.png");
	            case rayosX: return cargarImagen("images/cartas/set-1/amarillas/rayosX-amarillo.png");
	            case intercambio: return cargarImagen("images/cartas/set-1/amarillas/intercambio-amarillo.png");
	            case x2: return cargarImagen("images/cartas/set-1/amarillas/por2-amarillo.png");
	            case cambioColor: return cargarImagen("images/cartas/set-1/amarillas/cambioColor-amarillo.png");
	            case mas4: return cargarImagen("images/cartas/set-1/amarillas/mas4-amarillo.png");
	                default: return null;
            }
        } else {
            switch (tipo){
	            case n0: return cargarImagen("images/cartas/set-2/amarillas/0-amarillo.png");
	            case n1: return cargarImagen("images/cartas/set-2/amarillas/1-amarillo.png");
	            case n2: return cargarImagen("images/cartas/set-2/amarillas/2-amarillo.png");
	            case n3: return cargarImagen("images/cartas/set-2/amarillas/3-amarillo.png");
	            case n4: return cargarImagen("images/cartas/set-2/amarillas/4-amarillo.png");
	            case n5: return cargarImagen("images/cartas/set-2/amarillas/5-amarillo.png");
	            case n6: return cargarImagen("images/cartas/set-2/amarillas/6-amarillo.png");
	            case n7: return cargarImagen("images/cartas/set-2/amarillas/7-amarillo.png");
	            case n8: return cargarImagen("images/cartas/set-2/amarillas/8-amarillo.png");
	            case n9: return cargarImagen("images/cartas/set-2/amarillas/9-amarillo.png");
	            case mas2: return cargarImagen("images/cartas/set-2/amarillas/mas2-amarillo.png");
	            case salta: return cargarImagen("images/cartas/set-2/amarillas/bloqueo-amarillo.png");
	            case reversa: return cargarImagen("images/cartas/set-2/amarillas/cambioSentido-amarillo.png");
	            case rayosX: return cargarImagen("images/cartas/set-2/amarillas/rayosX-amarillo.png");
	            case intercambio: return cargarImagen("images/cartas/set-2/amarillas/intercambio-amarillo.png");
	            case x2: return cargarImagen("images/cartas/set-2/amarillas/por2-amarillo.png");
	            case cambioColor: return cargarImagen("images/cartas/set-2/amarillas/cambioColor-amarillo.png");
	            case mas4: return cargarImagen("images/cartas/set-2/amarillas/mas4-amarillo.png");
                default: return null;
            }
        }
    }
}
