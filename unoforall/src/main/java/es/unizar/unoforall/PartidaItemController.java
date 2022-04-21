package es.unizar.unoforall;

import es.unizar.unoforall.model.partidas.HaJugadoVO;
import es.unizar.unoforall.model.partidas.PartidaJugada;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PartidaItemController {
	private PartidaJugada partida;
    @FXML
    private Label fechaFin;
    @FXML
    private Label fechaInicio;
    @FXML
    private Label modoJuego;
	@FXML
    private ImageView iconoJug1;
    @FXML
    private Label nomJug1;
	@FXML
    private ImageView iconoJug2;
    @FXML
    private Label nomJug2;
	@FXML
    private ImageView iconoJug3;
    @FXML
    private Label nomJug3;
	@FXML
    private ImageView iconoJug4;
    @FXML
    private Label nomJug4;
	public void setData(PartidaJugada p) {
		partida = p;
		//RELLENAR DATOS DE PARTIDA
		
		//RELLENAR DATOS DE JUGADORES
		int restantes = partida.getPartida().getNumIas() + partida.getParticipantes().size();
		
		//1ero
		boolean humanoAsignado = false;
		for (HaJugadoVO jug : partida.getParticipantes()) {
			if (jug.getUsrsDebajo() == restantes - 1) {
				//ASIGNAR HUMANO
				humanoAsignado = true;
				break;
			}
		}
		if (!humanoAsignado); //ASIGNAR IA
		restantes--;
		
		//2do
		humanoAsignado = false;
		for (HaJugadoVO jug : partida.getParticipantes()) {
			if (jug.getUsrsDebajo() == restantes - 1) {
				//ASIGNAR HUMANO
				humanoAsignado = true;
				break;
			}
		}
		if (!humanoAsignado); //ASIGNAR IA
		restantes--;
		
		//3ero
		if (restantes > 0) {
			humanoAsignado = false;
			for (HaJugadoVO jug : partida.getParticipantes()) {
				if (jug.getUsrsDebajo() == restantes - 1) {
					//ASIGNAR HUMANO
					humanoAsignado = true;
					break;
				}
			}
			if (!humanoAsignado); //ASIGNAR IA
			restantes--;
		}
		//4o
		if (restantes > 0) {
			humanoAsignado = false;
			for (HaJugadoVO jug : partida.getParticipantes()) {
				if (jug.getUsrsDebajo() == restantes - 1) {
					//ASIGNAR HUMANO
					humanoAsignado = true;
					break;
				}
			}
			if (!humanoAsignado); //ASIGNAR IA
			restantes--;
		}
	}

}
