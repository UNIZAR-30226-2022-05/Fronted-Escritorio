package es.unizar.unoforall;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.utils.HashUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RecupContrasenyaController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML private Label labelError;
	@FXML private TextField cajaCodigo;
	@FXML private PasswordField cajaContrasenya;
	@FXML private PasswordField cajaContrasenya2;
	public static String correo = null;
	
	@FXML
    private void goBack(ActionEvent event) {
		App.setRoot("especificacionCorreo");
    }
    
	@FXML
    private void confirmCode(ActionEvent event) {
		labelError.setText("");
		String codigo = cajaCodigo.getText();

    	///RESTABLECER PASO 2
		RestAPI apirest = new RestAPI("/api/reestablecerContrasennaStepTwo");
		apirest.addParameter("correo", correo);
		apirest.addParameter("codigo", codigo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection();
    	String error = apirest.receiveObject(String.class);
    	
    	if (error == null) {
    		String contrasenna = cajaContrasenya.getText();
    		String contrasenna2 = cajaContrasenya2.getText();
    		
    		if (contrasenna.equals(contrasenna2)) {
    			///RESTABLECER PASO 3
				apirest = new RestAPI("/api/reestablecerContrasennaStepThree");
				apirest.addParameter("correo", correo);
				apirest.addParameter("contrasenna", HashUtils.cifrarContrasenna(contrasenna));
				apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		    	
				apirest.openConnection();
		    	error = apirest.receiveObject(String.class);
		    	
		    	if (error == null) {
		    		App.setRoot("login");
		    	} else {
		    		labelError.setText(error);
		    		if (DEBUG) System.out.println(error);
		    	}
    		} else {
    			labelError.setText("Las contraseñas no coinciden.");
    			if (DEBUG) System.out.println("Las contraseñas no coinciden.");
    		}
    	} else {
    		labelError.setText(error);
    		if (DEBUG) System.out.println(error);
    	}
    }
	
	@FXML
    private void onGoCajaContrasenya(ActionEvent event) {
    	cajaContrasenya.requestFocus();
    }
	
	@FXML
    private void onGoCajaContrasenya2(ActionEvent event) {
    	cajaContrasenya2.requestFocus();
    }
	
    @FXML
    private void onEnter(ActionEvent event) {
    	confirmCode(event);
    }
}