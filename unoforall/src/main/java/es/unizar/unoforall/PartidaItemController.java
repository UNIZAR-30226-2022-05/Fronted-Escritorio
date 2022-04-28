package es.unizar.unoforall;

import java.text.DateFormat;
import java.util.HashMap;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.HaJugadoVO;
import es.unizar.unoforall.model.partidas.Participante;
import es.unizar.unoforall.model.partidas.PartidaJugada;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class PartidaItemController {

	private static HashMap<Integer,Image> avatares = new HashMap<Integer, Image>();
	static {
		avatares.put(0, new Image(App.class.getResourceAsStream("images/avatares/0-cero.png")));
		avatares.put(1, new Image(App.class.getResourceAsStream("images/avatares/1-uno.png")));
		avatares.put(2, new Image(App.class.getResourceAsStream("images/avatares/2-dos.png")));
		avatares.put(3, new Image(App.class.getResourceAsStream("images/avatares/3-tres.png")));
		avatares.put(4, new Image(App.class.getResourceAsStream("images/avatares/4-cuatro.png")));
		avatares.put(5, new Image(App.class.getResourceAsStream("images/avatares/5-cinco.png")));
		avatares.put(6, new Image(App.class.getResourceAsStream("images/avatares/6-seis.png")));
		avatares.put(7, new Image(App.class.getResourceAsStream("images/avatares/7-IA.png")));
	}

    private static Image original = new Image(App.class.getResourceAsStream("images/avatares/4-cuatro.png"));
    private static Image attack = new Image(App.class.getResourceAsStream("images/avatares/5-cinco.png"));
    private static Image parejas = new Image(App.class.getResourceAsStream("images/avatares/2-dos.png"));
	
    private PartidaJugada partida;
    
    @FXML private Label fechaFin;
    @FXML private Label fechaInicio;
    @FXML private ImageView modoJuego;

    @FXML private Label pos1;
	@FXML private ImageView iconoJug1;
    @FXML private Label nomJug1;

    @FXML private Label pos2;
	@FXML private ImageView iconoJug2;
    @FXML private Label nomJug2;

    @FXML private Label pos3;
	@FXML private ImageView iconoJug3;
    @FXML private Label nomJug3;

    @FXML private Label pos4;
	@FXML private ImageView iconoJug4;
    @FXML private Label nomJug4;
    
	public void setData(PartidaJugada p) {
		partida = p;
		//RELLENAR DATOS DE PARTIDA
		fechaInicio.setText("Fecha de Inicio: " + DateFormat.getDateInstance().format(partida.getPartida().getFechaInicioPartida()));
		fechaFin.setText("Fecha de Fin: " + DateFormat.getDateInstance().format(partida.getPartida().getFechaFinPartida()));
		
		Integer modo = partida.getPartida().getModoJuego();
		if (modo == 0) modoJuego.setImage(original);
		else if (modo == 1) modoJuego.setImage(attack);
		else modoJuego.setImage(parejas);
		
		//RELLENAR DATOS DE JUGADORES
		int restantes = partida.getPartida().getNumIas() + partida.getParticipantes().size();
		
		//1ero
		boolean humanoAsignado = false;
		for (Participante part : partida.getParticipantes()) {
			HaJugadoVO jug = part.getDatosPartida();
			UsuarioVO usr = part.getUsuario();
			if (jug.getUsrsDebajo() == restantes - 1) {
				//ASIGNAR HUMANO
				nomJug1.setText(usr.getNombre());
				iconoJug1.setImage(avatares.get(usr.getAvatar()));
				humanoAsignado = true;
				break;
			}
		}
		if (!humanoAsignado) {
			//ASIGNAR IA
			nomJug1.setText("IA");
			iconoJug1.setImage(avatares.get(7));
		}
		
		restantes--;
		
		//2do
		humanoAsignado = false;
		for (Participante part : partida.getParticipantes()) {
			HaJugadoVO jug = part.getDatosPartida();
			UsuarioVO usr = part.getUsuario();
			if (jug.getUsrsDebajo() == restantes - 1) {
				//ASIGNAR HUMANO
				nomJug2.setText(usr.getNombre());
				iconoJug2.setImage(avatares.get(usr.getAvatar()));
				humanoAsignado = true;
				break;
			}
		}
		if (!humanoAsignado) {
			//ASIGNAR IA
			nomJug2.setText("IA");
			iconoJug2.setImage(avatares.get(7));
		}
		restantes--;
		
		//3ero
		if (restantes > 0) {
			humanoAsignado = false;
			for (Participante part : partida.getParticipantes()) {
				HaJugadoVO jug = part.getDatosPartida();
				UsuarioVO usr = part.getUsuario();
				if (jug.getUsrsDebajo() == restantes - 1) {
					//ASIGNAR HUMANO
					nomJug3.setText(usr.getNombre());
					iconoJug3.setImage(avatares.get(usr.getAvatar()));
					humanoAsignado = true;
					break;
				}
			}
			if (!humanoAsignado) {
				//ASIGNAR IA
				nomJug3.setText("IA");
				iconoJug3.setImage(avatares.get(7));
			}
			restantes--;
		} else {
			nomJug3.setDisable(true);
			nomJug3.setVisible(false);
			iconoJug3.setDisable(true);
			iconoJug3.setVisible(false);
		}
		//4o
		if (restantes > 0) {
			humanoAsignado = false;
			for (Participante part : partida.getParticipantes()) {
				HaJugadoVO jug = part.getDatosPartida();
				UsuarioVO usr = part.getUsuario();
				if (jug.getUsrsDebajo() == restantes - 1) {
					//ASIGNAR HUMANO
					nomJug4.setText(usr.getNombre());
					iconoJug4.setImage(avatares.get(usr.getAvatar()));
					humanoAsignado = true;
					break;
				}
			}
			if (!humanoAsignado) {
				//ASIGNAR IA
				nomJug4.setText("IA");
				iconoJug4.setImage(avatares.get(7));
			}
			restantes--;
		} else {
			nomJug4.setDisable(true);
			nomJug4.setVisible(false);
			iconoJug4.setDisable(true);
			iconoJug4.setVisible(false);
		}
	}

}
