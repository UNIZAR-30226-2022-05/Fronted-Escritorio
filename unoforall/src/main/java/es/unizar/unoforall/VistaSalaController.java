package es.unizar.unoforall;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.ListaUsuarios;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VistaSalaController extends SalaReceiver implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

	@FXML private VBox fondo;
	@FXML private ImageView imgMenu;
	@FXML private Label labelError;
	@FXML private TextArea textAreaInfo;
	
	private static Image ready = new Image(VistaSalaController.class.getResourceAsStream("images/ready.png"));
	private static Image notready = new Image(VistaSalaController.class.getResourceAsStream("images/notready.png"));

	@FXML private Button botonAbandonar;
	@FXML private Button botonListo;

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
	
	@FXML private MenuButton amigosMenuButton;
	private ArrayList<String> nombresAmigos = new ArrayList<String>();
	private ArrayList<UsuarioVO> listaAmigos = new ArrayList<UsuarioVO>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//ESTABLECER EN QUÉ PANTALLA ESTOY PARA SALAS Y PARTIDAS
		SuscripcionSala.dondeEstoy(this);
		//PONER EL FONDO CORRESPONDIENTE
		fondo.setBackground(ImageManager.getBackgroundImage(App.getPersonalizacion().get("tableroSelec")));
		
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
		
		//SE ASUME QUE YA SE HA ENTRADO EN LA SALA
		
		//BUSCAR AMIGOS
		String sesionID = App.getSessionID();
		
		RestAPI apirest = new RestAPI("/api/sacarAmigos");
		apirest.addParameter("sesionID", sesionID);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection();
		ListaUsuarios usuarios = apirest.receiveObject(ListaUsuarios.class);
		
		//COMPROBAR SI HA HABIDO ALGÚN ERROR
		String error = usuarios.getError();
		if (error.equals("null")) {
			
			for (UsuarioVO usuario : usuarios.getUsuarios()) {
				listaAmigos.add(usuario);
				String nombre = StringUtils.parseString(usuario.getNombre());
				String correo = StringUtils.parseString(usuario.getCorreo());
				nombresAmigos.add(nombre + " (" + correo + ") ");
				
    			if (DEBUG) System.out.println("amigo encontrado:" + usuario.getCorreo());
			}
			
		} else {
			labelError.setText(StringUtils.parseString(error));
			if (DEBUG) System.out.println(StringUtils.parseString(error));
		}
		
		//ACTUALIZAR LISTA DE AMIGOS PARA INVITAR
		for (int i=0; i < nombresAmigos.size(); i++) {
			int amigo = i;
			MenuItem item = new MenuItem(nombresAmigos.get(i));
			item.setOnAction(event -> {
				if (SuscripcionSala.sala.getParticipantes().size() < SuscripcionSala.sala.getConfiguracion().getMaxParticipantes()) {
					App.apiweb.sendObject("/app/notifSala/" + listaAmigos.get(amigo).getId(), SuscripcionSala.sala.getSalaID());
				}
			});
			amigosMenuButton.getItems().add(item);
		}

		Sala sala = SuscripcionSala.sala;
		if(sala != null){
			administrarSala(sala);
		}
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
				int tamanyo = sala.getConfiguracion().getMaxParticipantes();
				HashMap<UsuarioVO, Boolean> participantes = sala.getParticipantes();
				cargarParticipantes(tamanyo, participantes);
			}
		}
	}
	
	private void cargarParticipantes(int tamanyo, HashMap<UsuarioVO, Boolean> participantes) {
		if (tamanyo == 1) {
			//VISIBLES
			if (!caja1.isVisible())	{
				caja1.setDisable(false); 
				caja1.setVisible(true);
			}
			//INVISIBLES
			if (caja2.isVisible()) {
				caja2.setDisable(true);
				caja2.setVisible(false);
			}
			
			if (caja3.isVisible()) {
				caja3.setDisable(true);
				caja3.setVisible(false);
			}
			
			if (caja4.isVisible()) {
				caja4.setDisable(true);
				caja4.setVisible(false);
			}
			
		} else if (tamanyo == 2) {
			//VISIBLES
			if (!caja1.isVisible())	{
				caja1.setDisable(false);
				caja1.setVisible(true);
			}
			
			if (!caja2.isVisible())	{
				caja2.setDisable(false);
				caja2.setVisible(true);
			}
			//INVISIBLES
			if (caja3.isVisible()) {
				caja3.setDisable(true);
				caja3.setVisible(false);
			}
			if (caja4.isVisible()) {
				caja4.setDisable(true);
				caja4.setVisible(false);
			}
		} else if (tamanyo == 3) {
			//VISIBLES
			if (!caja1.isVisible())	{
				caja1.setDisable(false); 
				caja1.setVisible(true);
			}
			
			if (!caja2.isVisible())	{
				caja2.setDisable(false);
				caja2.setVisible(true);
			}
			
			if (!caja3.isVisible())	{
				caja3.setDisable(false);
				caja3.setVisible(true);
			}
			//INVISIBLES
			if (caja4.isVisible()) {
				caja4.setDisable(true);
				caja4.setVisible(false);
			}
		} else {
			//TODAS VISIBLES
			if (!caja1.isVisible())	{
				caja1.setDisable(false);
				caja1.setVisible(true);
			}
			if (!caja2.isVisible())	{
				caja2.setDisable(false);
				caja2.setVisible(true);
			}
			if (!caja3.isVisible())	{
				caja3.setDisable(false);
				caja3.setVisible(true);
			}
			if (!caja4.isVisible())	{
				caja4.setDisable(false);
				caja4.setVisible(true);
			}
		}
		List<UsuarioVO> usuariosVO = new ArrayList<>(participantes.keySet());
		usuariosVO.sort(Comparator.comparing(UsuarioVO::getNombre));
		
		//POR DEFECTO, PONER ESPERANDO JUGADOR E IMAGEN DE IA
		nomJug1.setText(StringUtils.parseString("Esperando Jugador 1"));
		ImageManager.setImagenPerfil(pfpJug1, ImageManager.ICONO_PERFIL_ID);
		nomJug2.setText(StringUtils.parseString("Esperando Jugador 2"));
		ImageManager.setImagenPerfil(pfpJug2, ImageManager.ICONO_PERFIL_ID);
		nomJug3.setText(StringUtils.parseString("Esperando Jugador 3"));
		ImageManager.setImagenPerfil(pfpJug3, ImageManager.ICONO_PERFIL_ID);
		nomJug4.setText(StringUtils.parseString("Esperando Jugador 4"));
		ImageManager.setImagenPerfil(pfpJug4, ImageManager.ICONO_PERFIL_ID);

		int i = 1;
		for (UsuarioVO jugador : usuariosVO) {
			String nombre = jugador.getNombre();
			boolean listo = participantes.get(jugador);
			if (i == 1) {	//EN LA CAJA 1
				//PONER NOMBRE DE USUARIO 1
				nomJug1.setText(StringUtils.parseString(nombre));
				//PONER ICONO DE USUARIO 1
				ImageManager.setImagenPerfil(pfpJug1, jugador.getAvatar());
				//PONER A LISTO USUARIO 1
				if (listo) {
					rdyIconJug1.setImage(ready);
					if (DEBUG) System.out.println("Usuario 1 (" + nombre + ") listo");
				} else {
					rdyIconJug1.setImage(notready);
				}
			} else if (i == 2) {	//EN LA CAJA 2
				//PONER NOMBRE DE USUARIO 2
				nomJug2.setText(StringUtils.parseString(nombre));
				//PONER ICONO DE USUARIO 2
				ImageManager.setImagenPerfil(pfpJug2, jugador.getAvatar());
				//PONER A LISTO USUARIO 2
				if (listo) {
					rdyIconJug2.setImage(ready);
					if (DEBUG) System.out.println("Usuario 2 (" + nombre + ") listo");
				} else {
					rdyIconJug2.setImage(notready);
				}
			} else if (i == 3) {	//EN LA CAJA 3
				//PONER NOMBRE DE USUARIO 3
				nomJug3.setText(StringUtils.parseString(nombre));
				//PONER ICONO DE USUARIO 3
				ImageManager.setImagenPerfil(pfpJug3, jugador.getAvatar());
				//PONER A LISTO USUARIO 3
				if (listo) {
					rdyIconJug3.setImage(ready);
					if (DEBUG) System.out.println("Usuario 3 (" + nombre + ") listo");
				} else {
					rdyIconJug3.setImage(notready);
				}
			} else {	//EN LA CAJA 4
				//PONER NOMBRE DE USUARIO 4
				nomJug4.setText(StringUtils.parseString(nombre));
				//PONER ICONO DE USUARIO 4
				ImageManager.setImagenPerfil(pfpJug4, jugador.getAvatar());
				//PONER A LISTO USUARIO 4
				if (listo) {
					rdyIconJug4.setImage(ready);
					if (DEBUG) System.out.println("Usuario 4 (" + nombre + ") listo");
				} else {
					rdyIconJug4.setImage(notready);
				}
			}
			i++;
		}
	}
	
	@FXML
    private void goBack(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Abandonar Sala");
    	alert.setHeaderText("¿Seguro que quieres abandonar la sala?");
    	alert.setContentText("Si te sales no podrás disfrutar de la partida");
    	
    	ButtonType respuesta = alert.showAndWait().get();
    	if (respuesta == ButtonType.OK) {
    		//Llamada a la clase de Sala para desubscribirse
    		SuscripcionSala.salirDeSala();
    		//Volver a la pantalla principal
        	App.setRoot("principal");
        	
    		if (DEBUG) System.out.println("Has abandonado la sala.");
    	}
	}

	@FXML
    private void goToMain(Event event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Abandonar Sala");
		alert.setHeaderText("¿Seguro que quieres abandonar la sala?");
		alert.setContentText("Si te sales no podrás disfrutar de la partida");
		
		ButtonType respuesta = alert.showAndWait().get();
		if (respuesta == ButtonType.OK) {
			//Llamada a la clase de Sala para desubscribirse
			SuscripcionSala.salirDeSala();
			//Volver a la pantalla principal
	    	App.setRoot("principal");
	    	
			if (DEBUG) System.out.println("Has abandonado la sala.");
		}
	}
	
	@FXML
    private void leaveRoom(ActionEvent event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Abandonar Sala");
    	alert.setHeaderText("¿Seguro que quieres abandonar la sala?");
    	alert.setContentText("Si te sales no podrás disfrutar de la partida");
    	
    	ButtonType respuesta = alert.showAndWait().get();
    	if (respuesta == ButtonType.OK) {
    		//Llamada a la clase de Sala para desubscribirse
    		SuscripcionSala.salirDeSala();
    		//Volver a la pantalla principal
        	App.setRoot("principal");
        	
    		if (DEBUG) System.out.println("Has abandonado la sala.");
    	}
	}
	
	@FXML
    private void ready(ActionEvent event) {
		SuscripcionSala.listoSala();

		botonListo.setVisible(false);
		botonListo.setDisable(true);
	}
}
