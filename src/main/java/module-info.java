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
    exports ifce.ppd.tuplespace.example;
    opens ifce.ppd.tuplespace.example to javafx.fxml;
}