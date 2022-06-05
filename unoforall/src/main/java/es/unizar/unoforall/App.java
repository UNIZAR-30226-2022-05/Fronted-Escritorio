package es.unizar.unoforall;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import es.unizar.unoforall.api.RestAPI;
import es.unizar.unoforall.api.WebSocketAPI;
import es.unizar.unoforall.model.RespuestaLogin;
import es.unizar.unoforall.model.UsuarioVO;
import es.unizar.unoforall.model.salas.NotificacionSala;
import es.unizar.unoforall.utils.Pantalla;
import es.unizar.unoforall.utils.StringUtils;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.image.Image;
//import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import me.i2000c.web_utils.multicast_utils.MulticastClient;


/**
 * JavaFX App
 */
public class App extends Application {
	//VARIABLE BOOLEANA PARA MOSTRAR MENSAJES POR LA CONSOLA
	public static final boolean DEBUG = true;
	public static final boolean MODO_PRODUCCION = false;
	
	private static final String AZURE_IP = "unoforall.westeurope.cloudapp.azure.com";

    private static Scene scene;
    private static Stage stage;
    private static Pantalla pantallaActual;
    
	private static RespuestaLogin respLogin;
	private static UUID salaID;
	private static UUID usuarioID;
	private static HashMap<String, Integer> personalizacion;
	public static WebSocketAPI apiweb;
	private static Alert alertaInvitacionSala;
	
	private static MulticastClient client;
	
	static {
		if (MODO_PRODUCCION) {
			RestAPI.setServerIP(AZURE_IP);
		}
	}
	
    public static void main(String[] args) throws InterruptedException, ExecutionException {
    	launch();
        
    }
    
    @Override
    public void start(Stage s) throws IOException {
    	startMulticastClient();
    	
    	pantallaActual = Pantalla.LOGIN;
        scene = new Scene(loadFXML(pantallaActual.getFXML()));
        stage = s;
        Image icon = new Image(getClass().getResourceAsStream("images/logoUno.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.setTitle("UnoForAll");
        stage.show();
        setBounds();
        
        stage.setOnCloseRequest(event -> {
        	event.consume();
        	salir();
        });
    }
    
    public static void setBounds() {
        stage.sizeToScene();
        
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        
        stage.setMaxWidth(1920);
        stage.setMaxHeight(1080);
    }
    
    public static void setRoot(Pantalla pantalla) {
    	if(apiweb == null || apiweb.isClosed()) {
    		switch(pantalla) {
    			case LOGIN:
    			case CAMBIAR_IP:
    			case REGISTER:
    			case CONFIRMACION_CORREO:
    			case REESTABLECER_PASSWORD:
    			case ESPECIFICACION_CORREO:
    				break;
				default:
					pantalla = Pantalla.LOGIN;
    		}
    	}
    	
    	if(pantalla == Pantalla.LOGIN){
    		restartMulticastClient();
    	}else{
    		stopMulticastClient();
    	}
    	
        scene.setRoot(loadFXML(pantalla.getFXML()));
        pantallaActual = pantalla;
        setBounds();
        
    	if (pantalla == Pantalla.PARTIDA) {
    		stage.setResizable(false);
    	} else {
    		stage.setResizable(true);
    	}
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
    
    public static void setUsuarioID(UUID uID) {
    	usuarioID = uID;
    }
    public static UUID getUsuarioID() {
    	return usuarioID;
    }
    

    public static void initializePersonalizacion(Consumer<Boolean> consumer) {
    	RestAPI apirest = apiweb.getRestAPI();
		apirest.setOnError(e -> {if (DEBUG) System.out.println(e);});
		
		apirest.openConnection("/api/sacarUsuarioVO");
		apirest.receiveObject(UsuarioVO.class, retorno -> {
			if (retorno.isExito()) {
				personalizacion = new HashMap<String, Integer>();
				personalizacion.put("avatarSelec", retorno.getAvatar());
				personalizacion.put("cartaSelec", retorno.getAspectoCartas());
				personalizacion.put("tableroSelec", retorno.getAspectoTablero());
			} else {
				if (DEBUG) System.out.println("No se han podido autocompletar los datos de la cuenta");
			}
			consumer.accept(retorno.isExito());
		});
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
    
    public static void salir() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Cierre de Aplicación");
    	alert.setHeaderText("¡Estás a punto de cerrar la aplicación!");
    	alert.setContentText("¿Estás seguro?");
    	
    	if (alert.showAndWait().get() == ButtonType.OK) {
    		if(apiweb != null) {
    			apiweb.close();
    		}
    		if (DEBUG) System.out.println("Has salido de la aplicación correctamente");
    		stage.close();
    	}
    }
    
    public static void cerrarConexion() {
		App.apiweb.close();
    	apiweb = new WebSocketAPI();
    }
///////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////PRIVADAS////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////// 
    private static void gestionarInvitacionAmigo(UsuarioVO remitente) {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Solicitud de amistad");
    	alert.setHeaderText(StringUtils.parseString(remitente.getNombre()) + " te ha enviado una solicitud de amistad");
    	alert.setContentText("Puedes aceptar pulsando ACEPTAR o rechazar pulsando CANCELAR");

    	if (DEBUG) System.out.println("Solicitud recibida de: " + remitente);
    	
    	ButtonType respuesta = alert.showAndWait().get();
    	if (respuesta == ButtonType.OK) {

    		//ACEPTAR PETICION DE AMISTAD
    		RestAPI apirest = apiweb.getRestAPI();
    		apirest.addParameter("amigo", remitente.getId());
    		apirest.setOnError(e -> {if(DEBUG) System.out.println(e);});
    		
    		apirest.openConnection("/api/aceptarPeticionAmistad");
    		
    		apirest.receiveObject(String.class, error -> {
    			if (error != null) {
	    			if(DEBUG) System.out.println("error: " + error);
	    		}else {
	    			if (DEBUG) System.out.println("Has aceptado la solicitud.");
	    		}
    		});
    	} else if (respuesta == ButtonType.CANCEL) {
    		
    		//CANCELAR PETICION
    		RestAPI apirest = apiweb.getRestAPI();
    		apirest.addParameter("amigo", remitente.getId());
    		apirest.setOnError(e -> {if(DEBUG) System.out.println(e);});
    		
    		apirest.openConnection("/api/cancelarPeticionAmistad");
    		
    		apirest.receiveObject(String.class, error -> {
    			if (error != null) {
	    			if(DEBUG) System.out.println("error: " + error);
	    		}else {
	    			if (DEBUG) System.out.println("Has rechazado la solicitud.");
	    		}
    		});
    	} else {
    		if (DEBUG) System.out.println("La solicitud se ha enviado al menú de notificaciones.");
    	}
    }
    
    private static void gestionarInvitacionSala(NotificacionSala notif) {
    	switch (pantallaActual) {
    		case PARTIDA:
    		case SALA:
    		case SALA_PAUSADA :
	    		NotificacionesController.annadirInvitacionSala(notif);
    			break;
    		default :
    			if (alertaInvitacionSala != null) System.out.println("uno--> " + alertaInvitacionSala.toString());
    	    	if (alertaInvitacionSala != null) {
    	    		 for ( ButtonType bt : alertaInvitacionSala.getDialogPane().getButtonTypes() ) {
    	    	            System.out.println( "bt = " + bt );
    	    	            if ( bt.getButtonData() == ButtonData.CANCEL_CLOSE ) {
    	    	            	alertaInvitacionSala.setResult(ButtonType.CANCEL);
			                    Button cancelButton = ( Button ) alertaInvitacionSala.getDialogPane().lookupButton( bt );
			                    cancelButton.fire();
			                    if (alertaInvitacionSala != null) System.out.println("a--> " + alertaInvitacionSala.toString());
			                    break;
    	    	            }
    	    		 }
    	    	}
    	    	if (alertaInvitacionSala != null) System.out.println("dos--> " + alertaInvitacionSala.toString());
    	    	alertaInvitacionSala = new Alert(AlertType.CONFIRMATION);
    	    	alertaInvitacionSala.setTitle("Solicitud de unirse a sala");
    	    	alertaInvitacionSala.setHeaderText(StringUtils.parseString(notif.getRemitente().getNombre()) + " te ha invitado a una sala");
    	    	alertaInvitacionSala.setContentText("En caso de cancelación, la invitación seguirá estando disponible en el menú de notificaciones");
		
		    	if (DEBUG) System.out.println("Invitación de: " + StringUtils.parseString(notif.getRemitente().getNombre()) + " a la sala " + notif.getSalaID());
		    	if (alertaInvitacionSala != null) System.out.println("tres--> " + alertaInvitacionSala.toString());
		    	//ButtonType respuesta = alertaInvitacionSala.showAndWait().get();
		    	alertaInvitacionSala.show();
		    	//ButtonType respuesta = alertaInvitacionSala.getResult();
		    	alertaInvitacionSala.setOnCloseRequest((EventHandler<DialogEvent>) new EventHandler<DialogEvent>() {
		    	    @Override
		    	    public void handle(DialogEvent event) {
		    	        ButtonType respuesta=alertaInvitacionSala.getResult();
		
				    	if (respuesta == ButtonType.OK) {
				    		//UNIRSE A LA SALA
							App.setSalaID(notif.getSalaID());
							SuscripcionSala.unirseASala(App.getSalaID(), exito -> {
								if (exito) {
									App.setRoot(Pantalla.SALA);									
								} else {
									App.setRoot(Pantalla.PRINCIPAL);
								}
							});
								
					    	
				    		if (DEBUG) System.out.println("Has aceptado la solicitud.");
				    		
				    	} else if (respuesta == ButtonType.CANCEL) {
				    		NotificacionesController.annadirInvitacionSala(notif);
				    		if (DEBUG) System.out.println("Has rechazado la invitación");
				    	}
		    	        //result logic
		    	    }
		    	});
    	}
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
    
    private static void startMulticastClient(){
    	System.out.println("Buscando servidor...");
    	client = new MulticastClient();
    	client.startScan(listaIps -> {
    		if(!listaIps.isEmpty()){
    			String ip = listaIps.get(0).toString();
    			System.out.println("Servidor encontrado: " + ip);
    			RestAPI.setServerIP(ip);
    		}
    	});
    }
    private static void stopMulticastClient(){
    	if(client != null){
    		client.stopScan();
    	}
    }
    private static void restartMulticastClient(){
    	stopMulticastClient();
    	startMulticastClient();
    }

}