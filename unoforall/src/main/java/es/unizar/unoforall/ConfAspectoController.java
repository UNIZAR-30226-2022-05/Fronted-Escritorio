package es.unizar.unoforall;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class ConfAspectoController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

	public static Integer avatarSelec = 0;
	public static Integer cartaSelec = 0;
	public static Integer tableroSelec = 0;
	

	@FXML
    private void goBack(ActionEvent event) {
	    App.setRoot("principal");
	}

	@FXML
    private void goToMain(Event event) {
	    App.setRoot("principal");
	}
	
	@FXML private void selecAvatar1(MouseEvent event) {avatarSelec = 1;}
	@FXML private void selecAvatar2(MouseEvent event) {avatarSelec = 2;}
	@FXML private void selecAvatar3(MouseEvent event) {avatarSelec = 3;}
	
	@FXML private void selecCarta1(MouseEvent event) {cartaSelec = 1;}
	@FXML private void selecCarta2(MouseEvent event) {cartaSelec = 2;}
	
	@FXML private void selecTablero1(MouseEvent event) {tableroSelec = 1;}
	@FXML private void selecTablero2(MouseEvent event) {tableroSelec = 2;}
	
	@FXML
    private void applyChanges(ActionEvent event) {
		if (DEBUG) System.out.println("Configuración seleccionada: \n" +
							"\t Avatar: " + avatarSelec + "\n" +
							"\t Cartas: " + cartaSelec + "\n" +
							"\t Tablero: " + tableroSelec);
		App.setRoot("confAspecto");
	}
}
