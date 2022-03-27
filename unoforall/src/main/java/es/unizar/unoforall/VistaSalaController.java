package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class VistaSalaController implements Initializable {
	
	public int numJugadores;
	
	private String[] nombresBots = {"StrikkerFurro", "12000C", "Raul", "Vendo Mandarinas"};
	
	@FXML private ImageView pfpJug1;
	@FXML private ImageView rdyIconJug1;
	@FXML private Label nomJug1;
	private boolean readyJug1 = false;
	
	@FXML private ImageView pfpJug2;
	@FXML private ImageView rdyIconJug2;
	@FXML private Label nomJug2;
	private boolean readyJug2 = false;
	
	@FXML private ImageView pfpJug3;
	@FXML private ImageView rdyIconJug3;
	@FXML private Label nomJug3;
	private boolean readyJug3 = false;
	
	@FXML private ImageView pfpJug4;
	@FXML private ImageView rdyIconJug4;
	@FXML private Label nomJug4;
	private boolean readyJug4 = false;
	
//	Por defecto deDondeVengo es la pantalla principal
//	para evitar posibles errores en ejecución
	public static String deDondeVengo = "principal";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomJug1.setText(nombresBots[0]);
		nomJug2.setText(nombresBots[1]);
		nomJug3.setText(nombresBots[2]);
		nomJug4.setText(nombresBots[3]);
	}
	
	@FXML
    private void goBack(ActionEvent event) {
		try {
	    	App.setRoot(deDondeVengo);
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
	
	@FXML
    private void ready(ActionEvent event) {
		readyJug1 = true;
	}
	
	@FXML
    private void leaveRoom(ActionEvent event) {
		try {
	    	App.setRoot(deDondeVengo);
		} catch (IOException e) {
			System.out.print(e);
		}
	}

}
