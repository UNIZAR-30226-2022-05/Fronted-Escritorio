package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.utils.HashUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ConfCuentaController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML TextField cajaNombre;	
	@FXML TextField cajaCorreo;	
	@FXML PasswordField cajaContrasenya;
	@FXML PasswordField cajaContrasenya2;
	
	@FXML VBox contenedorOculto;
	@FXML TextField cajaCodigo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {	
		RestAPI apirest = new RestAPI("/api/sacarUsuarioVO");
		String sesionID = App.getSessionID();
		apirest.addParameter("sessionID",sesionID);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection();
    	UsuarioVO retorno = apirest.receiveObject(UsuarioVO.class);
    	if (retorno.isExito()) {
    		cajaNombre.setText(retorno.getNombre());
    		cajaCorreo.setText(retorno.getCorreo());
    	}
	}
	
	@FXML
    private void goBack(ActionEvent event) {
		if (contenedorOculto.isVisible()) {
			RestAPI apirest = new RestAPI("/api/actualizarCancel");
			String sesionID = App.getSessionID();
			apirest.addParameter("sessionID",sesionID);
			
			apirest.openConnection();
	    	String retorno = apirest.receiveObject(String.class);
	    	if (retorno != null) {
	    		if (DEBUG) System.out.println(retorno);
	    	}
		}
    	App.setRoot("principal");
	}

	@FXML
    private void goToMain(Event event) {
		if (contenedorOculto.isVisible()) {
			RestAPI apirest = new RestAPI("/api/actualizarCancel");
			String sesionID = App.getSessionID();
			apirest.addParameter("sessionID",sesionID);
			
			apirest.openConnection();
	    	String retorno = apirest.receiveObject(String.class);
	    	if (retorno != null) {
	    		if (DEBUG) System.out.println(retorno);
	    	}
		}
    	App.setRoot("principal");
	}

	@FXML
	private void actualizarCuenta(ActionEvent event) {
		String nuevoCorreo = cajaCorreo.getText();
		String nuevoNombre = cajaNombre.getText();
		String nuevaContrasenna = cajaContrasenya.getText();
		String confirmarContrasenna = cajaContrasenya2.getText();
    	
		if (nuevoCorreo == null || nuevoNombre == null || nuevaContrasenna == null
				|| !nuevaContrasenna.equals(confirmarContrasenna)) {
			if (DEBUG) System.out.println("Faltan parámetros o hay parámetros incorrectos");
		} else {
			RestAPI apirest = new RestAPI("/api/actualizarCuentaStepOne");
			String sesionID = App.getSessionID();
			apirest.addParameter("sessionID",sesionID);
			apirest.addParameter("correoNuevo",nuevoCorreo);
			apirest.addParameter("nombre",nuevoNombre);
			apirest.addParameter("contrasenna",HashUtils.cifrarContrasenna(nuevaContrasenna));
			apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
			
			apirest.openConnection();
	    	String retorno = apirest.receiveObject(String.class);
	    	if (retorno == null) {
	    		desocultarContenedorOculto();
	    	} else {
	    		if (DEBUG) System.out.println(retorno);
	    	}
		}
	}
	
	private void desocultarContenedorOculto() {
		contenedorOculto.setDisable(false);
		contenedorOculto.setVisible(true);
	}
	
	@FXML
	private void confirmarCodigo (ActionEvent event) {
		Integer codigo = Integer.parseInt(cajaCodigo.getText());
		RestAPI apirest = new RestAPI("/api/actualizarCuentaStepTwo");
		String sesionID = App.getSessionID();
		apirest.addParameter("sessionID",sesionID);
		apirest.addParameter("codigo",codigo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection();
		String retorno = apirest.receiveObject(String.class);
    	if (retorno == null) {
    		if (DEBUG) System.out.println("Exito.");
    		//Para ocultar la caja del código una vez ya se ha usado
    		contenedorOculto.setDisable(true);
    		contenedorOculto.setVisible(false);
    	} else {
    		if (DEBUG) System.out.println(retorno);
    	}
	}
	
	@FXML
    private void deleteAccount(ActionEvent event) {
		RestAPI apirest = new RestAPI("/api/borrarCuenta");
		String sesionID = App.getSessionID();
		apirest.addParameter("sessionID",sesionID);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection();
    	String retorno = apirest.receiveObject(String.class);
    	if (retorno.equals("BORRADA")) {
	    	App.setRoot("login");
	    	if (DEBUG) System.out.println("Cuenta eliminada");
    	} else {
    		if (DEBUG) System.out.println(retorno);
    	}
	}

}
