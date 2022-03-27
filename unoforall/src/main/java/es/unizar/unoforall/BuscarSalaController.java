package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class BuscarSalaController implements Initializable {
	
	@FXML TextField cajaIdSala;
	
	@FXML ListView<String> listaSalas;
	String[] salas = {"Sala1", "Sala2"};
	private int idSalaSelec;
	private String salaSelec;
	
	private static boolean initParam = false;
	
	private static String selectedGamemode;
	private static int numParticipantes;	
	private static boolean rayosX;
	private static boolean intercambio;
	private static boolean modifX2;
	private static boolean encadenar;
	private static boolean redirigir;
	private static boolean jugarVarias;
	private static boolean evitarEspeciales;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listaSalas.getItems().addAll();
		listaSalas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				salaSelec = listaSalas.getSelectionModel().getSelectedItem();
				idSalaSelec = listaSalas.getSelectionModel().getSelectedIndex();
			}		
		});
	}
	
	public static void addSearchParameters(String gMode, int size, boolean Xray, boolean interchange, boolean x2,
									boolean concat, boolean redir, boolean multiple, boolean dodgeSpecial) {
		initParam = true;
		
		selectedGamemode = gMode;
		numParticipantes = size;
		rayosX = Xray;
		intercambio = interchange;
		modifX2 = x2;
		encadenar = concat;
		redirigir = redir;
		jugarVarias = multiple;
		evitarEspeciales = dodgeSpecial;
	}
	
	@FXML
    private void cleanSearchParameters() {
		initParam = false;
		cajaIdSala.clear();
	}

	@FXML
    private void goBack (ActionEvent event) {
		try {
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
	}

	@FXML
    private void goToMain (Event event) {
		try {
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
	}
	
	@FXML
    private void advancedSearch (Event event) {
		try {
	    	App.setRoot("busquedaAvanzadaSala");
		} catch (IOException e) {
			System.out.print(e);
		}
	}
	
	@FXML
	private void findRooms (ActionEvent event) {
		System.out.println("Buscando sala con id: " + cajaIdSala.getText());
	}
}
