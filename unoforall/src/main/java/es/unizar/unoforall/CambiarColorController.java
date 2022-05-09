package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CambiarColorController implements Initializable{
    
    @FXML private Button btnRojo;
    @FXML private Button btnVerde;
    @FXML private Button btnCancelar;
    @FXML private Button btnAmarillo;
    @FXML private Button btnAzul;
    
    private static int resultado;
	private EventHandler elegirColor;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		elegirColor = new EventHandler<ActionEvent>() {
	      	  @Override public void handle(ActionEvent actionEvent) {
	      		  if(actionEvent.getSource().equals(btnRojo)) {
	      			System.out.println("Has clickado el botón rojo");
	      		  } else if(actionEvent.getSource().equals(btnVerde)) {
	      			System.out.println("Has clickado el botón verde");
	      		  } else if(actionEvent.getSource().equals(btnAmarillo)) {
	      			System.out.println("Has clickado el botón amarillo");
	      		  } else if(actionEvent.getSource().equals(btnAzul)) {
	      			System.out.println("Has clickado el botón azul");
	      		  } else if(actionEvent.getSource().equals(btnCancelar)) {
	      			System.out.println("Has clickado el botón de cancelar jugada"); 
	      		  }
	      	    // take some action
	      	    //...
	      		resultado = 3;
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
    
    public void haceCosas(ActionEvent actionEvent) {
    	
    	resultado = 2;
    	System.out.println("test cambiado");
    	/*new EventHandler<ActionEvent>() {
    	  @Override public void handle(ActionEvent actionEvent) {
    	    // take some action
    	    //...
    	    // close the dialog.
    	    Node  source = (Node)  actionEvent.getSource(); 
    	    Stage stage  = (Stage) source.getScene().getWindow();
    	    stage.getOnCloseRequest().handle(null);
    	    stage.close();
    	  }
    	};*/
    	//handle(actionEvent);
    }



}
