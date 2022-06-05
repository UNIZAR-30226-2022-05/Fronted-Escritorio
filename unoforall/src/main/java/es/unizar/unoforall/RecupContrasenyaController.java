package es.unizar.unoforall;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.utils.HashUtils;
import es.unizar.unoforall.utils.Pantalla;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RecupContrasenyaController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;
	
	@FXML private Label labelError;
	@FXML private TextField cajaCodigo;
	@FXML private PasswordField cajaContrasenya;
	@FXML private PasswordField cajaContrasenya2;
	public static String correo = null;
	
	@FXML
    private void goBack(ActionEvent event) {
		App.setRoot(Pantalla.ESPECIFICACION_CORREO);
    }
    
	@FXML
    private void confirmCode(ActionEvent event) {
		labelError.setText("");
		String codigo = cajaCodigo.getText();
    	
		//Si el código no es un entero, no es válido
		try {
			Integer.parseInt(cajaCodigo.getText());
		} catch (Exception e) {
			labelError.setText("Por favor introduzca un código válido");
			return;
		}

    	///RESTABLECER PASO 2
		RestAPI apirest = new RestAPI();
		apirest.addParameter("correo", correo);
		apirest.addParameter("codigo", codigo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection("/api/reestablecerContrasennaStepTwo");
    	apirest.receiveObject(String.class, error -> {
    		if (error == null) {
	    		String contrasenna = cajaContrasenya.getText();
	    		String contrasenna2 = cajaContrasenya2.getText();
	    		
	    		if (contrasenna.equals(contrasenna2)) {
	    			///RESTABLECER PASO 3
					RestAPI apirest2 = new RestAPI();
					apirest2.addParameter("correo", correo);
					apirest2.addParameter("contrasenna", HashUtils.cifrarContrasenna(contrasenna));
					apirest2.setOnError(e -> {if (DEBUG) System.out.println(e);});
			    	
					apirest.openConnection("/api/reestablecerContrasennaStepThree");
			    	apirest.receiveObject(String.class, error2 -> {
			    		if (error2 == null) {
				    		App.setRoot(Pantalla.LOGIN);
				    	} else {
				    		labelError.setText(StringUtils.parseString(error2));
				    		if (DEBUG) System.out.println(error2);
				    	}
			    	});
	    		} else {
	    			labelError.setText("Las contraseñas no coinciden.");
	    			if (DEBUG) System.out.println("Las contraseñas no coinciden.");
	    		}
	    	} else {
	    		labelError.setText(StringUtils.parseString(error));
	    		if (DEBUG) System.out.println(error);
	    	}
    	});
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