package es.unizar.unoforall;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.Carta;
import es.unizar.unoforall.model.partidas.Jugada;
import es.unizar.unoforall.model.partidas.Jugador;
import es.unizar.unoforall.model.partidas.Partida;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.MyStage;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class PartidaController extends SalaReceiver implements Initializable {
    @FXML
    private Circle circulo;
    private static final Integer STARTTIME = (Partida.TIMEOUT_TURNO - 1000)/1000;
    private Timeline timeline;
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME*100);
    private IntegerProperty[] timersJugadores = new IntegerProperty[] {
    		new SimpleIntegerProperty(STARTTIME*100), 
    		new SimpleIntegerProperty(STARTTIME*100), 
    		new SimpleIntegerProperty(STARTTIME*100), 
    		new SimpleIntegerProperty(STARTTIME*100)
    };
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	private static final int CANCELAR = 0;
	private static final int ROJO = 1;
	private static final int VERDE = 2;
	private static final int AMARILLO = 3;
	private static final int AZUL = 4;
	
	private static final int JUGADOR_ABAJO = 0;
    private static final int JUGADOR_IZQUIERDA = 1;
    private static final int JUGADOR_ARRIBA = 2;
    private static final int JUGADOR_DERECHA = 3;

    public Lighting oscurecerCarta;
    
 // Relaciona los IDs de los jugadores con los layout IDs correspondientes
    private final Map<Integer, Integer> jugadorIDmap = new HashMap<>();
    Animation animation;
    @FXML private Label labelError;
    @FXML private Label timerLabel;
    @FXML private BorderPane marco;

    @FXML private ProgressIndicator progresoJugadorAbajo;
    @FXML private ProgressIndicator progresoJugadorIzquierda;
    @FXML private ProgressIndicator progresoJugadorArriba;
    @FXML private ProgressIndicator progresoJugadorDerecha;
    private ProgressIndicator[] progresoJugadores;
    
    @FXML private Group grupoEmojisJugadorAbajo;
    @FXML private Group grupoEmojisJugadorArriba;
    @FXML private Group grupoEmojisJugadorDerecha;
    @FXML private Group grupoEmojisJugadorIzquierda;
    
	@FXML private ScrollPane scrollJugadorAbajo;
	@FXML private ScrollPane scrollJugadorIzquierda;
	@FXML private ScrollPane scrollJugadorArriba;
	@FXML private ScrollPane scrollJugadorDerecha;
	
	@FXML private GridPane cartasJugadorAbajo;
	@FXML private GridPane cartasJugadorIzquierda;
	@FXML private GridPane cartasJugadorArriba;
	@FXML private GridPane cartasJugadorDerecha;
	private GridPane[] cartasJugadores;
	
	@FXML private Button btnCargarDatos;
	@FXML private Button test;
	
	@FXML private ImageView imagenTacoRobo;
	@FXML private ImageView imagenTacoDescartes;
	@FXML private ImageView imagenSentidoPartida;
	
	@FXML private ImageView avatarJugadorAbajo;
	@FXML private ImageView avatarJugadorIzquierda;
	@FXML private ImageView avatarJugadorArriba;
	@FXML private ImageView avatarJugadorDerecha;
	private ImageView[] avataresJugadores;
	
	private Sala sala;
	private Partida partida; 
    private MyStage stage;
    //int que representa el ID del jugador que está ejecutando la App.
	private int jugadorActualID = -1;
    private int turnoAnterior = -1;
    
    private boolean defaultMode;
    //Por defecto deDondeVengo es la pantalla principal
    //para evitar posibles errores en ejecución
	public static String deDondeVengo = "principal";
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//ESTABLECER EN QUÉ PANTALLA ESTOY PARA SALAS Y PARTIDAS
		
		inicializarEfectos();
		SuscripcionSala.dondeEstoy(this); 
		//La primera vez recuperamos partida y sala de la clase Suscripción sala. El resto por administrarSala();
		partida = SuscripcionSala.sala.getPartida();
		sala = SuscripcionSala.sala;
		int numJugadores = partida.getJugadores().size();
		//Si no conocemos quién es el jugador actual todavía
		if (jugadorActualID == -1) {
			
			cartasJugadores = new GridPane[] {
				cartasJugadorAbajo,
				cartasJugadorIzquierda,
				cartasJugadorArriba,
				cartasJugadorDerecha,
			};
			
			progresoJugadores = new ProgressIndicator[] {
				progresoJugadorAbajo,
				progresoJugadorIzquierda,
				progresoJugadorArriba,
				progresoJugadorDerecha
			};
			
			avataresJugadores = new ImageView[] {
				avatarJugadorAbajo,
				avatarJugadorIzquierda,
				avatarJugadorArriba,
				avatarJugadorDerecha
			};
			
 			jugadorActualID = partida.getIndiceJugador(App.getUsuarioID());
 			
			UsuarioVO usuarioActual = sala.getParticipante(App.getUsuarioID());
			defaultMode = usuarioActual.getAspectoCartas() == 0;
			//Creación del hashmap para los jugadores
			jugadorIDmap.clear();
            jugadorIDmap.put(jugadorActualID, JUGADOR_ABAJO);
			switch(numJugadores){
	            case 2:
	                jugadorIDmap.put((jugadorActualID+1) % numJugadores, JUGADOR_ARRIBA);
	                //A futuro ocultar jugador izquierda y jugador derecha
	                grupoEmojisJugadorIzquierda.setVisible(false);
	                grupoEmojisJugadorDerecha.setVisible(false);
	                
	                break;
	            case 3:
	                jugadorIDmap.put((jugadorActualID+1) % numJugadores, JUGADOR_IZQUIERDA);
	                jugadorIDmap.put((jugadorActualID+2) % numJugadores, JUGADOR_ARRIBA);
	              //A futuro ocultar jugador derecha
	                grupoEmojisJugadorDerecha.setVisible(false);
	                
	                break;
	            case 4:
	                jugadorIDmap.put((jugadorActualID+1) % numJugadores, JUGADOR_IZQUIERDA);
	                jugadorIDmap.put((jugadorActualID+2) % numJugadores, JUGADOR_ARRIBA);
	                jugadorIDmap.put((jugadorActualID+3) % numJugadores, JUGADOR_DERECHA);
	                break;
			}
			
			for (int i = 0; i < numJugadores; i++) {	
				final int jugadorID = i;
				Jugador jugador = partida.getJugadores().get(jugadorID);
				//Bindear el tiempo del timer a cada jugador
				progresoJugadores[jugadorIDmap.get(jugadorID)].progressProperty().bind(
						timersJugadores[jugadorIDmap.get(jugadorID)].divide(STARTTIME*100.0).subtract(1).multiply(-1));
				//Poner la imagen de perfil correcta.
	            String nombreJugador;
	            int imageID;
	            if(jugador.isEsIA()){
	                imageID = ImageManager.IA_IMAGE_ID;
	                //nombreJugador = getIAName(jugadorID);
	            } else {
	                UsuarioVO usuarioVO = sala.getParticipante(jugador.getJugadorID());
	                imageID = usuarioVO.getAvatar();
	                nombreJugador = usuarioVO.getNombre();
	            }
				ImageManager.setImagenPerfil(avataresJugadores[jugadorIDmap.get(jugadorID)], imageID);
			}
		}
		administrarSala(SuscripcionSala.sala);
	}
	
	@Override
	public void administrarSala(Sala sala) {
		if (DEBUG) System.out.println("Sala actualizada, recuperando partida...");
		//Recuperar la partida nueva
		partida = sala.getPartida();
		int turnoActual = partida.getTurno();
		boolean esNuevoTurno = turnoActual != turnoAnterior || partida.isRepeticionTurno();
		
		if(esNuevoTurno) {
			turnoAnterior = turnoActual;
		}
		
		
		int jugadorIDTurnoAnterior = partida.getTurnoUltimaJugada();
		//En caso de que esté abierto el popup de selección de color y se acaba tu turno,
		//el popup se cerrará automáticamente.
		if (!partida.isRepeticionTurno() && (stage != null && stage.isShowing())) {
			stage.close();
		}
		int numJugadores = partida.getJugadores().size();
		for (int i = 0; i < numJugadores; i++) {
			final int jugadorID = i;
			Jugador jugador = partida.getJugadores().get(jugadorID);
			Jugador jugadorActual = partida.getJugadorActual();
			boolean esMiTurno = jugador == jugadorActual;
            if(sala.isEnPartida() && (turnoActual == jugadorID) && esNuevoTurno){
            	mostrarTimerVisual(jugadorIDmap.get(jugadorID), jugadorIDmap.get(jugadorIDTurnoAnterior), jugadorID);  
            }

			if(jugadorActualID == jugadorID){
                //Esto a futuro
				/*
				if(jugador.isPenalizado_UNO()){
                    mostrarMensaje("Has sido penalizado por no decir UNO");
                }
				*/
                jugador.getMano().sort((carta1, carta2) -> {
                    boolean sePuedeUsarCarta1 = sePuedeUsarCarta(partida, carta1);
                    boolean sePuedeUsarCarta2 = sePuedeUsarCarta(partida, carta2);
                    if(sePuedeUsarCarta1 && !sePuedeUsarCarta2){
                        return -1;
                    } else if(!sePuedeUsarCarta1 && sePuedeUsarCarta2) {
                        return 1;
                    } else {
                        return carta1.compareTo(carta2);
                    }
                });
            } else {
                jugador.getMano().sort((carta1, carta2) -> {
                    boolean sePuedeVerCarta1 = carta1.isVisiblePor(jugadorActualID);
                    boolean sePuedeVerCarta2 = carta2.isVisiblePor(jugadorActualID);
                    if(sePuedeVerCarta1 && !sePuedeVerCarta2){
                        return -1;
                    }else if(!sePuedeVerCarta1 && sePuedeVerCarta2){
                        return 1;
                    }else{
                        return carta1.compareTo(carta2);
                    }
                });
            }
			cartasJugadores[jugadorIDmap.get(jugadorID)].getChildren().clear();
			//Si no es mi turno, no se encenderán mis cartas.
			
            for(Carta carta : jugador.getMano()){
            	addCarta(sala, jugadorIDmap.get(jugadorID), esMiTurno, carta, cartasJugadores[jugadorIDmap.get(jugadorID)]);
            }
			//partida.getJugadores().get(i).getMano().forEach(carta ->
		}
		//Recargar sentido de la partida
		ImageManager.setImagenSentidoPartida(imagenSentidoPartida, sala.getPartida().isSentidoHorario());

		//Poner la nueva carta en la pila de descartes
		ImageManager.setImagenCarta(imagenTacoDescartes, partida.getUltimaCartaJugada(), defaultMode, true);
	}
    
	public static String getIAName(){
        return "IA";
    }

    public static String getIAName(int jugadorID){
        return "IA_" + jugadorID;
    }

	private void mostrarTimerVisual(int jugadorIDmapTurnoActual, int jugadorIDmapTurnoAnterior, int jugadorID) {
        if (timeline != null || jugadorIDmapTurnoActual != jugadorIDmapTurnoAnterior){
        	timeline.stop();
        	timersJugadores[jugadorIDmapTurnoAnterior].set((STARTTIME)*100);
        }
        timersJugadores[jugadorIDmapTurnoActual].set((STARTTIME)*100);
        timeline = new Timeline();
        boolean miTimer = jugadorActualID == jugadorID;//jugadorIDmap.get(partida.getIndiceJugador(partida.getJugadorActual().getJugadorID()));
        if(miTimer) {
        	System.out.println("miTurno");
	        timeline.getKeyFrames().addAll(
	                new KeyFrame(Duration.seconds(STARTTIME + 1), new KeyValue(timersJugadores[jugadorIDmapTurnoActual], 0)),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.70), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.75), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.80), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.85), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.87), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.89), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.91), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.93), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.95), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.96), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.97), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.98), this::doAnimation),
	                new KeyFrame(Duration.millis(((STARTTIME + 1) * 1000) * 0.99), this::doAnimation));	
        } else {
        	System.out.println("no es miTurno");
        	timeline.getKeyFrames().addAll(
	                new KeyFrame(Duration.seconds(STARTTIME + 1), new KeyValue(timersJugadores[jugadorIDmapTurnoActual], 0)));
        }
        timeline.playFromStart();

	}
	
	private void doAnimation(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		animation.stop();
		animation.play();
	}

	//Esta función no se usa
	public HBox addImage(String imagen) {
		HBox hBox = new HBox();
		
		final ImageView image = new ImageView(new Image(getClass().getResourceAsStream(imagen)));
		
		hBox.getChildren().addAll(image);
		System.out.println("devolvió Hbox");
		return hBox;
		
	}
	
	public void cargarDatos() {
        final Animation animation = new Transition() {

            {
                setCycleDuration(Duration.millis(1000));
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(1, 0, 0, 1 - frac);
                circulo.setFill(new Color(1, 0, 0, 1 - frac));
            }
        };
        animation.play();
		
		/*Carta aux = new Carta();
		aux = partida.getJugadorActual().getMano().get(1);
		for (int i = 1; i < 20; i++) {
			
			//addCarta(sala, JUGADOR_ABAJO, jugadorActualID, aux, cartasJugadorAbajo);
			//addCarta(sala, JUGADOR_ABAJO, jugadorActualID, aux, cartasJugadorDerecha);
			//addCarta(sala, JUGADOR_ABAJO, jugadorActualID, aux, cartasJugadorIzquierda);
			//addCarta(sala, JUGADOR_ABAJO, jugadorActualID, aux, cartasJugadorArriba);
			
			partida.getJugadorActual().getMano().forEach(carta ->
			addCarta(sala, JUGADOR_ABAJO, jugadorActualID, carta, cartasJugadorAbajo));
        }*/
	}
	
	private void addCarta(Sala sala, int jugadorID, boolean esMiTurno, Carta carta, GridPane cartasJugadorX) {
		boolean isVisible = jugadorID == jugadorActualID;
		//ColumnConstraints col1 = new ColumnConstraints();
		ImageView imageview = new ImageView();
		//Si son las cartas del usuario, ilumina sus cartas usables.
		//Las cartas de otros usuarios nunca serán iluminadas.
		
		if( (!esMiTurno) || (!sePuedeUsarCarta(partida, carta) && isVisible)) {
			imageview.setEffect(oscurecerCarta);
		} else if (jugadorID != jugadorActualID) {
			imageview.setEffect(oscurecerCarta);
		}
		
		ImageManager.setImagenCarta(imageview, carta, defaultMode, isVisible);
		imageview.setFitWidth(96);
		imageview.setFitHeight(150);
		//Guarda el objeto carta en el ImageView que lo representa.
		imageview.setUserData(carta);
		//imageview.setOnMouseClicked(event -> System.out.println(carta.toString()));
		imageview.setOnMouseClicked(event -> cartaClickada(imageview));
		cartasJugadorX.addColumn(cartasJugadorX.getColumnCount(), imageview);
		//creo que la siguiente línea no hace nada
		GridPane.setHalignment(imageview, HPos.CENTER);
	}
	
	private void cartaClickada(ImageView cartaClickada) {
		Carta carta = (Carta)cartaClickada.getUserData();
		
		if(sePuedeUsarCarta(partida, carta)) {
			System.out.println("se valida la carta" + carta);
			if(carta.getColor() == Carta.Color.comodin) {
				try {
					CambiarColorController ccc = new CambiarColorController();
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cambiarColor.fxml"));
					fxmlLoader.setController(ccc);
					Parent root1 = (Parent) fxmlLoader.load();
					Scene scene = new Scene(root1);
					stage = new MyStage();
					
					scene.setFill(Color.TRANSPARENT);
					stage.setTitle("Pantalla Cambiar de color");
					stage.setScene(scene);
					stage.initStyle(StageStyle.UTILITY);
					stage.initModality(Modality.APPLICATION_MODAL);
					stage.initOwner(marco.getScene().getWindow());

					int resultado = stage.showAndReturnResult(ccc);
					System.out.println("recupero un " + resultado);
					//Ponerle a la carta el color deseado por el jugador mediante el popup.
					//Ojo, el defaultmode elige el color real, no se hace aquí.
					switch(resultado) {
						case ROJO: 
							carta.setColor(Carta.Color.rojo);
							break;
						case VERDE: 
							carta.setColor(Carta.Color.verde);
							break;
						case AMARILLO: 
							carta.setColor(Carta.Color.amarillo);
							break;
						case AZUL: 
							carta.setColor(Carta.Color.azul);
							break;
						default: break;
					}
					if (resultado != CANCELAR) {
						System.out.println("Se va a enviar la carta " + carta);
						Jugada jugada = new Jugada(Collections.singletonList(carta));
						SuscripcionSala.enviarJugada(jugada);
					}
				} catch (Exception e) {
					System.out.println("No se ha podido cargar la pagina: " + e);
				}
			} else {
				Jugada jugada = new Jugada(Collections.singletonList(carta));
				SuscripcionSala.enviarJugada(jugada);
			}
		}	
		System.out.println(carta.toString());
	}
	
	public void robarCarta() {
		Jugada jugada = new Jugada();
		SuscripcionSala.enviarJugada(jugada);
	}
	
    private boolean sePuedeUsarCarta(Partida partida, Carta carta){
        Jugada jugada = new Jugada(Collections.singletonList(carta));
        return partida.validarJugada(jugada);
    }
    
    private void inicializarEfectos() {
    	//Efecto para bajar el brillo a una carta
    	oscurecerCarta = new Lighting();
    	oscurecerCarta.setDiffuseConstant(1.0);
    	oscurecerCarta.setSpecularConstant(0.0);
    	oscurecerCarta.setSpecularExponent(0.0);
    	oscurecerCarta.setSurfaceScale(0.0);
    	
        animation = new Transition() {

            {
                setCycleDuration(Duration.millis(1000));
                setInterpolator(Interpolator.EASE_OUT);
            }

            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(1, 0, 0, 1 - frac);
                circulo.setFill(new Color(1, 0, 0, 1 - frac));
            }
        };
        //animation.play();
    }
}