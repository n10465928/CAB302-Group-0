module vpm.gui_prototype {
    requires javafx.controls;
    requires javafx.fxml;


    opens vpm.gui_prototype to javafx.fxml;
    exports vpm.gui_prototype;
    exports vpm.gui_prototype.controllers;
    opens vpm.gui_prototype.controllers to javafx.fxml;
}