package es.unizar.unoforall;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import es.unizar.unoforall.model.RespuestaLogin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.TextAlignment;


public class PrimaryController {	
	@FXML private Label labelInformacion;

	@FXML private TextField cajaCorreo;
	@FXML private Label labelCorreo;
	
	@FXML private TextField cajaContrasenya;
	@FXML private Label labelContrasenya;
	
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    
    @Bean
    	public static RestTemplate restTemplate() {
    		var factory = new SimpleClientHttpRequestFactory();
    		factory.setConnectTimeout(3000);
    		factory.setReadTimeout(3000);
    		return new RestTemplate(factory);
    	}
    
    @FXML
    private void checkLogin(ActionEvent event) {
    	
	    
    	Map<String, String> parametros;
		parametros = new HashMap<>();
		parametros.put("correo", "");
		parametros.put("contrasenna", "");

		ResponseEntity<RespuestaLogin> resp = restTemplate().postForEntity(
				"http://localhost/api/login", parametros, RespuestaLogin.class);
		
		RespuestaLogin result;
		HttpStatus statusCode = resp.getStatusCode();
	    if (statusCode == HttpStatus.ACCEPTED) {
	    	result = resp.getBody();
	    	System.out.println(result.isExito());
	    	System.out.println(result.getErrorInfo());
	    	System.out.println(result.getSesionID());
	    }
    	
    	
    	//Mandar datos a la BD
    	
    	//Verificar resultado de la llamada
    	
    	//Chequeo provisional

//		try {
//	    	if (cajaCorreo.getText().equals("a@unizar") && cajaContrasenya.getText().equals("1234")) {
//	    		labelInformacion.setText("Correo y contraseña correctos. Aplicación incorrecta");
//				switchToSecondary();
//	    	} else {
//	    		labelInformacion.setText("Correo y contraseña incorrectos. Pruebe otra cosa");
//	    		labelInformacion.setTextAlignment(TextAlignment.CENTER);
//	    		cajaCorreo.setText("");
//	    		cajaContrasenya.setText("");
//	    	}
//		}
//		catch (IOException e) {
//			System.out.print(e);
//		}
	}
}