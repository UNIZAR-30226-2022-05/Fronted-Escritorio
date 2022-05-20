package es.unizar.unoforall.utils;

import java.util.HashMap;

import es.unizar.unoforall.App;
import es.unizar.unoforall.model.partidas.Carta;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;

public class ImageManager {

	//Imagenes privadas para cachearlas y que sean cargadas una única vez en memoria.
	private static final HashMap<Carta, Image> defaultCardsMap = new HashMap<>();
    private static final HashMap<Carta, Image> altCardsMap = new HashMap<>();
    private static final HashMap<Integer, Image> numCardsMap = new HashMap<>();
    private static final HashMap<Integer, Image> backgroundsMap = new HashMap<>();
	private static final HashMap<Integer, Image> avatarsMap = new HashMap<>();
	private static final HashMap<Boolean, Image> readyMap = new HashMap<>();
	private static final HashMap<Boolean, Image> readyStairsMap = new HashMap<>();
	
    
    //public static final int DEFAULT_IMAGE_ID = -2;
    public static final int IA_IMAGE_ID = -1;
    public static final int IMAGEN_PERFIL_0_ID = 0;
    public static final int IMAGEN_PERFIL_1_ID = 1;
    public static final int IMAGEN_PERFIL_2_ID = 2;
    public static final int IMAGEN_PERFIL_3_ID = 3;
    public static final int IMAGEN_PERFIL_4_ID = 4;
    public static final int IMAGEN_PERFIL_5_ID = 5;
    public static final int IMAGEN_PERFIL_6_ID = 6;
    public static final int NUM_AVATARES = 8;
    

    public static final int IMAGEN_EMOJI_O_ID = 0;
    public static final int IMAGEN_EMOJI_1_ID = 1;
    public static final int IMAGEN_EMOJI_2_ID = 2;
    public static final int IMAGEN_EMOJI_3_ID = 3;
    public static final int IMAGEN_EMOJI_4_ID = 4;
	public static final int NUM_CARTAS = 20;
	
	public static final int FONDO_AZUL = 0;
	public static final int FONDO_MORADO = 1;
	public static final int FONDO_GRIS = 2;
    public static final int FONDO_DIBUJITOS = 3;
    
    private static Image imageSentidoHorario = null;
    private static Image imageSentidoAntihorario = null;
    private static Image imageRevesCartaDefault = null;
    private static Image imageRevesCartaAlt = null;
    private static Image imageMazoCartaDefault = null;
    private static Image imageMazoCartaAlt = null;
    
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
    public static void setImagenContador(ImageView imageView, int numCartas) {
    	if(numCardsMap.isEmpty()) {
    		for (int numCartasTemp = 0; numCartasTemp <= NUM_CARTAS; numCartasTemp++) {
    			Image contador = getResourceContadores(numCartasTemp);
    			numCardsMap.put(numCartasTemp, contador);
    		}
    	}
    	imageView.setImage(numCardsMap.get(numCartas));
    }
    
    public static void setImagenPerfil(ImageView imageView, int imageID) {
        if(imageID < -2 || imageID > 6){
            throw new IllegalArgumentException("ID de imagen inválido: " + imageID + ". Debe estar entre -2 y 6");
        }
        if(avatarsMap.isEmpty()){
			avatarsMap.put(IA_IMAGE_ID, cargarImagen("images/avatares/-1-IA.png"));
			//avatarsMap.put(IA_IMAGE_ID, cargarImagen("images/avatares/-1-IA.png"));
			avatarsMap.put(IMAGEN_PERFIL_0_ID, cargarImagen("images/avatares/0-cero.png"));
			avatarsMap.put(IMAGEN_PERFIL_1_ID, cargarImagen("images/avatares/1-uno.png"));
			avatarsMap.put(IMAGEN_PERFIL_2_ID, cargarImagen("images/avatares/2-dos.png"));
			avatarsMap.put(IMAGEN_PERFIL_3_ID, cargarImagen("images/avatares/3-tres.png"));
			avatarsMap.put(IMAGEN_PERFIL_4_ID, cargarImagen("images/avatares/4-cuatro.png"));
			avatarsMap.put(IMAGEN_PERFIL_5_ID, cargarImagen("images/avatares/5-cinco.png"));
			avatarsMap.put(IMAGEN_PERFIL_6_ID, cargarImagen("images/avatares/6-seis.png"));
        } 
		imageView.setImage(avatarsMap.get(imageID));
    }
    
    public static void setImagenListo(ImageView imageView, boolean listo) {
        if(readyMap.isEmpty()){
        	readyMap.put(true, cargarImagen("images/ready.png"));
        	readyMap.put(false, cargarImagen("images/notready.png"));
        }
		imageView.setImage(readyMap.get(listo));
    }
    
    public static void setImagenListoEscalera(ImageView imageView, boolean listo) {
        if(readyStairsMap.isEmpty()){
        	readyStairsMap.put(true, cargarImagen("images/readyStairs.png"));
        	readyStairsMap.put(false, cargarImagen("images/notreadyStairs.png"));
        }
		imageView.setImage(readyMap.get(listo));
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
    		AnimationManager.inicializarAnimacionesSentido();
    	//}
    	
    	System.out.println(AnimationManager.sentidoIsNotRunning());
    	AnimationManager.setAnimacionSentido(imageView, sentidoHorario);
    	//if (AnimationManager.sentidoIsNotRunning()) {
    		//AnimationManager.inicializarAnimacionesSentido(imageView, sentidoHorario);
		//Guardar la imagen correspondiente en el imageView
		if(sentidoHorario) {
			imageView.setImage(imageSentidoHorario);
			imageView.setUserData(imageSentidoHorario);
		} else {
			imageView.setImage(imageSentidoAntihorario);
			imageView.setUserData(imageSentidoAntihorario);
		}}
    	System.out.println(AnimationManager.sentidoIsNotRunning());
    	if (!AnimationManager.sentidoIsNotRunning()) { 
    		AnimationManager.setAnimacionSentido(imageView, sentidoHorario);}
    	
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
    
    public static Background getBackgroundImage(int fondo) {
    	if (backgroundsMap.isEmpty()) {
    		backgroundsMap.put(FONDO_AZUL, cargarImagen("images/fondos/azul.png"));
    		backgroundsMap.put(FONDO_MORADO, cargarImagen("images/fondos/morado.png"));
    		backgroundsMap.put(FONDO_GRIS, cargarImagen("images/fondos/gris.png"));
    		backgroundsMap.put(FONDO_DIBUJITOS, cargarImagen("images/fondos/dibujitos.png"));
    	}
    	
    	return new Background(
    				new BackgroundImage(
    					backgroundsMap.get(fondo),
						BackgroundRepeat.NO_REPEAT,
						BackgroundRepeat.NO_REPEAT,
						BackgroundPosition.CENTER,
						BackgroundSize.DEFAULT
					)
				);
    }
/////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////PRIVADAS//////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////
///////HAY QUE PONER LA URL DE LA IMAGEN TOMANDO COMO ORIGEN EL PATH DEL FICHERO FXML
///////NO EMPEZAR CON "/"CARPETAQUESEA PORQUE PETA, NO USAR CONTRABARRA AL PRINCIPIO.
    private static Image getResourceContadores(int numCartas) {
    	switch(numCartas) {
    		case 0: return cargarImagen("images/cartas/contadoresCartas/0NumCartas.png");
    		case 1: return cargarImagen("images/cartas/contadoresCartas/1NumCartas.png");
    		case 2: return cargarImagen("images/cartas/contadoresCartas/2NumCartas.png");
    		case 3: return cargarImagen("images/cartas/contadoresCartas/3NumCartas.png");
    		case 4: return cargarImagen("images/cartas/contadoresCartas/4NumCartas.png"); 
    		case 5: return cargarImagen("images/cartas/contadoresCartas/5NumCartas.png");
    		case 6: return cargarImagen("images/cartas/contadoresCartas/6NumCartas.png");
    		case 7: return cargarImagen("images/cartas/contadoresCartas/7NumCartas.png");
    		case 8: return cargarImagen("images/cartas/contadoresCartas/8NumCartas.png");
    		case 9: return cargarImagen("images/cartas/contadoresCartas/9NumCartas.png");
    		case 10: return cargarImagen("images/cartas/contadoresCartas/10NumCartas.png");
    		case 11: return cargarImagen("images/cartas/contadoresCartas/11NumCartas.png");
    		case 12: return cargarImagen("images/cartas/contadoresCartas/12NumCartas.png");
    		case 13: return cargarImagen("images/cartas/contadoresCartas/13NumCartas.png");
    		case 14: return cargarImagen("images/cartas/contadoresCartas/14NumCartas.png");
    		case 15: return cargarImagen("images/cartas/contadoresCartas/15NumCartas.png");
    		case 16: return cargarImagen("images/cartas/contadoresCartas/16NumCartas.png");
    		case 17: return cargarImagen("images/cartas/contadoresCartas/17NumCartas.png");
    		case 18: return cargarImagen("images/cartas/contadoresCartas/18NumCartas.png");
    		case 19: return cargarImagen("images/cartas/contadoresCartas/19NumCartas.png");
    		case 20: return cargarImagen("images/cartas/contadoresCartas/20NumCartas.png");
    		default: return null;
    	}
    }
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
