package es.unizar.unoforall;

import java.util.HashMap;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AmigoItemController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	//	private static final boolean DEBUG = true;
	
	private UsuarioVO usuario;

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
	
    @FXML
    private ImageView icono;
    @FXML
    private Label nombre;
    @FXML
    private Label pJugadas;
    @FXML
    private Label pGanadas;
    @FXML
    private Label puntos;
    
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
    	icono.setImage(avatares.get(usuario.getAvatar()));
    	
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