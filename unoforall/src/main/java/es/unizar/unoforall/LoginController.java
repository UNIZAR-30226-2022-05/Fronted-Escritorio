package es.unizar.unoforall;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.RespuestaLogin;
import es.unizar.unoforall.utils.HashUtils;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML private Label labelInformacion;
	@FXML private Label labelError;

	@FXML private TextField cajaCorreo;
	@FXML private PasswordField cajaContrasenya;
    
	@FXML
	private void cambiarIP (ActionEvent event) {
		App.setRoot("cambiarIP");
	}
	
    @FXML
    private void login(ActionEvent event) {
		labelError.setText("");
    	String correo = cajaCorreo.getText();
    	String contrasenna = cajaContrasenya.getText();

    	///LOGIN
		RestAPI apirest = new RestAPI("/api/login");
		apirest.addParameter("correo", correo);
		apirest.addParameter("contrasenna", HashUtils.cifrarContrasenna(contrasenna));
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection();
    	RespuestaLogin resp = apirest.receiveObject(RespuestaLogin.class);
    	
    	if (resp.isExito()) {
    		//GUARDAR RESPUESTALOGIN EN CASO DE NECESITARLO
    		App.setRespLogin(resp);
    		if (DEBUG) System.out.println("clave inicio: " + resp.getClaveInicio());
    		
    		//CONEXION
    		try {
    			App.apiweb.openConnection();
    		} catch (Exception e) {
    			if (DEBUG) e.printStackTrace();
			}
	    	
			App.apiweb.subscribe("/topic/conectarse/" + resp.getClaveInicio(), String.class, s -> {
	    		if (s == null) {
	    			labelError.setText("Error al conectarse al servidor");
	    			if (DEBUG) System.out.println("Error al conectarse al servidor");
	    		} else {
	    			App.setSessionID(s);
	    			if (DEBUG) {
	    				System.out.println("ID sesión: " + App.getSessionID());
		    			System.out.println("Sesión iniciada");
	    			}
	    			
	    			//ACTIVAR NOTIFICACIONES
	    			App.activarNotificaciones();
	    			
	    			//ENTRAR A LA APLICACION
		        	App.setRoot("principal");
		    		App.setFullScreen();
	    		}
	    	});
	    	
			App.apiweb.sendObject("/app/conectarse/" + resp.getClaveInicio(), "vacio");		
			if (DEBUG) System.out.println("Esperando inicio sesión... ");
    	} else {
    		if (DEBUG) {
    			labelError.setText(StringUtils.parseString(resp.getErrorInfo()));
		    	System.out.println("Exito: " + resp.isExito());
		    	System.out.println("Tipo de error: " + resp.getErrorInfo());
    		}
    	}
	}
    
    @FXML
    private void register(ActionEvent event) {
        App.setRoot("registro");
    }
    
    @FXML
    private void forgotPass(ActionEvent event) {
        App.setRoot("especificacionCorreo");
    	
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
