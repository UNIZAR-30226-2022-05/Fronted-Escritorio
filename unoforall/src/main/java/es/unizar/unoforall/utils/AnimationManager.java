package es.unizar.unoforall.utils;

import java.util.ArrayList;
import java.util.List;

import es.unizar.unoforall.model.partidas.Carta;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class AnimationManager {

	private static final long CARD_MOVEMENT_START_DELAY = 500L;
    private static final Duration CARD_MOVEMENT_DURATION = Duration.millis(650);
    //private static final Duration HOST_CARD_MOVEMENT_DURATION = Duration.millis(450);
    private static final Duration HOST_CARD_MOVEMENT_DURATION = CARD_MOVEMENT_DURATION.divide(2);
    private static RotateTransition rtHorario;
    private static RotateTransition rtAntihorario; 
    private static RotateTransition rtRapida;
    private static ScaleTransition agrandar;
    private static ScaleTransition disminuir;
    private static ScaleTransition aPequenito;
    private static ScaleTransition aGrande;
    private static ScaleTransition aPequenitoHost;
    private static ScaleTransition aGrandeHost;
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
		
		aPequenito = new ScaleTransition(CARD_MOVEMENT_DURATION.divide(2));
		aPequenito.setToX(0.5);
		aPequenito.setToY(0.5);
		
		aGrande = new ScaleTransition(CARD_MOVEMENT_DURATION.divide(2));
		aGrande.setToX(1);
		aGrande.setToY(1);
		aGrande.setOnFinished(event -> aPequenito.play());
		
		aPequenitoHost = new ScaleTransition(HOST_CARD_MOVEMENT_DURATION.divide(2));
		aPequenitoHost.setToX(0.5);
		aPequenitoHost.setToY(0.5);
		
		aGrandeHost = new ScaleTransition(HOST_CARD_MOVEMENT_DURATION.divide(2));
		aGrandeHost.setToX(1);
		aGrandeHost.setToY(1);
		aGrandeHost.setOnFinished(event -> aPequenitoHost.play());
    }
    
    public static void setAnimacionSentido(ImageView imageView, boolean sentidoAnterior, boolean sentidoActual) {
		//Inicializar animación inicial a la imagen del sentido de la partida
    	if(sentidoAnterior == sentidoActual) {
    		return;
    	} 
    	
    	
		if(sentidoActual) {
			rtHorario.setNode(imageView);
			rtHorario.play();
			AnimationManager.AntihorarioAHorario(imageView);
		} else {
			rtAntihorario.setNode(imageView);
			rtAntihorario.play();
			AnimationManager.HorarioAAntihorario(imageView);
		}
		
//		if (sentidoHorario) {
//    		if ((Image)imageView.getUserData() == imageSentidoAntihorario) {
//
//    			imageView.setUserData(imageSentidoHorario);
//        		imageView.setImage(imageSentidoHorario);
//        		//Transicionar a sentido Horario
//        		AnimationManager.AntihorarioAHorario(imageView);
//    		}
//    		
//    	} else {
//    		if((Image)imageView.getUserData() == imageSentidoHorario) {
//    			
//    			imageView.setUserData(imageSentidoAntihorario);
//    			imageView.setImage(imageSentidoAntihorario);
//    			//Transicionar a sentido Antihorario
//        		AnimationMcorreanager.HorarioAAntihorario(imageView);
//    		}
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

    private static void animateCardMovement(BorderPane viewGroup, Point2D startPoint, Point2D endPoint,
    	Carta carta, boolean isVisible, long startDelay, boolean defaultMode, boolean host, Runnable endAction){
		if(startPoint == endPoint){
			return;
		}
		
		ImageView cartaView = new ImageView();
		ImageManager.setImagenCarta(cartaView, carta, defaultMode, isVisible);
		cartaView.setFitWidth(96);
		cartaView.setFitHeight(150);
//		cartaView.setScaleX(0.5);
//		cartaView.setScaleY(0.5);
		viewGroup.getChildren().add(cartaView);
		cartaView.setX(-100);
		cartaView.setY(-100);
		Line movePath = new Line(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
		//movePath.setVisible(false);

		PathTransition path = new PathTransition(host ? HOST_CARD_MOVEMENT_DURATION : CARD_MOVEMENT_DURATION, movePath, cartaView);	
		path.setDelay(Duration.millis(startDelay));
		path.setOnFinished(event -> {
			viewGroup.getChildren().remove(cartaView);
			viewGroup.getChildren().remove(movePath);
			endAction.run();
		});
//		aGrande.setNode(viewGroup);
//		aGrande.play();
		path.play();
		
//		TranslateTransition movimiento =
//				new TranslateTransition(CARD_MOVEMENT_DURATION, cartaView);
//		
//		movimiento.setDelay(Duration.millis(CARD_MOVEMENT_START_DELAY));
		
		//TACO DESCARTES
		//X = 455
		//Y = 220
		
		//TACO ROBO
		//X = 750
		//Y = 220
		
		//JUGADOR ABAJO
		//X = 240
		//Y = 565
		
		//JUGADOR IZQUIERDA
		//X = 25
		//Y = 260
		
		//JUGADOR ARRIBA
		//X = 950
		//Y = -55
		
		//JUGADOR DERECHA
		//X = 1160
		//Y = 350
		
//		movimiento.setToX(25);
//		movimiento.setToY(260);
//		movimiento.setOnFinished(event -> {
//			viewGroup.getChildren().remove(cartaView);
//			endAction.run();
//		});
		
//		movimiento.play();
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
	
	public static class Builder{
        private final BorderPane viewGroup;
        private Point2D startPoint;
        private Point2D endPoint;

        private List<Carta> cartas;
        private boolean isVisible;
        private boolean defaultMode;
        private boolean host;

        private Runnable endAction;

        public Builder(BorderPane viewGroup){
            this.viewGroup = viewGroup;
            this.endAction = () -> {};
            this.host = false;
        }

        public Builder withstartPoint(Point2D startPoint){
            this.startPoint = startPoint;
            return this;
        }

        public Builder withendPoint(Point2D endPoint){
            this.endPoint = endPoint;
            return this;
        }

        public Builder withDefaultMode(boolean defaultMode){
            this.defaultMode = defaultMode;
            return this;
        }

        public Builder withCartasRobo(int numCartasRobo){
            this.isVisible = false;
            this.cartas = new ArrayList<>();
            for(int i=0; i<numCartasRobo; i++){
                this.cartas.add(null);
            }
            return this;
        }

        public Builder withCartas(List<Carta> cartas, boolean isVisible){
            this.isVisible = isVisible;
            this.cartas = cartas;
            return this;
        }

        public Builder withEndAction(Runnable endAction){
            this.endAction = endAction;
            return this;
        }
        
        public Builder withHost(boolean host){
            this.host = host;
            return this;
        }

        public void start(){
            int n = this.cartas.size();
            for(int i=0; i<n; i++){
                Runnable runnable;
                if(i == n-1){
                    runnable = this.endAction;
                }else{
                    runnable = () -> {};
                }
                
                animateCardMovement(viewGroup, startPoint, endPoint, this.cartas.get(i), isVisible,
                        i * CARD_MOVEMENT_START_DELAY, defaultMode, host, runnable);
            }
        }
    }
}
