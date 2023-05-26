module com.example.ogkg {
  requires javafx.controls;
  requires javafx.fxml;

  requires com.dlsc.formsfx;

  opens com.example.ogkg to javafx.fxml;
  exports com.example.ogkg;
}