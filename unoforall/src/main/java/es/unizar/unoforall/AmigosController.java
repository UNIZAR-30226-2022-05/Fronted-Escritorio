package es.unizar.unoforall;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.model.ListaUsuarios;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.utils.StringUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AmigosController implements Initializable{
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;
	
	private static HashMap<Integer,Image> fondos = new HashMap<Integer, Image>();
	static {
		fondos.put(0, new Image(App.class.getResourceAsStream("images/fondos/azul.png")));
		fondos.put(1, new Image(App.class.getResourceAsStream("images/fondos/morado.png")));
		fondos.put(2, new Image(App.class.getResourceAsStream("images/fondos/gris.png")));
	}
	
	@FXML private VBox fondo;
    @FXML private ImageView imgMenu;
	
    @FXML private TextField cajaCorreoAmigo;
    @FXML private Label labelError;
    @FXML private GridPane listaAmigos;
    @FXML private GridPane listaEnviadas;
    
    private ArrayList<UsuarioVO> listaAmigosLocal = new ArrayList<UsuarioVO>();

    @FXML
    void goBack(ActionEvent event) {
    	App.setRoot("principal");
    }

    @FXML
    void goToMain(MouseEvent event) {
    	App.setRoot("principal");
    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//PONER EL FONDO CORRESPONDIENTE
		fondo.setBackground(
			new Background(
				new BackgroundImage(
					fondos.get(App.getPersonalizacion().get("tableroSelec")),
					BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT,
					BackgroundPosition.CENTER,
					BackgroundSize.DEFAULT
				)
			)
		);

		//CONFIGURACION DE EFECTO DE HOVER
		imgMenu.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(124);
				imgMenu.setFitHeight(110);
				imgMenu.setEffect(new Glow(0.3));
			}
		});;
		imgMenu.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				imgMenu.setFitWidth(114);
				imgMenu.setFitHeight(100);
				imgMenu.setEffect(null);
			}
		});;
		
		//BUSCAR AMIGOS
		String sesionID = App.getSessionID();
		
		RestAPI apirest = new RestAPI("/api/sacarAmigos");
		apirest.addParameter("sesionID", sesionID);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
    	
		apirest.openConnection();
		ListaUsuarios usuarios = apirest.receiveObject(ListaUsuarios.class);
		
		//COMPROBAR SI HA HABIDO ALGÚN ERROR
		String error = usuarios.getError();
		if (error.equals("null")) {
			
			for (UsuarioVO usuario : usuarios.getUsuarios()) {
				try {
        	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("amigoItem.fxml"));
    	        	HBox amigoItem = fxmlLoader.load();
    	        	
    	        	AmigoItemController amigoItemController = fxmlLoader.getController();
    	        	amigoItemController.setData(usuario, true);
    	        	
        	        listaAmigos.addRow(listaAmigos.getRowCount(), amigoItem);
        	        listaAmigosLocal.add(usuario);
        			
        			if (DEBUG) System.out.println("amigo encontrado:" + usuario.getCorreo());
    			} catch (IOException e) {
    				if (DEBUG) e.printStackTrace();
    			}
			}
			
		} else {
			labelError.setText(StringUtils.parseString(error));
			if (DEBUG) System.out.println(StringUtils.parseString(error));
		}
		
		//CARGAR LISTA DE PETICIONES ENVIADAS
		apirest = new RestAPI("/api/sacarPeticionesEnviadas");
		apirest.addParameter("sesionID", App.getSessionID());
		apirest.setOnError(e -> {
			if(DEBUG) System.out.println(e);
			labelError.setText(StringUtils.parseString(e.toString()));
		});
		
		apirest.openConnection();
    	ListaUsuarios enviadas = apirest.receiveObject(ListaUsuarios.class);
    	if(enviadas.isExpirado()) {
    		if(DEBUG) System.out.println("La sesión ha expirado.");
			labelError.setText("La sesión ha expirado.");
    	} else if (!enviadas.getError().equals("null")) {
    		if(DEBUG) System.out.println("Error en peticiones Enviadas: " + StringUtils.parseString(enviadas.getError()));
			labelError.setText("Error en peticiones Enviadas: " + StringUtils.parseString(enviadas.getError()));
    	} else {
    		for (UsuarioVO enviada : enviadas.getUsuarios()) {
    			try {
        	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("peticionItem.fxml"));
    	        	HBox salaItem = fxmlLoader.load();
    	        	
    	        	PeticionItemController peticionItemController = fxmlLoader.getController();
    	        	peticionItemController.setData(enviada, true);
    	        	
    	        	listaEnviadas.addRow(listaEnviadas.getRowCount(), salaItem);
        			
        			if (DEBUG) System.out.println("amigo encontrado:" + enviada.getCorreo());
    			} catch (IOException e) {
    				if (DEBUG) e.printStackTrace();
    			}
    		}
    	}
	}
    @FXML
    void cleanSearch(ActionEvent event) {
		labelError.setText("");
		//BORRAR RESULTADOS ANTERIORES DE LA VENTANA DE RESULTADOS
		listaAmigos.getChildren().clear();
		
		//INTRODUCIR LA LISTA LOCAL DE AMIGOS
		for (UsuarioVO usuario : listaAmigosLocal) {
			try {
    	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("amigoItem.fxml"));
	        	HBox amigoItem = fxmlLoader.load();
	        	
	        	AmigoItemController amigoItemController = fxmlLoader.getController();
	        	amigoItemController.setData(usuario, true);
	        	
    	        listaAmigos.addRow(listaAmigos.getRowCount(), amigoItem);
    			
    			if (DEBUG) System.out.println("amigo encontrado:" + usuario.getCorreo());
			} catch (IOException e) {
				if (DEBUG) e.printStackTrace();
			}
		}
    }

    @FXML
    void findFriend(ActionEvent event) {
		labelError.setText("");
		//BORRAR RESULTADOS ANTERIORES DE LA VENTANA DE RESULTADOS
		listaAmigos.getChildren().clear();
		
		//COMPROBAR SI SE HA INTRODUCIDO UN CORREO
		String correoAmigo = cajaCorreoAmigo.getText();
		if (correoAmigo.equals("")) {
			labelError.setText("Para buscar un amigo, introduce su correo");
			if (DEBUG) System.out.println("Para buscar un amigo, introduce su correo");
		} else {
			//BUSCAR AMIGO
			String sesionID = App.getSessionID();
			
			RestAPI apirest = new RestAPI("/api/buscarAmigo");
			apirest.addParameter("sesionID", sesionID);
			apirest.addParameter("amigo", correoAmigo);
			apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
	    	
			apirest.openConnection();
			ListaUsuarios usuarios = apirest.receiveObject(ListaUsuarios.class);
			
			//COMPROBAR SI HA HABIDO ALGÚN ERROR
			String error = usuarios.getError();
			if (error.equals("null")) {
				
				//EN CASO DE QUE LLEGUEN VARIOS (NUNCA DEBERÍA) SÓLO OBTENEMOS EL PRIMER USUARIO
				UsuarioVO usuario = usuarios.getUsuarios().get(0);
				if (usuarios.getUsuarios().size() > 1) {
        			if (DEBUG) System.out.println("Revisar Backend. Se devuelve más de un usuario");
				}
				
				try {
        	        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("amigoItem.fxml"));
    	        	HBox amigoItem = fxmlLoader.load();
    	        	
    	        	AmigoItemController amigoItemController = fxmlLoader.getController();
    	        	
    	        	//VERIFICAR SI ES AMIGO O NO
    	        	boolean esAmigo = false;
    	        	for (UsuarioVO u : listaAmigosLocal) {
    	        		esAmigo |= u.getCorreo().equals(usuario.getCorreo());
    	        	}
    	        	
    	        	amigoItemController.setData(usuario, esAmigo);
    	        	
        	        listaAmigos.addRow(listaAmigos.getRowCount(), amigoItem);
        			
        			if (DEBUG) System.out.println("amigo encontrado:" + usuario.getCorreo());
    			} catch (IOException e) {
    				if (DEBUG) e.printStackTrace();
    			}
				
			} else {
				labelError.setText(StringUtils.parseString(error));
				if (DEBUG) System.out.println(StringUtils.parseString(error));
			}
		}
    }

}

