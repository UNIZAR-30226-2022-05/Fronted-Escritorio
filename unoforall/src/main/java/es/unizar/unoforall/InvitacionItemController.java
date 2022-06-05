package es.unizar.unoforall;

import es.unizar.unoforall.model.salas.NotificacionSala;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.Pantalla;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class InvitacionItemController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;

	private NotificacionSala notificacion;
	
	@FXML private Button botonAceptar;
	@FXML private Button botonCancelar;
    @FXML private ImageView icono;
    @FXML private Label idSala;
    @FXML private Label nombre;
	
	public void setData(NotificacionSala notif) {
		notificacion = notif;
		//COMPROBAR QUÉ AVATAR TIENE PUESTO
		ImageManager.setImagenPerfil(icono, notificacion.getRemitente().getAvatar());
    	
    	//ACTUALIZAR EL RESTO DE PARÁMETROS
    	nombre.setText(StringUtils.parseString(notificacion.getRemitente().getNombre()));
    	idSala.setText(notificacion.getSalaID().toString());
	}

    @FXML
    private void aceptar(ActionEvent event) {
    	//UNIRSE A LA SALA
		App.setSalaID(notificacion.getSalaID());
		
		SuscripcionSala.unirseASala(notificacion.getSalaID(), exito -> {
			if(exito) {
				App.setRoot(Pantalla.SALA);
			}			
		});
    	
    	//ELIMINAR DE INVITACIONES RECIBIDAS
    	NotificacionesController.invitaciones.remove(notificacion);
    	
		if (DEBUG) System.out.println("Has aceptado la solicitud.");
    }

    @FXML
    private void cancelar(ActionEvent event) {
		//ELIMINAR DE INVITACIONES RECIBIDAS (RECARGANDO LA PÁGINA)
    	NotificacionesController.invitaciones.remove(notificacion);
    	App.setRoot(Pantalla.NOTIFICACIONES);
    	
		if (DEBUG) System.out.println("Has cancelado la solicitud.");
    }
}
