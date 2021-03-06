package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;

import lombok.Setter;
import protocol.Message;
import protocol.Message.Type;
import protocol.RemoteFunctionsManager.Name;

public class RemoteClient {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	private Thread listenThread;

	@Setter
	private Consumer<String> answerEvent = null;
	@Setter
	private Consumer<String> clientNameChangeEvent = null;

	public RemoteClient(Socket socket) {
		this.socket = socket;

		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		sendMessage(Message.createRequest(Name.ClientName, ""));
		listenThread = new Thread(() -> listen());
		listenThread.start();
	}

	private void listen() {
		String input;
//		String messageType;
		Message message;

		try {

			while ((input = in.readLine()) != null) {

				try{
					message = new Message(input);
				} catch (IllegalArgumentException e)
				{
					System.out.println("wrong message: \"" + input + "\"");
					continue;
				}
//				messageType = ""; //TODO

				if(answerEvent != null)
					answerEvent.accept(message.getType().toString());

//				message.generateAnswer("content"); //TODO

				if (message.getType() == Type.Answer && message.getName() == Name.ClientName)
					if (clientNameChangeEvent != null)
						clientNameChangeEvent.accept(message.getContent());
				// final String input = inputLine.replaceAll(Pattern.quote("$"),
				// "\n");

				// if (input.startsWith("c|"))//clear screan
				// {
				////
				//// Platform.runLater(()->textArea.setText(new String(input)));
				// Platform.runLater(()->textArea.setText(""));
				// }
				// if (input.startsWith("m|"))//clear screan
				// {
				// String message = input.substring(2);
				// Platform.runLater(()->textArea.setText(textArea.getText()+message+"\n"));
				// }
				// if (input.startsWith("a|"))//add minute
				// {
				// int plus;
				// try{
				// plus = Integer.parseInt(input.substring(2));
				// } catch(NumberFormatException e) {
				// plus = 0;
				// }
				//
				// timeStop += (plus*60*1000000000l);
				//
				// }
				// if (input.startsWith("s|"))//start
				// {
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
				// Platform.runLater(()->time.setText(min + " min " + sec + "
				// s"));
				// else
				// Platform.runLater(()->time.setText(sec + " s"));
				//
				// }
				// }, 0, 1000);
				// }

				System.out.println("input: " + input);
				// Platform.runLater(()->textArea.setText(new String(input)));
				// new Runnable() {
				//
				// @Override
				// public void run() {
				// // TODO Auto-generated method stub
				// textArea.setText(input);
				// }
				// }
				// );
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public synchronized void sendMessage(String message) {
//		out.println(message);
//	}

	public boolean disconnect() {
		try {
			if (!socket.isClosed())
				socket.close();
			else
				return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public void sendMessage(Message message) {
		out.println(message);
	}
}
