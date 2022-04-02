package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.model.salas.ConfigSala;
import es.unizar.unoforall.model.salas.ReglasEspeciales;
import es.unizar.unoforall.model.salas.RespuestaSalas;
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
	private ArrayList<UUID> IDsalas = new ArrayList<UUID>();
	
	private static ConfigSala config =
			new ConfigSala(ConfigSala.ModoJuego.Undefined, new ReglasEspeciales(), -1, true);

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		listaSalas.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String salaSelec = listaSalas.getSelectionModel().getSelectedItem();
				int idxSalaSelec = listaSalas.getSelectionModel().getSelectedIndex();
				UUID idSala = IDsalas.get(idxSalaSelec);
				App.setSalaID(idSala);
				
				try {
					App.setRoot("vistaSala");
					System.out.println("Entrado a la sala: " + salaSelec);
				} catch (Exception e) {
					System.out.println("Error entrando a la sala: " + salaSelec);
				}
			}
		});
	}
	
	public static void addSearchParameters(ConfigSala c) {	
		config = c;
	}
	
	@FXML
    private void cleanSearchParameters() {
		cajaIdSala.clear();
		config = new ConfigSala(ConfigSala.ModoJuego.Undefined, new ReglasEspeciales(), -1, true);
		BusqAvanzSalaController.cleanSearchParameters();
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
		//BORRAR RESULTADOS ANTERIORES DE LA VENTANA DE RESULTADOS
		listaSalas.getItems().clear();
		IDsalas.clear();
		String sesionID = App.getSessionID();
		
		if (!cajaIdSala.getText().equals("")) {
			String salaID = cajaIdSala.getText();
			System.out.println("Buscando sala con id: " + salaID);
			
			RestAPI apirest = new RestAPI("/api/buscarSalaID");
			apirest.addParameter("sesionID", sesionID);
			apirest.addParameter("salaID", salaID);
			apirest.setOnError(e -> {System.out.println(e);});
	    	
			apirest.openConnection();
			Sala r = apirest.receiveObject(Sala.class);
			
			if (r.isNoExiste()) {
    			System.out.println("No se ha encontrado ninguna sala");
    		} else {
    			listaSalas.getItems().add(r.toString());
    			IDsalas.add(UUID.fromString(salaID));
    			System.out.println("sala encontrada:" + r);
    		}
		} else {

//			System.out.println("Buscadas Salas de tipo:");
//			System.out.println("Número de participantes: " + maxParticipantes);
//			System.out.print("Rayos X: ");
//			if (rayosX) System.out.println("Si"); else System.out.println("No");
//			System.out.print("Intercambio: ");
//			if (intercambio) System.out.println("Si"); else System.out.println("No");
//			System.out.print("X2: ");
//			if (modifX2) System.out.println("Si"); else System.out.println("No");
//			System.out.println("Modo de juego: " + selectedGamemode);
//			System.out.print("Encadenar +2 y +4: ");
//			if (encadenar) System.out.println("Si"); else System.out.println("No");
//			System.out.print("Redirigir +2 y +4: ");
//			if (redirigir) System.out.println("Si"); else System.out.println("No");
//			System.out.print("Jugar Varias Cartas: ");
//			if (jugarVarias) System.out.println("Si"); else System.out.println("No");
//			System.out.print("Evitar Cartas Especiales: ");
//			if (evitarEspeciales) System.out.println("Si"); else System.out.println("No");
			
			RestAPI apirest = new RestAPI("/api/filtrarSalas");
			apirest.addParameter("sesionID", sesionID);
			apirest.addParameter("configuracion", config);
			apirest.setOnError(e -> {System.out.println(e);});
	    	
			apirest.openConnection();
			RespuestaSalas r = apirest.receiveObject(RespuestaSalas.class);
			
			if (r.isExito()) {
				System.out.println("Filtrado salas con exito.");

				//MOSTRAR LOS RESULTADOS ACTUALIZADOS
				System.out.println("Salas encontradas:");
				r.getSalas().forEach((k,v) -> {
					listaSalas.getItems().add(v.toString());
					IDsalas.add(k);
					System.out.println(v.toString());
				});
			} else {
				System.out.println("Ha habido un error al filtrar las salas.");
			}
		}
		cajaIdSala.clear();
	}
}
