package es.unizar.unoforall;

import java.util.HashMap;

import es.unizar.unoforall.model.salas.NotificacionSala;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class InvitacionItemController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

	private static HashMap<Integer,Image> avatares = new HashMap<Integer, Image>();
	static {
		avatares.put(0, new Image(App.class.getResourceAsStream("images/avatares/0-cero.png")));
		avatares.put(1, new Image(App.class.getResourceAsStream("images/avatares/1-uno.png")));
		avatares.put(2, new Image(App.class.getResourceAsStream("images/avatares/2-dos.png")));
		avatares.put(3, new Image(App.class.getResourceAsStream("images/avatares/3-tres.png")));
		avatares.put(4, new Image(App.class.getResourceAsStream("images/avatares/4-cuatro.png")));
		avatares.put(5, new Image(App.class.getResourceAsStream("images/avatares/5-cinco.png")));
		avatares.put(6, new Image(App.class.getResourceAsStream("images/avatares/6-seis.png")));
	}

	private NotificacionSala notificacion;
	
	@FXML private Button botonAceptar;
	@FXML private Button botonCancelar;
    @FXML private ImageView icono;
    @FXML private Label idSala;
    @FXML private Label nombre;
	
	public void setData(NotificacionSala notif) {
		notificacion = notif;
		//COMPROBAR QUÉ AVATAR TIENE PUESTO
    	icono.setImage(avatares.get(notificacion.getRemitente().getAvatar()));
    	
    	//ACTUALIZAR EL RESTO DE PARÁMETROS
    	nombre.setText(StringUtils.parseString(notificacion.getRemitente().getNombre()));
    	idSala.setText(notificacion.getSalaID().toString());
	}

    @FXML
    void aceptar(ActionEvent event) {
    	//UNIRSE A LA SALA
		App.setSalaID(notificacion.getSalaID());
		
		if (SuscripcionSala.unirseASala(notificacion.getSalaID())) {
			App.setRoot("vistaSala");
		}
    	
    	//ELIMINAR DE INVITACIONES RECIBIDAS
    	NotificacionesController.invitaciones.remove(notificacion);
    	
		if (DEBUG) System.out.println("Has aceptado la solicitud.");
    }

    @FXML
    void cancelar(ActionEvent event) {
		//ELIMINAR DE INVITACIONES RECIBIDAS (RECARGANDO LA PÁGINA)
    	NotificacionesController.invitaciones.remove(notificacion);
    	App.setRoot("notificaciones");
    	
		if (DEBUG) System.out.println("Has cancelado la solicitud.");
    }
}
