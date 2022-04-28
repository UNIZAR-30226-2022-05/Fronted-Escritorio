package es.unizar.unoforall;

import java.util.HashMap;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PeticionItemController {
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
	private UsuarioVO usuario;
	boolean enviada;

    @FXML
    private Button botonAceptar;
    @FXML
    private Button botonCancelar;
    @FXML
    private ImageView icono;
    @FXML
    private Label nombre;

	public void setData(UsuarioVO usr, boolean env) {
    	usuario = usr;
    	enviada = env;
    	
    	if (env) {
    		botonAceptar.setVisible(false);
    		botonAceptar.setDisable(true);
    		botonCancelar.setVisible(false);
    		botonCancelar.setDisable(true);
    	}
    	
		//COMPROBAR QUÉ AVATAR TIENE PUESTO
    	icono.setImage(avatares.get(usuario.getAvatar()));
    	
    	//ACTUALIZAR EL RESTO DE PARÁMETROS
    	nombre.setText(usuario.getNombre());
	}

    @FXML
    void aceptar(ActionEvent event) {
		//ACEPTAR PETICION DE AMISTAD
		RestAPI apirest = new RestAPI("/api/aceptarPeticionAmistad");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.addParameter("amigo", usuario.getId());
		apirest.setOnError(e -> {if(DEBUG) System.out.println(e);});
		
		apirest.openConnection();
		
		String error = apirest.receiveObject(String.class);
		if (error != null) {
			if(DEBUG) System.out.println("error: " + error);
		} else {
			//ELIMINAR DE PETICIONES RECIBIDAS (RECARGANDO LA PÁGINA)
			App.setRoot("notificaciones");
		}
    }

    @FXML
    void cancelar(ActionEvent event) {
		//CANCELAR PETICION
		RestAPI apirest = new RestAPI("/api/cancelarPeticionAmistad");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.addParameter("amigo", usuario.getId());
		apirest.setOnError(e -> {if(DEBUG) System.out.println(e);});
		
		apirest.openConnection();
		
		String error = apirest.receiveObject(String.class);
		if (error != null) {
			if(DEBUG) System.out.println("error: " + error);
		} else {
			//ELIMINAR DE PETICIONES RECIBIDAS (RECARGANDO LA PÁGINA)
			App.setRoot("notificaciones");
		}
    }

}
