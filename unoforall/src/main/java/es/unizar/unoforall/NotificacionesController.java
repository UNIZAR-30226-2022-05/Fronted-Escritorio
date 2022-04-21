package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.ListaUsuarios;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class NotificacionesController implements Initializable{
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

    @FXML
    private Label labelError;

    @FXML
    private GridPane listaInvitacionesSala;

    @FXML
    private GridPane listaEnviadas;

    @FXML
    private GridPane listaRecibidas;

    @FXML
    void goBack(ActionEvent event) {
    	App.setRoot("principal");
    }

    @FXML
    void goToMain(MouseEvent event) {
    	App.setRoot("principal");
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//CARGAR LISTA DE PETICIONES ENVIADAS
		RestAPI apirest = new RestAPI("/api/sacarPeticionesEnviadas");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.setOnError(e -> {
			if(DEBUG) System.out.println(e);
			labelError.setText(StringUtils.parseString(e.toString()));
		});
		
		apirest.openConnection();
    	ListaUsuarios enviadas = apirest.receiveObject(ListaUsuarios.class);
    	if(enviadas.isExpirado()) {
    		if(DEBUG) System.out.println("La sesión ha expirado.");
			labelError.setText("La sesión ha expirado.");
    	} else if (!enviadas.getError().equals(null)) {
    		if(DEBUG) System.out.println("Ha sucedido el siguiente error: " + StringUtils.parseString(enviadas.getError()));
			labelError.setText("Ha sucedido el siguiente error: " + StringUtils.parseString(enviadas.getError()));
    	} else {
    		for (UsuarioVO enviada : enviadas.getUsuarios()) {
    			try {
        	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("peticionItem.fxml"));
    	        	HBox salaItem = fxmlLoader.load();
    	        	
    	        	PeticionItemController peticionItemController = fxmlLoader.getController();
    	        	peticionItemController.setData(enviada, true);
    	        	
    	        	listaEnviadas.addRow(listaEnviadas.getRowCount(), salaItem);
        			
        			if (DEBUG) System.out.println("amigo encontrado:" + enviada.getCorreo());
    			} catch (IOException e) {
    				if (DEBUG) e.printStackTrace();
    			}
    		}
    	}
    	
		//CARGAR LISTA DE PETICIONES RECIBIDAS
    	apirest = new RestAPI("/api/sacarPeticionesRecibidas");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.setOnError(e -> {
			if(DEBUG) System.out.println(e);
			labelError.setText(StringUtils.parseString(e.toString()));
		});
		
		apirest.openConnection();
    	ListaUsuarios recibidas = apirest.receiveObject(ListaUsuarios.class);
    	if(recibidas.isExpirado()) {
    		if(DEBUG) System.out.println("La sesión ha expirado.");
			labelError.setText("La sesión ha expirado.");
    	} else if (!recibidas.getError().equals(null)) {
    		if(DEBUG) System.out.println("Ha sucedido el siguiente error: " + StringUtils.parseString(recibidas.getError()));
			labelError.setText("Ha sucedido el siguiente error: " + StringUtils.parseString(recibidas.getError()));
    	} else {
    		for (UsuarioVO recibida : recibidas.getUsuarios()) {
    			try {
        	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("peticionItem.fxml"));
    	        	HBox salaItem = fxmlLoader.load();
    	        	
    	        	PeticionItemController peticionItemController = fxmlLoader.getController();
    	        	peticionItemController.setData(recibida, false);
    	        	
    	        	listaRecibidas.addRow(listaRecibidas.getRowCount(), salaItem);
        			
        			if (DEBUG) System.out.println("amigo encontrado:" + recibida.getCorreo());
    			} catch (IOException e) {
    				if (DEBUG) e.printStackTrace();
    			}
    		}
    	}
    	
		//CARGAR LISTA DE INVITACIONES
		//DE MOMENTO NO ESTÁ Y NO ESTOY SEGURO SI VA A ESTAR
	}

}
