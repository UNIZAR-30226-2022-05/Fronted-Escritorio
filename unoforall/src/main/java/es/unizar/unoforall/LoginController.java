package es.unizar.unoforall;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;

public class LoginController {
	
	@FXML private Label labelInformacion;

	@FXML private TextField cajaCorreo;
	@FXML private PasswordField cajaContrasenya;

//    @FXML
//    private void switchToSecondary() throws IOException {
//        App.setRoot("secondary");
//    }
    
    @FXML
    private void login(ActionEvent event) {
    	
    	//Mandar datos a la BD
    	
    	//Verificar resultado de la llamada
    	
    	//Chequeo provisional

		try {
	    	if (cajaCorreo.getText().equals("a@unizar") && cajaContrasenya.getText().equals("1234")) {
	    		labelInformacion.setText("Correo y contraseña correctos. Aplicación incorrecta");
	    		App.setRoot("principal");
	    	} else {
	    		labelInformacion.setText("Correo y contraseña incorrectos. Pruebe otra cosa");
//	    		labelInformacion.setTextAlignment(TextAlignment.CENTER);
	    		cajaCorreo.setText("");
	    		cajaContrasenya.setText("");
	    	}
		}
		catch (IOException e) {
			System.out.print(e);
		}
	}
    
    @FXML
    private void register(ActionEvent event) {
    	try {
        	App.setRoot("registro");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
    
    @FXML
    private void forgotPass(ActionEvent event) {
    	try {
        	App.setRoot("especificacionCorreo");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    	
    }
}
