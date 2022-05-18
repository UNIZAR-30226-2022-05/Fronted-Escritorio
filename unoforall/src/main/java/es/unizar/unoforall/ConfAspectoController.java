package es.unizar.unoforall;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.utils.ImageManager;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ConfAspectoController implements Initializable {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

	private Integer avatarSelec;
	private Integer cartaSelec;
	private Integer tableroSelec;
	
	@FXML private VBox fondo;
	
	@FXML private ImageView imgMenu;
	
	@FXML private Label labelError;
	
	@FXML private ImageView avatar0;
	@FXML private ImageView avatar1;
	@FXML private ImageView avatar2;
	@FXML private ImageView avatar3;
	@FXML private ImageView avatar4;
	@FXML private ImageView avatar5;
	@FXML private ImageView avatar6;
	private ImageView[] avatares;

	@FXML private ImageView carta0;
	@FXML private ImageView carta1;
	private ImageView[] cartas;

	@FXML private ImageView tablero0;
	@FXML private ImageView tablero1;
	@FXML private ImageView tablero2;
	private ImageView[] tableros;

	DropShadow dropShadow;
	

	public ConfAspectoController(){
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
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//OBTENER CONFIGURACION ACTUAL
		
		HashMap<String, Integer> personalizacion = App.getPersonalizacion();
		avatarSelec = personalizacion.get("avatarSelec");
		cartaSelec = personalizacion.get("cartaSelec");
		tableroSelec = personalizacion.get("tableroSelec");
		fondo.setBackground(ImageManager.getBackgroundImage(App.getPersonalizacion().get("tableroSelec")));

		inicializarEfectos();
		
		//PONER SELECCIONADOS INICIALES
		//AVATARES
		avatares[avatarSelec].setEffect(dropShadow);
		//CARTAS
		if(cartaSelec==0) carta0.setEffect(dropShadow);
		else carta1.setEffect(dropShadow);
		//TABLEROS
		switch(tableroSelec){
			case 0: tablero0.setEffect(dropShadow); break;
			case 1: tablero1.setEffect(dropShadow); break;
			case 2: tablero2.setEffect(dropShadow); break;
		}

		for(int i = 0; i < avatares.length; i++){
			int avatar = i;
			avatares[i].setOnMouseClicked(event -> {
				for (int j = 0; j < avatares.length; j++) {
					avatares[j].setEffect(null);
				}
				avatares[avatar].setEffect(dropShadow);
			});
		}
		
		for(int i = 0; i < avatares.length; i++){
			int avatar = i;
			avatares[i].setOnMouseClicked(event -> {				
				for (int j = 0; j < avatares.length; j++) {
					avatares[j].setEffect(null);
				}
				avatares[avatar].setEffect(dropShadow);
			});
		}
		
		carta0.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cartaSelec = 0;
				carta0.setEffect(dropShadow);
				carta1.setEffect(null);
			}
		});
		carta1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				cartaSelec = 1;
				carta1.setEffect(dropShadow);
				carta0.setEffect(null);
			}
		});

		tablero0.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tableroSelec = 0;
				tablero0.setEffect(dropShadow);
				tablero1.setEffect(null); tablero2.setEffect(null);
			}
		});
		tablero1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tableroSelec = 1;
				tablero1.setEffect(dropShadow);
				tablero0.setEffect(null); tablero2.setEffect(null);
			}
		});
		tablero2.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tableroSelec = 2;
				tablero2.setEffect(dropShadow);
				tablero0.setEffect(null); tablero1.setEffect(null);
			}
		});
		
		//ASOCIAR EVENTOS DE AREA ENTERED A LAS IMAGENES
		imgMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(210);
				imgMenu.setFitHeight(160);
				imgMenu.setEffect(new Glow(0.3));
			}
		});
		imgMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(200);
				imgMenu.setFitHeight(150);
				imgMenu.setEffect(null);
			}
		});
		
		avatar0.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar0.setFitWidth(110);
				avatar0.setFitHeight(110);
			}
		});
		avatar1.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar1.setFitWidth(110);
				avatar1.setFitHeight(110);
			}
		});
		avatar2.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar2.setFitWidth(110);
				avatar2.setFitHeight(110);
			}
		});
		avatar3.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar3.setFitWidth(110);
				avatar3.setFitHeight(110);
			}
		});
		avatar4.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar4.setFitWidth(110);
				avatar4.setFitHeight(110);
			}
		});
		avatar5.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar5.setFitWidth(110);
				avatar5.setFitHeight(110);
			}
		});
		avatar6.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar6.setFitWidth(110);
				avatar6.setFitHeight(110);
			}
		});		

		carta0.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				carta0.setFitWidth(210);
				carta0.setFitHeight(160);
			}
		});
		carta1.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				carta1.setFitWidth(210);
				carta1.setFitHeight(160);
			}
		});

		tablero0.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tablero0.setFitWidth(210);
				tablero0.setFitHeight(160);
			}
		});
		tablero1.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tablero1.setFitWidth(210);
				tablero1.setFitHeight(160);
			}
		});
		tablero2.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tablero2.setFitWidth(210);
				tablero2.setFitHeight(160);
			}
		});
		
		//ASOCIAR EVENTOS DE AREA EXITED A LAS IMAGENES
		avatar0.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar0.setFitWidth(100);
				avatar0.setFitHeight(100);
			}
		});
		avatar1.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar1.setFitWidth(100);
				avatar1.setFitHeight(100);
			}
		});
		avatar2.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar2.setFitWidth(100);
				avatar2.setFitHeight(100);
			}
		});
		avatar3.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar3.setFitWidth(100);
				avatar3.setFitHeight(100);
			}
		});
		avatar4.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar4.setFitWidth(100);
				avatar4.setFitHeight(100);
			}
		});
		avatar5.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar5.setFitWidth(100);
				avatar5.setFitHeight(100);
			}
		});
		avatar6.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				avatar6.setFitWidth(100);
				avatar6.setFitHeight(100);
			}
		});		

		carta0.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				carta0.setFitWidth(200);
				carta0.setFitHeight(150);
			}
		});
		carta1.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				carta1.setFitWidth(200);
				carta1.setFitHeight(150);
			}
		});

		tablero0.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tablero0.setFitWidth(200);
				tablero0.setFitHeight(150);
			}
		});
		tablero1.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tablero1.setFitWidth(200);
				tablero1.setFitHeight(150);
			}
		});
		tablero2.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				tablero2.setFitWidth(200);
				tablero2.setFitHeight(150);
			}
		});
		
	}

	private static void inicializarEfectos() {
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.ALICEBLUE);
		dropShadow.setRadius(20.0);
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
		
		RestAPI apirest = new RestAPI("/api/cambiarAvatar");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.addParameter("avatar", avatarSelec);
		apirest.addParameter("aspectoCartas", cartaSelec);
		apirest.addParameter("aspectoFondo", tableroSelec);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection();
    	String retorno = apirest.receiveObject(String.class);
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
	}
}

