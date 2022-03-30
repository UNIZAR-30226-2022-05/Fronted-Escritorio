package es.unizar.unoforall;

import java.io.IOException;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.RespuestaLogin;
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
//	    	String correo = cajaCorreo.getText();
    		
    		String correo = "prueba.info@gmail.com";

	    	///RESTABLECER PASO 1
			RestAPI apirest = new RestAPI("/api/reestablecerContrasennaStepOne");
			apirest.addParameter("correo", correo);
			apirest.setOnError(e -> {System.out.println(e);});
	    	
			apirest.openConnection();
	    	String error = apirest.receiveObject(String.class);
	    	
	    	if (error.equals("null")) {
	    		RecupContrasenyaController.correo = correo;
	        	App.setRoot("recuperacionContrasenya");
	    	} else {
	    		System.out.println(error);
	    	}
	    	
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
}