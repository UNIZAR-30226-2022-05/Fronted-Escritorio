package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.Pantalla;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class PrincipalController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;
	
	@FXML private Button btnBuscarSala;
    @FXML private Button btnCrearSala;
    @FXML private ImageView imgAmigos;
    @FXML private ImageView imgHistorial;
    @FXML private ImageView imgNotificaciones;

	@FXML private MenuButton btnMenuConfiguracion;
	
	@FXML private VBox fondo;
	
	private boolean estaPausada = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//PONER EL FONDO CORRESPONDIENTE
		fondo.setBackground(ImageManager.getBackgroundImage(App.getPersonalizacion().get("tableroSelec")));
		
		//CONFIGURAR HOVER DE OBJETOS
		btnCrearSala.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnCrearSala.setEffect(new Glow(0.5));
			}
		});
		btnCrearSala.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnCrearSala.setEffect(null);
			}
		});
		
		btnBuscarSala.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnBuscarSala.setEffect(new Glow(0.5));
			}
		});
		btnBuscarSala.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				btnBuscarSala.setEffect(null);
			}
		});
		
		imgHistorial.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgHistorial.setFitWidth(93);
				imgHistorial.setFitHeight(104);
				imgHistorial.setEffect(new Glow(0.3));
			}
		});
		imgHistorial.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgHistorial.setFitWidth(83);
				imgHistorial.setFitHeight(94);
				imgHistorial.setEffect(null);
			}
		});
		
		imgAmigos.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgAmigos.setFitWidth(130);
				imgAmigos.setFitHeight(130);
				imgAmigos.setEffect(new Glow(0.3));
			}
		});
		imgAmigos.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgAmigos.setFitWidth(120);
				imgAmigos.setFitHeight(120);
				imgAmigos.setEffect(null);
			}
		});
		
		imgNotificaciones.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgNotificaciones.setFitWidth(130);
				imgNotificaciones.setFitHeight(130);
				imgNotificaciones.setEffect(new Glow(0.3));
			}
		});
		imgNotificaciones.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgNotificaciones.setFitWidth(120);
				imgNotificaciones.setFitHeight(120);
				imgNotificaciones.setEffect(null);
			}
		});
		
		//COMPROBAR SI HABÍA UNA PARTIDA EN CURSO
		RestAPI apirest = App.apiweb.getRestAPI();
		apirest.setOnError(e -> {
			if (DEBUG) System.out.println(e);
		});
		apirest.openConnection("/api/comprobarPartidaPausada");
		apirest.receiveObject(Sala.class, salaPausada -> {
			estaPausada = !salaPausada.isNoExiste();
			
			//SI NO ESTÁ PAUSADA
			if(!estaPausada) {
				//PONER TEXTO Y COMPORTAMIENTO BOTONES POR DEFECTO
			    btnCrearSala.setText("Crear Sala");
				btnBuscarSala.setText("Buscar Sala");
				
				btnCrearSala.setStyle("-fx-background-color: #2ec322; ");
				btnBuscarSala.setStyle("-fx-background-color: #2ec322; ");
				
				btnCrearSala.setOnAction(event -> makeRoom(event));
				btnBuscarSala.setOnAction(event -> searchRooms(event));
			} else {
				App.setSalaID(salaPausada.getSalaID());
				SuscripcionSala.sala = salaPausada;
				
				ColorAdjust colorAdjust = new ColorAdjust();
				colorAdjust.setBrightness(-0.5);
				colorAdjust.setSaturation(-0.7);
				
				imgAmigos.setEffect(colorAdjust);
				imgAmigos.setDisable(true);
	
				imgHistorial.setEffect(colorAdjust);
				imgHistorial.setDisable(true);
	
				imgNotificaciones.setEffect(colorAdjust);
				imgNotificaciones.setDisable(true);
				
				//CAMBIAR TEXTO Y COMPORTAMIENTO BOTONES
			    btnCrearSala.setText("Reanudar Partida");
				btnBuscarSala.setText("Abandonar Sala");
				
				btnCrearSala.setStyle("-fx-background-color: #ff9800; ");
				btnBuscarSala.setStyle("-fx-background-color: #b61d1d; ");
				
				btnCrearSala.setOnAction(event -> joinPausedRoom(event));
				btnBuscarSala.setOnAction(event -> leavePausedRoom(event));
			}
		});
	}
	
	@FXML
	private void configurarCuenta(ActionEvent event) {
		if (estaPausada) {
			Alert alert = new Alert(AlertType.WARNING);
	    	alert.setTitle("Aviso");
	    	alert.setHeaderText("Esta opción está deshabilitada");
	    	alert.setContentText("Para poder configurar la cuenta,\nabandona primero la partida");
	    	alert.showAndWait().get();
		} else {
			goToConfCuenta(event);
		}
	}

	@FXML
	private void configurarAspecto(ActionEvent event) {
		if (estaPausada) {
			Alert alert = new Alert(AlertType.WARNING);
	    	alert.setTitle("Aviso");
	    	alert.setHeaderText("Esta opción está deshabilitada");
	    	alert.setContentText("Para poder configurar el aspecto,\nabandona primero la partida");
	    	alert.showAndWait().get();
		} else {
			goToConfAspecto(event);
		}
	}

	@FXML
    private void goToLogin(Event event) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Cierre de Sesión");
    	alert.setHeaderText("¡Estás a punto de cerrar sesión!");
    	alert.setContentText("¿Estás seguro de querer salir de la aplicación?: ");
    	
    	if (alert.showAndWait().get() == ButtonType.OK) {
    		if (DEBUG) System.out.println("Has cerrado sesión.");
            App.setRoot(Pantalla.LOGIN);
            App.cerrarConexion();
    	}
    }
	
	@FXML
    private void searchRooms(ActionEvent event) {
        App.setRoot(Pantalla.BUSQUEDA_SALA);
    }
	
	@FXML
    private void makeRoom(ActionEvent event) {
        App.setRoot(Pantalla.CREAR_SALA);
    }

	@FXML
    private void joinPausedRoom(ActionEvent event) {
    	if (DEBUG) System.out.println("INTENTANDO UNIRSE A SALA PAUSADA");
		
		SuscripcionSala.unirseASala(App.getSalaID(), exito -> {
			if (!exito) {
				App.setRoot(Pantalla.PRINCIPAL);
			} else {
				App.setRoot(Pantalla.SALA_PAUSADA);
			}
		});
    }

	@FXML
    private void leavePausedRoom(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Abandonar Sala");
    	alert.setHeaderText("¿Seguro que quieres abandonar la sala?");
    	alert.setContentText("Si te sales serás expulsado de la partida\n y por tanto, no recibirás ningún punto");
    	
    	ButtonType respuesta = alert.showAndWait().get();
    	if (respuesta == ButtonType.OK) {
        	SuscripcionSala.salirDeSalaDefinitivo();
        	App.setRoot(Pantalla.PRINCIPAL);
        	
    		if (DEBUG) System.out.println("Has abandonado la sala.");
    	}
	}

	@FXML
    private void goToNotificaciones(MouseEvent event) {
        App.setRoot(Pantalla.NOTIFICACIONES);
    }

	@FXML
    private void goToAmigos(MouseEvent event) {
        App.setRoot(Pantalla.AMIGOS);
    }

	@FXML
    private void goToHistorial(MouseEvent event) {
		//BUSCAR DATOS DE MI USUARIO
		RestAPI apirest = App.apiweb.getRestAPI();
		apirest.setOnError(e -> {
			if (DEBUG) System.out.println(e);
		});
		apirest.openConnection("/api/sacarUsuarioVO");
		apirest.receiveObject(UsuarioVO.class, usuario -> {
			//PASAR EL USUARIO A LA VENTANA DE HISTORIAL
			HistorialController.usuario = usuario;
			App.setRoot(Pantalla.HISTORIAL);
		});
    }

	@FXML
    private void goToConfAspecto(ActionEvent event) {
        btnMenuConfiguracion.hide();
		App.setRoot(Pantalla.CONFIGURAR_ASPECTO);
    }

	@FXML
    private void goToConfCuenta(ActionEvent event) {
      	btnMenuConfiguracion.hide();
	    App.setRoot(Pantalla.CONFIGURAR_CUENTA);
    }
}