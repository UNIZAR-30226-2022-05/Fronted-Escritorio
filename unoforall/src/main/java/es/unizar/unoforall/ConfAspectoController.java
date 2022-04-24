package es.unizar.unoforall;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ConfAspectoController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

	private Integer avatarSelec;
	private Integer cartaSelec;
	private Integer tableroSelec;

	@FXML private Label labelError;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		HashMap<String, Integer> personalizacion = App.getPersonalizacion();
		avatarSelec = personalizacion.get("avatarSelec");
		cartaSelec = personalizacion.get("cartaSelec");
		tableroSelec = personalizacion.get("tableroSelec");
	}

	@FXML
    private void goBack(ActionEvent event) {
	    App.setRoot("principal");
	}

	@FXML
    private void goToMain(Event event) {
	    App.setRoot("principal");
	}

	@FXML private void selecAvatar0(MouseEvent event) {avatarSelec = 0;}
	@FXML private void selecAvatar1(MouseEvent event) {avatarSelec = 1;}
	@FXML private void selecAvatar2(MouseEvent event) {avatarSelec = 2;}
	@FXML private void selecAvatar3(MouseEvent event) {avatarSelec = 3;}
	@FXML private void selecAvatar4(MouseEvent event) {avatarSelec = 4;}
	@FXML private void selecAvatar5(MouseEvent event) {avatarSelec = 5;}
	@FXML private void selecAvatar6(MouseEvent event) {avatarSelec = 6;}
	
	@FXML private void selecCarta0(MouseEvent event) {cartaSelec = 0;}
	@FXML private void selecCarta1(MouseEvent event) {cartaSelec = 1;}
	
	@FXML private void selecTablero0(MouseEvent event) {tableroSelec = 0;}
	@FXML private void selecTablero1(MouseEvent event) {tableroSelec = 1;}
	@FXML private void selecTablero2(MouseEvent event) {tableroSelec = 2;}
	
	@FXML
    private void applyChanges(ActionEvent event) {
		labelError.setText("");
		
		RestAPI apirest = new RestAPI("/api/cambiarAvatar");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.addParameter("avatar", avatarSelec);
		apirest.addParameter("aspectoCartas", cartaSelec);
		apirest.addParameter("aspectoFondo", tableroSelec);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection();
    	String retorno = apirest.receiveObject(String.class);
    	if (retorno != null) {
    		labelError.setText(StringUtils.parseString(retorno));
    		if (DEBUG) System.out.println(StringUtils.parseString(retorno));
    	} else {
    		HashMap<String, Integer> personalizacion = new HashMap<String, Integer>();
			personalizacion.put("avatarSelec", avatarSelec);
			personalizacion.put("cartaSelec", cartaSelec);
			personalizacion.put("tableroSelec", tableroSelec);
    		App.setPersonalizacion(personalizacion);
    		
    		App.setRoot("confAspecto");
    		if (DEBUG) System.out.println("Configuración seleccionada: \n" +
					"\t Avatar: " + avatarSelec + "\n" +
					"\t Cartas: " + cartaSelec + "\n" +
					"\t Tablero: " + tableroSelec);
    	}
	}
}
