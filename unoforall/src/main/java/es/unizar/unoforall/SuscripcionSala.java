package es.unizar.unoforall;

import java.util.UUID;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.partidas.Jugada;
import es.unizar.unoforall.model.salas.Sala;

public class SuscripcionSala {
	private static final String VACIO = "vacio";
	public static Sala sala;
	private static SalaReceiver pantallaActual;
	
	public static boolean unirseASala(UUID salaID){
		RestAPI api = new RestAPI("/api/comprobarUnirseSala");
        api.addParameter("sesionID", App.getSessionID());
        api.addParameter("salaID", salaID);
        api.openConnection();
        boolean exito = api.receiveObject(Boolean.class);
        
		if (exito) {
			App.apiweb.subscribe("/topic/salas/" + salaID, Sala.class, s -> {
				if (pantallaActual != null) {
					sala = s;
					ackSala();
					pantallaActual.administrarSala(s);
				}
			});
			App.apiweb.sendObject("/app/salas/unirse/" + salaID, VACIO);
		} else {
			System.err.println("No se puede unir a la sala");
		}
		return exito;
	}
	
	public static void salirDeSala() {
		if (sala == null) {return;}
			
		App.apiweb.sendObject("/app/salas/salir/" + sala.getSalaID(), VACIO);
		App.apiweb.unsubscribe("/topic/salas/" + sala.getSalaID());
		sala = null;
	}
	
	public static void listoSala() {
		if (sala == null) {return;}
		App.apiweb.sendObject("/app/salas/listo/" + sala.getSalaID(), VACIO);
	}
	
	public static void dondeEstoy(SalaReceiver pantallaActual) {
		SuscripcionSala.pantallaActual = pantallaActual;
	}
	
	public static void enviarJugada(Jugada jugada) {
		App.apiweb.sendObject("/app/partidas/turnos/" + sala.getSalaID(), jugada);
	}

	private static void ackSala() {
		if(sala != null){
            RestAPI api = new RestAPI("/api/ack");
            api.addParameter("sesionID", App.getSessionID());
            api.addParameter("salaID", sala.getSalaID());
            api.openConnection();
            boolean exito = api.receiveObject(Boolean.class);
            if (!exito) {
            	System.err.println("Se ha producido un error al enviar el ACK");
            }
        }
	}

}
