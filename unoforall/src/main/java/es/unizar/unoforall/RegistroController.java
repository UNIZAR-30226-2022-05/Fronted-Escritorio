package es.unizar.unoforall;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.utils.HashUtils;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistroController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;
	
	@FXML private Label labelError;
	@FXML private TextField cajaCorreo;
	@FXML private PasswordField cajaContrasenya;
	@FXML private PasswordField cajaContrasenya2;
	@FXML private TextField cajaNomUsuario;
	
	@FXML
    private void goBack(ActionEvent event) {
        App.setRoot("login");
    }
    
	@FXML
    private void register(ActionEvent event) {
		labelError.setText("");
    	String correo = cajaCorreo.getText();
    	String contrasenna = cajaContrasenya.getText();
    	String contrasenna2 = cajaContrasenya2.getText();
    	String nombre = cajaNomUsuario.getText();
    	
    	if (contrasenna.equals(contrasenna2)) {
    		///REGISTRO
			RestAPI apirest = new RestAPI();
			apirest.addParameter("correo", correo);
			apirest.addParameter("contrasenna", HashUtils.cifrarContrasenna(contrasenna));
			apirest.addParameter("nombre", nombre);
			apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});

			apirest.openConnection("/api/registerStepOne");
	    	apirest.receiveObject(String.class, error -> {
	    		if (error == null) {	    	
		    		ConfirmCorreoController.correo = correo;
		        	App.setRoot("confirmacionCorreo");
		    	} else {
		    		labelError.setText(StringUtils.parseString(error));
		    		if (DEBUG) System.out.println(error);
		    	}
	    	});
    	} else {
    		labelError.setText("Las contraseñas no coinciden.");
    		if (DEBUG) System.out.println("Las contraseñas no coinciden.");
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
    private void onGoCajaNomUsuario(ActionEvent event) {
    	cajaNomUsuario.requestFocus();
    }
	
    @FXML
    private void onEnter(ActionEvent event) {
    	register(event);
    }
}