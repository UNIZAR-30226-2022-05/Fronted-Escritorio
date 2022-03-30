package es.unizar.unoforall;

import java.io.IOException;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.RespuestaLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;

public class LoginController {
	
	@FXML private Label labelInformacion;

	@FXML private TextField cajaCorreo;
	@FXML private PasswordField cajaContrasenya;

//    @FXML
//    private void switchToSecondary() throws IOException {
//        App.setRoot("secondary");
//    }
    
    @FXML
    private void login(ActionEvent event) {
    	
    	//Mandar datos a la BD
    	
    	//Verificar resultado de la llamada
    	
    	//Chequeo provisional

//		try {
//	    	if (cajaCorreo.getText().equals("a@unizar") && cajaContrasenya.getText().equals("1234")) {
//	    		labelInformacion.setText("Correo y contraseña correctos. Aplicación incorrecta");
//	    		App.setRoot("principal");
//	    	} else {
//	    		labelInformacion.setText("Correo y contraseña incorrectos. Pruebe otra cosa");
////	    		labelInformacion.setTextAlignment(TextAlignment.CENTER);
//	    		cajaCorreo.setText("");
//	    		cajaContrasenya.setText("");
//	    	}
//		}
//		catch (IOException e) {
//			System.out.print(e);
//		}

    	try {
//	    	String correo = cajaCorreo.getText();
//	    	String contrasenya = cajaContrasenya.getText();
    		
    		String correo = "prueba.info@gmail.com";
	    	String contrasenya = "asdfasdf";
    		
	    	RestAPI api = new RestAPI("/api/login");
	    	api.addParameter("correo", correo);
	    	api.addParameter("contrasenna", contrasenya);
	    	api.setOnError(e -> {System.out.println(e);});
	    	
	    	api.openConnection();
	    	RespuestaLogin resp = api.receiveObject(RespuestaLogin.class);
	    	
	    	if (resp.isExito()) {
	    		App.setRoot("principal");
	    		cajaCorreo.setText("");
	    		cajaContrasenya.setText("");
	    	} else {
	    		cajaCorreo.setText("");
	    		cajaContrasenya.setText("");
	    		
		    	System.out.println("Exito: " + resp.isExito());
		    	System.out.println("Tipo de error: " + resp.getErrorInfo());
	    	}
    	} catch (Exception e) {
    		System.out.println(e);
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
}
