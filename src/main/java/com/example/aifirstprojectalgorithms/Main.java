package com.example.aifirstprojectalgorithms;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	@FXML
	private ScrollPane parent;
	@FXML
	private AnchorPane pane;
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
			Scene scene = new Scene(root,1370.0,700);

			primaryStage.setScene(scene);
			primaryStage.setTitle("Palestine Map"); // <------------
			
			primaryStage.show();
			primaryStage.setOnCloseRequest(e -> {
				Platform.exit();
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args){
		
		launch(args);
	}
}
