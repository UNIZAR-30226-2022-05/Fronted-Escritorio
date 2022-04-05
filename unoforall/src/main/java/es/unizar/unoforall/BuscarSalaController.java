package es.unizar.unoforall;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class BuscarSalaController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

	@FXML private Label labelError;
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
				VistaSalaController.deDondeVengo = "buscarSala";
				App.setSalaID(idSala);
				App.setRoot("vistaSala");
				if (DEBUG) System.out.println("Entrado a la sala: " + salaSelec);
			}
		});
	}
	
	public static void addSearchParameters(ConfigSala c) {	
		config = c;
	}
	
	@FXML
    private void cleanSearchParameters() {
		labelError.setText("");
		cajaIdSala.clear();
		config = new ConfigSala(ConfigSala.ModoJuego.Undefined, new ReglasEspeciales(), -1, true);
		BusqAvanzSalaController.cleanSearchParameters();
	}

	@FXML
    private void goBack (ActionEvent event) {
	    App.setRoot("principal");
	}

	@FXML
    private void goToMain (Event event) {
	    App.setRoot("principal");
	}
	
	@FXML
    private void advancedSearch (Event event) {
	    App.setRoot("busquedaAvanzadaSala");
	}
	
	@FXML
	private void findRooms (ActionEvent event) {
		labelError.setText("");
		//BORRAR RESULTADOS ANTERIORES DE LA VENTANA DE RESULTADOS
		listaSalas.getItems().clear();
		IDsalas.clear();
		String sesionID = App.getSessionID();
		
		if (!cajaIdSala.getText().equals("")) {
			String salaID = cajaIdSala.getText();
			if (DEBUG) System.out.println("Buscando sala con id: " + salaID);
			
			RestAPI apirest = new RestAPI("/api/buscarSalaID");
			apirest.addParameter("sesionID", sesionID);
			apirest.addParameter("salaID", salaID);
			apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
	    	
			apirest.openConnection();
			Sala r = apirest.receiveObject(Sala.class);
			
			if (r.isNoExiste()) {
				labelError.setText("No se ha encontrado ninguna sala con ese ID");
				if (DEBUG) System.out.println("No se ha encontrado ninguna sala con ese ID");
    		} else {
    			listaSalas.getItems().add(r.toString());
    			IDsalas.add(UUID.fromString(salaID));
    			if (DEBUG) System.out.println("sala encontrada:" + r);
    		}
		} else {
			if (DEBUG) {
				System.out.println("Buscando: " + config);
			}
			
			RestAPI apirest = new RestAPI("/api/filtrarSalas");
			apirest.addParameter("sesionID", sesionID);
			apirest.addParameter("configuracion", config);
			apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
	    	
			apirest.openConnection();
			RespuestaSalas r = apirest.receiveObject(RespuestaSalas.class);
			
			if (r.isExito()) {
				if (DEBUG) System.out.println("Filtrado salas con exito.");

				//MOSTRAR LOS RESULTADOS ACTUALIZADOS
				if (DEBUG) System.out.println("Salas encontradas:");
				r.getSalas().forEach((k,v) -> {
					listaSalas.getItems().add(v.getConfiguracion().toString());
					IDsalas.add(k);
					if (DEBUG) System.out.println(v.getConfiguracion().toString());
				});
			} else {
				labelError.setText("Ha habido un error al filtrar las salas.");
				if (DEBUG) System.out.println("Ha habido un error al filtrar las salas.");
			}
		}
	}
}
