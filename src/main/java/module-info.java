module com.examplez.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.smartcardio;


    opens com.examplez.demo to javafx.fxml;
    opens com.examplez.demo.Controller to javafx.fxml;
    exports com.examplez.demo.Controller;
    exports com.examplez.demo;
}