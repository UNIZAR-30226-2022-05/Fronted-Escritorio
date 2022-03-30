package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;

public class PrincipalController implements Initializable {
	
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
    	try {
        	App.setRoot("login");
    	} catch (IOException e) {
			System.out.println(e);
    	}
    }
	
	@FXML
    private void searchRooms(ActionEvent event) {
    	try {
        	App.setRoot("buscarSala");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
	
	@FXML
    private void makeRoom(ActionEvent event) {
    	try {
        	App.setRoot("crearSala");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
	
	@FXML
    private void joinPausedRoom(ActionEvent event) {
//    	try {
//        	App.setRoot("vistaSala");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.println("vistaSala");
    }

	@FXML
    private void goToNotificaciones(MouseEvent event) {
//    	try {
//        	App.setRoot("notificaciones");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.println("notificaciones");
    }

	@FXML
    private void goToAmigos(MouseEvent event) {
//    	try {
//        	App.setRoot("amigos");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.println("amigos");
    }

	@FXML
    private void goToHistorial(MouseEvent event) {
//    	try {
//        	App.setRoot("historial");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.println("historial");
    }

	@FXML
    private void goToConfAspecto(ActionEvent event) {
    	try {
        	App.setRoot("confAspecto");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }

	@FXML
    private void goToConfCuenta(ActionEvent event) {
    	try {
        	App.setRoot("confCuenta");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
}