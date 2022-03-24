package es.unizar.unoforall;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

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
    private void goToMain(ActionEvent event) {
		try {
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
	}
	
	@FXML private void selecAvatar1(ActionEvent event) {avatarSelec = 1;}
	@FXML private void selecAvatar2(ActionEvent event) {avatarSelec = 2;}
	@FXML private void selecAvatar3(ActionEvent event) {avatarSelec = 3;}
	
	@FXML private void selecCarta1(ActionEvent event) {cartaSelec = 1;}
	@FXML private void selecCarta2(ActionEvent event) {cartaSelec = 2;}
	
	@FXML private void selecTablero1(ActionEvent event) {tableroSelec = 1;}
	@FXML private void selecTablero2(ActionEvent event) {tableroSelec = 2;}
	
	@FXML
    private void applyChanges(ActionEvent event) {
		System.out.print("Configuración seleccionada: \n" +
							"\t Avatar: " + avatarSelec + "\n" +
							"\t Cartas: " + cartaSelec + "\n" +
							"\t Tablero: " + tableroSelec);
		try {
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
	}
	
}
