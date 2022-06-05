package es.unizar.unoforall;

import java.util.UUID;
import java.util.function.Consumer;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.partidas.*;
import es.unizar.unoforall.model.salas.Sala;

public class SuscripcionSala {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	public static final boolean DEBUG = App.DEBUG;
	public static Sala sala;
	private static SalaReceiver pantallaActual;
	
	public static void unirseASala(UUID salaID, Consumer<Boolean> consumer){
		RestAPI api = App.apiweb.getRestAPI();
        api.addParameter("salaID", salaID);
        api.openConnection("/api/comprobarUnirseSala");
        api.receiveObject(Boolean.class, exito -> {
        	if (exito) {
				App.apiweb.subscribe("/topic/salas/" + salaID, Sala.class, s -> {
					if (pantallaActual != null) {
						sala = s;
						ackSala();
						pantallaActual.administrarSala(s);
					}
				});
				RestAPI api2 = App.apiweb.getRestAPI();
				api2.openConnection("/app/salas/unirse/" + salaID);
				api2.receiveObject(String.class, null);
			} else {
				if (DEBUG) System.err.println("No se puede unir a la sala");
			}
        	consumer.accept(exito);
        });
	}
	
	public static void salirDeSala() {
		if (sala == null) {return;}
		cancelarSuscripcionCanalVotacionPausa();
		cancelarSuscripcionCanalEmojis();
		App.apiweb.unsubscribe("/topic/salas/" + sala.getSalaID());
		
		RestAPI api = App.apiweb.getRestAPI();
		api.openConnection("/app/salas/salir/" + sala.getSalaID());
		api.receiveObject(String.class, null);
		sala = null;
	}
	
	public static void salirDeSalaDefinitivo() {
		if (sala == null) {return;}
			
		cancelarSuscripcionCanalVotacionPausa();
		cancelarSuscripcionCanalEmojis();
		App.apiweb.unsubscribe("/topic/salas/" + sala.getSalaID());
		
		RestAPI api = App.apiweb.getRestAPI();
		api.openConnection("/app/salas/salirDefinitivo/" + sala.getSalaID());
		api.receiveObject(String.class, null);
		sala = null;
	}
	
	public static void listoSala() {
		if (sala == null) {return;}
		RestAPI api = App.apiweb.getRestAPI();
		api.openConnection("/app/salas/listo/" + sala.getSalaID());
		api.receiveObject(String.class, null);
	}
	
	public static void dondeEstoy(SalaReceiver pantallaActual) {
		SuscripcionSala.pantallaActual = pantallaActual;
	}
	
	public static void enviarJugada(Jugada jugada) {
		RestAPI api = App.apiweb.getRestAPI();
		api.addParameter("jugada", jugada);
		api.openConnection("/app/partidas/turnos/" + sala.getSalaID());
		api.receiveObject(String.class, null);
	}
	

	// Votación pausa
	public static void enviarVotacion(){
		RestAPI api = App.apiweb.getRestAPI();
		api.openConnection("/app/partidas/votaciones/" + sala.getSalaID());
		api.receiveObject(String.class, null);
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
        
        RestAPI api = App.apiweb.getRestAPI();
        api.addParameter("emoji", envioEmoji);
        api.openConnection("/app/partidas/emojiPartida/" + sala.getSalaID());
        api.receiveObject(String.class, null);
    }
    
    public static void pulsarBotonUNO(){
    	RestAPI api = App.apiweb.getRestAPI();
    	api.openConnection("/app/partidas/botonUNO/" + SuscripcionSala.sala.getSalaID());
    	api.receiveObject(String.class, null);
        if (DEBUG) System.out.println("Has pulsado el botón UNO");
    }


	private static void ackSala() {
		/*if(sala != null){
            RestAPI api = new RestAPI("/api/ack");
            api.addParameter("sesionID", App.getSessionID());
            api.addParameter("salaID", sala.getSalaID());
            api.openConnection();
            boolean exito = api.receiveObject(Boolean.class);
            if (!exito) {
            	if (DEBUG) System.err.println("Se ha producido un error al enviar el ACK");
            }
        }*/
	}

}
