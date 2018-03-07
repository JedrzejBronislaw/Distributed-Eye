package application;

import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import client.Console;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class ClientWindowController implements Initializable {

	@FXML private TextArea textArea;

	private Console console;
	private Client client;

	public void startClient(String hostName, int portNumber){
		client = new Client(hostName, portNumber);
		client.setConsole(console);
		if(!client.start())
			console.println("Connection error");
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		console = new Console(textArea);
	}

}
