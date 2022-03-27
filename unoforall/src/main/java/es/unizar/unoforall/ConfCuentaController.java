package es.unizar.unoforall;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConfCuentaController {
	
	@FXML TextField cajaNombre;	
	@FXML TextField cajaCorreo;	
	@FXML PasswordField cajaContrasenya;
	@FXML PasswordField cajaContrasenya2;
	
	@FXML
    private void goBack(ActionEvent event) {
		try {
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
	}

	@FXML
    private void goToMain(Event event) {
		try {
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
	}

	@FXML
    private void changeNomUsuario(ActionEvent event) {
		String oldName = "oldName";
	    System.out.println("Nombre de Usuario cambiado de: " + oldName + " a: " + cajaNombre.getText());
	}

	@FXML
    private void changeCorreo(ActionEvent event) {
		String oldMail = "oldMail";
	    System.out.println("Correo cambiado de: " + oldMail + " a: " + cajaCorreo.getText());
	}

	@FXML
    private void changeContrasenya(ActionEvent event) {
		String oldPass = "oldPass";
		if (cajaContrasenya.getText().equals(cajaContrasenya2.getText())) {
	    	System.out.println("Contraseña cambiada de: " + oldPass + " a: " + cajaContrasenya.getText());
		}
		else {
			System.out.println("Las contrasenyas " + cajaContrasenya.getText() + " y "
								+ cajaContrasenya.getText() + " no coinciden.");
		}
	}
	
	@FXML
    private void deleteAccount(ActionEvent event) {
		try {
	    	App.setRoot("login");
			System.out.println("Cuenta eliminada");
		} catch (IOException e) {
			System.out.print(e);
		}
	}

}
