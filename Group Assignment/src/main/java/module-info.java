module com.example.groupassignmentfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.groupassignmentfx to javafx.fxml;
    exports com.example.groupassignmentfx;
}