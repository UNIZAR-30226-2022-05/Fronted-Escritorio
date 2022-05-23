package es.unizar.unoforall;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.PartidaJugada;
import es.unizar.unoforall.utils.FechaUtils;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PartidaItemController {
    
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
    
	public void setData(PartidaJugada partida) {
		//RELLENAR DATOS DE PARTIDA
		String fechaI = FechaUtils.formatDate(partida.getPartida().getFechaInicioPartida());
		String fechaF = FechaUtils.formatDate(partida.getPartida().getFechaFinPartida());
		fechaInicio.setText("Fecha de Inicio: " + fechaI);
		fechaFin.setText("Fecha de Fin: " + fechaF);
		
		Integer modo = partida.getPartida().getModoJuego();
		if (modo == 0) {
			ImageManager.setImagenPerfil(modoJuego, ImageManager.IMAGEN_PERFIL_4_ID);
		} else if(modo == 1) {
			ImageManager.setImagenPerfil(modoJuego, ImageManager.IMAGEN_PERFIL_5_ID);
		} else {
			ImageManager.setImagenPerfil(modoJuego, ImageManager.IMAGEN_PERFIL_2_ID);
		}
		
		//OCULTAR DATOS DE JUGADORES INEXISTENTES
		switch(partida.getParticipantes().size()){
	        case 2:
	            pos3.setVisible(false);			
	            pos3.setDisable(true);
	            iconoJug3.setVisible(false);	
	            iconoJug3.setDisable(true);
	            nomJug3.setVisible(false);		
	            nomJug3.setDisable(true);
	        case 3:
	            pos4.setVisible(false);			
	            pos4.setDisable(true);
	            iconoJug4.setVisible(false);	
	            iconoJug4.setDisable(true);
	            nomJug4.setVisible(false);		
	            nomJug4.setDisable(true);
	            break;
		}
		
		//RELLENAR DATOS DE JUGADORES
		//1o
		UsuarioVO usr = partida.getParticipantes().get(0).getUsuario();
		if (usr != null) {
			//ASIGNAR HUMANO
			nomJug1.setText(StringUtils.parseString(usr.getNombre()));
			ImageManager.setImagenPerfil(iconoJug1, usr.getAvatar());
		} else {
			//ASIGNAR IA
			nomJug1.setText("IA");
			ImageManager.setImagenPerfil(iconoJug1, ImageManager.IA_IMAGE_ID);
		}

		//2do
		if (partida.getPartida().getModoJuego() == 2) {
			//SI ES POR PAREJAS, TAMBIÉN QUEDA EN PRIMER LUGAR
			pos2.setText("1º");
		}
		
		usr = partida.getParticipantes().get(1).getUsuario();
		if (usr != null) {
			//ASIGNAR HUMANO
			nomJug2.setText(StringUtils.parseString(usr.getNombre()));
			ImageManager.setImagenPerfil(iconoJug2, usr.getAvatar());
		} else {
			//ASIGNAR IA
			nomJug2.setText("IA");
			ImageManager.setImagenPerfil(iconoJug2, ImageManager.IA_IMAGE_ID);
		}
		
		//3o
		if (partida.getPartida().getModoJuego() == 2) {
			//SI ES POR PAREJAS, QUEDA EN SEGUNDO LUGAR
			pos3.setText("2º");
		}
		
		if (partida.getParticipantes().size() > 2) {
			//3ero
			usr = partida.getParticipantes().get(2).getUsuario();
			if (usr != null) {
				//ASIGNAR HUMANO
				nomJug3.setText(StringUtils.parseString(usr.getNombre()));
				ImageManager.setImagenPerfil(iconoJug3, usr.getAvatar());
			} else {
				//ASIGNAR IA
				nomJug3.setText("IA");
				ImageManager.setImagenPerfil(iconoJug3, ImageManager.IA_IMAGE_ID);
			} 
		}
		
		//4o
		if (partida.getPartida().getModoJuego() == 2) {
			//SI ES POR PAREJAS, TAMBIÉN QUEDA EN SEGUNDO LUGAR
			pos4.setText("2º");
		}
		if (partida.getParticipantes().size() > 3) {
			//3ero
			usr = partida.getParticipantes().get(3).getUsuario();
			if (usr != null) {
				//ASIGNAR HUMANO
				nomJug4.setText(StringUtils.parseString(usr.getNombre()));
				ImageManager.setImagenPerfil(iconoJug4, usr.getAvatar());
			} else {
				//ASIGNAR IA
				nomJug4.setText("IA");
				ImageManager.setImagenPerfil(iconoJug4, ImageManager.IA_IMAGE_ID);
			} 
		}
	}

}
