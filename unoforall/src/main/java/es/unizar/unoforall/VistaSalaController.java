package es.unizar.unoforall;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.ListaUsuarios;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class VistaSalaController extends SalaReceiver implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

	private static HashMap<Integer,Image> fondos = new HashMap<Integer, Image>();
	static {
		fondos.put(0, new Image(App.class.getResourceAsStream("images/fondos/azul.png")));
		fondos.put(1, new Image(App.class.getResourceAsStream("images/fondos/morado.png")));
		fondos.put(2, new Image(App.class.getResourceAsStream("images/fondos/gris.png")));
	}
	@FXML private VBox fondo;
	@FXML private ImageView imgMenu;
	@FXML private Label labelError;
	
	private static HashMap<Integer,Image> avatares = new HashMap<Integer, Image>();
	static {
		avatares.put(0, new Image(App.class.getResourceAsStream("images/avatares/0-cero.png")));
		avatares.put(1, new Image(App.class.getResourceAsStream("images/avatares/1-uno.png")));
		avatares.put(2, new Image(App.class.getResourceAsStream("images/avatares/2-dos.png")));
		avatares.put(3, new Image(App.class.getResourceAsStream("images/avatares/3-tres.png")));
		avatares.put(4, new Image(App.class.getResourceAsStream("images/avatares/4-cuatro.png")));
		avatares.put(5, new Image(App.class.getResourceAsStream("images/avatares/5-cinco.png")));
		avatares.put(6, new Image(App.class.getResourceAsStream("images/avatares/6-seis.png")));
		avatares.put(7, new Image(App.class.getResourceAsStream("images/avatares/7-IA.png")));
	}
	
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
	
	@FXML private ChoiceBox<String> amigosChoiceBox;
	private ArrayList<String> nombresAmigos = new ArrayList<String>();
	private ArrayList<UsuarioVO> listaAmigos = new ArrayList<UsuarioVO>();
	
	private Sala sala;
	
//	Por defecto deDondeVengo es la pantalla principal
//	para evitar posibles errores en ejecución
	public static String deDondeVengo = "principal";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//ESTABLECER EN QUÉ PANTALLA ESTOY PARA SALAS Y PARTIDAS
		SuscripcionSala.dondeEstoy(this);
		//PONER EL FONDO CORRESPONDIENTE
		fondo.setBackground(
			new Background(
				new BackgroundImage(
					fondos.get(App.getPersonalizacion().get("tableroSelec")),
					BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER,
					BackgroundSize.DEFAULT
				)
			)
		);
		
		//ASOCIAR EVENTOS DE AREA ENTERED A LAS IMAGENES
		imgMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(210);
				imgMenu.setFitHeight(160);
				imgMenu.setEffect(new Glow(0.3));
			}
		});;
		imgMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(200);
				imgMenu.setFitHeight(150);
				imgMenu.setEffect(null);
			}
		});;
		
		botonAbandonar.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				botonAbandonar.setEffect(new Glow(0.3));
			}
		});;
		botonAbandonar.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				botonAbandonar.setEffect(null);
			}
		});;
		
		botonListo.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				botonListo.setEffect(new Glow(0.3));
			}
		});;
		botonListo.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				botonListo.setEffect(null);
			}
		});;
		
		//Recuperar salaID solo la primera vez para establecer suscripción
		UUID salaID = App.getSalaID();
		SuscripcionSala.unirseASala(salaID);
		
		
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
				nombresAmigos.add(usuario.getNombre());
				
    			if (DEBUG) System.out.println("amigo encontrado:" + usuario.getCorreo());
			}
			
		} else {
			labelError.setText(StringUtils.parseString(error));
			if (DEBUG) System.out.println(StringUtils.parseString(error));
		}
		
		//ACTUALIZAR LISTA DE AMIGOS PARA INVITAR
		amigosChoiceBox.getItems().add("Invitar amigos");
		amigosChoiceBox.getSelectionModel().selectFirst();
		amigosChoiceBox.getItems().addAll(nombresAmigos);
		amigosChoiceBox.setOnAction(this::invitarAmigo);
	}
	
	@Override
	public void administrarSala(Sala sala) {
		//Aquí ya existe Sala sala.
		if (DEBUG) System.out.println("sala actualizada");
		labelError.setText("");
		if (sala.isNoExiste()) {
			labelError.setText(StringUtils.parseString(sala.getError()));
			if (DEBUG) System.out.println("sala no existe");
			if (DEBUG) System.out.println(sala.getError());
			//Si error volver a la pantalla anterior
			SuscripcionSala.salirDeSala();
			App.setRoot(deDondeVengo);
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
	
	@FXML
	public void invitarAmigo(ActionEvent event) {
		Integer idx = amigosChoiceBox.getSelectionModel().getSelectedIndex();
		if (idx != 0 && sala.getParticipantes().size() < sala.getConfiguracion().getMaxParticipantes()) {
			App.apiweb.sendObject("/app/notifSala/" + listaAmigos.get(idx-1).getId(), App.getSalaID());
		}
	}
	
	private void cargarParticipantes(int tamanyo, HashMap<UsuarioVO, Boolean> participantes) {
		if (tamanyo == 1) {
			//VISIBLES
			if (!caja1.isVisible())	{caja1.setDisable(false); caja1.setVisible(true);}
			//INVISIBLES
			if (caja2.isVisible())	{caja2.setDisable(true); caja2.setVisible(false);}
			if (caja3.isVisible())	{caja3.setDisable(true); caja3.setVisible(false);}
			if (caja4.isVisible())	{caja4.setDisable(true); caja4.setVisible(false);}
		} else if (tamanyo == 2) {
			//VISIBLES
			if (!caja1.isVisible())	{caja1.setDisable(false); caja1.setVisible(true);}
			if (!caja2.isVisible())	{caja2.setDisable(false); caja2.setVisible(true);}
			//INVISIBLES
			if (caja3.isVisible())	{caja3.setDisable(true); caja3.setVisible(false);}
			if (caja4.isVisible())	{caja4.setDisable(true); caja4.setVisible(false);}
		} else if (tamanyo == 3) {
			//VISIBLES
			if (!caja1.isVisible())	{caja1.setDisable(false); caja1.setVisible(true);}
			if (!caja2.isVisible())	{caja2.setDisable(false); caja2.setVisible(true);}
			if (!caja3.isVisible())	{caja3.setDisable(false); caja3.setVisible(true);}
			//INVISIBLES
			if (caja4.isVisible())	{caja4.setDisable(true); caja4.setVisible(false);}
		} else {
			//TODAS VISIBLES
			if (!caja1.isVisible())	{caja1.setDisable(false); caja1.setVisible(true);}
			if (!caja2.isVisible())	{caja2.setDisable(false); caja2.setVisible(true);}
			if (!caja3.isVisible())	{caja3.setDisable(false); caja3.setVisible(true);}
			if (!caja4.isVisible())	{caja4.setDisable(false); caja4.setVisible(true);}
		}
		List<UsuarioVO> usuariosVO = new ArrayList<>(participantes.keySet());
		usuariosVO.sort(Comparator.comparing(UsuarioVO::getNombre));
		
		//POR DEFECTO, PONER ESPERANDO JUGADOR E IMAGEN DE IA
		nomJug1.setText(StringUtils.parseString("Esperando Jugador 1"));
		pfpJug1.setImage(avatares.get(7));
		nomJug2.setText(StringUtils.parseString("Esperando Jugador 2"));
		pfpJug2.setImage(avatares.get(7));
		nomJug3.setText(StringUtils.parseString("Esperando Jugador 3"));
		pfpJug3.setImage(avatares.get(7));
		nomJug4.setText(StringUtils.parseString("Esperando Jugador 4"));
		pfpJug4.setImage(avatares.get(7));

		int i = 1;
		for (UsuarioVO u : usuariosVO) {
			String nombre = u.getNombre();
			boolean listo = participantes.get(u);
			if (i == 1) {	//EN LA CAJA 1
				//PONER NOMBRE DE USUARIO 1
				nomJug1.setText(StringUtils.parseString(nombre));
				//PONER ICONO DE USUARIO 1
				pfpJug1.setImage(avatares.get(u.getAvatar()));
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
				pfpJug2.setImage(avatares.get(u.getAvatar()));
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
				pfpJug3.setImage(avatares.get(u.getAvatar()));
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
				pfpJug4.setImage(avatares.get(u.getAvatar()));
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
		//Llamada a la clase de Sala para desubscribirse
		SuscripcionSala.salirDeSala();
		//Volver a la pantalla anterior
    	App.setRoot(deDondeVengo);
	}

	@FXML
    private void goToMain(Event event) {
		//Llamada a la clase de Sala para desubscribirse
		SuscripcionSala.salirDeSala();
		//Volver a la pantalla principal
    	App.setRoot("principal");
	}
	
	@FXML
    private void leaveRoom(ActionEvent event) {
		//Llamada a la clase de Sala para desubscribirse
		SuscripcionSala.salirDeSala();
		//Volver a la pantalla anterior
    	App.setRoot(deDondeVengo);
	}
	
	@FXML
    private void ready(ActionEvent event) {
		//Llamada a la clase de Sala para desubscribirse
		SuscripcionSala.listoSala();

		botonListo.setVisible(false);
		botonListo.setDisable(true);
	}
}
