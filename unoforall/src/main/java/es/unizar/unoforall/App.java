package es.unizar.unoforall;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

//import es.unizar.unoforall.api.WebSocketAPI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    
    @Override
    public void start(Stage s) throws IOException {
        scene = new Scene(loadFXML("login"));
        stage = s;
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setTitle("UnoForAll");
        stage.show();
        
        stage.setOnCloseRequest(event -> {
        	event.consume();
        	logout();
        });
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        
    	if (fxml.equals("principal")) {
    		setFullScreen();
    	}
    	else if (fxml.equals("login")) {
    		setWindowed();
    	}
    }
    
    static void setFullScreen() throws IOException {
//	Para cambiar la tecla para salir de pantalla completa:
//    	stage.setFullScreenExitHint("Q para salir de pantalla completa");
//    	stage.setFullScreenExitKeyCombination(KeyCombination.valueOf("Q"));
    	
//	Para imposibilitar salir de pantalla completa:
//    	stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    	stage.setFullScreen(true);
        stage.setMinWidth(1280);
        stage.setMinHeight(720);
    }
    
    static void setWindowed() throws IOException {
    	stage.setFullScreen(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
    }
    
    static void logout() {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Cierre de Sesión");
    	alert.setHeaderText("¡Estás a punto de cerrar sesión!");
    	alert.setContentText("¿Estás seguro de querer salir de la aplicación?: ");
    	
    	if (alert.showAndWait().get() == ButtonType.OK) {
    		System.out.println("Has salido de la aplicación correctamente");
    		stage.close();
    	}
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
//    	test();
    	launch();
        
    }
    
//    public static void test() throws InterruptedException, ExecutionException {
//    	
//    	WebSocketAPI api = new WebSocketAPI();
//    	
//    	api.openConnection();
//    	
//    	//Nos suscribimos a un canal de mensajes en el que el servidor nos envia 
//    	//la respuesta tras el siguiente send
//    	//   tenemos que poner el tipo de dato de respueta; no se pueden recibir strings
//    	api.subscribe("/topic/greetings", Integer.class, i -> {
//    		System.out.println(i);
//    	
//    	});
//    	
//    	// se envía "ey", y el servidor nos responderá por /topic/greeting
//    	api.sendObject("/app/hello", "ey");
//    	
//    }

}