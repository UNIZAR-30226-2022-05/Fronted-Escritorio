package es.unizar.unoforall;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class IntercambiarManoController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	public static final boolean DEBUG = App.DEBUG;
	
	@FXML private ImageView avatarJugador1;
    @FXML private ImageView avatarJugador2;
    @FXML private ImageView avatarJugador3;
    @FXML private ImageView avatarJugador4;
    private ImageView[] avatares;
    
    @FXML private Button btnJugador1;
    @FXML private Button btnJugador2;
    @FXML private Button btnJugador3;
    @FXML private Button btnJugador4;
    @FXML private Button btnCancelar;
	private Button[] botones;
    
    public List<Integer> listaAvatares = new ArrayList<Integer>();
    public List<String> listaNombres = new ArrayList<String>();
    
    public enum Resultado {
    	JUGADOR1, JUGADOR2, JUGADOR3, JUGADOR4,
    	CANCELAR;
    };
    private Resultado resultado;
	
	private EventHandler<ActionEvent> elegirJugador;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Handler que realiza una acción dependiendo de qué botón ha sido pulsado.
		//Tras pulsarse, está configurado para cerrar el popup.
		elegirJugador = new EventHandler<ActionEvent>() {
	      	@Override 
	      	public void handle(ActionEvent actionEvent) {
	      		if(actionEvent.getSource().equals(btnJugador1)) {
	      			if (DEBUG) System.out.println("Has elegido el jugador 1");
	      			resultado = Resultado.JUGADOR1;
	      		} else if(actionEvent.getSource().equals(btnJugador2)) {
					if (DEBUG) System.out.println("Has elegido el jugador 2");
	      			resultado = Resultado.JUGADOR2;
	      		} else if(actionEvent.getSource().equals(btnJugador3)) {
					if (DEBUG) System.out.println("Has elegido el jugador 3");
	      			resultado = Resultado.JUGADOR3;
	      		} else if(actionEvent.getSource().equals(btnJugador4)) {
					if (DEBUG) System.out.println("Has elegido el jugador 4");
	      			resultado = Resultado.JUGADOR4; 
	      	  	} else {
					resultado = Resultado.CANCELAR; 
					if (DEBUG) System.out.println("Has elegido no elegir");
	      		}
	      		
		      	Node  source = (Node)  actionEvent.getSource(); 
		      	Stage stage  = (Stage) source.getScene().getWindow();
		      	stage.close();
	      	}
		};
		
		avatares = new ImageView[] {
			avatarJugador1,
			avatarJugador2, 
			avatarJugador3,
			avatarJugador4
		};

		botones = new Button[] {
			btnJugador1,
			btnJugador2,
			btnJugador3,
			btnJugador4,
			btnCancelar
		};
		
		resultado = Resultado.CANCELAR;
		int i;
		for (i=0;i<listaAvatares.size();i++) {
			ImageManager.setImagenPerfil(avatares[i], listaAvatares.get(i));
			botones[i].setText(StringUtils.parseString(listaNombres.get(i)));
			botones[i].setOnAction(elegirJugador);
		}
		btnCancelar.setOnAction(elegirJugador);

		for (; i < 4; i++) {
			avatares[i].setVisible(false);
			avatares[i].setDisable(true);
			botones[i].setVisible(false);
			botones[i].setDisable(true);
		}
	}
    
    public int getReturn() {
    	return resultado.ordinal();
    }
}
