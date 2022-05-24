package es.unizar.unoforall;

import java.util.UUID;
import java.util.function.Consumer;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.partidas.*;
import es.unizar.unoforall.model.salas.Sala;

public class SuscripcionSala {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	public static final boolean DEBUG = App.DEBUG;
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
			if (DEBUG) System.err.println("No se puede unir a la sala");
		}
		return exito;
	}
	
	public static void salirDeSala() {
		if (sala == null) {return;}
		cancelarSuscripcionCanalVotacionPausa();
		cancelarSuscripcionCanalEmojis();
		App.apiweb.unsubscribe("/topic/salas/" + sala.getSalaID());
		App.apiweb.sendObject("/app/salas/salir/" + sala.getSalaID(), VACIO);
		
		sala = null;
	}
	
	public static void salirDeSalaDefinitivo() {
		if (sala == null) {return;}
			
		cancelarSuscripcionCanalVotacionPausa();
		cancelarSuscripcionCanalEmojis();
		App.apiweb.unsubscribe("/topic/salas/" + sala.getSalaID());
		App.apiweb.sendObject("/app/salas/salirDefinitivo/" + sala.getSalaID(), VACIO);
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
	

	// Votación pausa
	public static void enviarVotacion(){
		App.apiweb.sendObject("/app/partidas/votaciones/" + sala.getSalaID(), VACIO);
	}

	public static void suscribirseCanalVotacionPausa(Consumer<RespuestaVotacionPausa> consumer){
		App.apiweb.subscribe("/topic/salas/" + sala.getSalaID() + "/votaciones",
				RespuestaVotacionPausa.class, respuestaVotacionPausa -> {
					if(respuestaVotacionPausa != null){
						consumer.accept(respuestaVotacionPausa);
					}
				});
	}

    public static void cancelarSuscripcionCanalVotacionPausa(){
    	App.apiweb.unsubscribe("/topic/salas/" + sala.getSalaID() + "/votaciones");
    }


	// Emojis
	public static void suscribirseCanalEmojis(Consumer<EnvioEmoji> consumer){
        App.apiweb.subscribe("/topic/salas/" + sala.getSalaID() + "/emojis",
                EnvioEmoji.class, consumer);
    }
    public static void cancelarSuscripcionCanalEmojis(){
        App.apiweb.unsubscribe("/topic/salas/" + sala.getSalaID() + "/emojis");
    }
    public static void enviarEmoji(int jugadorID, int emojiID){
        EnvioEmoji envioEmoji = new EnvioEmoji(emojiID, jugadorID, false);
        App.apiweb.sendObject("/app/partidas/emojiPartida/" + sala.getSalaID(), envioEmoji);
    }


	private static void ackSala() {
		if(sala != null){
            RestAPI api = new RestAPI("/api/ack");
            api.addParameter("sesionID", App.getSessionID());
            api.addParameter("salaID", sala.getSalaID());
            api.openConnection();
            boolean exito = api.receiveObject(Boolean.class);
            if (!exito) {
            	if (DEBUG) System.err.println("Se ha producido un error al enviar el ACK");
            }
        }
	}

}
