package es.unizar.unoforall;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.salas.Sala;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class VistaSalaController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

	@FXML private Label labelError;
	private String[] nombresBots = {"StrikkerFurro", "I2000C", "Raúl", "Vendo Mandarinas"};
	
	private static Image ready = new Image(VistaSalaController.class.getResourceAsStream("images/ready.png"));
	private static Image notready = new Image(VistaSalaController.class.getResourceAsStream("images/notready.png"));
	@FXML private Button botonListo;

	@FXML private HBox caja1;
	@FXML private ImageView pfpJug1;
	@FXML private ImageView rdyIconJug1;
	@FXML private Label nomJug1;

	@FXML private HBox caja2;
	@FXML private ImageView pfpJug2;
	@FXML private ImageView rdyIconJug2;
	@FXML private Label nomJug2;

	@FXML private HBox caja3;
	@FXML private ImageView pfpJug3;
	@FXML private ImageView rdyIconJug3;
	@FXML private Label nomJug3;

	@FXML private HBox caja4;
	@FXML private ImageView pfpJug4;
	@FXML private ImageView rdyIconJug4;
	@FXML private Label nomJug4;
	
//	Por defecto deDondeVengo es la pantalla principal
//	para evitar posibles errores en ejecución
	public static String deDondeVengo = "principal";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		UUID salaID = App.getSalaID();
		App.apiweb.subscribe("/topic/salas/" + salaID, Sala.class, s -> actualizarSala(s, salaID));
		App.apiweb.sendObject("/app/salas/unirse/" + salaID, "vacio");
	}
	
	private void actualizarSala(Sala s, UUID salaID) {
		labelError.setText("");
		if (s.isNoExiste()) {
			labelError.setText(StringUtils.parseString(s.getError()));
			if (DEBUG) System.out.println(s.getError());
			App.apiweb.unsubscribe("/topic/salas/" + salaID);
			App.setRoot(deDondeVengo);
		} else {
			if (DEBUG) System.out.println("Estado de la sala: " + s);
			if (s.isEnPartida()) {
				//CARGAR LA VISTA DE LA PARTIDA
				if (DEBUG) System.out.println("En partida");
			} else {
				//RECARGAR LA VISTA DE SALA
				int tamanyo = s.getConfiguracion().getMaxParticipantes();
				HashMap<UsuarioVO, Boolean> participantes = s.getParticipantes();
				cargarParticipantes(tamanyo, participantes);
			}
		}
	}
	
	private void cargarParticipantes(int tamanyo, HashMap<UsuarioVO, Boolean> participantes) {
		if (tamanyo == 1) {
			//VISIBLES
			if (!caja1.isVisible())	{caja1.setDisable(false); caja1.setVisible(true);}
			//INVISIBLES
			if (caja2.isVisible())	{caja2.setDisable(true); caja2.setVisible(false);}
			if (caja3.isVisible())	{caja3.setDisable(true); caja3.setVisible(false);}
			if (caja4.isVisible())	{caja4.setDisable(true); caja4.setVisible(false);}
		} else if (tamanyo == 2) {
			//VISIBLES
			if (!caja1.isVisible())	{caja1.setDisable(false); caja1.setVisible(true);}
			if (!caja2.isVisible())	{caja2.setDisable(false); caja2.setVisible(true);}
			//INVISIBLES
			if (caja3.isVisible())	{caja3.setDisable(true); caja3.setVisible(false);}
			if (caja4.isVisible())	{caja4.setDisable(true); caja4.setVisible(false);}
		} else if (tamanyo == 3) {
			//VISIBLES
			if (!caja1.isVisible())	{caja1.setDisable(false); caja1.setVisible(true);}
			if (!caja2.isVisible())	{caja2.setDisable(false); caja2.setVisible(true);}
			if (!caja3.isVisible())	{caja3.setDisable(false); caja3.setVisible(true);}
			//INVISIBLES
			if (caja4.isVisible())	{caja4.setDisable(true); caja4.setVisible(false);}
		} else {
			//TODAS VISIBLES
			if (caja1.isVisible())	{caja1.setDisable(false); caja1.setVisible(true);}
			if (caja2.isVisible())	{caja2.setDisable(false); caja2.setVisible(true);}
			if (caja3.isVisible())	{caja3.setDisable(false); caja3.setVisible(true);}
			if (caja4.isVisible())	{caja4.setDisable(false); caja4.setVisible(true);}
		}
		List<UsuarioVO> usuariosVO = new ArrayList<>(participantes.keySet());
		usuariosVO.sort(Comparator.comparing(UsuarioVO::getNombre));
		
		//POR DEFECTO, PONER NOMBRES DE BOTS
		nomJug1.setText(StringUtils.parseString("Esperando Jugador 1"));
		nomJug2.setText(StringUtils.parseString("Esperando Jugador 2"));
		nomJug3.setText(StringUtils.parseString("Esperando Jugador 3"));
		nomJug4.setText(StringUtils.parseString("Esperando Jugador 4"));

		int i = 1;
		for (UsuarioVO u : usuariosVO) {
			String nombre = u.getNombre();
			boolean listo = participantes.get(u);
			if (i == 1) {	//EN LA CAJA 1
				//PONER NOMBRE DE USUARIO 1
				nomJug1.setText(StringUtils.parseString(nombre));
				//PONER ICONO DE USUARIO 1
				//PONER A LISTO USUARIO 1
				if (listo) {
					rdyIconJug1.setImage(ready);
					if (DEBUG) System.out.println("Usuario 1 (" + nombre + ") listo");
				} else {
					rdyIconJug1.setImage(notready);
				}
			} else if (i == 2) {	//EN LA CAJA 2
				//PONER NOMBRE DE USUARIO 2
				nomJug2.setText(StringUtils.parseString(nombre));
				//PONER ICONO DE USUARIO 2
				//PONER A LISTO USUARIO 2
				if (listo) {
					rdyIconJug2.setImage(ready);
					if (DEBUG) System.out.println("Usuario 2 (" + nombre + ") listo");
				} else {
					rdyIconJug2.setImage(notready);
				}
			} else if (i == 3) {	//EN LA CAJA 3
				//PONER NOMBRE DE USUARIO 3
				nomJug3.setText(StringUtils.parseString(nombre));
				//PONER ICONO DE USUARIO 3
				//PONER A LISTO USUARIO 3
				if (listo) {
					rdyIconJug3.setImage(ready);
					if (DEBUG) System.out.println("Usuario 3 (" + nombre + ") listo");
				} else {
					rdyIconJug3.setImage(notready);
				}
			} else {	//EN LA CAJA 4
				//PONER NOMBRE DE USUARIO 4
				nomJug4.setText(StringUtils.parseString(nombre));
				//PONER ICONO DE USUARIO 4
				//PONER A LISTO USUARIO 4
				if (listo) {
					rdyIconJug4.setImage(ready);
					if (DEBUG) System.out.println("Usuario 4 (" + nombre + ") listo");
				} else {
					rdyIconJug4.setImage(notready);
				}
			}
			i++;
		}
	}
	
	@FXML
    private void goBack(ActionEvent event) {
		UUID salaID = App.getSalaID();
		App.apiweb.sendObject("/app/salas/salir/" + salaID, "vacio");
		App.apiweb.unsubscribe("/topic/salas/" + salaID);
    	App.setRoot(deDondeVengo);
	}

	@FXML
    private void goToMain(Event event) {
		UUID salaID = App.getSalaID();
		App.apiweb.sendObject("/app/salas/salir/" + salaID, "vacio");
		App.apiweb.unsubscribe("/topic/salas/" + salaID);
    	App.setRoot("principal");
	}
	
	@FXML
    private void leaveRoom(ActionEvent event) {
		UUID salaID = App.getSalaID();
		App.apiweb.sendObject("/app/salas/salir/" + salaID, "vacio");
		App.apiweb.unsubscribe("/topic/salas/" + salaID);
    	App.setRoot(deDondeVengo);
	}
	
	@FXML
    private void ready(ActionEvent event) {
		UUID salaID = App.getSalaID();
		App.apiweb.sendObject("/app/salas/listo/" + salaID, "vacio");
		botonListo.setVisible(false);
		botonListo.setDisable(true);
	}
}
