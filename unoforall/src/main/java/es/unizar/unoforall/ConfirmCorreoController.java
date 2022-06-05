package es.unizar.unoforall;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.utils.Pantalla;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;

public class ConfirmCorreoController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;
	
	Clipboard systemClipboard = Clipboard.getSystemClipboard();
	String clipboardText = systemClipboard.getString();
	
	@FXML private Label labelInfo;
	@FXML private Label labelError;
	@FXML private TextField cajaCodigo;
	public static String correo = null;
	
	@FXML private void goBack(ActionEvent event) {
		labelError.setText("");
    	//CANCELACION DE REGISTRO
		RestAPI apirest = new RestAPI();
		apirest.addParameter("correo", correo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});

		apirest.openConnection("/api/registerCancel");
    	apirest.receiveObject(String.class, error -> {
    		if (error != null) {
	    		labelError.setText(StringUtils.parseString(error));
	    		if (DEBUG) System.out.println(error);
	    	}else {
	    		App.setRoot(Pantalla.REGISTER);
	    	}
    	});
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

    	//CONFIRMACION DE CORREO
		RestAPI apirest = new RestAPI();
		apirest.addParameter("correo", correo);
		apirest.addParameter("codigo", codigo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});

		apirest.openConnection("/api/registerStepTwo");
    	apirest.receiveObject(String.class, error -> {
    		if (error == null) {
	        	App.setRoot(Pantalla.LOGIN);
	    	} else {
	    		labelError.setText(StringUtils.parseString(error));
	    		if (DEBUG) System.out.println(error);
	    	}
    	});
    }
	
	@FXML
    private void onEnter(ActionEvent event) {
    	confirmCode(event);
    }
}