package es.unizar.unoforall;

import javafx.scene.media.AudioClip;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.Carta;
import es.unizar.unoforall.model.partidas.Jugada;
import es.unizar.unoforall.model.partidas.Jugador;   
import es.unizar.unoforall.model.partidas.Partida;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.model.salas.ConfigSala;
import es.unizar.unoforall.utils.AnimationManager;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.MyStage;
import es.unizar.unoforall.utils.StringUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class PartidaController extends SalaReceiver implements Initializable {
    private static final Integer STARTTIME = (Partida.TIMEOUT_TURNO - 1000)/1000;
    private Timeline timeline;
    private IntegerProperty[] timersJugadores = new IntegerProperty[] {
    		new SimpleIntegerProperty(STARTTIME*100), 
    		new SimpleIntegerProperty(STARTTIME*100), 
    		new SimpleIntegerProperty(STARTTIME*100), 
    		new SimpleIntegerProperty(STARTTIME*100)
    };
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	private static final int CANCELAR = -1;
	private static final int ROJO = 1;
	private static final int VERDE = 2;
	private static final int AMARILLO = 3;
	private static final int AZUL = 4;
	
	private static final int ROBAR_CARTA = 0;
	private static final int JUGAR_CARTA = 1;
	
	private static final int JUGADOR_ABAJO = 0;
    private static final int JUGADOR_IZQUIERDA = 1;
    private static final int JUGADOR_ARRIBA = 2;
    private static final int JUGADOR_DERECHA = 3;
    private List<Carta> listaCartasEscalera;
	public DropShadow dropShadow;
    public Lighting oscurecerCarta;
    
 // Relaciona los IDs de los jugadores con los layout IDs correspondientes
    private final Map<Integer, Integer> jugadorIDmap = new HashMap<>();
    Animation animation;
 
    @FXML private BorderPane marco;   

    @FXML private Label nombreJugadorAbajo;
    @FXML private Label nombreJugadorArriba;
    @FXML private Label nombreJugadorDerecha;
    @FXML private Label nombreJugadorIzquierda;
    private Label[] nombresJugadores;
    
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
	
	@FXML private Button test;
	
	@FXML private ImageView imagenTacoRobo;
	@FXML private ImageView imagenTacoDescartes;
	@FXML private ImageView imagenSentidoPartida;
	
	@FXML private ImageView avatarJugadorAbajo;
	@FXML private ImageView avatarJugadorIzquierda;
	@FXML private ImageView avatarJugadorArriba;
	@FXML private ImageView avatarJugadorDerecha;
	private ImageView[] avataresJugadores;
	
	@FXML private Label contadorJugadorAbajo;
	@FXML private Label contadorJugadorIzquierda;
	@FXML private Label contadorJugadorArriba;
	@FXML private Label contadorJugadorDerecha;
	private Label[] contadoresJugadores;
	
	@FXML private ImageView fondoContadorJugadorAbajo;
	@FXML private ImageView fondoContadorJugadorIzquierda;
	@FXML private ImageView fondoContadorJugadorArriba;
	@FXML private ImageView fondoContadorJugadorDerecha;

	@FXML private ImageView emojiJugadorAbajo;
	@FXML private ImageView emojiJugadorIzquierda;
	@FXML private ImageView emojiJugadorArriba;
	@FXML private ImageView emojiJugadorDerecha;
	private ImageView[] emojisJugadores;
	
	@FXML private ImageView readyStairs;
	@FXML private ImageView notreadyStairs;
	@FXML private ImageView btnUno;
	@FXML private ImageView btnHabilitarEmojis;
    @FXML private ImageView btnSeleccionarEmoji;
    
    @FXML private ImageView iluminacionFondo;
	
	@FXML private Label labelVotacion;
    @FXML private Label errorEscalera;
	
	private Sala sala;
	private Partida partida; 
    private MyStage popUpRobarCarta;
    private MyStage popUpCambiarColor;
	private MyStage popUpIntercambiarMano;
	private MyStage popUpFinalizarPartida;
	private MyStage popUpSeleccionarEmojis;
    //int que representa el ID del jugador que está ejecutando la App.
	private int jugadorActualID = -1;
    private int turnoAnterior = -1;
	private final int  MAX_JUGADORES = 4;
    
    private boolean defaultMode;
    private boolean comenzarEscalera;
    private boolean sePuedePulsarBotonUNO;
	private boolean emojisHabilitados;
	
	private final int[] numCartasAnteriores = {-1, -1, -1, -1};
	
	private static final Point2D COORDS_TACO_ROBO = new Point2D(800, 300); 
	private static final Point2D COORDS_TACO_DESCARTES = new Point2D(503, 295); 
	private static final Point2D COORDS_JUGADOR_ABAJO = new Point2D(288, 640);
	private static final Point2D COORDS_JUGADOR_IZQUIERDA = new Point2D(73, 335);
	private static final Point2D COORDS_JUGADOR_ARRIBA = new Point2D(998, 20);
	private static final Point2D COORDS_JUGADOR_DERECHA = new Point2D(1208, 425);
	
	private static final Point2D[] COORDS_JUGADORES = {
		COORDS_JUGADOR_ABAJO,
		COORDS_JUGADOR_IZQUIERDA,
		COORDS_JUGADOR_ARRIBA,
		COORDS_JUGADOR_DERECHA
	};
	
	private boolean sentidoAnterior = false;
	
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		marco.setOnMouseClicked(mouseEvent -> {
            System.out.println("X: " + mouseEvent.getX());
            System.out.println("Y: " + mouseEvent.getY());
        });
		SuscripcionSala.dondeEstoy(this); 
		//La primera vez recuperamos partida y sala de la clase Suscripción sala. El resto por administrarSala();
		partida = SuscripcionSala.sala.getPartida();
		sala = SuscripcionSala.sala;
	
		jugadorActualID = partida.getIndiceJugador(App.getUsuarioID());
		UsuarioVO usuarioActual = sala.getParticipante(App.getUsuarioID());
		defaultMode = usuarioActual.getAspectoCartas() == 0;
		
		marco.setBackground(ImageManager.getBackgroundImage(App.getPersonalizacion().get("tableroSelec")));
		ImageManager.setImagenMazoCartas(imagenTacoRobo, defaultMode);
		AnimationManager.inicializarAnimacionesSentido();
		inicializarEfectos();
		
		emojisHabilitados = true;
		SuscripcionSala.suscribirseCanalVotacionPausa(respuestaVotacionPausa -> {
			int numVotantes = respuestaVotacionPausa.getNumVotantes();
			int numVotos = respuestaVotacionPausa.getNumVotos();
			
			if (numVotos == 0) {
				labelVotacion.setVisible(false);
			} else {
				labelVotacion.setVisible(true);
				labelVotacion.setText(String.format("Votación pausa: %d/%d", numVotos, numVotantes));
			}
			if (DEBUG) System.out.println("Votantes: " + numVotantes + "; Votos: " + numVotos);
		});
		
		int numJugadores = partida.getJugadores().size();
		
		listaCartasEscalera = new ArrayList<>();
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
		
		contadoresJugadores = new Label[] {
			contadorJugadorAbajo,
			contadorJugadorIzquierda,
			contadorJugadorArriba,
			contadorJugadorDerecha
		};
		
		nombresJugadores = new Label[] {
			nombreJugadorAbajo,
			nombreJugadorIzquierda,
		    nombreJugadorArriba,
		    nombreJugadorDerecha
		};

		emojisJugadores = new ImageView[] {
			emojiJugadorAbajo,
			emojiJugadorIzquierda,
			emojiJugadorArriba,
			emojiJugadorDerecha
		};
			
		//Creación del hashmap para los jugadores
		jugadorIDmap.clear();
        jugadorIDmap.put(jugadorActualID, JUGADOR_ABAJO);
		switch(numJugadores){
            case 2:
                jugadorIDmap.put((jugadorActualID+1) % numJugadores, JUGADOR_ARRIBA);
                //A futuro ocultar jugador izquierda y jugador derecha
                grupoEmojisJugadorIzquierda.setVisible(false);
                grupoEmojisJugadorDerecha.setVisible(false);
                contadorJugadorIzquierda.setVisible(false);
                contadorJugadorDerecha.setVisible(false);
				fondoContadorJugadorIzquierda.setVisible(false);
                fondoContadorJugadorDerecha.setVisible(false);
				
                scrollJugadorIzquierda.setVisible(false);
                scrollJugadorDerecha.setVisible(false);
                
                break;
            case 3:
                jugadorIDmap.put((jugadorActualID+1) % numJugadores, JUGADOR_IZQUIERDA);
                jugadorIDmap.put((jugadorActualID+2) % numJugadores, JUGADOR_ARRIBA);
              //A futuro ocultar jugador derecha
                grupoEmojisJugadorDerecha.setVisible(false);
                contadorJugadorDerecha.setVisible(false);
				fondoContadorJugadorDerecha.setVisible(false);
                scrollJugadorDerecha.setVisible(false);
                
                break;
            case 4:
                jugadorIDmap.put((jugadorActualID+1) % numJugadores, JUGADOR_IZQUIERDA);
                jugadorIDmap.put((jugadorActualID+2) % numJugadores, JUGADOR_ARRIBA);
                jugadorIDmap.put((jugadorActualID+3) % numJugadores, JUGADOR_DERECHA);
                break;
		}
		jugadorIDmap.forEach((k, v) -> System.out.println(k + " - " + v));
		System.out.println(jugadorIDmap);
		System.out.println(numJugadores);

		for (int i = 0; i < MAX_JUGADORES; i++) {
			final int jugadorID = i;
			if(jugadorIDmap.containsKey(jugadorID)) {
				//Bindear el tiempo del timer a cada jugador
				progresoJugadores[jugadorIDmap.get(jugadorID)].progressProperty().bind(
						timersJugadores[jugadorIDmap.get(jugadorID)].divide(STARTTIME*100.0).subtract(1).multiply(-1));
			}
		}
		readyStairs.setOnMouseClicked(event -> validarEscalera());
		notreadyStairs.setOnMouseClicked(event -> cancelarEscalera());

		SuscripcionSala.suscribirseCanalEmojis(respuestaEmojis -> {
			int jugadorID = respuestaEmojis.getEmisor();
			int emoji = respuestaEmojis.getEmoji();
			
			ImageManager.setImagenEmoji(emojisJugadores[jugadorIDmap.get(jugadorID)], emoji);
			emojisJugadores[jugadorIDmap.get(jugadorID)].setVisible(true);
			AnimationManager.fadeErrorEscalera(emojisJugadores[jugadorIDmap.get(jugadorID)]);
		});

//		AnimationManager.Builder builder = new AnimationManager.Builder(marco);
//		builder.withstartPoint(COORDS_TACO_ROBO)
//		.withendPoint(COORDS_JUGADOR_ABAJO)
//		.withDefaultMode(defaultMode)
//		.withCartasRobo(2).start();
		
		
		administrarSala(SuscripcionSala.sala);
	}
	
	@Override
	public void administrarSala(Sala sala) {
		if (sala.isEnPausa()) {
			if(timeline != null) {
				timeline.stop();
			}
			SuscripcionSala.cancelarSuscripcionCanalVotacionPausa();
			SuscripcionSala.cancelarSuscripcionCanalEmojis();
			App.setRoot("vistaSalaPausada");
			return;
		}
		
		if (DEBUG) System.out.println("Sala actualizada, recuperando partida...");
		//Recuperar la partida nueva
		partida = sala.getPartida();
		int turnoActual = partida.getTurno();
		boolean esNuevoTurno = turnoActual != turnoAnterior || partida.isRepeticionTurno();

		if(turnoActual == jugadorActualID){
			imagenTacoRobo.setEffect(null);
		}else{
			imagenTacoRobo.setEffect(oscurecerCarta);
		}
		
		if(esNuevoTurno) {
			comenzarEscalera = false;
			listaCartasEscalera.clear();
			readyStairs.setVisible(false);
			notreadyStairs.setVisible(false);
			
			turnoAnterior = turnoActual;
			sePuedePulsarBotonUNO = true;
			btnUno.setDisable(false);
			btnUno.setEffect(null);
			

			Jugada jugada = partida.getUltimaJugada();
            if(jugada != null && !jugada.isRobar()){
                List<Carta> cartasJugada = jugada.getCartas();
                // Es una jugada del jugador del turno anterior que no es robar
                int jugadorIDTurnoAnterior = partida.getTurnoUltimaJugada();
                
                //Caso jugador juega una carta.
                AnimationManager.Builder builder = new AnimationManager.Builder(marco);
                builder
                        .withstartPoint(COORDS_JUGADORES[jugadorIDmap.get(jugadorIDTurnoAnterior)])
                        .withendPoint(COORDS_TACO_DESCARTES)
                        .withDefaultMode(defaultMode)
                        .withCartas(cartasJugada, true)
                        .withEndAction(() -> {
                            // Se vuelve a obtener la sala, porque podría estar desactualizada
                            Sala salaActual = SuscripcionSala.sala;
                            if(salaActual != null){
                                Partida partidaActual = salaActual.getPartida();
                                if(partidaActual != null){
                                	ImageManager.setImagenCarta(imagenTacoDescartes, partida.getUltimaCartaJugada(), defaultMode, true);
                                }
                            }
                        })
                        .start();

                if(cartasJugada.get(0).getTipo() == Carta.Tipo.intercambio){
                    // Caso es un intercambio de cartas entre el jugador del turno anterior
                    //  y el jugador objetivo de la jugada

                    int jugadorIDObjetivo = jugada.getJugadorObjetivo();

                    // Mover las cartas del jugador anterior(host) al jugador objetivo
                    AnimationManager.Builder builder1 = new AnimationManager.Builder(marco);
                    builder1
                            .withstartPoint(COORDS_JUGADORES[jugadorIDmap.get(jugadorIDTurnoAnterior)])
                            .withendPoint(COORDS_JUGADORES[jugadorIDmap.get(jugadorIDObjetivo)])
                            .withDefaultMode(defaultMode)
                            .withCartas(partida.getJugadores().get(jugadorIDObjetivo).getMano(), false)
                            .withHost(true)
                            .start();

                    // Mover las cartas del jugador objetivo al jugador anterior(host)
                    AnimationManager.Builder builder2 = new AnimationManager.Builder(marco);
                    builder2
                            .withstartPoint(COORDS_JUGADORES[jugadorIDmap.get(jugadorIDObjetivo)])
                            .withendPoint(COORDS_JUGADORES[jugadorIDmap.get(jugadorIDTurnoAnterior)])
                            .withDefaultMode(defaultMode)
                            .withCartas(partida.getJugadores().get(jugadorIDTurnoAnterior).getMano(), false)
                            .start();
                }
            } else {
            	//Poner la nueva carta en la pila de descartes
        		ImageManager.setImagenCarta(imagenTacoDescartes, partida.getUltimaCartaJugada(), defaultMode, true);
            }
		}
		
		
		int jugadorIDTurnoAnterior = partida.getTurnoUltimaJugada();
		//En caso de que esté abierto el popup de selección de color y se acaba tu turno,
		//el popup se cerrará automáticamente.
		if (!partida.isRepeticionTurno() && (popUpCambiarColor != null && popUpCambiarColor.isShowing())) {
			popUpCambiarColor.close();
		}
		if (!partida.isRepeticionTurno() && (popUpRobarCarta != null && popUpRobarCarta.isShowing())) {
			popUpRobarCarta.close();
		}
		int numJugadores = partida.getJugadores().size();
		for (int i = 0; i < numJugadores; i++) {
			final int jugadorID = i;
			Jugador jugador = partida.getJugadores().get(jugadorID);
			Jugador jugadorActual = partida.getJugadorActual();
			boolean esMiTurno = jugador == jugadorActual;
			
			//Poner la imagen de perfil correcta.
			String nombreJugador = "";
			int imageID;
			if(jugador.isEsIA()){
				imageID = ImageManager.IA_IMAGE_ID;
				nombreJugador = getIAName(jugadorID);
			} else {
				UsuarioVO usuarioVO = sala.getParticipante(jugador.getJugadorID());
				imageID = usuarioVO.getAvatar();
				nombreJugador = usuarioVO.getNombre();
			}
			ImageManager.setImagenPerfil(avataresJugadores[jugadorIDmap.get(jugadorID)], imageID);
			nombresJugadores[jugadorIDmap.get(jugadorID)].setText(StringUtils.parseString(nombreJugador));
			
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
				if(esMiTurno) {
					iluminacionFondo.setVisible(true);
					comprobarRobo();
				} else {
					iluminacionFondo.setVisible(false);
				}
				
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
            	addCarta(sala, jugadorID, esMiTurno, carta, cartasJugadores[jugadorIDmap.get(jugadorID)]);
            }

			contadoresJugadores[jugadorIDmap.get(i)].setText(String.format("%d", jugador.getMano().size()));
			if(jugador.getMano().size() >= 20){
				// RED
				contadoresJugadores[jugadorIDmap.get(i)].setTextFill(Color.web("#FF0000"));
			}else{
				// WHITE
				contadoresJugadores[jugadorIDmap.get(i)].setTextFill(Color.web("#FFFFFF"));
			}
			
			
            int numCartasAntes = numCartasAnteriores[jugadorID];
            int numCartasAhora = jugador.getMano().size();
            if(numCartasAntes != -1 && numCartasAhora > numCartasAntes){
                if(partida.getUltimaCartaJugada().getTipo() != Carta.Tipo.intercambio){
                    int numCartasRobadas = numCartasAhora - numCartasAntes;
                    if(jugadorID == jugadorActualID){
                        System.out.println("Has robado " + numCartasRobadas + " carta(s)");
                    }else{
                    	System.out.println(nombreJugador + " robó " + numCartasRobadas + " carta(s)");
                    }

                    // Mostrar animación de las cartas robadas
                    AnimationManager.Builder builder = new AnimationManager.Builder(marco);
                    builder
                            .withstartPoint(COORDS_TACO_ROBO)
                            .withendPoint(COORDS_JUGADORES[jugadorIDmap.get(jugadorID)])
                            .withDefaultMode(defaultMode)
                            .withCartasRobo(numCartasRobadas)
                            .start();
                }
            }
            numCartasAnteriores[jugadorID] = numCartasAhora;
		}
		boolean sentidoActual = sala.getPartida().isSentidoHorario();
		//Recargar sentido de la partida
		AnimationManager.setAnimacionSentido(imagenSentidoPartida, sentidoAnterior, sentidoActual);
		ImageManager.setImagenSentidoPartida(imagenSentidoPartida, sentidoActual);
		sentidoAnterior = sentidoActual;
		//Poner la nueva carta en la pila de descartes
		//ImageManager.setImagenCarta(imagenTacoDescartes, partida.getUltimaCartaJugada(), defaultMode, true);

		if (sala.getPartida().estaTerminada()) {
			if(timeline != null) {
				timeline.stop();
			}
			switch (mostrarPopUpFinalizacionPartida()) {
				case FinalizarPartidaController.SALIR:
					App.setRoot("principal");
					break;
				case FinalizarPartidaController.CONTINUAR:
					SuscripcionSala.cancelarSuscripcionCanalVotacionPausa();
					SuscripcionSala.cancelarSuscripcionCanalEmojis();
					App.setRoot("vistaSala");
					break;
			}
		}
	}
    

	public static String getIAName(){
        return "IA";
    }

    public static String getIAName(int jugadorID){
        return "IA_" + jugadorID;
    }

	private void mostrarTimerVisual(int jugadorIDmapTurnoActual, int jugadorIDmapTurnoAnterior, int jugadorID) {
        if (timeline != null){
        	timeline.stop();
        	timersJugadores[jugadorIDmapTurnoAnterior].set((STARTTIME)*100);
        }
        timersJugadores[jugadorIDmapTurnoActual].set((STARTTIME)*100);
        timeline = new Timeline();
        timeline.getKeyFrames().addAll(
        		new KeyFrame(Duration.seconds(STARTTIME + 1), new KeyValue(timersJugadores[jugadorIDmapTurnoActual], 0)));
        timeline.playFromStart();

	}

	private int getParejaID(int jugadorID){
		return (jugadorID + 2) % 4;
	}
	private void addCarta(Sala sala, int jugadorID, boolean esMiTurno, Carta carta, GridPane cartasJugadorX) {
		boolean isVisible;
		if(jugadorID == jugadorActualID){
			isVisible = true;
		}else if(sala.getConfiguracion().getModoJuego() == ConfigSala.ModoJuego.Parejas){
			if(jugadorID == getParejaID(jugadorActualID)){
				isVisible = true;
			}else{
				isVisible = carta.isVisiblePor(jugadorActualID);
			}
		}else{
			isVisible = carta.isVisiblePor(jugadorActualID);
		}

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
		if (jugadorID == jugadorActualID && esMiTurno) {
			imageview.setOnMouseClicked(event -> {
				if(event.getButton() == MouseButton.PRIMARY) {
					cartaClickada(imageview);
				} else if (event.getButton() == MouseButton.SECONDARY && sala.getConfiguracion().getReglas().isJugarVariasCartas()){  
					cartaSeleccionada(imageview);
				}
			});
		}
			//imageview.setOnContextMenuRequested(event -> cartaSeleccionada(imageview));
		cartasJugadorX.addColumn(cartasJugadorX.getColumnCount(), imageview);
		//creo que la siguiente línea no hace nada
		GridPane.setHalignment(imageview, HPos.CENTER);
	}
	
	private void cartaClickada(ImageView cartaClickada) {
		if(!comenzarEscalera) {
			Carta carta = (Carta)cartaClickada.getUserData();
			if(sePuedeUsarCarta(partida, carta)) {
				System.out.println("se valida la carta" + carta);
				if(carta.getColor() == Carta.Color.comodin) {
					mostrarPopUpCambiarColor(carta);
				} else if(carta.getTipo() == Carta.Tipo.intercambio) {
					mostrarPopUpIntercambiarMano(carta);
				} else {
					Jugada jugada = new Jugada(Collections.singletonList(carta));
					SuscripcionSala.enviarJugada(jugada);
				}
			}	
			System.out.println(carta.toString());
		}
	}
	
	private void cartaSeleccionada(ImageView imageview) {
		Carta cartaSeleccionada = (Carta) imageview.getUserData();
		if(!comenzarEscalera && (Carta.esNumero(cartaSeleccionada.getTipo())) && sePuedeUsarCarta(partida, cartaSeleccionada)
				&& partida.getTurno() == jugadorActualID) {
			readyStairs.setVisible(true);
			notreadyStairs.setVisible(true);
			comenzarEscalera = true;
			//for(int i=0;i<partida.getJugadorActual().getMano().size();i++){
			for (Node child : cartasJugadores[jugadorIDmap.get(jugadorActualID)].getChildren()) {
				Carta aux = (Carta) child.getUserData();
				if (Carta.esNumero(aux.getTipo())) {
					child.setEffect(null);
				} else {
					child.setEffect(oscurecerCarta);
				}
			}
			//partida.getJugadorActual().getMano().forEach(carta ->
				//if (Carta.esNumero(cartas.get(i).getTipo());
			imageview.setEffect(new Glow(0.8));
			System.out.println(imageview.getEffect().toString());
		}
		
		if((comenzarEscalera && Carta.esNumero( ( (Carta) imageview.getUserData()).getTipo() ) )) {
			imageview.setEffect(new Glow(0.8));
			listaCartasEscalera.add((Carta) imageview.getUserData());
		}
		
	}
	private void validarEscalera() {
		Jugada jugada = new Jugada(listaCartasEscalera);
		if(partida.validarJugada(jugada)) {
			SuscripcionSala.enviarJugada(jugada);
			errorEscalera.setText("Has jugado un combo de " + listaCartasEscalera.size() +" carta(s).");
		    errorEscalera.setTextFill(Color.GREEN);
			AnimationManager.fadeErrorEscalera(errorEscalera);
		} else {
			//mostrar mensaje de error 
			//Tu escalera no es correcta. Comprueba tu jugada.
			errorEscalera.setText("Tu escalera no es correcta. Comprueba tu jugada.");
			errorEscalera.setTextFill(Color.RED);
		    AnimationManager.fadeErrorEscalera(errorEscalera);
		}
		
		
	}
	
	private void cancelarEscalera() {
		comenzarEscalera = false;
		listaCartasEscalera.clear();
		readyStairs.setVisible(false);
		notreadyStairs.setVisible(false);
		cartasJugadores[jugadorIDmap.get(jugadorActualID)].getChildren().clear();
		
		Jugador jugador = partida.getJugadores().get(jugadorActualID);
        for(Carta carta : jugador.getMano()){
        	addCarta(sala, jugadorIDmap.get(jugadorActualID), true, carta, cartasJugadores[jugadorIDmap.get(jugadorActualID)]);
        }
		
	}
	
	
    private boolean sePuedeUsarCarta(Partida partida, Carta carta){
        Jugada jugada = new Jugada(Collections.singletonList(carta));
        return partida.validarJugada(jugada);
    }
    
    
    private void comprobarRobo() {
    	Jugada jugada = new Jugada();
		if(partida.isModoJugarCartaRobada()) {
			try {
				Carta cartaRobada = partida.getCartaRobada(); 
				RobarOJugarCartaController rojcc = new RobarOJugarCartaController();
				popUpRobarCarta = new MyStage();
				(rojcc).setCard(cartaRobada);
				FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("robarOJugarCarta.fxml"));
				fxmlLoader.setController(rojcc);
				Parent root1 = (Parent) fxmlLoader.load();
				Scene scene = new Scene(root1);
				
				scene.setFill(Color.TRANSPARENT);
				popUpRobarCarta.setTitle("Pantalla Cambiar de color");
				popUpRobarCarta.setScene(scene);
				popUpRobarCarta.initStyle(StageStyle.UNDECORATED);
				popUpRobarCarta.initModality(Modality.APPLICATION_MODAL);
				popUpRobarCarta.initOwner(marco.getScene().getWindow());
	
				int resultado = popUpRobarCarta.showAndReturnDrawResult(rojcc, cartaRobada);
				System.out.println("recupero un " + resultado);
				
				switch(resultado) {
					case ROBAR_CARTA:
						break;
					case JUGAR_CARTA:
						if(cartaRobada.getColor() == Carta.Color.comodin) {
							mostrarPopUpCambiarColor(cartaRobada);
						} else if(cartaRobada.getTipo() == Carta.Tipo.intercambio) {
							mostrarPopUpIntercambiarMano(cartaRobada);
						} else {
							jugada = new Jugada(Collections.singletonList(cartaRobada));
							SuscripcionSala.enviarJugada(jugada);
						}
						break;
				}
			} catch (Exception e) {
				System.out.println("No se ha podido cargar la pagina: " + e);
			}
		SuscripcionSala.enviarJugada(jugada);	
		}
    }
    
    private void inicializarEfectos() {
    	//Agrandar botón UNO
    	btnUno.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnUno.setFitWidth(210);
				btnUno.setFitHeight(110);
			}
		});
    	//Disminuir botón UNO
    	btnUno.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnUno.setFitWidth(200);
				btnUno.setFitHeight(100);
			}
		});
		
    	//Efecto para bajar el brillo a una carta
    	oscurecerCarta = new Lighting();
    	oscurecerCarta.setDiffuseConstant(1.0);
    	oscurecerCarta.setSpecularConstant(0.0);
    	oscurecerCarta.setSpecularExponent(0.0);
    	oscurecerCarta.setSurfaceScale(0.0);
    }
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////SECCIÓN DE FUNCIONES PARA NODOS FXML////////////////////
///////////////////////////////////////////////////////////////////////////////////////      
    
	@FXML
	private void robarCarta() throws IOException {
		Jugada jugada = new Jugada();
		if(partida.validarJugada(jugada)) {
			SuscripcionSala.enviarJugada(jugada);	
		}
		
	}
    
    @FXML
    private void botonUnoPulsado(MouseEvent event) {
    	if(sePuedePulsarBotonUNO) {
    		sePuedePulsarBotonUNO = false;
    		btnUno.setDisable(true);
    		
    		ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(-0.5);
			colorAdjust.setSaturation(-0.7);
			
    		btnUno.setEffect(colorAdjust);

            App.apiweb.sendObject("/app/partidas/botonUNO/" + SuscripcionSala.sala.getSalaID(), "vacio");
            if (DEBUG) System.out.println("Has pulsado el botón UNO");
        }
    }
    
	@FXML
	private void habilitarEmojis() {
		if (emojisHabilitados) {
			emojisHabilitados = false;
			ColorAdjust colorAdjust = new ColorAdjust();
			colorAdjust.setBrightness(-0.5);
			colorAdjust.setSaturation(-0.7);
			
			btnSeleccionarEmoji.setEffect(colorAdjust);
		} else {
			emojisHabilitados = true;
			btnSeleccionarEmoji.setEffect(null);
		}
		ImageManager.setImagenEnableEmojis(btnHabilitarEmojis, emojisHabilitados);
	}

	@FXML
	private void seleccionarEmojis() {
		if (emojisHabilitados) {
			mostrarPopUpSeleccionarEmoji();
		}
	}

    @FXML
	private void pausarPartida(ActionEvent event) {
    	//MEGAPAUSA
    	SuscripcionSala.enviarVotacion();
    }
    
    @FXML
	private void abandonarPartida(ActionEvent event) {
		ButtonType styledExit = new ButtonType("Salir con estilo");
		Alert alert = new Alert(AlertType.CONFIRMATION, "  ", styledExit, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Abandonar Sala");
        alert.setHeaderText("¿Seguro que quieres abandonar la sala?");
        alert.setContentText("Si sales, serás expulsado de la partida\n y no obtendrás puntos");
        ButtonType respuesta = alert.showAndWait().get();
        if (respuesta == ButtonType.OK) {
            App.setRoot("principal");

            System.out.println("Has abandonado la sala.");
            
			SuscripcionSala.salirDeSala();
			App.setRoot("principal");
        } else if (respuesta == styledExit) {
        	System.out.println("SORPRESA");
        	AudioClip buzzer = new AudioClip(getClass().getResource("audio/styledExit.mp3").toExternalForm()); 
        	buzzer.play();
        	
        	App.setRoot("principal");
        	SuscripcionSala.salirDeSala();
        }
    }
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////SECCIÓN DE POPUPS///////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////    
	private void mostrarPopUpIntercambiarMano(Carta carta) {
		try {
			IntercambiarManoController imc = new IntercambiarManoController();
			int numJugadores = partida.getJugadores().size();
			for (int i = 0; i < numJugadores; i++) {
				Jugador jugador = partida.getJugadores().get(i);
				String nombreJugador = "";
				int imageID;
				if(jugador.isEsIA()){
					imageID = ImageManager.IA_IMAGE_ID;
					nombreJugador = getIAName(i);
				} else {
					UsuarioVO usuarioVO = sala.getParticipante(jugador.getJugadorID());
					imageID = usuarioVO.getAvatar();
					nombreJugador = usuarioVO.getNombre();
				}
				imc.listaAvatares.add(imageID);
				imc.listaNombres.add(nombreJugador);
			}
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("intercambiarMano.fxml"));
			fxmlLoader.setController(imc);
			Parent root1 = (Parent) fxmlLoader.load();
			Scene scene = new Scene(root1);
			popUpIntercambiarMano = new MyStage();

			scene.setFill(Color.TRANSPARENT);
			popUpIntercambiarMano.setTitle("Pantalla Cambiar de color");
			popUpIntercambiarMano.setScene(scene);
			popUpIntercambiarMano.initStyle(StageStyle.UNDECORATED);
			popUpIntercambiarMano.initModality(Modality.APPLICATION_MODAL);
			popUpIntercambiarMano.initOwner(marco.getScene().getWindow());
			
			int jugadorIDdevuelto = popUpIntercambiarMano.showAndReturnSwapHandResult(imc);
			System.out.println("recupero un " + jugadorIDdevuelto);
			
			if (jugadorIDdevuelto != IntercambiarManoController.Resultado.CANCELAR.ordinal()) {
				System.out.println("Se va a enviar la carta " + carta);
				Jugada jugada = new Jugada(Collections.singletonList(carta));
				jugada.setJugadorObjetivo(jugadorIDdevuelto);
				SuscripcionSala.enviarJugada(jugada);
			}
		} catch (Exception e) {
			System.out.println("No se ha podido cargar la pagina: " + e);

		} 
	}

	private void mostrarPopUpCambiarColor(Carta carta){
		try {
			CambiarColorController ccc = new CambiarColorController();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cambiarColor.fxml"));
			fxmlLoader.setController(ccc);
			Parent root1 = (Parent) fxmlLoader.load();
			Scene scene = new Scene(root1);
			popUpCambiarColor = new MyStage();
			
			scene.setFill(Color.TRANSPARENT);
			popUpCambiarColor.setTitle("Pantalla Cambiar de color");
			popUpCambiarColor.setScene(scene);
			popUpCambiarColor.initStyle(StageStyle.UNDECORATED);
			popUpCambiarColor.initModality(Modality.APPLICATION_MODAL);
			popUpCambiarColor.initOwner(marco.getScene().getWindow());

			int resultado = popUpCambiarColor.showAndReturnColourResult(ccc);
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
	}
	
	private int mostrarPopUpFinalizacionPartida() {
		int resultado = -2;
		try {
			FinalizarPartidaController fpc = new FinalizarPartidaController();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("finalizarPartida.fxml"));
			fxmlLoader.setController(fpc);
			Parent root1 = (Parent) fxmlLoader.load();
			Scene scene = new Scene(root1);
			popUpFinalizarPartida = new MyStage();
			
			scene.setFill(Color.TRANSPARENT);
			popUpFinalizarPartida.setTitle("Pantalla Finalización");
			popUpFinalizarPartida.setScene(scene);
			popUpFinalizarPartida.initStyle(StageStyle.UNDECORATED);
			popUpFinalizarPartida.initModality(Modality.APPLICATION_MODAL);
			popUpFinalizarPartida.initOwner(marco.getScene().getWindow());
			resultado = popUpFinalizarPartida.showAndReturnFinishedGameResult(fpc);
		} catch (Exception e) {
			System.out.println("No se ha podido cargar la pagina: " + e);
			e.printStackTrace();
		}
		return resultado;
	}

	private void mostrarPopUpSeleccionarEmoji() {
		try {
			SeleccionarEmojisController sec = new SeleccionarEmojisController();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("seleccionarEmojis.fxml"));
			fxmlLoader.setController(sec);
			Parent root1 = (Parent) fxmlLoader.load();
			Scene scene = new Scene(root1);
			popUpSeleccionarEmojis = new MyStage();
			
			scene.setFill(Color.TRANSPARENT);
			popUpSeleccionarEmojis.setTitle("Pantalla de selección de emojis");
			popUpSeleccionarEmojis.setScene(scene);
			popUpSeleccionarEmojis.initStyle(StageStyle.UNDECORATED);
			popUpSeleccionarEmojis.initModality(Modality.APPLICATION_MODAL);
			popUpSeleccionarEmojis.initOwner(marco.getScene().getWindow());
			int resultado = popUpSeleccionarEmojis.showAndReturnSelectedEmojiResult(sec);

			if (resultado != CANCELAR) {
				SuscripcionSala.enviarEmoji(jugadorActualID,resultado);
			}
		} catch (Exception e) {
			System.out.println("No se ha podido cargar la pagina: " + e);
			e.printStackTrace();
		}
	}
}