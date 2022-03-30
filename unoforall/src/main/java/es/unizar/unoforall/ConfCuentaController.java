package es.unizar.unoforall;

import java.io.IOException;

import es.unizar.unoforall.api.RestAPI;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ConfCuentaController {
	
	@FXML TextField cajaNombre;	
	@FXML TextField cajaCorreo;	
	@FXML PasswordField cajaContrasenya;
	@FXML PasswordField cajaContrasenya2;
	
	@FXML VBox contenedorOculto;
	@FXML TextField cajaCodigo;
	
	@FXML
    private void goBack(ActionEvent event) {
		try {
			if (contenedorOculto.isVisible()) {
				RestAPI apirest = new RestAPI("/api/actualizarCancel");
				String sesionID = App.getSessionID();
				apirest.addParameter("sessionID",sesionID);
				
				apirest.openConnection();
		    	String retorno = apirest.receiveObject(String.class);
		    	if (retorno != null) {
		    		System.out.println(retorno);
		    	}
			}
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
	}

	@FXML
    private void goToMain(Event event) {
		try {
			if (contenedorOculto.isVisible()) {
				RestAPI apirest = new RestAPI("/api/actualizarCancel");
				String sesionID = App.getSessionID();
				apirest.addParameter("sessionID",sesionID);
				
				apirest.openConnection();
		    	String retorno = apirest.receiveObject(String.class);
		    	if (retorno != null) {
		    		System.out.println(retorno);
		    	}
			}
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
	}

	@FXML
	private void actualizarCuenta(ActionEvent event) {
		String nuevoCorreo = cajaCorreo.getText();
		String nuevoNombre = cajaNombre.getText();
		String nuevaContrasenna = cajaContrasenya.getText();
		String confirmarContrasenna = cajaContrasenya2.getText();
		
		if (/*nuevoCorreo == null || nuevoNombre == null || nuevaContrasenna == null
				||*/ !nuevaContrasenna.equals(confirmarContrasenna)) {
			System.out.println("Faltan parámetros o hay parámetros incorrectos");
		} else {
			RestAPI apirest = new RestAPI("/api/actualizarCuentaStepOne");
			String sesionID = App.getSessionID();
			apirest.addParameter("sessionID",sesionID);
			apirest.addParameter("correoNuevo",nuevoCorreo);
			apirest.addParameter("nombre",nuevoNombre);
			apirest.addParameter("contrasenna",nuevaContrasenna);
			apirest.setOnError(e -> {System.out.println(e);});
			
			apirest.openConnection();
	    	String retorno = apirest.receiveObject(String.class);
	    	if (retorno == null) {
	    		desocultarContenedorOculto();
	    	} else {
	    		System.out.println(retorno);
	    	}
		}
	}
	
	private void desocultarContenedorOculto() {
		contenedorOculto.setDisable(false);
		contenedorOculto.setVisible(true);
	}
	
	@FXML
	private void confirmarCodigo (ActionEvent event) {
		try {
			Integer codigo = Integer.parseInt(cajaCodigo.getText());
			RestAPI apirest = new RestAPI("/api/actualizarCuentaStepTwo");
			String sesionID = App.getSessionID();
			apirest.addParameter("sessionID",sesionID);
			apirest.addParameter("codigo",codigo);
			apirest.setOnError(e -> {System.out.println(e);});
			
			apirest.openConnection();
			String retorno = apirest.receiveObject(String.class);
	    	if (retorno == null) {
	    		System.out.println("Exito.");
	    		//Para ocultar la caja del código una vez ya se ha usado
	    		contenedorOculto.setDisable(true);
	    		contenedorOculto.setVisible(false);
	    	} else {
	    		System.out.println(retorno);
	    	}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
//	@FXML
//    private void changeNomUsuario(ActionEvent event) {
//		String oldName = "oldName";
//	    System.out.println("Nombre cambiado de: " + oldName + " a: " + cajaNombre.getText());
//	}
//
//	@FXML
//    private void changeCorreo(ActionEvent event) {
//		String oldMail = "oldMail";
//	    System.out.println("Correo cambiado de: " + oldMail + " a: " + cajaCorreo.getText());
//	}
//
//	@FXML
//    private void changeContrasenya(ActionEvent event) {
//		String oldPass = "oldPass";
//		if (cajaContrasenya.getText().equals(cajaContrasenya2.getText())) {
//	    	System.out.println("Contraseña cambiada de: " + oldPass + " a: " + cajaContrasenya.getText());
//		}
//		else {
//			System.out.println("Las contrasenyas " + cajaContrasenya.getText() + " y "
//								+ cajaContrasenya.getText() + " no coinciden.");
//		}
//	}
	
	@FXML
    private void deleteAccount(ActionEvent event) {
		try {
			RestAPI apirest = new RestAPI("/api/borrarCuenta");
			String sesionID = App.getSessionID();
			apirest.addParameter("sessionID",sesionID);
			apirest.setOnError(e -> {System.out.println(e);});
			
			apirest.openConnection();
	    	String retorno = apirest.receiveObject(String.class);
	    	if (retorno.equals("BORRADA")) {
		    	App.setRoot("login");
				System.out.println("Cuenta eliminada");
	    	} else {
		    	System.out.println(retorno);
	    	}
		} catch (IOException e) {
			System.out.print(e);
		}
	}

}
