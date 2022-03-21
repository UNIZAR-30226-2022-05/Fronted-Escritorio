module es.unizar.unoforall {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
	requires javafx.base;
	requires API;

    opens es.unizar.unoforall to javafx.fxml;
    exports es.unizar.unoforall;
}
