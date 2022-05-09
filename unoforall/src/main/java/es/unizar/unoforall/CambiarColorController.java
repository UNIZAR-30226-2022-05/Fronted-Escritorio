package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class CambiarColorController implements Initializable{
    
	private static final int CANCELAR = 0;
	private static final int ROJO = 1;
	private static final int VERDE = 2;
	private static final int AMARILLO = 3;
	private static final int AZUL = 4;
	
	
    @FXML private Button btnRojo;
    @FXML private Button btnVerde;
    @FXML private Button btnAmarillo;
    @FXML private Button btnAzul;
    @FXML private Button btnCancelar;
    
    @FXML private Circle colorRojo;
    @FXML private Circle colorVerde;
    @FXML private Circle colorAmarillo;
    @FXML private Circle colorAzul;
    
    private static int resultado;
	private EventHandler<ActionEvent> elegirColor;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resultado = CANCELAR;
		
		if (App.getPersonalizacion().get("cartaSelec") == 1) {
			colorRojo.setFill(Paint.valueOf("#20f3f3"));
			colorVerde.setFill(Paint.valueOf("#d851b0"));
			colorAmarillo.setFill(Paint.valueOf("#3948e9"));
			colorAzul.setFill(Paint.valueOf("#faaa1b"));
			
			btnRojo.setText("Azul claro");
			btnVerde.setText("Rosa");
			btnAmarillo.setText("Azul Oscuro");
			btnAzul.setText("Naranja");	
		}	
		//Handler que realiza una acción dependiendo de qué botón ha sido pulsado.
		//Tras pulsarse, está configurado para cerrar el popup.
		elegirColor = new EventHandler<ActionEvent>() {
	      	  @Override 
	      	  public void handle(ActionEvent actionEvent) {
	      		  if(actionEvent.getSource().equals(btnRojo)) {
	      			System.out.println("Has clickado el botón rojo");
	      			resultado = ROJO;
	      		  } else if(actionEvent.getSource().equals(btnVerde)) {
	      			System.out.println("Has clickado el botón verde");
	      			resultado = VERDE;
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
