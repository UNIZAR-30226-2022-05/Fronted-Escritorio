package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.interfaces.SalaListener;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.model.salas.ConfigSala;
import es.unizar.unoforall.model.salas.ReglasEspeciales;
import es.unizar.unoforall.model.salas.RespuestaSalas;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class BuscarSalaController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML private VBox fondo;
	@FXML private ImageView imgMenu;
	
	private SalaListener myListener = new SalaListener() {
		@Override
		public void onClickListener(UUID salaID) {
			VistaSalaController.deDondeVengo = "buscarSala";
			App.setSalaID(salaID);
			App.setRoot("vistaSala");
		}
	};

	@FXML private Label labelError;
	@FXML TextField cajaIdSala;
	
	@FXML GridPane listaSalas;
	
	private static ConfigSala config =
			new ConfigSala(ConfigSala.ModoJuego.Undefined, new ReglasEspeciales(), -1, true);
	
	public static void addSearchParameters(ConfigSala c) {	
		config = c;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//PONER EL FONDO CORRESPONDIENTE
		fondo.setBackground(ImageManager.getBackgroundImage(App.getPersonalizacion().get("tableroSelec")));

		//ASOCIAR EVENTOS DE AREA ENTERED A LAS IMAGENES
		imgMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(210);
				imgMenu.setFitHeight(160);
				imgMenu.setEffect(new Glow(0.3));
			}
		});;
		imgMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(200);
				imgMenu.setFitHeight(150);
				imgMenu.setEffect(null);
			}
		});;
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
		listaSalas.getChildren().clear();
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
    	        try {
        	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("salaItem.fxml"));
    	        	HBox salaItem = fxmlLoader.load();
    	        	
    	        	SalaItemController salaItemController = fxmlLoader.getController();
    	        	salaItemController.setData(r.getConfiguracion(), UUID.fromString(salaID), myListener);
    	        	
        	        listaSalas.addRow(listaSalas.getRowCount(), salaItem);
        			
        			if (DEBUG) System.out.println("sala encontrada:" + r);
    			} catch (IOException e) {
    				if (DEBUG) e.printStackTrace();
    			}
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
	    	        try {
	        	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("salaItem.fxml"));
	        	        HBox salaItem = fxmlLoader.load();
	    	        	
	    	        	SalaItemController salaItemController = fxmlLoader.getController();
	    	        	salaItemController.setData(v.getConfiguracion(), k, myListener);
	    	        	
	        	        listaSalas.addRow(listaSalas.getRowCount(), salaItem);
	        	        //ESTABLECER ANCHURA DE ITEM
	        	        listaSalas.setMinWidth(Region.USE_COMPUTED_SIZE);
	        	        listaSalas.setPrefWidth(Region.USE_COMPUTED_SIZE);
	        	        listaSalas.setMaxWidth(Region.USE_PREF_SIZE);
						
						if (DEBUG) System.out.println(v.getConfiguracion().toString());
	    			} catch (IOException e) {
	    				if (DEBUG) e.printStackTrace();
	    			}
				});
			} else {
				labelError.setText("Ha habido un error al filtrar las salas.");
				if (DEBUG) System.out.println("Ha habido un error al filtrar las salas.");
			}
		}
	}
}
