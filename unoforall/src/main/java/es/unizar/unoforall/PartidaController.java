package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import es.unizar.unoforall.interfaces.CartaListener;
import es.unizar.unoforall.interfaces.SalaListener;
import es.unizar.unoforall.model.partidas.Carta;
import es.unizar.unoforall.model.partidas.Partida;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.StringUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PartidaController extends SalaReceiver implements Initializable {

	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	
	private static final int JUGADOR_ABAJO = 0;
    private static final int JUGADOR_IZQUIERDA = 1;
    private static final int JUGADOR_ARRIBA = 2;
    private static final int JUGADOR_DERECHA = 3;
    
    private static final String [] imageNames = new String [] {"fw1.jpg",};
    
	@FXML private Label labelError;
	
	@FXML ScrollPane misCartas;
	
	@FXML GridPane listaCartas;
	
	@FXML ImageView imagen;
	@FXML ImageView iview;
	
	@FXML Image image;
	
	private Sala sala;
	private Partida partida; 
	
//	Por defecto deDondeVengo es la pantalla principal
//	para evitar posibles errores en ejecución
	public static String deDondeVengo = "principal";
	//getJugadores().get(i).getMano();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//ESTABLECER EN QUÉ PANTALLA ESTOY PARA SALAS Y PARTIDAS
		//SuscripcionSala.dondeEstoy(this); 
		
		
		//System.out.println(SuscripcionSala.sala);
		//System.out.println(SuscripcionSala.sala.getPartida().getUltimaCartaJugada());
		//System.out.println(SuscripcionSala.sala.getPartida().getNumIAs());
		
		
		//if (DEBUG) System.out.println("Entrando en partida...");
		//partida = SuscripcionSala.sala.getPartida();
		
		//listaCartas.getChildren().clear();
		
		//misCartas.getChildren().clear();
		//misCartas.setContent(addImage());
		//Image image = new Image("/images/cartas/set-1/amarillas/0-amarillo.png");
		//image = new Image("/unoforall/src/main/java/es/unizar/unoforall/arista.png", false);
		
		
		//HAY QUE PONER LA URL DE LA IMAGEN TOMANDO COMO ORIGEN EL PATH DEL FICHERO FXML
		//NO EMPEZAR CON "/"CARPETAQUESEA PORQUE PETA, NO USAR CONTRABARRA.
		//image = new Image(getClass().getResourceAsStream("images/arista.png"));
		//image = new Image(getClass().getResourceAsStream("images/cartas/set-1/amarillas/0-amarillo.png"));
		
		
		//image = new Image("@images/cartas/set-1/amarillas/0-amarillo.png");
		//iview = new ImageView(image);
		//ImageView iview = new ImageView(image);
		
		//imagen = iview;
		//ImageView image = new ImageView("images/cartas/set-1/amarillas/0-amarillo.png");
		//HBox test = new HBox();
		
		//misCartas.setContent(iview);
		//misCartas.setContent(addImage("images/cartas/set-1/amarillas/0-amarillo.png"));
		//misCartas.setContent(addImage("images/cartas/set-1/amarillas/1-amarillo.png"));
		
		//listaCartas.addColumn(listaCartas.getColumnCount(), iview);
		
		//sala.getParticipantes()
		//int numJugadores = partida.getJugadores().size();
		
		//jugadorActualID = partida.getIndiceJugador(BackendAPI.getUsuarioID());
        //UsuarioVO usuarioActual = sala.getParticipante(BackendAPI.getUsuarioID());
		

	}
	@Override
	public void administrarSala(Sala sala) {
		
		labelError.setText("");
		if (sala.isNoExiste()) {
			labelError.setText(StringUtils.parseString(sala.getError()));
			if (DEBUG) System.out.println(sala.getError());
			SuscripcionSala.salirDeSala();
			App.setRoot(deDondeVengo);
		} else {
			//if (DEBUG) System.out.println("Estado de la sala: " + sala);
			//System.out.println(sala.getPartida().getUltimaCartaJugada());
			if (sala.isEnPartida()) {
				if (DEBUG) System.out.println("Sala actualizada, recuperando partida...");
				partida = sala.getPartida();
				partida.getJugadores();
				partida.getJugadorActual().getMano().forEach(carta ->
                System.out.println(carta.toString()));
			}
		}
	}
	
	/*
	@Override
	public void administrarSala(Sala sala) {
		// TODO Auto-generated method stub
	}*/
	
	public void pintarCarta(Carta carta) {
		
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
		}
	};
	
	public void cargarDatos() {
		SuscripcionSala.dondeEstoy(this); 
		
		if (DEBUG) System.out.println("Entrando en partida...");
		partida = SuscripcionSala.sala.getPartida();
		
		listaCartas.getChildren().clear();
		
		image = new Image(getClass().getResourceAsStream("images/cartas/set-1/amarillas/0-amarillo.png"));
		iview = new ImageView(image);
		listaCartas.addColumn(listaCartas.getColumnCount(), iview);
		
		iview = new ImageView(new Image(getClass().getResourceAsStream("images/cartas/set-1/amarillas/1-amarillo.png")));
		listaCartas.addColumn(listaCartas.getColumnCount(), iview);
	}
}