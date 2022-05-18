package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class RobarOJugarCartaController implements Initializable{
    
	private static final int GUARDAR_CARTA = 0;
	private static final int JUGAR_CARTA = 0;
   
	@FXML private Button btnRobarCarta;
    @FXML private Button btnJugarCarta;
    @FXML private ImageView cartaRobada;
    
    private static int resultado;
	private EventHandler<ActionEvent> elegirColor;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Handler que realiza una acción dependiendo de qué botón ha sido pulsado.
		//Tras pulsarse, está configurado para cerrar el popup.
		elegirColor = new EventHandler<ActionEvent>() {
	      	@Override 
	      	public void handle(ActionEvent actionEvent) {
	      		if(actionEvent.getSource().equals(btnRobarCarta)) {
	      			System.out.println("Has clickado el botón guardar carta");
	      			resultado = GUARDAR_CARTA;
	      		} else if(actionEvent.getSource().equals(btnJugarCarta)) {
	      			System.out.println("Has clickado el botón jugar carta");
	      			resultado = JUGAR_CARTA;
	      		} else if(actionEvent.getSource().equals(btnAmarillo)) {
	      			System.out.println("Has clickado el botón amarillo");
	      			resultado = AMARILLO;
	      		} else if(actionEvent.getSource().equals(btnAzul)) {
	      			System.out.println("Has clickado el botón azul");
	      			resultado = AZUL; 
	      	  	} else if(actionEvent.getSource().equals(btnCancelar)) {
	      			System.out.println("Has clickado el botón de cancelar jugada"); 
	      		}
		      	// take some action
		      	//...
		      	//resultado = 3;
		      	// close the dialog.
		      	Node  source = (Node)  actionEvent.getSource(); 
		      	Stage stage  = (Stage) source.getScene().getWindow();
		      	//Por alguna razón peta, ¿será realmente necesario?
		      	//stage.getOnCloseRequest().handle(null);
		      	stage.close();
	      	}
		};
		
      	btnRojo.setOnAction(elegirColor);
      	btnVerde.setOnAction(elegirColor);
      	btnAmarillo.setOnAction(elegirColor);
      	btnAzul.setOnAction(elegirColor);
      	btnCancelar.setOnAction(elegirColor);		
	}
    
    public int getReturn() {
    	return resultado;
    }
}
