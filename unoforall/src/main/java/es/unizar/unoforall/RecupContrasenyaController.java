package es.unizar.unoforall;

import java.io.IOException;

import es.unizar.unoforall.api.RestAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RecupContrasenyaController {
	
	@FXML private TextField cajaCodigo;
	@FXML private PasswordField cajaContrasenya;
	@FXML private PasswordField cajaContrasenya2;
	public static String correo = null;
	
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
    		String codigo = cajaCodigo.getText();

	    	///RESTABLECER PASO 2
			RestAPI apirest = new RestAPI("/api/reestablecerContrasennaStepTwo");
			apirest.addParameter("correo", correo);
			apirest.setOnError(e -> {System.out.println(e);});
	    	
			apirest.openConnection();
	    	String error = apirest.receiveObject(String.class);
	    	
	    	if (error.equals("null")) {
//	    		String contrasenna = cajaContrasenya.getText();
//	    		String contrasenna2 = cajaContrasenya2.getText();

		    	String contrasenna = "asdfasdf";
		    	String contrasenna2 = "asdfasdf";
	    		
	    		if (contrasenna.equals(contrasenna2)) {
	    			///RESTABLECER PASO 3
					apirest = new RestAPI("/api/reestablecerContrasennaStepThree");
					apirest.addParameter("correo", correo);
					apirest.addParameter("contrasenna", contrasenna);
					apirest.setOnError(e -> {System.out.println(e);});
			    	
					apirest.openConnection();
			    	error = apirest.receiveObject(String.class);
			    	
			    	if (error.equals("null")) {
			    		App.setRoot("principal");
			    	} else {
			    		System.out.println(error);
			    	}
	    		} else {
		    		System.out.println("Las contraseñas no coinciden.");
	    		}
	    	} else {
	    		System.out.println(error);
	    	}
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
}