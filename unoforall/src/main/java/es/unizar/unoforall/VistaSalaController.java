package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.salas.Sala;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class VistaSalaController implements Initializable {
	
	private String[] nombresBots = {"StrikkerFurro", "12000C", "Raul", "Vendo Mandarinas"};
	
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
		if (s.isNoExiste()) {
			try {
				System.out.println("Error al conectarse a la sala");
				App.apiweb.unsubscribe("/topic/salas/" + salaID);
				App.setRoot(deDondeVengo);
			} catch (Exception e) {
	    		System.out.println(e);
			}
		} else {
			System.out.println("Estado de la sala: " + s);
			
			if (s.isEnPartida()) {
				//CARGAR LA VISTA DE LA PARTIDA
				System.out.println("En partida");
			} else {
				//RECARGAR LA VISTA DE SALA
//				Map<UsuarioVO, Boolean> participantes = sala.getParticipantes();
//		        List<UsuarioVO> usuarios = new ArrayList<>(participantes.keySet());
//		        usuarios.sort(Comparator.comparing(UsuarioVO::getNombre));
				int numParticipantes = s.numParticipantes();
				HashMap<UsuarioVO, Boolean> participantes = s.getParticipantes();
				cargarParticipantes(numParticipantes, participantes);
			}
		}
	}
	
	private void cargarParticipantes(int numParticipantes, HashMap<UsuarioVO, Boolean> participantes) {
		if (numParticipantes == 1) {
			//VISIBLES
			if (!caja1.isVisible())	{caja1.setDisable(false); caja1.setVisible(true);}
			//INVISIBLES
			if (caja2.isVisible())	{caja2.setDisable(true); caja2.setVisible(false);}
			if (caja3.isVisible())	{caja3.setDisable(true); caja3.setVisible(false);}
			if (caja4.isVisible())	{caja4.setDisable(true); caja4.setVisible(false);}
		} else if (numParticipantes == 2) {
			//VISIBLES
			if (!caja1.isVisible())	{caja1.setDisable(false); caja1.setVisible(true);}
			if (!caja2.isVisible())	{caja2.setDisable(false); caja2.setVisible(true);}
			//INVISIBLES
			if (caja3.isVisible())	{caja3.setDisable(true); caja3.setVisible(false);}
			if (caja4.isVisible())	{caja4.setDisable(true); caja4.setVisible(false);}
		} else if (numParticipantes == 3) {
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
		Set<UsuarioVO> usuariosVO = participantes.keySet();
		
		//POR DEFECTO, PONER NOMBRES DE BOTS
		nomJug1.setText(nombresBots[0]);
		nomJug2.setText(nombresBots[1]);
		nomJug3.setText(nombresBots[2]);
		nomJug4.setText(nombresBots[3]);

		int i = 1;
		for (UsuarioVO u : usuariosVO) {
			String nombre = u.getNombre();
			boolean listo = participantes.get(u);
			if (i == 1) {	//EN LA CAJA 1
				//PONER NOMBRE DE USUARIO 1
				nomJug1.setText(nombre);
				//PONER ICONO DE USUARIO 1
				//PONER A LISTO USUARIO 1
				if (listo) System.out.println("Usuario 1 (" + nombre + ") listo");
			} else if (i == 2) {	//EN LA CAJA 2
				//PONER NOMBRE DE USUARIO 2
				nomJug2.setText(nombre);
				//PONER ICONO DE USUARIO 2
				//PONER A LISTO USUARIO 2
				if (listo) System.out.println("Usuario 2 (" + nombre + ") listo");
			} else if (i == 3) {	//EN LA CAJA 3
				//PONER NOMBRE DE USUARIO 3
				nomJug3.setText(nombre);
				//PONER ICONO DE USUARIO 3
				//PONER A LISTO USUARIO 3
				if (listo) System.out.println("Usuario 3 (" + nombre + ") listo");
			} else {	//EN LA CAJA 4
				//PONER NOMBRE DE USUARIO 4
				nomJug4.setText(nombre);
				//PONER ICONO DE USUARIO 4
				//PONER A LISTO USUARIO 4
				if (listo) System.out.println("Usuario 4 (" + nombre + ") listo");
			}
			i++;
		}
	}
	
	@FXML
    private void goBack(ActionEvent event) {
		try {
			UUID salaID = App.getSalaID();
			App.apiweb.sendObject("/app/salas/salir/" + salaID, "vacio");
			App.apiweb.unsubscribe("/topic/salas/" + salaID);
	    	App.setRoot(deDondeVengo);
		} catch (IOException e) {
			System.out.print(e);
		}
	}

	@FXML
    private void goToMain(Event event) {
		try {
			UUID salaID = App.getSalaID();
			App.apiweb.sendObject("/app/salas/salir/" + salaID, "vacio");
			App.apiweb.unsubscribe("/topic/salas/" + salaID);
	    	App.setRoot("principal");
		} catch (IOException e) {
			System.out.print(e);
		}
	}
	
	@FXML
    private void leaveRoom(ActionEvent event) {
		try {
			UUID salaID = App.getSalaID();
			App.apiweb.sendObject("/app/salas/salir/" + salaID, "vacio");
			App.apiweb.unsubscribe("/topic/salas/" + salaID);
	    	App.setRoot(deDondeVengo);
		} catch (IOException e) {
			System.out.print(e);
		}
	}
	
	@FXML
    private void ready(ActionEvent event) {
		UUID salaID = App.getSalaID();
		App.apiweb.sendObject("/app/salas/listo/" + salaID, "vacio");
	}
}
