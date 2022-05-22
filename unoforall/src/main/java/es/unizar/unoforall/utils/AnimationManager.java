package es.unizar.unoforall.utils;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AnimationManager {
	
    private static RotateTransition rtHorario;
    private static RotateTransition rtAntihorario; 
    private static RotateTransition rtRapida;
    private static ScaleTransition agrandar;
    private static ScaleTransition disminuir;
    private static FadeTransition fadeOut;
    
    
    public static void inicializarAnimacionesSentido() {
	    //Animación rotación de nodo en sentido Horario	
		rtHorario = new RotateTransition(Duration.seconds(6));
		rtHorario.setFromAngle(0);
		rtHorario.setToAngle(360);
		rtHorario.setCycleCount(Animation.INDEFINITE);
		rtHorario.setInterpolator(Interpolator.LINEAR);
		
		//Animación rotación de nodo en sentido Antihorario
		rtAntihorario = new RotateTransition(Duration.seconds(6));
		rtAntihorario.setFromAngle(360);
		rtAntihorario.setToAngle(0);
		rtAntihorario.setCycleCount(Animation.INDEFINITE);
		rtAntihorario.setInterpolator(Interpolator.LINEAR);
		
		//Animación rotación de nodo rápida, 2 vueltas
		rtRapida = new RotateTransition(Duration.millis(500));
		rtRapida.setFromAngle(0);
		rtRapida.setToAngle(720);
		rtRapida.setInterpolator(Interpolator.LINEAR);
		
		//Animación agrandar y disminuir nodo
		agrandar = new ScaleTransition(Duration.millis(250));
		agrandar.setToX(1.5);
		agrandar.setToY(1.5);
		agrandar.setOnFinished(event-> disminuir.play());
	
		disminuir = new ScaleTransition(Duration.millis(250));
		disminuir.setToX(1);
		disminuir.setToY(1);
    }
    public static void setAnimacionSentido(ImageView imageView, boolean sentidoHorario) {
		//Inicializar animación inicial a la imagen del sentido de la partida
		if(sentidoHorario) {
			rtHorario.setNode(imageView);
			rtHorario.play();
		} else {
			rtAntihorario.setNode(imageView);
			rtAntihorario.play();
		}
    }
  
    //Transición de rotación en sentido Antihorario a sentido Horario
    public static void AntihorarioAHorario(ImageView imageView) {
		rtAntihorario.stop();
    	rtRapida.setOnFinished(event -> rtHorario.play());
		rtHorario.setNode(imageView);
		rtRapida.setNode(imageView);
		agrandar.setNode(imageView);
		disminuir.setNode(imageView);
		
		rtRapida.play();
		agrandar.play();
    }
    
    //Transición de rotación en sentido Antihorario a sentido Horario
    public static void HorarioAAntihorario(ImageView imageView) {
    	rtHorario.stop();
		rtRapida.setOnFinished(event -> rtAntihorario.play());
		rtAntihorario.setNode(imageView);
		rtRapida.setNode(imageView);
		agrandar.setNode(imageView);
		disminuir.setNode(imageView);
		
		rtRapida.play();
		agrandar.play();
    }

	public static boolean sentidoIsNotRunning() {
		return (rtHorario.getCurrentRate()!=0.0d || rtAntihorario.getCurrentRate()!=0.0d);
	}
	
	public static void fadeErrorEscalera (Node node) {
		fadeOut = new FadeTransition(Duration.millis(5000));
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		
		fadeOut.setNode(node);
		fadeOut.play();
	}


}
