package es.unizar.unoforall.api;

import java.util.function.Consumer;

import me.i2000c.web_utils.client.RestClient;

public class RestAPI{
    protected static String SERVER_URL = "http://localhost";

    private RestClient restClient;

    public static void setServerIP(String serverIP){
        RestAPI.SERVER_URL = "http://" + serverIP;
    }

    public RestAPI(){
        this.restClient = new RestClient(SERVER_URL);

        restClient.setOnError(ex -> {
            ex.printStackTrace();
            close();
            System.err.println("RestAPI: Se ha producido un error de conexión");
        });
    }
    protected RestAPI(RestClient restClient){
        this.restClient = restClient;
    }

    public void setOnError(Consumer<Exception> onError){
        restClient.setOnError(onError);
    }

    public <T> void addParameter(String key, T value){
        restClient.addParameter(key, value);
    }

    public void openConnection(String path){
        restClient.openConnection(path);
    }

    public <T> void receiveObject(Class<T> requestedClass, Consumer<T> consumer){
        if(consumer == null){
            return;
        }

        restClient.receiveObject(requestedClass, consumer);
    }

    public void close(){
        restClient.close();
    }
}
