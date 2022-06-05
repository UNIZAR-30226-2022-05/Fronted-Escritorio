package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.ListaPartidas;
import es.unizar.unoforall.model.partidas.PartidaJugada;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.Pantalla;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class HistorialController implements Initializable{
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;
	
	@FXML private VBox fondo;

	public static UsuarioVO usuario;
	@FXML private ImageView imgMenu;
    @FXML private ImageView icono;
    
    @FXML private GridPane listaPartidas;
    
    @FXML private Label nombre;
    @FXML private Label pGanadas;
    @FXML private Label pJugadas;
    @FXML private Label puntos;
    @FXML private Label labelError;
    
    @FXML
    void goBack(ActionEvent event) {
    	App.setRoot(Pantalla.PRINCIPAL);
    }

    @FXML
    void goToMain(MouseEvent event) {
    	App.setRoot(Pantalla.PRINCIPAL);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//PONER EL FONDO CORRESPONDIENTE
		fondo.setBackground(ImageManager.getBackgroundImage(App.getPersonalizacion().get("tableroSelec")));
		
		//PONER LA IMAGEN ADECUADA
		ImageManager.setImagenPerfil(icono, usuario.getAvatar());

    	//ACTUALIZAR EL RESTO DE PARÁMETROS
    	nombre.setText(StringUtils.parseString(usuario.getNombre()));
    	pJugadas.setText("Jugadas: " + Integer.toString(usuario.getTotalPartidas()));
    	pGanadas.setText("Ganadas: " + Integer.toString(usuario.getNumVictorias()));
    	puntos.setText("Puntos: " + Integer.toString(usuario.getPuntos()));
    	
    	//BUSCAR DATOS DEL HISTORIAL DE PARTIDAS
    	RestAPI apirest = App.apiweb.getRestAPI();
		apirest.addParameter("usuarioID", usuario.getId());
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection("/api/sacarPartidasJugadas");
		apirest.receiveObject(ListaPartidas.class, partidas -> {
			//COMPROBAR SI HA HABIDO ALGÚN ERROR
			String error = partidas.getError();
			if (error == null) {
				partidas.getPartidas().stream()
	            .map(PartidaJugada::getPartidaJugadaCompacta)
	            .sorted(((partidaJugadaCompacta1, partidaJugadaCompacta2) ->
	                            partidaJugadaCompacta2.getFechaInicio().compareTo(partidaJugadaCompacta1.getFechaInicio())))
	            .forEach(partida -> {
					try {
	        	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("partidaItem.fxml"));
	    	        	VBox salaItem = fxmlLoader.load();
	    	        	
	    	        	//CONFIGURACION DE EFECTO DE HOVER
	    	        	salaItem.setOnMouseEntered(new EventHandler<MouseEvent>() {
	    	    			@Override
	    	    			public void handle(MouseEvent event) {
	    	    				salaItem.setEffect(new Glow(0.3));
	    	    			}
	    	    		});
	    	    		salaItem.setOnMouseExited(new EventHandler<MouseEvent>() {
	    	    			@Override
	    	    			public void handle(MouseEvent event) {
	    	    				salaItem.setEffect(null);
	    	    			}
	    	    		});
	    	        	
	    	        	PartidaItemController partidaItemController = fxmlLoader.getController();
	    	        	partidaItemController.setData(partida);
	    	        	
	        	        listaPartidas.addRow(listaPartidas.getRowCount(), salaItem);
	    			} catch (IOException e) {
	    				if (DEBUG) e.printStackTrace();
	    			}
	            });
				
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
			});
			imgMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					imgMenu.setFitWidth(114);
					imgMenu.setFitHeight(100);
					imgMenu.setEffect(null);
				}
			});
		});
	}

}
