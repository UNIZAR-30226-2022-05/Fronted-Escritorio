package es.unizar.unoforall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


@SpringBootApplication
public class FrontendEscritorioApplication extends Application {

    public void start(Stage primaryStage) throws Exception{
    	GridPane formulario = new GridPane();
    	formulario.setAlignment(Pos.CENTER);
    	formulario.setHgap(10);
    	formulario.setVgap(10);
    	formulario.setPadding(new Insets(25, 25, 25, 25));

    	Scene scene = new Scene(formulario, 300, 275);
    	
    	Label emailLabel = new Label("Email");
    	formulario.add(emailLabel, 0, 3);
    	TextField email = new TextField();
    	formulario.add(email, 1, 3);
    	
    	Label passwordLabel = new Label("Password");
    	formulario.add(passwordLabel, 0, 4);
    	PasswordField password = new PasswordField();
    	formulario.add(password, 1, 04);
    	
    	Button btn = new Button("Registrarse");
    	HBox hbBtn = new HBox(10);
    	hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
    	hbBtn.getChildren().add(btn);
    	formulario.add(hbBtn, 1, 6);
    	
    	primaryStage.setTitle("Hello World!");
    	primaryStage.setScene(scene);
    	primaryStage.show();
    	//Parent root = FXMLLoader.load(getClass().getResource("hellofx.fxml"));
        
    }


    public static void main(String[] args) {
        launch(args);
    }
	//public static void main(String[] args) {
		//SpringApplication.run(FrontendEscritorioApplication.class, args);
	//}

}
