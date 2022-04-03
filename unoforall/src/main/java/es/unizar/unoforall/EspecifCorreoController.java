package es.unizar.unoforall;

import es.unizar.unoforall.api.RestAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EspecifCorreoController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML private TextField cajaCorreo;
	
	@FXML
    private void goBack(ActionEvent event) {
        App.setRoot("login");
    }
    
	@FXML
    private void sendCode(ActionEvent event) {
    	String correo = cajaCorreo.getText();

    	///RESTABLECER PASO 1
		RestAPI apirest = new RestAPI("/api/reestablecerContrasennaStepOne");
		apirest.addParameter("correo", correo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection();
    	String error = apirest.receiveObject(String.class);
    	
    	if (error == null) {
    		RecupContrasenyaController.correo = correo;
        	App.setRoot("recuperacionContrasenya");
    	} else {
    		if (DEBUG) System.out.println(error);
    	}
    }
	@FXML
    private void onEnter(ActionEvent event) {
    	sendCode(event);
    }
}