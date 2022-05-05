package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.ListaPartidas;
import es.unizar.unoforall.model.partidas.PartidaJugada;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class HistorialController implements Initializable{
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	private static HashMap<Integer,Image> fondos = new HashMap<Integer, Image>();
	static {
		fondos.put(0, new Image(App.class.getResourceAsStream("images/fondos/azul.png")));
		fondos.put(1, new Image(App.class.getResourceAsStream("images/fondos/morado.png")));
		fondos.put(2, new Image(App.class.getResourceAsStream("images/fondos/gris.png")));
	}
	
	@FXML private VBox fondo;

	@FXML private ImageView imgMenu;
	
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

	public static UsuarioVO usuario;
	
    @FXML private ImageView icono;
    @FXML private GridPane listaPartidas;
    @FXML private Label nombre;
    @FXML private Label pGanadas;
    @FXML private Label pJugadas;
    @FXML private Label puntos;
    
    @FXML private Label labelError;
    
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
		//PONER EL FONDO CORRESPONDIENTE
		fondo.setBackground(
			new Background(
				new BackgroundImage(
					fondos.get(App.getPersonalizacion().get("tableroSelec")),
					BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER,
					BackgroundSize.DEFAULT
				)
			)
		);
		
		//PONER LA IMAGEN ADECUADA
		icono.setImage(avatares.get(usuario.getAvatar()));

    	//ACTUALIZAR EL RESTO DE PARÁMETROS
    	nombre.setText(StringUtils.parseString(usuario.getNombre()));
    	pJugadas.setText("Jugadas: " + Integer.toString(usuario.getTotalPartidas()));
    	pGanadas.setText("Ganadas: " + Integer.toString(usuario.getNumVictorias()));
    	puntos.setText("Puntos: " + Integer.toString(usuario.getPuntos()));
    	
    	//BUSCAR DATOS DEL HISTORIAL DE PARTIDAS
    	RestAPI apirest = new RestAPI("/api/sacarPartidasJugadas");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.addParameter("usuarioID", usuario.getId());
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection();
		ListaPartidas partidas = apirest.receiveObject(ListaPartidas.class);
		
		//COMPROBAR SI HA HABIDO ALGÚN ERROR
		String error = partidas.getError();
		if (error != null) {
			
			for (PartidaJugada partida : partidas.getPartidas()) {
				try {
        	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("partidaItem.fxml"));
    	        	VBox salaItem = fxmlLoader.load();
    	        	
    	        	//CONFIGURACION DE EFECTO DE HOVER
    	        	salaItem.setOnMouseEntered(new EventHandler<MouseEvent>() {
    	    			@Override
    	    			public void handle(MouseEvent event) {
    	    				salaItem.setEffect(new Glow(0.3));
    	    			}
    	    		});;
    	    		salaItem.setOnMouseExited(new EventHandler<MouseEvent>() {
    	    			@Override
    	    			public void handle(MouseEvent event) {
    	    				salaItem.setEffect(null);
    	    			}
    	    		});;
    	        	
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
		//CONFIGURACION DE EFECTO DE HOVER
		imgMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(124);
				imgMenu.setFitHeight(110);
				imgMenu.setEffect(new Glow(0.3));
			}
		});;
		imgMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(114);
				imgMenu.setFitHeight(100);
				imgMenu.setEffect(null);
			}
		});;
	}

}
