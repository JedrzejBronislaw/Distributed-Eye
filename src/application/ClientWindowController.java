package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import client.Client;
import client.Console;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

public class ClientWindowController implements Initializable {

	// @FXML private Label time;
	// @FXML private TextField minutes;
	// @FXML private TextField minutesAdd;
	@FXML
	private TextArea textArea;
	// @FXML private Button addButton;
	// @FXML private Button startButton;

	// int actMinutes;

	// Timer timer;
	// long timeStart;
	// long timeStop;
	// private int timelong = 30;

	Console console;
	Client client;

	public void startClient(String hostName, int portNumber){
		client = new Client(hostName, portNumber);
		client.start();
	}



	// @FXML
	// private void start(){
	// out.println("s|");
	// startButton.setDisable(true);
	// minutesAdd.setDisable(false);
	// addButton.setDisable(false);
	// startTimer();
	// }
	//
	// @FXML
	// private void addMinutes(){
	// try{
	// int plus = Integer.parseInt(minutesAdd.getText());
	// out.println("a|"+plus);
	// addMinutes(plus);
	// } catch(NumberFormatException e){
	//
	// }
	// }

	// @FXML
	// private void sendMessage(){
	// String fromUser;
	//
	// fromUser = textArea.getText();//stdIn.readLine();
	// String[] lines =fromUser.split(Pattern.quote("\n"));
	// if (fromUser != null) {
	// out.println("c|");
	// for(String l : lines)
	// {
	// System.out.println("Client: " + l);
	// out.println("m|"+l);
	// }
	// }
	// }

	// private void addMinutes(int plus){
	//
	// timeStop += (plus*60*1000000000l);
	// }
	// private void startTimer(){
	//
	// if (timer != null) timer.cancel();
	//
	// timeStart = System.nanoTime();
	// timeStop = timeStart+(timelong*60*1000000000l);
	// System.out.println("start: "+timeStart);
	// System.out.println("stop: "+timeStop);
	// timer = new Timer();
	// timer.schedule(new TimerTask() {
	//
	//
	// @Override
	// public void run() {
	// long timeCurr = System.nanoTime();
	// timeStart = System.nanoTime();
	//
	// System.out.println("cur : " + timeCurr);
	// if (timeCurr >= timeStop) {
	// timer.cancel();
	// Platform.runLater(()->time.setText("KONIEC CZASU :("));
	// return;
	// }
	//
	// long secTotal = (timeStop-timeCurr)/1000000000;
	// int min = (int) (secTotal/60);
	// long sec = secTotal%60;
	//
	// if (min > 0)
	// Platform.runLater(()->time.setText(min + " min " + sec + " s"));
	// else
	// Platform.runLater(()->time.setText(sec + " s"));
	//
	// }
	// }, 0, 1000);
	// }

	// @FXML
	// private void changeMinutes(ActionEvent event){
	// int newMinutes, oldMinutes;
	//
	// oldMinutes = actMinutes;
	//
	// try{
	// newMinutes = Integer.parseInt(minutes.getText());
	// } catch(NumberFormatException e) {
	// newMinutes = oldMinutes;
	// }
	//
	// actMinutes = newMinutes;
	//
	// if (newMinutes == oldMinutes)
	// System.out.println("no change minutes (" + actMinutes + ")");
	// else
	// System.out.println("change minutes to " + actMinutes);
	// }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		console = new Console(textArea);
	}

}
