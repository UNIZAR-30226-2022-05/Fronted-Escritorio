package es.unizar.unoforall;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//import java.lang.reflect.Type;
//import java.util.Scanner;
//import java.util.concurrent.ExecutionException;
//
////import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaders;
//import org.springframework.messaging.simp.stomp.StompSession;
//import org.springframework.messaging.simp.stomp.StompSessionHandler;
//import org.springframework.messaging.simp.stomp.StompSession.Subscription;
//import org.springframework.web.socket.client.WebSocketClient;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.util.concurrent.ExecutionException;

import es.unizar.pruebaCliente.PruebaClienteApplication;




/**
 * JavaFX App
 */
public class App extends Application {
	private static Object LOCK = new Object();
	
    private static Scene scene;

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

    public static void main(String[] args) {
        launch();
        try {
			test();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
   public static void test() throws InterruptedException, ExecutionException {
	   PruebaClienteApplication.main(null);
	   
//		WebSocketClient client = new StandardWebSocketClient();
//
//		WebSocketStompClient stompClient = new WebSocketStompClient(client);
//		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//
//		StompSessionHandler sessionHandler = new StompSessionHandler() {
//			@Override
//			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
//				synchronized (LOCK) {
//					LOCK.notify();
//				}
//			}
//			@Override
//			public void handleFrame(StompHeaders headers, Object payload) {
//				String e = (String) payload;
//			    System.out.println(e);
//			}
//			@Override
//			public Type getPayloadType(StompHeaders headers) {
//				return String.class;
//			}
//			@Override
//			public void handleException(StompSession session, StompCommand command, StompHeaders headers,
//					byte[] payload, Throwable exception) {
//				// TODO Auto-generated method stub
//				
//			}
//			@Override
//			public void handleTransportError(StompSession session, Throwable exception) {
//				// TODO Auto-generated method stub
//				
//			}
//		};
//		StompSession sesion = stompClient.connect("ws://localhost/gs-guide-websocket", sessionHandler).get();
//		
//		while(!sesion.isConnected()) {
//			synchronized (LOCK) {
//				LOCK.wait();
//			}
//		}
//
//	    Subscription s = sesion.subscribe("/topic/greetings", sessionHandler);
//	    sesion.send("/app/hello", "buenos dias");
//	    System.out.println("mensaje enviado");
//	    
//		new Scanner(System.in).nextLine(); // Don't close immediately.
//		
//		sesion.disconnect();
   }

}