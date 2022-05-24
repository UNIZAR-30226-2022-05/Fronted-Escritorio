package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.RespuestaLogin;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.utils.HashUtils;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class LoginController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML private VBox fondo;
	@FXML private Button btnCambiarIP;
	
	@FXML private Label labelInformacion;
	@FXML private Label labelError;

	@FXML private TextField cajaCorreo;
	@FXML private PasswordField cajaContrasenya;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fondo.setBackground(ImageManager.getBackgroundImage(ImageManager.FONDO_DIBUJITOS));
		if (App.MODO_PRODUCCION) {
			btnCambiarIP.setVisible(false);
			btnCambiarIP.setDisable(true);
		}
	}
	
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
    	
    	if (resp != null && resp.isExito()) {
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
	    			RestAPI apirest2 = new RestAPI("/api/sacarUsuarioVO");
	    			apirest2.addParameter("sesionID", s);
	    			apirest2.openConnection();
	    			
	    			UsuarioVO usuarioVO = apirest2.receiveObject(UsuarioVO.class);
	    			
	    			App.setUsuarioID(usuarioVO.getId());
	    			if (DEBUG) {
	    				System.out.println("ID sesión: " + App.getSessionID());
		    			System.out.println("Sesión iniciada");
	    			}
	    			
	    			//OBTENER ASPECTO DE LA APLICACION
	    			App.initializePersonalizacion();
	    			
	    			//ACTIVAR NOTIFICACIONES
	    			App.activarNotificaciones();
	    			
	    			//ENTRAR A LA APLICACION
		        	App.setRoot("principal");
	    		}
	    	});
	    	
			App.apiweb.sendObject("/app/conectarse/" + resp.getClaveInicio(), "vacio");		
			if (DEBUG) System.out.println("Esperando inicio sesión... ");
    	} else if (resp != null) {
    		if (DEBUG) {
    			labelError.setText(StringUtils.parseString(resp.getErrorInfo()));
		    	System.out.println("Exito: " + resp.isExito());
		    	System.out.println("Tipo de error: " + resp.getErrorInfo());
    		}
    	} else {
    		labelError.setText("El servidor no es alcanzable");
    		if (DEBUG) System.out.println("El servidor no es alcanzable");
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
