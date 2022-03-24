package es.unizar.unoforall;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EspecifCorreoController {
	
	@FXML private TextField cajaCorreo;
	
	@FXML
    private void goBack(ActionEvent event) {
    	try {
        	App.setRoot("login");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
    
	@FXML
    private void sendCode(ActionEvent event) {
    	try {
        	App.setRoot("recuperacionContrasenya");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
}