package es.unizar.unoforall;

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

    private static Image dfltImg = new Image(App.class.getResourceAsStream("images/social.png"));
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
    	}
    	
		//COMPROBAR QUÉ AVATAR TIENE PUESTO
    	icono.setImage(dfltImg);
    	
    	//ACTUALIZAR EL RESTO DE PARÁMETROS
    	nombre.setText(usuario.getNombre());
	}

    @FXML
    void aceptar(ActionEvent event) {
    	botonAceptar.setDisable(true); //Para no permitir pretar antes de procesar la acción
    	botonAceptar.setText("Aceptando...");
		
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
			//ELIMINAR DE PETICIONES RECIBIDAS
		}
		
		botonAceptar.setDisable(false); //Una vez recibida respuesta el botón sigue activo
    }

    @FXML
    void cancelar(ActionEvent event) {
    	botonCancelar.setDisable(true); //Para no permitir pretar antes de procesar la acción
		botonCancelar.setText("Cancelando...");
    	if (enviada) {
    		//CANCELAR ENVIO DE PETICION
    		//DE MOMENTO NO ESTOY SEGURO SI ES POSIBLE
    	} else {
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
				//ELIMINAR DE PETICIONES RECIBIDAS
			}
    	}
    	botonCancelar.setDisable(false); //Una vez recibida respuesta el botón sigue activo
    }

}
