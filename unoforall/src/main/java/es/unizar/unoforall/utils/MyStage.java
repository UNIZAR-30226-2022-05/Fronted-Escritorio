package es.unizar.unoforall.utils;

import es.unizar.unoforall.CambiarColorController;
import javafx.stage.Stage;

public class MyStage extends Stage{
	public int showAndReturnResult(CambiarColorController controller) {
		super.showAndWait();
		return controller.getReturn();
	}

}
