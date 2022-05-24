package es.unizar.unoforall;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.Jugador;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VistaSalaPausadaController extends SalaReceiver implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML private VBox fondo;
	@FXML private ImageView imgMenu;
	@FXML private Label labelError;
	@FXML private TextArea textAreaInfo;

	@FXML private Button botonAbandonar;
	@FXML private Button botonListo;
	@FXML private Button botonVolver;

	@FXML private HBox caja1;
	@FXML private ImageView pfpJug1;
	@FXML private ImageView rdyIconJug1;
	@FXML private Label nomJug1;

	@FXML private HBox caja2;
	@FXML private ImageView pfpJug2;
	@FXML private ImageView rdyIconJug2;
	@FXML private Label nomJug2;

	@FXML private HBox caja3;
	@FXML private ImageView pfpJug3;
	@FXML private ImageView rdyIconJug3;
	@FXML private Label nomJug3;

	@FXML private HBox caja4;
	@FXML private ImageView pfpJug4;
	@FXML private ImageView rdyIconJug4;
	@FXML private Label nomJug4;
	
	private HBox[] cajas;
	private ImageView[] avatares;
	private ImageView[] iconosListo;
	private Label[] nombres;
	
	private boolean estoyListo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cajas = new HBox[] {caja1, caja2, caja3, caja4};
		avatares = new ImageView[] {pfpJug1, pfpJug2, pfpJug3, pfpJug4};
		iconosListo = new ImageView[] {rdyIconJug1, rdyIconJug2, rdyIconJug3, rdyIconJug4};
		nombres = new Label[] {nomJug1, nomJug2, nomJug3, nomJug4};
		
		//ESTABLECER EN QUÉ PANTALLA ESTOY PARA SALAS Y PARTIDAS
		SuscripcionSala.dondeEstoy(this);
		//PONER EL FONDO CORRESPONDIENTE
		Background bg = ImageManager.getBackgroundImage(App.getPersonalizacion().get("tableroSelec"));
		fondo.setBackground(bg);
		
		//ASOCIAR EVENTOS DE AREA ENTERED A LAS IMAGENES
		imgMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(210);
				imgMenu.setFitHeight(160);
				imgMenu.setEffect(new Glow(0.3));
			}
		});
		imgMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(200);
				imgMenu.setFitHeight(150);
				imgMenu.setEffect(null);
			}
		});
		
		botonAbandonar.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				botonAbandonar.setEffect(new Glow(0.3));
			}
		});
		botonAbandonar.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				botonAbandonar.setEffect(null);
			}
		});
		
		botonListo.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				botonListo.setEffect(new Glow(0.3));
			}
		});
		botonListo.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				botonListo.setEffect(null);
			}
		});
		
		botonVolver.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				botonVolver.setEffect(new Glow(0.3));
			}
		});
		botonVolver.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				botonVolver.setEffect(null);
			}
		});
		
		administrarSala(SuscripcionSala.sala);
	}
	
	@Override
	public void administrarSala(Sala sala) {
		//Aquí ya existe Sala sala.
		
		//PONER INFORMACIÓN DE LA SALA
		textAreaInfo.setText("ID de sala: " + sala.getSalaID());
		
		if (DEBUG) System.out.println("sala actualizada");
		labelError.setText("");
		if (sala.isNoExiste()) {
			labelError.setText(StringUtils.parseString(sala.getError()));
			if (DEBUG) System.out.println("sala no existe");
			if (DEBUG) System.out.println(sala.getError());
			//Si error volver a la pantalla principal
			SuscripcionSala.salirDeSala();
			App.setRoot("principal");
		} else {
			if (DEBUG) System.out.println("");
			if (DEBUG) System.out.println("Estado de la sala: " + sala);
			if (sala.isEnPartida()) {
				//CARGAR LA VISTA DE LA PARTIDA
				if (DEBUG) System.out.println("En partida");
				App.setRoot("partida");
			} else {
				//RECARGAR LA VISTA DE SALA
				cargarParticipantes(sala);
			}
		}
	}
	
	private void cargarParticipantes(Sala sala) {
		
		int tamanyo = sala.getConfiguracion().getMaxParticipantes();
		HashMap<UsuarioVO, Boolean> participantes = sala.getParticipantes();
		List<Jugador> listaJugadores = sala.getPartida().getJugadores();
		
		for (int i=0; i<cajas.length; i++) {
			HBox caja = cajas[i];
			if (i<tamanyo) {
				cajas[i].setVisible(true); cajas[i].setDisable(false);
			} else {
				caja.setVisible(false); caja.setDisable(true);
			}
		}
		
		List<UsuarioVO> usuariosVO = new ArrayList<>(participantes.keySet());
		usuariosVO.sort(Comparator.comparing(UsuarioVO::getNombre));
		
		for(int i=0; i<listaJugadores.size(); i++){
            Jugador jugador = listaJugadores.get(i);
			
			String nombre;
			boolean listo;
			int avatar;
			if (jugador.isEsIA()) {
				nombre = "IA_"+i;
				listo = true;
				avatar = ImageManager.IA_IMAGE_ID;
			} else {
				UsuarioVO usuario = sala.getParticipante(jugador.getJugadorID());
				nombre = usuario.getNombre();
				listo = sala.getParticipantes().get(usuario);
				avatar = usuario.getAvatar();
			}
			
			nombres[i].setText(StringUtils.parseString(nombre));
			ImageManager.setImagenPerfil(avatares[i], avatar);
			ImageManager.setImagenListo(iconosListo[i], listo);
			if (DEBUG && listo) System.out.println("Usuario " + i + " (" + nombre + ") listo");
		}
	}
	
	@FXML
    private void goBack(ActionEvent event) {
		if (estoyListo) {
			leaveRoom(null);
		} else {
			//Llamada a la clase de Sala para desubscribirse
			SuscripcionSala.salirDeSala();
			//Volver a la pantalla principal
	    	App.setRoot("principal");
		}
	}

	@FXML
    private void goToMain(Event event) {
		if (estoyListo) {
			leaveRoom(null);
		} else {
			//Llamada a la clase de Sala para desubscribirse
			SuscripcionSala.salirDeSala();
			//Volver a la pantalla principal
	    	App.setRoot("principal");
		}
	}
	
	@FXML
    private void leaveRoom(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Abandonar Sala");
    	alert.setHeaderText("¿Seguro que quieres abandonar la sala?");
    	alert.setContentText("Si te sales serás expulsado de la partida\n y por tanto, no recibirás ningún punto");
    	
    	ButtonType respuesta = alert.showAndWait().get();
    	if (respuesta == ButtonType.OK) {
    		//Llamada a la clase de Sala para desubscribirse
    		SuscripcionSala.salirDeSalaDefinitivo();
    		//Volver a la pantalla principal
        	App.setRoot("principal");
        	
    		if (DEBUG) System.out.println("Has abandonado la sala.");
    	}
	}
	
	@FXML
    private void ready(ActionEvent event) {
		estoyListo = true;
		SuscripcionSala.listoSala();

		botonListo.setVisible(false);
		botonListo.setDisable(true);
		
		botonVolver.setVisible(false);
		botonVolver.setDisable(true);
	}
}
