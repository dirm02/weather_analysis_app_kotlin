module com.example.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires org.json;
    requires okhttp3;


    opens com.example.weatherapp to javafx.fxml;
    exports com.example.weatherapp;
}