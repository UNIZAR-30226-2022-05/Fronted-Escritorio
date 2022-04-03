package es.unizar.unoforall;

import java.io.IOException;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.RespuestaLogin;
import es.unizar.unoforall.utils.HashUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	
	@FXML private Label labelInformacion;

	@FXML private TextField cajaCorreo;
	@FXML private PasswordField cajaContrasenya;
    
	@FXML
	private void cambiarIP (ActionEvent event) {
		try {
			App.setRoot("cambiarIP");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    @FXML
    private void login(ActionEvent event) {
    	String correo = cajaCorreo.getText();
    	String contrasenna = cajaContrasenya.getText();
		
//    	String correo = "prueba.info@gmail.com";
//	    String contrasenna = "asdfasdf";

    	///LOGIN
		RestAPI apirest = new RestAPI("/api/login");
		apirest.addParameter("correo", correo);
		apirest.addParameter("contrasenna", HashUtils.cifrarContrasenna(contrasenna));
		apirest.setOnError(e -> {System.out.println(e);});
    	
		apirest.openConnection();
    	RespuestaLogin resp = apirest.receiveObject(RespuestaLogin.class);
    	
    	if (resp.isExito()) {
    		//GUARDAR RESPUESTALOGIN EN CASO DE NECESITARLO
    		App.setRespLogin(resp);
    		System.out.println("clave inicio: " + resp.getClaveInicio());
    		
    		//CONEXION
    		try {
    			App.apiweb.openConnection();
    		} catch (Exception e) {
				e.printStackTrace();
			}
	    	
			App.apiweb.subscribe("/topic/conectarse/" + resp.getClaveInicio(), String.class, s -> {
	    		if (s == null) {
	    			System.out.println("Error al iniciar sesión (se queda bloqueado el cliente)");
	    		} else {
	    			App.setSessionID(s);
	    			System.out.println("ID sesión: " + App.getSessionID());
	    			System.out.println("Sesión iniciada");
	    			
	    			//ENTRAR A LA APLICACION
	    			try {
		        		App.setRoot("principal");
		    			App.setFullScreen();
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    			}
	    		}
	    	});
	    	
			App.apiweb.sendObject("/app/conectarse/" + resp.getClaveInicio(), "vacio");			
	    	System.out.println("Esperando inicio sesión... ");
    	} else {
    		cajaCorreo.setText("");
    		cajaContrasenya.setText("");
    		
	    	System.out.println("Exito: " + resp.isExito());
	    	System.out.println("Tipo de error: " + resp.getErrorInfo());
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
    @FXML
    private void onGoCajaContrasenya(ActionEvent event) {
    	cajaContrasenya.requestFocus();
    }
    
    @FXML
    private void onEnter(ActionEvent event) {
    	login(event);
    }
}
