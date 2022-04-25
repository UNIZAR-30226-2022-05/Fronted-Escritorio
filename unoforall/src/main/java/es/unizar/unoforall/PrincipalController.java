package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;

public class PrincipalController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML private ChoiceBox<String> configChoiceBox;
	private String[] configChoices = {"Configuración de Cuenta", "Configuración de Aspecto", "Cerrar Sesión"};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configChoiceBox.getItems().addAll(configChoices);
		configChoiceBox.setOnAction(this::getConfigChoice);
	}
	
	@FXML
	public void getConfigChoice(ActionEvent event) {
		String choice = configChoiceBox.getValue();
		
		if (choice.equals(configChoices[0])) {
			goToConfCuenta(event);
		} else if (choice.equals(configChoices[1])) {
			goToConfAspecto(event);
		} else {
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