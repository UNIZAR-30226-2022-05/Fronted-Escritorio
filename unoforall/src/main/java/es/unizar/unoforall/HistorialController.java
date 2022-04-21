package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.ListaUsuarios;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.ListaPartidas;
import es.unizar.unoforall.model.partidas.Partida;
import es.unizar.unoforall.model.partidas.PartidaJugada;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class HistorialController implements Initializable{
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

    private static Image dfltImg = new Image(App.class.getResourceAsStream("images/social.png"));
    
    @FXML
    private ImageView icono;
    @FXML
    private Label labelError;
    @FXML
    private GridPane listaPartidas;
    @FXML
    private Label nombre;
    @FXML
    private Label pGanadas;
    @FXML
    private Label pJugadas;
    @FXML
    private Label puntos;
    
    @FXML
    void goBack(ActionEvent event) {
    	App.setRoot("principal");
    }

    @FXML
    void goToMain(MouseEvent event) {
    	App.setRoot("principal");
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//BUSCAR DATOS DE MI USUARIO
		RestAPI apirest = new RestAPI("/api/sacarUsuarioVO");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.setOnError(e -> {
			if (DEBUG) System.out.println(e);
			labelError.setText(StringUtils.parseString(e.toString()));
		});
		apirest.openConnection();
		UsuarioVO usuario = apirest.receiveObject(UsuarioVO.class);
		if (DEBUG) System.out.println("Tu ID de usuario es: " + usuario.getId());
		
		//PONER LA IMAGEN ADECUADA
		icono.setImage(dfltImg);

    	//ACTUALIZAR EL RESTO DE PARÁMETROS
    	nombre.setText(StringUtils.parseString(usuario.getNombre()));
    	pJugadas.setText("Jugadas: " + Integer.toString(usuario.getTotalPartidas()));
    	pGanadas.setText("Ganadas: " + Integer.toString(usuario.getNumVictorias()));
    	puntos.setText("Puntos: " + Integer.toString(usuario.getPuntos()));
    	
    	//BUSCAR DATOS DEL HISTORIAL DE PARTIDAS
    	apirest = new RestAPI("/api/sacarPartidasJugadas");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection();
		ListaPartidas partidas = apirest.receiveObject(ListaPartidas.class);
		
		//COMPROBAR SI HA HABIDO ALGÚN ERROR
		String error = partidas.getError();
		if (error.equals("null")) {
			
			for (PartidaJugada partida : partidas.getPartidas()) {
				try {
        	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("partidaItem.fxml"));
    	        	HBox salaItem = fxmlLoader.load();
    	        	
    	        	PartidaItemController partidaItemController = fxmlLoader.getController();
    	        	partidaItemController.setData(partida);
    	        	
        	        listaPartidas.addRow(listaPartidas.getRowCount(), salaItem);
    			} catch (IOException e) {
    				if (DEBUG) e.printStackTrace();
    			}
			}
			
		} else {
			labelError.setText(StringUtils.parseString(error));
			if (DEBUG) System.out.println(StringUtils.parseString(error));
		}
	}

}
