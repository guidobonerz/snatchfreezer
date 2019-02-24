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

		FXMLLoader loader = null;
		try {
			loader = new FXMLLoader(getClass().getResource("SnatchFreezer.fxml"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Parent root = loader.load();

		Scene scene = new Scene(root, 1024, 715);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
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
