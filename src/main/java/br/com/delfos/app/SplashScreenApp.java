package br.com.delfos.app;

import java.io.IOException;

import br.com.delfos.view.AlertAdapter;
import javafx.application.Preloader;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class SplashScreenApp extends Preloader {

	private static final String LOCATION = "fxml/basic/SplashScreen.fxml";
	private static Stage stage;

	@Override
	public synchronized void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(SplashScreenApp.class.getClassLoader().getResource(LOCATION));
		AnchorPane pane = (AnchorPane) loader.load();

		primaryStage.setScene(new Scene(pane));
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();

		SplashScreenApp.stage = primaryStage;

		primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWING, evt -> inicializando());

	}

	private EventHandler<?> inicializando() {
		return event -> {
			try {
				new LoginApp().start(stage);
			} catch (IOException e) {
				AlertAdapter.erroLoadFXML(e);
			}
		};
	}

	public void start() throws IOException {
		this.start(new Stage());
	}

	public static void close() {
		SplashScreenApp.stage.close();
	}

	public static Stage getStage() {
		return stage;
	}

}
