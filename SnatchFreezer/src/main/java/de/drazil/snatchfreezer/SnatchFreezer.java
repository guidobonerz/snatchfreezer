package de.drazil.snatchfreezer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SnatchFreezer extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		setUserAgentStylesheet(STYLESHEET_MODENA);
		FXMLLoader loader = new FXMLLoader(SnatchFreezer.class.getResource("SnatchFreezer.fxml"));
		
		Parent root = loader.load();
		Scene scene = new Scene(root, 1024, 715);
		scene.getStylesheets().add("css/application.css");
		stage.setTitle("SnatchFreezer");
		stage.setScene(scene);
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
