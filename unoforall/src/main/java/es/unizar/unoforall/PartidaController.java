package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.ListaUsuarios;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.StringUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class PartidaController extends SalaReceiver implements Initializable {

	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	@FXML private Label labelError;
	
	private Sala sala;
	
//	Por defecto deDondeVengo es la pantalla principal
//	para evitar posibles errores en ejecución
	public static String deDondeVengo = "principal";
	//getJugadores().get(i).getMano();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//ESTABLECER EN QUÉ PANTALLA ESTOY PARA SALAS Y PARTIDAS
		SuscripcionSala.dondeEstoy(this); 
		System.out.println(SuscripcionSala.sala);
		//System.out.println(SuscripcionSala.sala.getPartida().getUltimaCartaJugada());
		//System.out.println(SuscripcionSala.sala.getPartida().getNumIAs());
		if (DEBUG) System.out.println("Entrando en partida...");
		//UUID salaID = App.getSalaID();
		//App.apiweb.subscribe("/topic/salas/" + salaID, Sala.class, s -> actualizarSala(s, salaID));
		//App.apiweb.sendObject("/app/salas/listo/" + salaID, "vacio");
		
		//BUSCAR AMIGOS
		//String sesionID = App.getSessionID();
		
	}
	@Override
	public void administrarSala(Sala sala) {
		
		labelError.setText("");
		if (sala.isNoExiste()) {
			labelError.setText(StringUtils.parseString(sala.getError()));
			if (DEBUG) System.out.println(sala.getError());
			SuscripcionSala.salirDeSala();
			App.setRoot(deDondeVengo);
		} else {
			
			if (DEBUG) System.out.println("Estado de la sala: " + sala);
			System.out.println(sala.getPartida().getUltimaCartaJugada());
			if (sala.isEnPartida()) {
				//Hacer cosas dependiendo de la sala s (???)
				if (DEBUG) System.out.println("Jugando");
				
			}
		}
	}
	/*
	@Override
	public void administrarSala(Sala sala) {
		// TODO Auto-generated method stub
	}*/
}