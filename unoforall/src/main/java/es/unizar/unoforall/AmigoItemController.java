package es.unizar.unoforall;


import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class AmigoItemController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	//	private static final boolean DEBUG = true;
	
	private UsuarioVO usuario;
	
    @FXML private ImageView icono;
    @FXML private Label nombre;
    @FXML private Label pJugadas;
    @FXML private Label pGanadas;
    @FXML private Label puntos;
    
    @FXML
    private Button botonHacerAmigo;
    
    public void setData(UsuarioVO usr, boolean amigo) {
    	usuario = usr;

    	//PONER EL BOTON DE HACER AMIGO AL ESTADO CORRESPONDIENTE
    	if (amigo) {
    		botonHacerAmigo.setText("Ya es tu Amigo");
    		botonHacerAmigo.setDisable(true);
    	} else {
    		botonHacerAmigo.setText("Hacer Amigo");
    	}
    	
    	//COMPROBAR QUÉ AVATAR TIENE PUESTO
		ImageManager.setImagenPerfil(icono, usuario.getAvatar());
    	
    	//ACTUALIZAR EL RESTO DE PARÁMETROS
    	nombre.setText(StringUtils.parseString(usuario.getNombre()));
    	pJugadas.setText("Jugadas: " + Integer.toString(usuario.getTotalPartidas()));
    	pGanadas.setText("Ganadas: " + Integer.toString(usuario.getNumVictorias()));
    	puntos.setText("Puntos: " + Integer.toString(usuario.getPuntos()));
    }

    @FXML
    void verHistorial(ActionEvent event) {
		//PASAR EL USUARIO A LA VENTANA DE HISTORIAL
		HistorialController.usuario = usuario;
		App.setRoot("historial");
    }
    
    @FXML
    void hacerAmigo(ActionEvent event) {
		//ENVIAR PETICIÓN DE AMISTAD
    	App.apiweb.sendObject("/app/notifAmistad/" + usuario.getId(), "vacio");
		botonHacerAmigo.setDisable(true);
		botonHacerAmigo.setText("Petición enviada");
    }

}