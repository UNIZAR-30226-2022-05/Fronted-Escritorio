package es.unizar.unoforall;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistroController {
	
	@FXML private TextField cajaCorreo;
	@FXML private PasswordField cajaContrasenya;
	@FXML private PasswordField cajaContrasenya2;
	@FXML private TextField cajaNomUsuario;
	
	@FXML
    private void goBack(ActionEvent event) {
    	try {
        	App.setRoot("login");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
    
	@FXML
    private void register(ActionEvent event) {
    	try {
        	App.setRoot("confirmacionCorreo");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
}