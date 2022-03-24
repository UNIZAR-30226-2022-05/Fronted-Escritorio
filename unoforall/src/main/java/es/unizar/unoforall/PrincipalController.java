package es.unizar.unoforall;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class PrincipalController {

	@FXML
    private void goToLogin(ActionEvent event) {
    	try {
        	App.setRoot("login");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
	
	@FXML
    private void searchRooms(ActionEvent event) {
//    	try {
//        	App.setRoot("buscarSala");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.print("buscarSala");
    }
	
	@FXML
    private void makeRoom(ActionEvent event) {
//    	try {
//        	App.setRoot("crearSala");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.print("crearSala");
    }
	
	@FXML
    private void joinPausedRoom(ActionEvent event) {
//    	try {
//        	App.setRoot("vistaSala");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.print("vistaSala");
    }

	@FXML
    private void goToNotificaciones(ActionEvent event) {
//    	try {
//        	App.setRoot("notificaciones");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.print("notificaciones");
    }

	@FXML
    private void goToAmigos(ActionEvent event) {
//    	try {
//        	App.setRoot("amigos");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.print("amigos");
    }

	@FXML
    private void goToHistorial(ActionEvent event) {
//    	try {
//        	App.setRoot("historial");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.print("historial");
    }

	@FXML
    private void goToEstadisticas(ActionEvent event) {
//    	try {
//        	App.setRoot("estadisticas");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.print("estadisticas");
    }

	@FXML
    private void goToConfAspecto(ActionEvent event) {
//    	try {
//        	App.setRoot("confAspecto");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.print("confAspecto");
    }

	@FXML
    private void goToConfCuenta(ActionEvent event) {
//    	try {
//        	App.setRoot("confCuenta");
//    	} catch (IOException e) {
//			System.out.print(e);
//    	}
		System.out.print("confCuenta");
    }
}