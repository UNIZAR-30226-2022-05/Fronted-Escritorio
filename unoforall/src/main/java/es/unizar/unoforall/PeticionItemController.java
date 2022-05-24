package es.unizar.unoforall;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PeticionItemController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;

	private UsuarioVO usuario;
	boolean enviada;

    @FXML private Button botonAceptar;
    @FXML private Button botonCancelar;
    
    @FXML private ImageView icono;
    
    @FXML private Label nombre;

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
    	ImageManager.setImagenPerfil(icono, usuario.getAvatar());
    	
    	//ACTUALIZAR EL RESTO DE PARÁMETROS
    	nombre.setText(StringUtils.parseString(usuario.getNombre()));
	}

    @FXML
    private void aceptar(ActionEvent event) {
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
    private void cancelar(ActionEvent event) {
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
