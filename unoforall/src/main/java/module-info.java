module es.unizar.unoforall {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens es.unizar.unoforall to javafx.fxml;
    exports es.unizar.unoforall;
}
