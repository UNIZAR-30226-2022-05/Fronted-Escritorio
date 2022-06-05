package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.api.WebSocketAPI;
import es.unizar.unoforall.model.RespuestaLogin;
import es.unizar.unoforall.utils.HashUtils;
import es.unizar.unoforall.utils.ImageManager;
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
	private static final boolean DEBUG = App.DEBUG;
	
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
    	App.apiweb = new WebSocketAPI();
    	App.apiweb.setOnError(ex -> {
    		labelError.setText("El servidor no es alcanzable");
    		if (DEBUG) System.out.println("El servidor no es alcanzable");
    		ex.printStackTrace();
    		App.apiweb.close();
    	});
    	
    	App.apiweb.openConnection("/topic");
    	
    	RestAPI api = App.apiweb.getRestAPI();
    	api.addParameter("correo", correo);
    	api.addParameter("contrasenna", HashUtils.cifrarContrasenna(contrasenna));
    	api.openConnection("/api/login");
    	api.receiveObject(RespuestaLogin.class, respuestaLogin -> {
    		if(respuestaLogin.isExito()) {    			
    			App.setUsuarioID(respuestaLogin.getUsuarioID());
    			if (DEBUG) {
    				System.out.println("ID sesión: " + App.apiweb.getSessionID());
	    			System.out.println("Sesión iniciada");
    			}
    			
    			App.setRespLogin(respuestaLogin);
    			
    			//OBTENER ASPECTO DE LA APLICACION
    			App.initializePersonalizacion(exito -> {
    				if(exito) {
    					//ACTIVAR NOTIFICACIONES
		    			App.activarNotificaciones();
		    			
		    			//ENTRAR A LA APLICACION
			        	App.setRoot("principal");
    				}else {
    					App.apiweb.close();
    				}	    				
    			});
    		}else {
    			App.apiweb.close();
    		}
    	});
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
