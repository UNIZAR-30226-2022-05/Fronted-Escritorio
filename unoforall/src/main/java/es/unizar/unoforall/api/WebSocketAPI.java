package es.unizar.unoforall.api;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class WebSocketAPI {
	
	private static final String SERVER_IP = "ws://localhost/gs-guide-websocket";

    public static final int GLOBAL_ERROR = 0;
    public static final int SUBSCRIPTION_ERROR = 1;

    private final Object LOCK = new Object();
    
    private final Map<String, Subscription> suscripciones;
    private WebSocketStompClient client;
    private StompSession sesion;
    private boolean closed;

    private Consumer<Throwable> onError;
    public void setOnError(Consumer<Throwable> onError){
        this.onError = onError;
    }

    
    public WebSocketAPI(){
        suscripciones = new HashMap<>();
        
        WebSocketClient c = new StandardWebSocketClient();
        client = new WebSocketStompClient(c);
        client.setMessageConverter(new MappingJackson2MessageConverter());
        sesion = null;
        closed = false;
        onError = t -> t.printStackTrace();
    }
    
    public void openConnection() throws InterruptedException, ExecutionException{
    	if(closed){
            return;
        }
    	
    	StompSessionHandler sessionHandler = new StompSessionHandler() {
			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
				synchronized (LOCK) {
					LOCK.notify();
				}
			}
			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
			}
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return String.class;
			}
			@Override
			public void handleException(StompSession session, StompCommand command, StompHeaders headers,
					byte[] payload, Throwable exception) {
			}
			@Override
			public void handleTransportError(StompSession session, Throwable exception) {
				onError.accept(exception);
			}
		};
    	
    	sesion = client.connect(SERVER_IP, sessionHandler).get();
		
		while(!sesion.isConnected()) {
			synchronized (LOCK) {
				LOCK.wait();
			}
		}
    }
    
    public <T> void subscribe(String topic, Class<T> expectedClass, Consumer<T> consumer){
    	if(closed){
            return;
        }
    	
    	if (expectedClass.equals(String.class)) {    		
    		throw new IllegalArgumentException("No se soporta la recepción de Strings");
    	}
    	
    	StompSessionHandler sessionHandler = new StompSessionHandler() {
			@Override
			public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
			}
			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				T dato = expectedClass.cast(payload);
				consumer.accept(dato);
			}
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return expectedClass;
			}
			@Override
			public void handleException(StompSession session, StompCommand command, StompHeaders headers,
					byte[] payload, Throwable exception) {
			}
			@Override
			public void handleTransportError(StompSession session, Throwable exception) {
				onError.accept(exception);
			}
		};
    	
    	Subscription s = sesion.subscribe(topic, sessionHandler);
    	suscripciones.put(topic, s);
    }
    
    public void unsubscribe(String topic){
    	if(closed){
            return;
        }
    	
    	Subscription suscripcion = suscripciones.remove(topic);
        if(suscripcion != null){
            suscripcion.unsubscribe();
        }
    }
    
    public <T> void sendObject(String topic, T object){
    	if(closed){
            return;
        }
    	
    	sesion.send(topic, object);
    }
    
    public void close(){
        if(closed){
           return;
        }
        
        sesion.disconnect();
        suscripciones.clear();
        closed = true;
    }
    
    
    @SuppressWarnings("deprecation")
	@Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
    
}
