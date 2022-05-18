package es.unizar.unoforall.utils;

import es.unizar.unoforall.CambiarColorController;
import es.unizar.unoforall.IntercambiarManoController;
import es.unizar.unoforall.RobarOJugarCartaController;
import es.unizar.unoforall.model.partidas.Carta;
import javafx.stage.Stage;

public class MyStage extends Stage{
	public int showAndReturnColourResult(CambiarColorController controller) {
		super.showAndWait();
		return controller.getReturn();
	}
	
	public int showAndReturnDrawResult(RobarOJugarCartaController controller, Carta carta) {
		super.showAndWait();
		return controller.getReturn();
	}

	public int showAndReturnSwapHandResult(IntercambiarManoController controller) {
		super.showAndWait();
		return controller.getReturn();
	}
}
