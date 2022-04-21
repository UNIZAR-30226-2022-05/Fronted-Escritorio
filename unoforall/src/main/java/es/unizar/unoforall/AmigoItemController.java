package es.unizar.unoforall;

import es.unizar.unoforall.api.RestAPI;
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
	private static final boolean DEBUG = true;
	
	private UsuarioVO usuario;

    private static Image dfltImg = new Image(App.class.getResourceAsStream("images/social.png"));
	
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
    private enum EstadoAmistad{EsAmigo, NoEsAmigo, PeticionEnviada};
    private EstadoAmistad estadoBoton;
    
    public void setData(UsuarioVO usr, boolean amigo) {
    	usuario = usr;

    	//PONER EL BOTON DE HACER AMIGO AL ESTADO CORRESPONDIENTE
    	if (amigo) {
    		estadoBoton = EstadoAmistad.EsAmigo;
    		botonHacerAmigo.setText("Ya es tu Amigo");
    		botonHacerAmigo.setDisable(true);
    	} else {
    		estadoBoton = EstadoAmistad.NoEsAmigo;
    		botonHacerAmigo.setText("Hacer Amigo");
    	}
    	
    	//COMPROBAR QUÉ AVATAR TIENE PUESTO
    	icono.setImage(dfltImg);
    	
    	//ACTUALIZAR EL RESTO DE PARÁMETROS
    	nombre.setText(StringUtils.parseString(usuario.getNombre()));
    	pJugadas.setText("Jugadas: " + Integer.toString(usuario.getTotalPartidas()));
    	pGanadas.setText("Ganadas: " + Integer.toString(usuario.getNumVictorias()));
    	puntos.setText("Puntos: " + Integer.toString(usuario.getPuntos()));
    }

    @FXML
    void verHistorial(ActionEvent event) {
    	//IR A PANTALLA DE HISTORIAL PARA ÉSTE USUARIO
    	if(DEBUG) System.out.println("IR A HISTORIAL");
    }
    
    @FXML
    void hacerAmigo(ActionEvent event) {
    	if (estadoBoton == EstadoAmistad.NoEsAmigo) {

        	//HACERSE AMIGO DE ÉSTE USUARIO
	    	estadoBoton = EstadoAmistad.PeticionEnviada;
			botonHacerAmigo.setText("Cancelar Petición");
			
			//ENVIAR PETICIÓN DE AMISTAD
	    	App.apiweb.sendObject("/app/notifAmistad/" + usuario.getId(), "vacio");
	    	
    	} else if (estadoBoton == EstadoAmistad.PeticionEnviada) {
    		
    		//CANCELAR LA PETICIÓN DE AMISTAD
    		botonHacerAmigo.setDisable(true); //Para no mandar varias cancelaciones
    		botonHacerAmigo.setText("Cancelando...");
    		
    		RestAPI apirest = new RestAPI("/api/cancelarPeticionAmistad");
			apirest.addParameter("sesionID", App.getSessionID());
			apirest.addParameter("amigo", usuario.getId());
			apirest.setOnError(e -> {if(DEBUG) System.out.println(e);});
			
			apirest.openConnection();
			
			String error = apirest.receiveObject(String.class);
			if (error != null) {
				if(DEBUG) System.out.println("error: " + error);
			} else {
				estadoBoton = EstadoAmistad.NoEsAmigo;
				botonHacerAmigo.setText("Hacer Amigo");
			}

    		botonHacerAmigo.setDisable(false); //Una vez recibida respuesta el botón sigue activo
    	}
    }

}