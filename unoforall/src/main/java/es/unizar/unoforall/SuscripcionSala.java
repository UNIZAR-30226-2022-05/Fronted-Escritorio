package es.unizar.unoforall;

import java.util.UUID;

import es.unizar.unoforall.model.salas.Sala;

public class SuscripcionSala {
	private static final String VACIO = "vacio";
	public static Sala sala;
	private static SalaReceiver pantallaActual;
	
	public static void unirseASala(UUID salaID){ 
		App.apiweb.subscribe("/topic/salas/" + salaID, Sala.class, s -> {
			if (pantallaActual != null) {
				sala = s;
				pantallaActual.administrarSala(s);
			} 
		});
		App.apiweb.sendObject("/app/salas/unirse/" + salaID, VACIO);
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


}
