package es.unizar.unoforall;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ConfAspectoController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = App.DEBUG;

	private Integer avatarSelec;
	private Integer cartaSelec;
	private Integer tableroSelec;
	
	@FXML private VBox fondo;
	
	@FXML private ImageView imgMenu;
	
	@FXML private Label labelError;
	@FXML private Label labelPuntos;
	
	@FXML private ImageView avatar0;
	@FXML private ImageView avatar1;
	@FXML private ImageView avatar2;
	@FXML private ImageView avatar3;
	@FXML private ImageView avatar4;
	@FXML private ImageView avatar5;
	@FXML private ImageView avatar6;
	private ImageView[] avatares;
	private int[] puntuaciones = {
		0, 10, 30, 50, 100, 200, 500
	};
	private int puntuacionUsuario;

	@FXML private ImageView carta0;
	@FXML private ImageView carta1;
	private ImageView[] cartas;

	@FXML private ImageView tablero0;
	@FXML private ImageView tablero1;
	@FXML private ImageView tablero2;
	private ImageView[] tableros;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//OBTENER CONFIGURACION ACTUAL
		RestAPI apirest = App.apiweb.getRestAPI();
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection("/api/sacarUsuarioVO");
		apirest.receiveObject(UsuarioVO.class, usuario -> {
			if (usuario.isExito()) {
				mostrarDatosUsuario(usuario);
			}else {
				if (DEBUG) System.out.println("No se han podido autocompletar los datos de la cuenta");
			}
		});
	}
	
	private void mostrarDatosUsuario(UsuarioVO usuario) {
		puntuacionUsuario = usuario.getPuntos();
		labelPuntos.setText("Elige un icono de perfil: Tienes " + puntuacionUsuario + " puntos");
		
		avatares = new ImageView[] {
			avatar0, 
			avatar1, 
			avatar2, 
			avatar3,
			avatar4,
			avatar5,
			avatar6
		};

		cartas = new ImageView[] {
			carta0,
			carta1
		};

		tableros = new ImageView[] {
			tablero0,
			tablero1,
			tablero2
		};
		
		avatarSelec = usuario.getAvatar();
		cartaSelec = usuario.getAspectoCartas();
		tableroSelec = usuario.getAspectoTablero();
		
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.ALICEBLUE);
		dropShadow.setRadius(20.0);
		
		ColorAdjust colorAdjust = new ColorAdjust();
		colorAdjust.setBrightness(-0.3);
		colorAdjust.setContrast(-0.3);

		fondo.setBackground(ImageManager.getBackgroundImage(App.getPersonalizacion().get("tableroSelec")));
		
		
		//PONER SELECCIONADOS INICIALES
		//AVATARES
		avatares[avatarSelec].setEffect(dropShadow);
		//CARTAS
		cartas[cartaSelec].setEffect(dropShadow);
		//TABLEROS
		tableros[tableroSelec].setEffect(dropShadow);
		
		//IMPEDIR SELECCIONAR LOS QUE TENGAN PUNTUACIÓN NECESARIA MAYOR
		//AVATARES
		for (int i=0; i< avatares.length; i++) {
			if (puntuaciones[i] > puntuacionUsuario) {
				avatares[i].setEffect(colorAdjust);
				avatares[i].setDisable(true);
			}
		}
		avatares[avatarSelec].setEffect(dropShadow);
		//CARTAS
		cartas[cartaSelec].setEffect(dropShadow);
		//TABLEROS
		tableros[tableroSelec].setEffect(dropShadow);

		for(int i=0; i<avatares.length; i++){
			int avatar = i;
			avatares[i].setOnMouseClicked(event -> {
				for (int j = 0; j < avatares.length; j++) {
					if (puntuaciones[j] > puntuacionUsuario) {
						avatares[j].setEffect(colorAdjust);
						avatares[j].setDisable(true);
					} else {
						avatares[j].setEffect(null);
					}
				}
				avatares[avatar].setEffect(dropShadow);
				avatarSelec = avatar;
			});
			avatares[i].setOnMouseEntered(event -> {
				avatares[avatar].setFitWidth(80);
				avatares[avatar].setFitHeight(80);
			});
			avatares[i].setOnMouseExited(event -> {
				avatares[avatar].setFitWidth(70);
				avatares[avatar].setFitHeight(70);
			});
		}

		for(int i=0; i<tableros.length; i++){
			int tablero = i;
			tableros[i].setOnMouseClicked(event -> {
				for(int j=0; j<tableros.length; j++){
					tableros[j].setEffect(null);
				}
				tableros[tablero].setEffect(dropShadow);
				tableroSelec = tablero;
			});
			tableros[i].setOnMouseEntered(event -> {
				tableros[tablero].setFitWidth(160);
				tableros[tablero].setFitHeight(160);
			});
			tableros[i].setOnMouseExited(event -> {
				tableros[tablero].setFitWidth(150);
				tableros[tablero].setFitHeight(150);
			});
		}

		for(int i=0; i<cartas.length; i++){
			int tipoCartas = i;
			cartas[i].setOnMouseClicked(event -> {
				for(int j=0; j<cartas.length; j++){
					cartas[j].setEffect(null);
				}
				cartas[tipoCartas].setEffect(dropShadow);
				cartaSelec = tipoCartas;
			});
			cartas[i].setOnMouseEntered(event -> {
				cartas[tipoCartas].setFitWidth(135);
				cartas[tipoCartas].setFitHeight(135);
			});
			cartas[i].setOnMouseExited(event -> {
				cartas[tipoCartas].setFitWidth(125);
				cartas[tipoCartas].setFitHeight(125);
			});
		}

		//ASOCIAR EVENTOS DE AREA ENTERED A LAS IMÁGENES
		imgMenu.setOnMouseEntered(event -> {
			imgMenu.setFitWidth(160);
			imgMenu.setFitHeight(160);
			imgMenu.setEffect(new Glow(0.3));
		});
		imgMenu.setOnMouseExited(event -> {
			imgMenu.setFitWidth(150);
			imgMenu.setFitHeight(150);
			imgMenu.setEffect(null);
		});
	}

	@FXML
    private void goBack(ActionEvent event) {
	    App.setRoot("principal");
	}

	@FXML
    private void goToMain(Event event) {
	    App.setRoot("principal");
	}
	
	@FXML
    private void applyChanges(ActionEvent event) {
		labelError.setText("");
		
		RestAPI apirest = App.apiweb.getRestAPI();
		apirest.addParameter("avatar", avatarSelec);
		apirest.addParameter("aspectoCartas", cartaSelec);
		apirest.addParameter("aspectoFondo", tableroSelec);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection("/api/cambiarAvatar");
    	apirest.receiveObject(String.class, retorno -> {
    		if (retorno != null) {
	    		labelError.setText(StringUtils.parseString(retorno));
	    		if (DEBUG) System.out.println(StringUtils.parseString(retorno));
	    	} else {
	    		HashMap<String, Integer> personalizacion = new HashMap<String, Integer>();
				personalizacion.put("avatarSelec", avatarSelec);
				personalizacion.put("cartaSelec", cartaSelec);
				personalizacion.put("tableroSelec", tableroSelec);
	    		App.setPersonalizacion(personalizacion);
	    		
	    		App.setRoot("confAspecto");
	    		if (DEBUG) System.out.println("Configuración seleccionada: \n" +
						"\t Avatar: " + avatarSelec + "\n" +
						"\t Cartas: " + cartaSelec + "\n" +
						"\t Tablero: " + tableroSelec);
	    	}
    	});
	}
}

