package es.unizar.unoforall;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import es.unizar.unoforall.api.WebSocketAPI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Object LOCK = new Object();
    
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
    	test();
    	launch();
        
    }
    
    public static void test() throws InterruptedException, ExecutionException {
    	
    	WebSocketAPI api = new WebSocketAPI();
    	
    	api.openConnection();
    	
    	//Nos suscribimos a un canal de mensajes en el que el servidor nos envia 
    	//la respuesta tras el siguiente send
    	//   tenemos que poner el tipo de dato de respueta; no se pueden recibir strings
    	api.subscribe("/topic/greetings", Integer.class, i -> {
    		System.out.println(i);
    	
    	});
    	
    	// se envía "ey", y el servidor nos responderá por /topic/greeting
    	api.sendObject("/app/hello", "ey");
    	
    }

}