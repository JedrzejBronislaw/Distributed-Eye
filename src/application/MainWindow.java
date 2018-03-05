package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MainWindow implements Initializable{

	@FXML private Label portLabel;
	@FXML private TextField portText;
	@FXML private Label ipLabel;
	@FXML private TextField ipText;
	@FXML private Button serverButton;
	@FXML private Button klientButton;

	@FXML
	private void openServer(ActionEvent event){
		Stage stage;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ServerWindow.fxml"));
			Parent root = fxmlLoader.load();
			stage = new Stage();
			Scene scene = new Scene(root,900,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();

			if(fxmlLoader.getController() == null) System.out.println("fxmlLoader.getController() == null");
			if(portText == null) System.out.println("portText == null");
			((ServerWindowController)fxmlLoader.getController()).startServer(Integer.parseInt(portText.getText()));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void openClient(ActionEvent event){
		Stage stage;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ClientWindow.fxml"));
			Parent root = fxmlLoader.load();
			stage = new Stage();
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();

			((ClientWindowController)fxmlLoader.getController()).startClient(ipText.getText(), Integer.parseInt(portText.getText()));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
