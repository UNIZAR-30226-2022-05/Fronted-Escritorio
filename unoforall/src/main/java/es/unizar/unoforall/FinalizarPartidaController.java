package es.unizar.unoforall;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.Participante;
import es.unizar.unoforall.model.partidas.Partida;
import es.unizar.unoforall.model.partidas.PartidaJugadaCompacta;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.model.salas.ConfigSala;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class FinalizarPartidaController implements Initializable {
	
	public static final int SALIR = -1;
	public static final int CONTINUAR = 1;
	public static final int SALIR_CON_ESTILO = 2;
	
	private static int resultado;
    @FXML private VBox marco;
    
    @FXML private HBox jugador1;
    @FXML private HBox jugador2;
    @FXML private HBox jugador3;
    @FXML private HBox jugador4;
	private HBox[] jugadores;

    @FXML private ImageView avatarJugador1;
    @FXML private ImageView avatarJugador2;
    @FXML private ImageView avatarJugador3;
    @FXML private ImageView avatarJugador4;
    private ImageView[] avatares;

    @FXML private Label labelPuesto1;
    @FXML private Label labelPuesto2;
    @FXML private Label labelPuesto3;
    @FXML private Label labelPuesto4;

    @FXML private Label nombreJugador1;
    @FXML private Label nombreJugador2;
    @FXML private Label nombreJugador3;
    @FXML private Label nombreJugador4;
    private Label[] labelsNombreJugadores;

    @FXML private Label puntosGanados1;
    @FXML private Label puntosGanados2;
    @FXML private Label puntosGanados3;
    @FXML private Label puntosGanados4;
    private Label[] labelsPuntosGanados;

    @FXML private Button btnContinuar;
    @FXML private Button btnSalir;
    
    public List<Integer> listaAvatares = new ArrayList<Integer>();
    public List<String> listaNombres = new ArrayList<String>();
	
	private EventHandler<ActionEvent> siguientePantalla;
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//Handler que realiza una acción dependiendo de qué botón ha sido pulsado.
		//Tras pulsarse, está configurado para cerrar el popup.
		siguientePantalla = new EventHandler<ActionEvent>() {
	      	@Override 
	      	public void handle(ActionEvent actionEvent) {
	      		if(actionEvent.getSource().equals(btnContinuar)) {
	      			System.out.println("Has elegido continuar");
	      			resultado = CONTINUAR;
	      		} else if(actionEvent.getSource().equals(btnSalir)) {
					System.out.println("Has elegido Salir");
					int aux = abandonarPartida(null);
					if (aux == CONTINUAR) {
						return;
					} else if (aux == SALIR_CON_ESTILO) {
						resultado = SALIR_CON_ESTILO;
					} else {
	      			resultado = SALIR;
					}
	      			
	      		}
		      	// take some action
		      	//...
		      	//resultado = 3;
		      	// close the dialog.
				System.out.println("Has elegido " + resultado);
		      	Node  source = (Node)  actionEvent.getSource(); 
		      	Stage stage  = (Stage) source.getScene().getWindow();
		      	//Por alguna razón peta, ¿será realmente necesario?
		      	//stage.getOnCloseRequest().handle(null);
		      	stage.close();
	      	}
		};
		
		jugadores = new HBox[] {
			jugador1,
			jugador2, 
			jugador3,
			jugador4
		};

		avatares = new ImageView[] {
			avatarJugador1,
			avatarJugador2, 
			avatarJugador3,
			avatarJugador4
		};

		labelsNombreJugadores = new Label[] {
			nombreJugador1,
			nombreJugador2,
			nombreJugador3,
			nombreJugador4
		};
		
		labelsPuntosGanados = new Label[] {
			puntosGanados1,
			puntosGanados2,
			puntosGanados3,
			puntosGanados4
		};

		Sala sala = SuscripcionSala.sala;
		Partida partida = sala.getPartida();
		switch (partida.getJugadores().size()) {
			case 4:
				break;
			case 3:
				jugadores[3].setVisible(false);
				break;
			case 2:
				jugadores[3].setVisible(false);
				jugadores[2].setVisible(false);
				break;
		}

		if(sala.getConfiguracion().getModoJuego() == ConfigSala.ModoJuego.Parejas){
			labelPuesto1.setText("1º");
			labelPuesto2.setText("1º");
			labelPuesto3.setText("2º");
			labelPuesto4.setText("2º");
			labelPuesto1.setTextFill(Color.web("#D4AF37"));
			labelPuesto2.setTextFill(Color.web("#D4AF37"));
			labelPuesto3.setTextFill(Color.web("#C2C2C3"));
			labelPuesto4.setTextFill(Color.web("#C2C2C3"));
		} else {
			labelPuesto1.setText("1º");
			labelPuesto2.setText("2º");
			labelPuesto3.setText("3º");
			labelPuesto4.setText("4º");
			labelPuesto1.setTextFill(Color.web("#D4AF37"));
			labelPuesto2.setTextFill(Color.web("#C2C2C3"));
			labelPuesto3.setTextFill(Color.web("#CD7F32"));
			labelPuesto4.setTextFill(Color.web("#6C78BD"));
		}

		PartidaJugadaCompacta partidaJugada = sala.getUltimaPartidaJugada().getPartidaJugadaCompacta();
		for(int i = 0; i < partidaJugada.getParticipantes().size(); i++){
			Participante participante = partidaJugada.getParticipantes().get(i);
			int puesto = participante.getPuesto();
			int puntos = participante.getPuntos();
			UsuarioVO usuario = participante.getUsuario();
			int avatar;
			String nombre;
			if(usuario == null){
				// Es IA
				avatar = ImageManager.IA_IMAGE_ID;
				nombre = "IA";
				labelsPuntosGanados[puesto-1].setText(" ");
			} else {
				// Es un usuario de verdad
				avatar = usuario.getAvatar();
				nombre = usuario.getNombre();
				labelsPuntosGanados[puesto-1].setText(puntos + "");
			}
			
			ImageManager.setImagenPerfil(avatares[puesto-1], avatar);
			labelsNombreJugadores[puesto-1].setText(StringUtils.parseString(nombre));
		}
		
		btnContinuar.setOnAction(siguientePantalla);
		btnSalir.setOnAction(siguientePantalla);
	}
    
    public int getReturn() {
    	return resultado;
    }

	public int abandonarPartida(ActionEvent event) {
		ButtonType styledExit = new ButtonType("Salir con estilo");
		Alert alert = new Alert(AlertType.CONFIRMATION, "  ", styledExit, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Abandonar Sala");
        alert.setHeaderText("¿Seguro que quieres abandonar la sala?");
        alert.setContentText("Si sales, serás expulsado de la partida\n y tus amigos te llamarán cobarde");
        ButtonType respuesta = alert.showAndWait().get();
        if (respuesta == ButtonType.OK) {
            //Llamada a la clase de Sala para desubscribirse
            //SuscripcionSala.salirDeSalaDefinitivo();
            //Volver a la pantalla anterior
            System.out.println("Has abandonado la sala.");
			return SALIR;
        } else if (respuesta == styledExit) {
        	System.out.println("SORPRESA");
        	AudioClip buzzer = new AudioClip(getClass().getResource("images/styledExit.mp3").toExternalForm()); 
        	buzzer.play();
        	//SuscripcionSala.salirDeSalaDefinitivo();
        	return SALIR_CON_ESTILO;
        } else{
			return CONTINUAR;
		}
    }

}
