package es.unizar.unoforall;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RecupContrasenyaController {
	
	@FXML private TextField cajaCodigo;
	@FXML private PasswordField cajaContrasenya;
	@FXML private PasswordField cajaContrasenya2;
	
	@FXML
    private void goBack(ActionEvent event) {
    	try {
        	App.setRoot("especificacionCorreo");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
    
	@FXML
    private void confirmCode(ActionEvent event) {
    	try {
        	App.setRoot("principal");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
}