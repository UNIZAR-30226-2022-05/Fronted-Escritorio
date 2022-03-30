package es.unizar.unoforall;

import java.io.IOException;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.RespuestaLogin;
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
	    	String correo = cajaCorreo.getText();
	    	String contrasenna = cajaContrasenya.getText();
	    	String contrasenna2 = cajaContrasenya2.getText();
	    	String nombre = cajaNomUsuario.getText();
    		
//    		String correo = "prueba.info@gmail.com";
//	    	String contrasenna = "asdfasdf";
//	    	String contrasenna2 = "asdfasdf";
//    		String nombre = "Prueba";
	    	
	    	if (contrasenna.equals(contrasenna2)) {
	    		///REGISTRO
				RestAPI apirest = new RestAPI("/api/registerStepOne");
				apirest.addParameter("correo", correo);
				apirest.addParameter("contrasenna", contrasenna);
				apirest.addParameter("nombre", nombre);
				apirest.setOnError(e -> {System.out.println(e);});

				apirest.openConnection();
		    	String error = apirest.receiveObject(String.class);
		    	
		    	if (error == null) {	    	
		    		ConfirmCorreoController.correo = correo;
		        	App.setRoot("confirmacionCorreo");
		    	} else {
		    		System.out.println(error);
		    	}
	    	} else {	    		
				System.out.println("Las contraseñas no coinciden.");
	    	}
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
}