module ifce.ppd.tuplespace {
    requires javafx.controls;
    requires javafx.fxml;


    opens ifce.ppd.tuplespace to javafx.fxml;
    exports ifce.ppd.tuplespace;
}