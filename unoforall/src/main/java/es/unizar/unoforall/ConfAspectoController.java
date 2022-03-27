package es.unizar.unoforall;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class ConfAspectoController {

	public static Integer avatarSelec = 0;
	public static Integer cartaSelec = 0;
	public static Integer tableroSelec = 0;
	

	@FXML
    private void goBack(ActionEvent event) {
		try {
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
	}

	@FXML
    private void goToMain(Event event) {
		try {
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
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
		System.out.println("Configuración seleccionada: \n" +
							"\t Avatar: " + avatarSelec + "\n" +
							"\t Cartas: " + cartaSelec + "\n" +
							"\t Tablero: " + tableroSelec);
		try {
//	    	App.setRoot("principal");
			App.setRoot("confAspecto");
		} catch (IOException e) {
			System.out.print(e);
		}
	}
	
}
