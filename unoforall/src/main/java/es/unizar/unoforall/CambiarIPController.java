package es.unizar.unoforall;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.utils.Pantalla;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CambiarIPController {
	@FXML private TextField cajaIP;
	
	@FXML
	private void cambiarIP (ActionEvent event) {
		String ip = cajaIP.getText();
		RestAPI.setServerIP(ip);
		try {
			App.setRoot(Pantalla.LOGIN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
