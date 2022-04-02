package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.model.salas.ConfigSala;
import es.unizar.unoforall.model.salas.ReglasEspeciales;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.GridPane;

public class BusqAvanzSalaController implements Initializable {
	
	@FXML private ChoiceBox<String> GameModeChoiceBox;
	private static final String[] gamemodes = {"Todos", "Uno Clásico", "Uno Attack", "Uno por Parejas" };
	private static String selectedGamemode = gamemodes[0];
	
	private static int maxParticipantes = -1;
	@FXML private RadioButton partTodos;
	@FXML private RadioButton part2;
	@FXML private RadioButton part3;
	@FXML private RadioButton part4;
	
	//PARA NO FILTRAR POR REGLAS EN CASO DE QUE NO SE HAYA ESPECIFICADO
	private static boolean filtrarPorReglas = false;
	@FXML private RadioButton filtrarReglasSi;
	@FXML private RadioButton filtrarReglasNo;
	
	//CUADRO DONDE SE PUEDEN SELECCIONAR LAS REGLAS A FILTRAR
	@FXML private GridPane cajaReglas;
	
	private static boolean rayosX = false;
	@FXML private RadioButton rayosXSi;
	@FXML private RadioButton rayosXNo;
	
	private static boolean intercambio = false;
	@FXML private RadioButton intercambioSi;
	@FXML private RadioButton intercambioNo;
	
	private static boolean modifX2 = false;
	@FXML private RadioButton modifX2Si;
	@FXML private RadioButton modifX2No;
	
	private static boolean encadenar = false;
	@FXML private RadioButton encadenarSi;
	@FXML private RadioButton encadenarNo;
	
	private static boolean redirigir = false;
	@FXML private RadioButton redirigirSi;
	@FXML private RadioButton redirigirNo;
	
	private static boolean jugarVarias = false;
	@FXML private RadioButton jugarVariasSi;
	@FXML private RadioButton jugarVariasNo;
	
	private static boolean evitarEspeciales = false;
	@FXML private RadioButton evitarEspecialesSi;
	@FXML private RadioButton evitarEspecialesNo;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		partTodos.setSelected(maxParticipantes==-1);
		part2.setSelected(maxParticipantes==2);
		part3.setSelected(maxParticipantes==3);
		part4.setSelected(maxParticipantes==4);
		
		GameModeChoiceBox.getItems().addAll(gamemodes);
		GameModeChoiceBox.setOnAction(this::getGameMode);
		GameModeChoiceBox.getSelectionModel().select(selectedGamemode);;

		filtrarReglasSi.setSelected(filtrarPorReglas);
		filtrarReglasNo.setSelected(!filtrarPorReglas);

		if (filtrarPorReglas) {
			cajaReglas.setDisable(false);
			cajaReglas.setVisible(true);
		} else {
			cajaReglas.setDisable(true);
			cajaReglas.setVisible(false);
		}
		
		rayosXSi.setSelected(rayosX);
		rayosXNo.setSelected(!rayosX);

		intercambioSi.setSelected(intercambio);
		intercambioNo.setSelected(!intercambio);

		modifX2Si.setSelected(modifX2);
		modifX2No.setSelected(!modifX2);

		encadenarSi.setSelected(encadenar);
		encadenarNo.setSelected(!encadenar);

		redirigirSi.setSelected(redirigir);
		redirigirNo.setSelected(!redirigir);

		jugarVariasSi.setSelected(jugarVarias);
		jugarVariasNo.setSelected(!jugarVarias);

		evitarEspecialesSi.setSelected(evitarEspeciales);
		evitarEspecialesNo.setSelected(!evitarEspeciales);
	}
	
	@FXML
	public void getGameMode (ActionEvent event) {
		String choice = GameModeChoiceBox.getValue();
		
		if (choice.equals(gamemodes[0])) {
			selectedGamemode = gamemodes[0];
		} else if (choice.equals(gamemodes[1])) {
			selectedGamemode = gamemodes[1];
		} else if (choice.equals(gamemodes[2])) {
			selectedGamemode = gamemodes[2];
		} else {
			selectedGamemode = gamemodes[3];
		}
	}
	
	@FXML
    private void goBack(ActionEvent event) {
		try {
	    	App.setRoot("buscarSala");
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
	
    public static void cleanSearchParameters() {
		selectedGamemode = gamemodes[0];
		maxParticipantes = -1;
		filtrarPorReglas = false;
		
		rayosX = false;
		intercambio = false;
		modifX2 = false;
		encadenar = false;
		redirigir = false;
		jugarVarias = false;
		evitarEspeciales = false;
	}
	
	@FXML
    private void selectNumParticip(ActionEvent event) {
		if (part2.isSelected()) {
			maxParticipantes = 2;
		}
		else if (part3.isSelected()) {
			maxParticipantes = 3;
		}
		else if (part4.isSelected()) {
			maxParticipantes = 4;
		}
	}
	
	@FXML
	private void filtrarPorReglas(ActionEvent event) {
		if (filtrarReglasSi.isSelected()) {
			filtrarPorReglas = true;
			cajaReglas.setDisable(false);
			cajaReglas.setVisible(true);
		}
		else if (filtrarReglasNo.isSelected()) {
			filtrarPorReglas = false;
			cajaReglas.setDisable(true);
			cajaReglas.setVisible(false);
		}
	}
	
	@FXML
    private void selectRayosX(ActionEvent event) {
		if (rayosXSi.isSelected()) {
			rayosX = true;
		}
		else if (rayosXNo.isSelected()) {
			rayosX = false;
		}
	}
	
	@FXML
    private void selectIntercambio(ActionEvent event) {
		if (intercambioSi.isSelected()) {
			intercambio = true;
		}
		else if (intercambioNo.isSelected()) {
			intercambio = false;
		}
	}
	
	@FXML
    private void selectX2(ActionEvent event) {
		if (modifX2Si.isSelected()) {
			modifX2 = true;
		}
		else if (modifX2No.isSelected()) {
			modifX2 = false;
		}
	}
	
	@FXML
    private void selectEncadenar(ActionEvent event) {
		if (encadenarSi.isSelected()) {
			encadenar = true;
		}
		else if (encadenarNo.isSelected()) {
			encadenar = false;
		}
	}
	
	@FXML
    private void selectRedirigir(ActionEvent event) {
		if (redirigirSi.isSelected()) {
			redirigir = true;
		}
		else if (redirigirNo.isSelected()) {
			redirigir = false;
		}
	}
	
	@FXML
    private void selectJugarVarias(ActionEvent event) {
		if (jugarVariasSi.isSelected()) {
			jugarVarias = true;
		}
		else if (jugarVariasNo.isSelected()) {
			jugarVarias = false;
		}
	}
	
	@FXML
    private void selectEvitarEspeciales(ActionEvent event) {
		if (evitarEspecialesSi.isSelected()) {
			evitarEspeciales = true;
		}
		else if (evitarEspecialesNo.isSelected()) {
			evitarEspeciales = false;
		}
	}
	
	@FXML
    private void findRooms (ActionEvent event) {
		ReglasEspeciales reglas;
		if (filtrarPorReglas) {
			reglas = new ReglasEspeciales(encadenar, redirigir, jugarVarias,
			evitarEspeciales, rayosX, intercambio, modifX2);
		} else {
			reglas = new ReglasEspeciales();
		}

		ConfigSala.ModoJuego modoJuego;
		if (selectedGamemode.equals(gamemodes[0])) modoJuego = ConfigSala.ModoJuego.Undefined;
		else if (selectedGamemode.equals(gamemodes[1])) modoJuego = ConfigSala.ModoJuego.Original;
		else if (selectedGamemode.equals(gamemodes[2])) modoJuego = ConfigSala.ModoJuego.Attack;
		else modoJuego = ConfigSala.ModoJuego.Parejas;

		ConfigSala config = new ConfigSala(modoJuego, reglas, maxParticipantes, true);
		
		BuscarSalaController.addSearchParameters(config);

		try {	
	    	App.setRoot("buscarSala");
		} catch (IOException e) {
			System.out.print(e);
		}
	}

}
