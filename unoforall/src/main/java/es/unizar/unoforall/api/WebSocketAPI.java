package es.unizar.unoforall.api;

import java.util.UUID;
import java.util.function.Consumer;

import me.i2000c.web_utils.client.RestClient;
import me.i2000c.web_utils.client.WebsocketClient;

public class WebSocketAPI {
    private WebsocketClient client;

    public void setOnError(Consumer<Exception> onError){
        client.setOnError(onError);
    }

    public WebSocketAPI(){
        this.client = new WebsocketClient(RestAPI.SERVER_URL);
        this.client.setOnError(ex -> {
            ex.printStackTrace();
            close();
        });
    }

    public UUID getSessionID(){
        return client.getSessionID();
    }

    public void openConnection(String path){
        if(isClosed()){
            gotoPantallaInicial();
        }

        client.openConnection(path);
    }

    public <T> void subscribe(String topic, Class<T> expectedClass, Consumer<T> consumer){
        if(isClosed()){
            gotoPantallaInicial();
        }

        if(consumer == null){
            return;
        }

        client.subscribe(topic, expectedClass, consumer);
    }

    public void unsubscribe(String topic){
        if(isClosed()){
            gotoPantallaInicial();
        }

        client.unsubscribe(topic);
    }

    public RestAPI getRestAPI() {
        if(isClosed()){
            gotoPantallaInicial();
        }

        RestClient restClient = client.getRestClient();
        if(restClient == null){
            restClient = new RestClient("");
            restClient.close();
        }

        RestAPI api = new RestAPI(restClient);
        if(isClosed()){
            api.close();
        }
        return api;
    }

    public boolean isClosed() {
        return client.isClosed();
    }

    public void close(){
        client.close();
    }

    private void gotoPantallaInicial(){
        // Volver a la pantalla de login
    }
}
