package es.unizar.unoforall;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EspecifCorreoController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;

	@FXML private Label labelError;
	@FXML private TextField cajaCorreo;
	
	@FXML
    private void goBack(ActionEvent event) {
        App.setRoot("login");
    }
    
	@FXML
    private void sendCode(ActionEvent event) {
		labelError.setText("");
    	String correo = cajaCorreo.getText();

    	///RESTABLECER PASO 1
		RestAPI apirest = new RestAPI();
		apirest.addParameter("correo", correo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection("/api/reestablecerContrasennaStepOne");
    	apirest.receiveObject(String.class, error -> {
    		if (error == null) {
	    		RecupContrasenyaController.correo = correo;
	        	App.setRoot("recuperacionContrasenya");
	    	} else {
	    		labelError.setText(StringUtils.parseString(error));
	    		if (DEBUG) System.out.println(error);
	    	}
    	});
    }
	@FXML
    private void onEnter(ActionEvent event) {
    	sendCode(event);
    }
}