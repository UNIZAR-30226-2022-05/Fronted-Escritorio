package es.unizar.unoforall;

import java.text.DateFormat;
import java.util.HashMap;

import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.partidas.PartidaJugada;
import es.unizar.unoforall.utils.StringUtils;
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
		String fechaI = DateFormat.getTimeInstance().format(partida.getPartida().getFechaInicioPartida());
		String fechaF = DateFormat.getTimeInstance().format(partida.getPartida().getFechaFinPartida());
		fechaInicio.setText("Fecha de Inicio: " + fechaI);
		fechaFin.setText("Fecha de Fin: " + fechaF);
		
		Integer modo = partida.getPartida().getModoJuego();
		if (modo == 0) modoJuego.setImage(original);
		else if (modo == 1) modoJuego.setImage(attack);
		else modoJuego.setImage(parejas);
		
		//OCULTAR DATOS DE JUGADORES INEXISTENTES
		switch(partida.getParticipantes().size()){
	        case 2:
	            pos3.setVisible(false);			pos3.setDisable(true);
	            iconoJug3.setVisible(false);	iconoJug3.setDisable(true);
	            nomJug3.setVisible(false);		nomJug3.setDisable(true);
	        case 3:
	            pos4.setVisible(false);			pos4.setDisable(true);
	            iconoJug4.setVisible(false);	iconoJug4.setDisable(true);
	            nomJug4.setVisible(false);		nomJug4.setDisable(true);
	            break;
		}
		
		//RELLENAR DATOS DE JUGADORES
		//1o
		UsuarioVO usr = partida.getParticipantes().get(0).getUsuario();
		if (usr != null) {
			//ASIGNAR HUMANO
			nomJug1.setText(StringUtils.parseString(usr.getNombre()));
			iconoJug1.setImage(avatares.get(usr.getAvatar()));
		} else {
			//ASIGNAR IA
			nomJug1.setText("IA");
			iconoJug1.setImage(avatares.get(7));
		}

		//2do
		usr = partida.getParticipantes().get(1).getUsuario();
		if (usr != null) {
			//ASIGNAR HUMANO
			nomJug2.setText(StringUtils.parseString(usr.getNombre()));
			iconoJug2.setImage(avatares.get(usr.getAvatar()));
		} else {
			//ASIGNAR IA
			nomJug2.setText("IA");
			iconoJug2.setImage(avatares.get(7));
		}
		
		//3o
		if (partida.getParticipantes().size() > 2) {
			//3ero
			usr = partida.getParticipantes().get(2).getUsuario();
			if (usr != null) {
				//ASIGNAR HUMANO
				nomJug3.setText(StringUtils.parseString(usr.getNombre()));
				iconoJug3.setImage(avatares.get(usr.getAvatar()));
			} else {
				//ASIGNAR IA
				nomJug3.setText("IA");
				iconoJug3.setImage(avatares.get(7));
			} 
		}
		
		//4o
		if (partida.getParticipantes().size() > 3) {
			//3ero
			usr = partida.getParticipantes().get(3).getUsuario();
			if (usr != null) {
				//ASIGNAR HUMANO
				nomJug4.setText(StringUtils.parseString(usr.getNombre()));
				iconoJug4.setImage(avatares.get(usr.getAvatar()));
			} else {
				//ASIGNAR IA
				nomJug4.setText("IA");
				iconoJug4.setImage(avatares.get(7));
			} 
		}
	}

}
