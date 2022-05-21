module ifce.ppd.tuplespace {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires jini.core;
    requires jini.ext;


    opens ifce.ppd.tuplespace to javafx.fxml;
    exports ifce.ppd.tuplespace;
    exports ifce.ppd.tuplespace.controller;
    opens ifce.ppd.tuplespace.controller to javafx.fxml;
    exports ifce.ppd.tuplespace.server;
    opens ifce.ppd.tuplespace.server to javafx.fxml;
    exports ifce.ppd.tuplespace.model;
    opens ifce.ppd.tuplespace.model to javafx.fxml;
}