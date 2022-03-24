package es.unizar.unoforall;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

//import es.unizar.unoforall.api.WebSocketAPI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    private static Object LOCK = new Object();
    
    @Override
    public void start(Stage s) throws IOException {
        scene = new Scene(loadFXML("login"));
        stage = s;
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setTitle("UnoForAll");
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
    	if (fxml.equals("principal")) {
    		setFullScreen();
    	}
    	else if (fxml.equals("login")) {
    		setWindowed();
    	}
        scene.setRoot(loadFXML(fxml));
    }
    
    static void setFullScreen() throws IOException {
    	stage.setFullScreen(true);
    }
    
    static void setWindowed() throws IOException {
    	stage.setFullScreen(false);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
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