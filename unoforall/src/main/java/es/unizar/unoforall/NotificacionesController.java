package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.ResourceBundle;
import java.util.Set;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.ListaUsuarios;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.salas.NotificacionSala;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.Pantalla;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class NotificacionesController implements Initializable{
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;
	
	@FXML private VBox fondo;
    @FXML private ImageView imgMenu;
    
    @FXML private Label labelError;
    @FXML private GridPane listaInvitacionesSala;
    @FXML private GridPane listaRecibidas;
    
    public static Set<NotificacionSala> invitaciones = new LinkedHashSet<NotificacionSala>();
    
    @FXML
    void goBack(ActionEvent event) {
    	App.setRoot(Pantalla.PRINCIPAL);
    }

    @FXML
    void goToMain(MouseEvent event) {
    	App.setRoot(Pantalla.PRINCIPAL);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//PONER EL FONDO CORRESPONDIENTE
		fondo.setBackground(ImageManager.getBackgroundImage(App.getPersonalizacion().get("tableroSelec")));

		//CONFIGURACION DE EFECTO DE HOVER
		imgMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(124);
				imgMenu.setFitHeight(110);
				imgMenu.setEffect(new Glow(0.3));
			}
		});
		
		imgMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(114);
				imgMenu.setFitHeight(100);
				imgMenu.setEffect(null);
			}
		});
    	
		//CARGAR LISTA DE PETICIONES RECIBIDAS
    	RestAPI apirest = App.apiweb.getRestAPI();
		apirest.setOnError(e -> {
			if(DEBUG) System.out.println(e);
			labelError.setText(StringUtils.parseString(e.toString()));
		});
		
		apirest.openConnection("/api/sacarPeticionesRecibidas");
    	apirest.receiveObject(ListaUsuarios.class, recibidas -> {
    		if(recibidas.isExpirado()) {
	    		if(DEBUG) System.out.println("La sesión ha expirado.");
				labelError.setText("La sesión ha expirado.");
	    	} else if (!recibidas.getError().equals("null")) {
	    		if(DEBUG) System.out.println("Error en peticiones Recibidas: " + StringUtils.parseString(recibidas.getError()));
				labelError.setText("Error en peticiones Recibidas: " + StringUtils.parseString(recibidas.getError()));
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
	    	for (NotificacionSala notif : invitaciones) {
				try {
	    	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("invitacionItem.fxml"));
		        	HBox invitacionItem = fxmlLoader.load();
		        	
		        	InvitacionItemController invitacionItemController = fxmlLoader.getController();
		        	invitacionItemController.setData(notif);
		        	
		        	listaInvitacionesSala.addRow(listaInvitacionesSala.getRowCount(), invitacionItem);
	    			
	    			if (DEBUG) System.out.println("invitación encontrada");
				} catch (IOException e) {
					if (DEBUG) e.printStackTrace();
				}
			}
    	});
	}

	public static void annadirInvitacionSala(NotificacionSala notif) {
		invitaciones.add(notif);
	}

}
