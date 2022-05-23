package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.text.TextAlignment;

public class ConfirmCorreoController implements Initializable{
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	Clipboard systemClipboard = Clipboard.getSystemClipboard();
	String clipboardText = systemClipboard.getString();
	
	@FXML private Label labelInfo;
	@FXML private Label labelError;
	@FXML private TextField cajaCodigo;
	public static String correo = null;
	
	@FXML
    private void goBack(ActionEvent event) {
		labelError.setText("");
    	//CANCELACION DE REGISTRO
		RestAPI apirest = new RestAPI("/api/registerCancel");
		apirest.addParameter("correo", correo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});

		apirest.openConnection();
    	String error = apirest.receiveObject(String.class);
    	
    	if (error != null) {
    		labelError.setText(StringUtils.parseString(error));
    		if (DEBUG) System.out.println(error);
    	}
    	
    	App.setRoot("registro");
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
		RestAPI apirest = new RestAPI("/api/registerStepTwo");
		apirest.addParameter("correo", correo);
		apirest.addParameter("codigo", codigo);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});

		apirest.openConnection();
    	String error = apirest.receiveObject(String.class);
    	
    	if (error == null) {
        	App.setRoot("login");
    	} else {
    		labelError.setText(StringUtils.parseString(error));
    		if (DEBUG) System.out.println(error);
    	}
    }
	
	@FXML
    private void onEnter(ActionEvent event) {
    	confirmCode(event);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		//labelInfo.setTextAlignment(TextAlignment.CENTER);
	}
}