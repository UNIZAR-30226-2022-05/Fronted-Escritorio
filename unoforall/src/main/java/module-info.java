module es.unizar.unoforall {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
	requires javafx.base;

    opens es.unizar.unoforall to javafx.fxml;
    exports es.unizar.unoforall;
}
