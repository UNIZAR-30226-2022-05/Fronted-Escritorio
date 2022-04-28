package es.unizar.unoforall;

import java.util.UUID;

import es.unizar.unoforall.interfaces.SalaListener;
import es.unizar.unoforall.model.salas.ConfigSala;
import es.unizar.unoforall.model.salas.ReglasEspeciales;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class SalaItemController {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
    private static Image original = new Image(App.class.getResourceAsStream("images/avatares/4-cuatro.png"));
    private static Image attack = new Image(App.class.getResourceAsStream("images/avatares/5-cinco.png"));
    private static Image parejas = new Image(App.class.getResourceAsStream("images/avatares/2-dos.png"));
    
    private SalaListener myListener;
    private UUID idSala;

    @FXML private ImageView modoJuego;
    @FXML private Label tamanno;
    @FXML private Label rayosX;
    @FXML private Label intercambio;
    @FXML private Label x2;
	@FXML private Label encadenar;
    @FXML private Label redirigir;
    @FXML private Label jugarVarias;
    @FXML private Label evitarEspecial;
    
    @FXML
    private void click (MouseEvent mouseEvent) {
    	myListener.onClickListener(idSala);
    }
    
    public void setData(ConfigSala config, UUID salaID, SalaListener listener) {
    	//INTERFAZ PARA COMUNICAR
    	myListener = listener;
    	
    	//ID DE LA SALA
    	idSala = salaID;
    	
    	//CONFIGURAR MODO DE JUEGO
    	if (config.getModoJuego() == ConfigSala.ModoJuego.Original) modoJuego.setImage(original);
    	else if (config.getModoJuego() == ConfigSala.ModoJuego.Attack) modoJuego.setImage(attack);
    	else if (config.getModoJuego() == ConfigSala.ModoJuego.Parejas) modoJuego.setImage(parejas);
    	else if (DEBUG) System.out.println("Modo de juego inválido: " + config.getModoJuego().name());
    	
    	//CONFIGURAR TAMAÑO DE SALA
    	tamanno.setText(config.getMaxParticipantes() + " Jugadores");
    	
    	//CONFIGURAR REGLAS ESPECIALES
    	ReglasEspeciales reglas = config.getReglas();
    	if (reglas.isCartaRayosX()) rayosX.setText("Rayos X: SI"); else rayosX.setText("Rayos X: NO");
    	if (reglas.isCartaIntercambio()) intercambio.setText("Intercambio: SI"); else intercambio.setText("Intercambio: NO");
    	if (reglas.isCartaX2()) x2.setText("x2: SI"); else x2.setText("x2: NO");
    	if (reglas.isEncadenarRoboCartas()) encadenar.setText("Encadenar: SI"); else encadenar.setText("Encadenar: NO");
    	if (reglas.isRedirigirRoboCartas()) redirigir.setText("Redirigir: SI"); else redirigir.setText("Redirigir: NO");
    	if (reglas.isJugarVariasCartas()) jugarVarias.setText("Jugar Varias: SI"); else jugarVarias.setText("Jugar Varias: NO");
    	if (reglas.isEvitarEspecialFinal()) evitarEspecial.setText("Evitar Especiales: SI"); else evitarEspecial.setText("Evitar Especiales: NO");
    }
 }
