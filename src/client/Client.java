package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

	Console console;
	String hostName;
	int portNumber;

	Socket socket;
	PrintWriter out;
	BufferedReader in;

	public Client(String hostName, int portNumber) {
		this.hostName = hostName;
		this.portNumber = portNumber;
	}

	public void start() {

		try {
			System.out.println("wlaczanie klienta (host:" + hostName + ", port:" + portNumber + ")");
			socket = new Socket(hostName, portNumber);
			System.out.println("klient polaczony z serwerem");
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			// ){
			// while ((fromServer = in.readLine()) != null) {
			// System.out.println("From server: " + fromServer);
			// if (fromServer.equals("Bye."))
			// break;

			// fromUser = "echo: " +
			// fromServer;//textArea.getText();//stdIn.readLine();
			// String[] lines = fromUser.split(TextArea.)
			// fromUser = fromUser.replaceAll(Pattern.quote("\n"), "$");
			// if (fromUser != null) {
			// System.out.println("Client: " + fromUser);
			// out.println(fromUser);
			// }
			// }
		} catch (IOException e) {
			e.printStackTrace();
		}

		Thread t = new Thread(() -> listening());
		t.start();

	}

	private void listening() {
		String fromUser, fromServer;
		try {
			System.out.println("Klient wlacza nasluch.");
			while ((fromServer = in.readLine()) != null) {
				System.out.println("From server: " + fromServer);
				if(console != null)
					console.println("-> " + fromServer);
				fromUser = "echo: " + fromServer;
				out.println(fromUser);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
