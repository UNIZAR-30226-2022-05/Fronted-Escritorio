package es.unizar.unoforall;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.api.WebSocketAPI;
import es.unizar.unoforall.model.RespuestaLogin;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.salas.NotificacionSala;
import es.unizar.unoforall.utils.StringUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
//import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	private static final boolean DEBUG = true;

    private static Scene scene;
    private static Stage stage;
	private static String sesionID;
	private static RespuestaLogin respLogin;
	private static UUID salaID;
	private static UUID usuarioID;
	private static HashMap<String, Integer> personalizacion;
	public static WebSocketAPI apiweb;
	
	static {
		apiweb = new WebSocketAPI();
	}
    
    @Override
    public void start(Stage s) throws IOException {
        scene = new Scene(loadFXML("cambiarIP"));
        stage = s;
        Image icon = new Image(getClass().getResourceAsStream("images/logoUno.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setTitle("UnoForAll");
        stage.show();
        
        stage.setOnCloseRequest(event -> {
        	event.consume();
        	salir();
        });
    }   
    public static Scene getScene() {
    	return scene;
    }
    public static Stage getStage() {
    	return stage;
    }
    public static void setRespLogin(RespuestaLogin r) {
    	respLogin = r;
    }
    public static RespuestaLogin getRespLogin() {
    	return respLogin;
    }
    
    public static void setSalaID(UUID r) {
    	salaID = r;
    }
    public static UUID getSalaID() {
    	return salaID;
    }
    
    public static void setSessionID(String sID) {
    	sesionID = sID;
    }
    public static String getSessionID() {
    	return sesionID;
    }
    public static void setUsuarioID(UUID uID) {
    	usuarioID = uID;
    }
    public static UUID getUsuarioID() {
    	return usuarioID;
    }
    

    public static void initializePersonalizacion() {
    	RestAPI apirest = new RestAPI("/api/sacarUsuarioVO");
		String sesionID = App.getSessionID();
		apirest.addParameter("sesionID",sesionID);
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection();
		UsuarioVO retorno = apirest.receiveObject(UsuarioVO.class);
		if (retorno.isExito()) {
			personalizacion = new HashMap<String, Integer>();
			personalizacion.put("avatarSelec", retorno.getAvatar());
			personalizacion.put("cartaSelec", retorno.getAspectoCartas());
			personalizacion.put("tableroSelec", retorno.getAspectoTablero());
		} else {
			if (DEBUG) System.out.println("No se han podido autocompletar los datos de la cuenta");
		}
    }
    
    public static void setPersonalizacion(HashMap<String, Integer> p) {
    	personalizacion = p;
    }
    
    public static HashMap<String, Integer> getPersonalizacion() {
    	return personalizacion;
    }
    
    public static void activarNotificaciones() {
    	apiweb.subscribe("/topic/notifAmistad/" + respLogin.getUsuarioID(), UsuarioVO.class, remitente -> {
    		if (remitente != null) {
        		gestionarInvitacionAmigo(remitente);
    		}
    	});
    	apiweb.subscribe("/topic/notifSala/" + respLogin.getUsuarioID(), NotificacionSala.class, notif -> {
    		if (notif != null) {
        		gestionarInvitacionSala(notif);
    		}
    	});
    }
    
    private static void gestionarInvitacionAmigo(UsuarioVO remitente) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Solicitud de amistad");
    	alert.setHeaderText(remitente.getNombre() + " te ha enviado una solicitud de amistad");
    	alert.setContentText("Puedes aceptar pulsando ACEPTAR o rechazar pulsando CANCELAR");

    	if (DEBUG) System.out.println("Solicitud recibida de: " + remitente);
    	
    	ButtonType respuesta = alert.showAndWait().get();
    	if (respuesta == ButtonType.OK) {

    		//ACEPTAR PETICION DE AMISTAD
    		RestAPI apirest = new RestAPI("/api/aceptarPeticionAmistad");
    		apirest.addParameter("sesionID", App.getSessionID());
    		apirest.addParameter("amigo", remitente.getId());
    		apirest.setOnError(e -> {if(DEBUG) System.out.println(e);});
    		
    		apirest.openConnection();
    		
    		String error = apirest.receiveObject(String.class);
    		if (error != null) {
    			if(DEBUG) System.out.println("error: " + error);
    		}
    		
    		if (DEBUG) System.out.println("Has aceptado la solicitud.");
    		
    	} else if (respuesta == ButtonType.CANCEL) {
    		
    		//CANCELAR PETICION
    		RestAPI apirest = new RestAPI("/api/cancelarPeticionAmistad");
    		apirest.addParameter("sesionID", App.getSessionID());
    		apirest.addParameter("amigo", remitente.getId());
    		apirest.setOnError(e -> {if(DEBUG) System.out.println(e);});
    		
    		apirest.openConnection();
    		
    		String error = apirest.receiveObject(String.class);
    		if (error != null) {
    			if(DEBUG) System.out.println("error: " + error);
    		}
    		
    		if (DEBUG) System.out.println("Has rechazado la solicitud.");
    		
    	} else {
    		if (DEBUG) System.out.println("La solicitud se ha enviado al menú de notificaciones.");
    	}
    }
    
    private static void gestionarInvitacionSala(NotificacionSala notif) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Solicitud de amistad");
    	alert.setHeaderText(StringUtils.parseString(notif.getRemitente().getNombre()) + " te ha invitado a una sala");
    	alert.setContentText("En caso de cancelación, la invitación seguirá estando disponible en el menú de notificaciones");

    	if (DEBUG) System.out.println("Invitación de: " + notif.getRemitente() + " a la sala " + notif.getSalaID());
    	
    	ButtonType respuesta = alert.showAndWait().get();
    	if (respuesta == ButtonType.OK) {
    		
    		//UNIRSE A LA SALA
			App.setSalaID(notif.getSalaID());
		    VistaSalaController.deDondeVengo = "principal";
	    	App.setRoot("vistaSala");
	    	
    		if (DEBUG) System.out.println("Has aceptado la solicitud.");
    		
    	} else if (respuesta == ButtonType.CANCEL) {
    		NotificacionesController.annadirInvitacionSala(notif);
    		if (DEBUG) System.out.println("Has rechazado la invitación");
    	}
    }
    
    static void setRoot(String fxml) {
        scene.setRoot(loadFXML(fxml));

    	if (fxml.equals("login")) {
    		setWindowed();
    	}
    }
    
    static void setFullScreen() {
//	Para cambiar la tecla para salir de pantalla completa:
//    	stage.setFullScreenExitHint("Q para salir de pantalla completa");
//    	stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("Q"));
    	
//	Para imposibilitar salir de pantalla completa:
//    	stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
//    	stage.setFullScreen(true);
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
    }
    
    static void setWindowed() {
    	stage.setFullScreen(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
    }
    
    static void salir() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Cierre de Sesión");
    	alert.setHeaderText("¡Estás a punto de cerrar sesión!");
    	alert.setContentText("¿Estás seguro de querer salir de la aplicación?: ");
    	
    	if (alert.showAndWait().get() == ButtonType.OK) {
    		if (DEBUG) System.out.println("Has salido de la aplicación correctamente");
    		stage.close();
    	}
    }
    
    public static void cerrarConexion() {
		App.apiweb.close();
    	apiweb = new WebSocketAPI();
    }

    private static Parent loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent result = null;
        try {
        	result = fxmlLoader.load();
		} catch (IOException e) {
			if (DEBUG) e.printStackTrace();
		}
        return result;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
    	launch();
        
    }
}