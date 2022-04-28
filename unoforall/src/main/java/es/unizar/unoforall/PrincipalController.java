package es.unizar.unoforall;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;

public class PrincipalController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML private Button btnBuscarSala;
    @FXML private Button btnCrearSala;
    @FXML private ImageView imgAmigos;
    @FXML private ImageView imgHistorial;
    @FXML private ImageView imgNotificaciones;
	
	private static HashMap<Integer,Image> fondos = new HashMap<Integer, Image>();
	static {
		fondos.put(0, new Image(App.class.getResourceAsStream("images/fondos/azul.png")));
		fondos.put(1, new Image(App.class.getResourceAsStream("images/fondos/morado.png")));
		fondos.put(2, new Image(App.class.getResourceAsStream("images/fondos/gris.png")));
	}
	
	@FXML private VBox fondo;
	
	@FXML private ChoiceBox<String> configChoiceBox;
	private String[] configChoices = {"--Seleccione una--", "Configuración de Cuenta", "Configuración de Aspecto", "Cerrar Sesión"};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configChoiceBox.getItems().addAll(configChoices);
		configChoiceBox.getSelectionModel().selectFirst();
		configChoiceBox.setOnAction(this::getConfigChoice);
		
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
		
		//CONFIGURAR HOVER DE OBJETOS
		btnCrearSala.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnBuscarSala.setEffect(new Glow(0.001));
			}
		});;
		btnCrearSala.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnCrearSala.setEffect(null);
			}
		});;
		
		btnBuscarSala.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnBuscarSala.setEffect(new Glow(0.001));
			}
		});;
		btnBuscarSala.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnBuscarSala.setEffect(null);
			}
		});;
		
		imgHistorial.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgHistorial.setFitWidth(93);
				imgHistorial.setFitHeight(104);
				imgHistorial.setEffect(new Glow(0.3));
			}
		});;
		imgHistorial.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgHistorial.setFitWidth(83);
				imgHistorial.setFitHeight(94);
				imgHistorial.setEffect(null);
			}
		});;
		
		imgAmigos.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgAmigos.setFitWidth(130);
				imgAmigos.setFitHeight(130);
				imgAmigos.setEffect(new Glow(0.3));
			}
		});;
		imgAmigos.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgAmigos.setFitWidth(120);
				imgAmigos.setFitHeight(120);
				imgAmigos.setEffect(null);
			}
		});;
		
		imgNotificaciones.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgNotificaciones.setFitWidth(130);
				imgNotificaciones.setFitHeight(130);
				imgNotificaciones.setEffect(new Glow(0.3));
			}
		});;
		imgNotificaciones.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgNotificaciones.setFitWidth(120);
				imgNotificaciones.setFitHeight(120);
				imgNotificaciones.setEffect(null);
			}
		});;
	}
	
	@FXML
	public void getConfigChoice(ActionEvent event) {
		String choice = configChoiceBox.getValue();
		
		if (choice.equals(configChoices[1])) {
			goToConfCuenta(event);
		} else if (choice.equals(configChoices[2])) {
			goToConfAspecto(event);
		} else if (choice.equals(configChoices[3])) {
			goToLogin(event);
		}
	}

	@FXML
    private void goToLogin(Event event) {
        App.setRoot("login");
        App.cerrarConexion();
    }
	
	@FXML
    private void searchRooms(ActionEvent event) {
        App.setRoot("buscarSala");
    }
	
	@FXML
    private void makeRoom(ActionEvent event) {
        App.setRoot("crearSala");
    }
	
	@FXML
    private void joinPausedRoom(ActionEvent event) {
		System.out.println("vistaSala");
    }

	@FXML
    private void goToNotificaciones(MouseEvent event) {
        App.setRoot("notificaciones");
    }

	@FXML
    private void goToAmigos(MouseEvent event) {
        App.setRoot("amigos");
    }

	@FXML
    private void goToHistorial(MouseEvent event) {
		//BUSCAR DATOS DE MI USUARIO
		RestAPI apirest = new RestAPI("/api/sacarUsuarioVO");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.setOnError(e -> {
			if (DEBUG) System.out.println(e);
		});
		apirest.openConnection();
		UsuarioVO usuario = apirest.receiveObject(UsuarioVO.class);
		
		//PASAR EL USUARIO A LA VENTANA DE HISTORIAL
		HistorialController.usuario = usuario;
		App.setRoot("historial");
    }

	@FXML
    private void goToConfAspecto(ActionEvent event) {
        App.setRoot("confAspecto");
    }

	@FXML
    private void goToConfCuenta(ActionEvent event) {
        App.setRoot("confCuenta");
    }
}