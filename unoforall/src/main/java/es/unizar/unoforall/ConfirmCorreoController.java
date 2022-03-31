package es.unizar.unoforall;

import java.io.IOException;
import es.unizar.unoforall.api.WebSocketAPI;
import es.unizar.unoforall.api.RestAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

public class ConfirmCorreoController {
	
	Clipboard systemClipboard = Clipboard.getSystemClipboard();
	String clipboardText = systemClipboard.getString();

	@FXML private TextField cajaCodigo;
	public static String correo = null;
	
	@FXML
    private void goBack(ActionEvent event) {
    	try {
	    	//CANCELACION DE REGISTRO
			RestAPI apirest = new RestAPI("/api/registerCancel");
			apirest.addParameter("correo", correo);
			apirest.setOnError(e -> {System.out.println(e);});

			apirest.openConnection();
	    	String error = apirest.receiveObject(String.class);
	    	
	    	if (error != null) System.out.println(error);
	    	
        	App.setRoot("registro");
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
    
	@FXML
    private void confirmCode(ActionEvent event) {
    	try {
	    	String codigo = cajaCodigo.getText();
	    	//CONFIRMACION DE CORREO
			RestAPI apirest = new RestAPI("/api/registerStepTwo");
			apirest.addParameter("correo", correo);
			apirest.addParameter("codigo", codigo);
			apirest.setOnError(e -> {System.out.println(e);});

			apirest.openConnection();
	    	String error = apirest.receiveObject(String.class);
	    	
	    	if (error == null) {
	        	App.setRoot("login");
	    	} else {
	    		System.out.println(error);
	    	}
    	} catch (IOException e) {
			System.out.print(e);
    	}
    }
	
	@FXML
    private void onEnter(ActionEvent event) {
    	confirmCode(event);
    }
	/*@FXML
	public void paste() {
		if( !systemClipboard.hasContent(DataFormat.PLAIN_TEXT) ) {
			adjustForEmptyClipboard();
			return;
		}
	
	
	}*/
}