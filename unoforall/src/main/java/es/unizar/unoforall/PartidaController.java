package es.unizar.unoforall;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import es.unizar.unoforall.interfaces.CartaListener;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.Carta;
import es.unizar.unoforall.model.partidas.Jugada;
import es.unizar.unoforall.model.partidas.Partida;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class PartidaController extends SalaReceiver implements Initializable {

	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	
	private static final int JUGADOR_ABAJO = 0;
    private static final int JUGADOR_IZQUIERDA = 1;
    private static final int JUGADOR_ARRIBA = 2;
    private static final int JUGADOR_DERECHA = 3;
    
 // Relaciona los IDs de los jugadores con los layout IDs correspondientes
    private final Map<Integer, Integer> jugadorIDmap = new HashMap<>();
    
    private boolean defaultMode;
    
	@FXML private Label labelError;
	
	@FXML ScrollPane scrollJugadorAbajo;
	@FXML ScrollPane scrollJugadorIzquierda;
	@FXML ScrollPane scrollJugadorArriba;
	@FXML ScrollPane scrollJugadorDerecha;
	
	@FXML GridPane cartasJugadorAbajo;
	@FXML GridPane cartasJugadorIzquierda;
	@FXML GridPane cartasJugadorArriba;
	@FXML GridPane cartasJugadorDerecha;
	
	
	GridPane[] cartasJugadores;
	
	@FXML ImageView imagenTacoRobo;
	@FXML ImageView imagenTacoDescartes;
	@FXML ImageView imagenSentidoPartida;
	
	private Sala sala;
	private Partida partida; 
	
	private int jugadorActualID = -1;
	
	
//	Por defecto deDondeVengo es la pantalla principal
//	para evitar posibles errores en ejecución
	public static String deDondeVengo = "principal";
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//ESTABLECER EN QUÉ PANTALLA ESTOY PARA SALAS Y PARTIDAS
		SuscripcionSala.dondeEstoy(this); 
		
		if (DEBUG) System.out.println("Entrando en partida...");
		//La primera vez recuperamos partida y sala de la clase Suscripción sala. El resto por administrarSala();
		partida = SuscripcionSala.sala.getPartida();
		sala = SuscripcionSala.sala;
		//
		int numJugadores = partida.getJugadores().size();
		//Si no conocemos quién es el jugador actual todavía
		if (jugadorActualID == -1) {
			
			cartasJugadores = new GridPane[] {
				cartasJugadorAbajo,
				cartasJugadorIzquierda,
				cartasJugadorArriba,
				cartasJugadorDerecha,
			};
			
			jugadorActualID = partida.getIndiceJugador(App.getUsuarioID());
			
			UsuarioVO usuarioActual = sala.getParticipante(App.getUsuarioID());
			
			defaultMode = usuarioActual.getAspectoCartas() == 0;
			
			jugadorIDmap.clear();
            jugadorIDmap.put(jugadorActualID, JUGADOR_ABAJO);
			switch(numJugadores){
	            case 2:
	                jugadorIDmap.put((jugadorActualID+1) % numJugadores, JUGADOR_ARRIBA);
	                //A futuro ocultar jugador izquierda y jugador derecha
	                
	                break;
	            case 3:
	                jugadorIDmap.put((jugadorActualID+1) % numJugadores, JUGADOR_IZQUIERDA);
	                jugadorIDmap.put((jugadorActualID+2) % numJugadores, JUGADOR_ARRIBA);
	              //A futuro ocultar jugador derecha
	                
	                break;
	            case 4:
	                jugadorIDmap.put((jugadorActualID+1) % numJugadores, JUGADOR_IZQUIERDA);
	                jugadorIDmap.put((jugadorActualID+2) % numJugadores, JUGADOR_ARRIBA);
	                jugadorIDmap.put((jugadorActualID+3) % numJugadores, JUGADOR_DERECHA);
	                break;
			}
			if(DEBUG) System.out.println(jugadorIDmap);
		}
		administrarSala(SuscripcionSala.sala);
	}
	
	@Override
	public void administrarSala(Sala sala) {
		if (DEBUG) System.out.println("Sala actualizada, recuperando partida...");
		//Recuperar la partida nueva
		partida = sala.getPartida();
		
		int numJugadores = partida.getJugadores().size();
		for (int i = 0; i < numJugadores; i++) {
			final int jugadorID = i;
			cartasJugadores[jugadorIDmap.get(jugadorID)].getChildren().clear();
			partida.getJugadores().get(i).getMano().forEach(carta ->
			addCarta(sala, jugadorIDmap.get(jugadorID), jugadorID, carta, cartasJugadores[jugadorIDmap.get(jugadorID)]));
		}
		//Recargar sentido de la partida
		ImageManager.setImagenSentidoPartida(imagenSentidoPartida, sala.getPartida().isSentidoHorario());

		//Poner la nueva carta en la pila de descartes
		ImageManager.setImagenCarta(imagenTacoDescartes, partida.getUltimaCartaJugada(), defaultMode, true);
		
		
	}
	
	public HBox addImage(String imagen) {
		HBox hBox = new HBox();
		
		final ImageView image = new ImageView(new Image(getClass().getResourceAsStream(imagen)));
		
		hBox.getChildren().addAll(image);
		System.out.println("devolvió Hbox");
		return hBox;
		
	}
	
	
	private CartaListener myListener = new CartaListener() {
		@Override
		public void onClickListener(Carta carta) {
			//comprobar si la carta jugada es correcta y mandarla a la API
			//sala.
			System.out.println("He sido clickado");
		}
	};
	
	public void cargarDatos() {
		
		sala.getPartida().isSentidoHorario();
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
	
	private void addCarta(Sala sala, int jugadorLayoutID, int jugadorID, Carta carta, GridPane cartasJugadorX) {
		
		boolean isVisible = jugadorID == jugadorActualID;
		//ColumnConstraints col1 = new ColumnConstraints();
		
		ImageView imageview = new ImageView();
		Lighting lighting = new Lighting();
		lighting.setDiffuseConstant(1.0);
		lighting.setSpecularConstant(0.0);
		lighting.setSpecularExponent(0.0);
		lighting.setSurfaceScale(0.0);
		
		imageview.setEffect(lighting);
		ImageManager.setImagenCarta(imageview, carta, defaultMode, isVisible);
		imageview.setFitWidth(96);
		imageview.setFitHeight(150);
		//Guarda el objeto carta en el ImageView que lo representa.
		imageview.setUserData(carta);
		//imageview.setOnMouseClicked(event -> System.out.println(carta.toString()));
		imageview.setOnMouseClicked(event -> cartaClickada(imageview));
		cartasJugadorX.addColumn(cartasJugadorX.getColumnCount(), imageview);
		
		GridPane.setHalignment(imageview, HPos.CENTER);
	}
	
	private void cartaClickada(ImageView cartaClickada) {
		Carta carta = (Carta)cartaClickada.getUserData();
		Jugada jugada = new Jugada(Collections.singletonList(carta));
		
		if(partida.validarJugada(jugada)) {
			System.out.println("se valida la carta" + carta);
			SuscripcionSala.enviarJugada(jugada);
		}
		
		System.out.println(carta.toString());
	}
	
	public void robarCarta() {
		Jugada jugada = new Jugada();
		SuscripcionSala.enviarJugada(jugada);
	}
}