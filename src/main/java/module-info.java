module vpm.gui_prototype {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jdk.jconsole;


    opens vpm.gui_prototype to javafx.fxml;
    exports vpm.gui_prototype;
    exports vpm.gui_prototype.services;
    exports vpm.gui_prototype.controllers;
    exports vpm.gui_prototype.models.PetStuff;
    exports vpm.gui_prototype.models.DatabaseStuff.UserData;
    exports vpm.gui_prototype.models.UserStuff;
    exports vpm.gui_prototype.models.DatabaseStuff.PetData;
    exports vpm.gui_prototype.models.FoodStuff;
    opens vpm.gui_prototype.controllers to javafx.fxml;
}