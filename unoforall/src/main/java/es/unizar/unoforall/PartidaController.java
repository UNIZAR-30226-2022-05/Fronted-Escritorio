package es.unizar.unoforall;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.ListaUsuarios;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PartidaController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML private Label labelError;
	
	private Sala sala;
	
//	Por defecto deDondeVengo es la pantalla principal
//	para evitar posibles errores en ejecución
	public static String deDondeVengo = "principal";
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (DEBUG) System.out.println("Entrando en partida...");
		UUID salaID = App.getSalaID();
		App.apiweb.subscribe("/topic/salas/" + salaID, Sala.class, s -> actualizarSala(s, salaID));
		App.apiweb.sendObject("/app/salas/unirse/" + salaID, "vacio");
		
		//BUSCAR AMIGOS
		String sesionID = App.getSessionID();
		
		RestAPI apirest = new RestAPI("/api/sacarAmigos");
		apirest.addParameter("sesionID", sesionID);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection();
		ListaUsuarios usuarios = apirest.receiveObject(ListaUsuarios.class);
		
	}
	private void actualizarSala(Sala s, UUID salaID) {
		sala = s;
		labelError.setText("");
		if (s.isNoExiste()) {
			labelError.setText(StringUtils.parseString(s.getError()));
			if (DEBUG) System.out.println(s.getError());
			App.apiweb.unsubscribe("/topic/salas/" + salaID);
			App.setRoot(deDondeVengo);
		} else {
			if (DEBUG) System.out.println("Estado de la sala: " + s);
			if (s.isEnPartida()) {
				//CARGAR LA VISTA DE LA PARTIDA
				if (DEBUG) System.out.println("En partida");
				
			}
		}
	}
}