package es.unizar.unoforall;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.Participante;
import es.unizar.unoforall.model.partidas.PartidaJugada;
import es.unizar.unoforall.model.salas.ConfigSala;
import es.unizar.unoforall.utils.FechaUtils;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

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
    
    private Label[] posiciones;
    private ImageView[] iconos;
    private Label[] nombres;
    
	public void setData(PartidaJugada partida) {
		//INICIALIZAR LOS VECTORES
		posiciones = new Label[] {
			pos1, pos2, pos3, pos4
		};
		
		iconos = new ImageView[] {
			iconoJug1, iconoJug2, iconoJug3, iconoJug4
		};
		
		nombres = new Label[] {
			nomJug1, nomJug2, nomJug3, nomJug4
		};
		
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
		
		//CARGAR DATOS DE PARTIDA
		if(modo == ConfigSala.ModoJuego.Parejas.ordinal()){
			posiciones[0].setText("1º");
			posiciones[1].setText("1º");
			posiciones[2].setText("2º");
			posiciones[3].setText("2º");
			posiciones[0].setTextFill(Color.web("#D4AF37"));
			posiciones[1].setTextFill(Color.web("#D4AF37"));
			posiciones[2].setTextFill(Color.web("#C2C2C3"));
			posiciones[3].setTextFill(Color.web("#C2C2C3"));
		} else {
			posiciones[0].setText("1º");
			posiciones[1].setText("2º");
			posiciones[2].setText("3º");
			posiciones[3].setText("4º");
			posiciones[0].setTextFill(Color.web("#D4AF37"));
			posiciones[1].setTextFill(Color.web("#C2C2C3"));
			posiciones[2].setTextFill(Color.web("#CD7F32"));
			posiciones[3].setTextFill(Color.web("#6C78BD"));
		}
		
		for(Participante participante : partida.getParticipantes()) {
			int puesto = participante.getPuesto();
			UsuarioVO usuario = participante.getUsuario();
			String nombre;
			int avatar;
			if (usuario == null) {
				//ES UNA IA
				nombre = "IA";
				avatar = ImageManager.IA_IMAGE_ID;
			} else {
				nombre = usuario.getNombre();
				avatar = usuario.getAvatar();
			}
			nombres[puesto-1].setText(StringUtils.parseString(nombre));
			ImageManager.setImagenPerfil(iconos[puesto-1], avatar);
		}
	}

}
