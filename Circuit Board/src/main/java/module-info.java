module com.example.ca1printeccircuitboard {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ca1printeccircuitboard to javafx.fxml;
    exports com.example.ca1printeccircuitboard;
}