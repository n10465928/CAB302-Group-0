module vpm.gui_prototype {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens vpm.gui_prototype to javafx.fxml;
    exports vpm.gui_prototype;
    exports vpm.gui_prototype.controllers;
    exports vpm.gui_prototype.models.PetStuff;
    opens vpm.gui_prototype.controllers to javafx.fxml;
}