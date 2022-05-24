package es.unizar.unoforall;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SeleccionarEmojisController implements Initializable{
    
	private static final int CANCELAR = -1;
    @FXML private Button btnCancelar;

    @FXML private ImageView emoji0;
    @FXML private ImageView emoji1;
    @FXML private ImageView emoji2;
    @FXML private ImageView emoji3;
    @FXML private ImageView emoji4;
	private ImageView emojis[];
    
    private static int resultado;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		resultado = CANCELAR;
      	
		emojis = new ImageView[] {
			emoji0,
			emoji1, 
			emoji2,
			emoji3,
			emoji4
		};

		btnCancelar.setOnAction(event -> {
			System.out.println("Has clickado el botón de cancelar"); 
			resultado = CANCELAR;
			Stage stage  = (Stage) ((Node)event.getSource()).getScene().getWindow();
			stage.close();
		});	
		  
		for (int i = 0; i < emojis.length; i++){
			int emoji = i;
			emojis[i].setOnMouseClicked(event -> {
				System.out.println("Has clickado el emoji " + emoji);
				resultado = emoji;
				Stage stage  = (Stage) ((Node)event.getSource()).getScene().getWindow();
				stage.close();
			});
		}
	}
    
    public int getReturn() {
    	return resultado;
    }
    
    public void startGlow(ActionEvent actionEvent) {
    	((Node) actionEvent.getSource()).setEffect(new Glow(0.3));
    }
    
    public void stopGlow(ActionEvent actionEvent) {
    	((Node) actionEvent.getSource()).setEffect(null);
    }
}
