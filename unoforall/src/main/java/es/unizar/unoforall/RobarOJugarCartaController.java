package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import es.unizar.unoforall.model.partidas.Carta;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.MyStage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RobarOJugarCartaController implements Initializable{
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	public static final boolean DEBUG = App.DEBUG;
    
	private static final int ROBAR_CARTA = 0;
	private static final int JUGAR_CARTA = 1;
   
	@FXML private Button btnRobarCarta;
    @FXML private Button btnJugarCarta;
    @FXML private ImageView cartaRobada;
    private Carta carta;
    
    @FXML private VBox vb;
    MyStage myStage;
    private boolean defaultMode;
    private boolean isVisible;
    private static int resultado;
	private EventHandler<ActionEvent> robarOJugar;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Handler que realiza una acción dependiendo de qué botón ha sido pulsado.
		//Tras pulsarse, está configurado para cerrar el popup.
		defaultMode = App.getPersonalizacion().get("cartaSelec") == 0;
		isVisible = true;
		if (DEBUG) System.out.println(carta);
		ImageManager.setImagenCarta(cartaRobada, carta, defaultMode, isVisible);
		robarOJugar = new EventHandler<ActionEvent>() {
	      	@Override 
	      	public void handle(ActionEvent actionEvent) {
	      		if(actionEvent.getSource().equals(btnRobarCarta)) {
	      			if (DEBUG) System.out.println("Has clickado el botón robar carta");
	      			resultado = ROBAR_CARTA;
	      		} else if(actionEvent.getSource().equals(btnJugarCarta)) {
	      			if (DEBUG) System.out.println("Has clickado el botón jugar carta");
	      			resultado = JUGAR_CARTA;
	      		} 

		      	Node  source = (Node)  actionEvent.getSource(); 
		      	Stage stage  = (Stage) source.getScene().getWindow();
		      	stage.close();
	      	}
		};
		btnRobarCarta.setOnAction(robarOJugar);
		btnJugarCarta.setOnAction(robarOJugar);
	}
    
    public int getReturn() {
    	return resultado;
    }
    
    public void setCard(Carta card) {
    	this.carta = card;
    }

}
