module com.example.tablero {
    requires javafx.controls;
    requires javafx.fxml;
    requires JColor;


    opens com.example.tablero to javafx.fxml;
    exports com.example.tablero;
}