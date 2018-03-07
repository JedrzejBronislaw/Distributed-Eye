package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import lombok.Setter;

public class Client {

	@Setter
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

	public boolean start() {

		try {
//			System.out.println("wlaczanie klienta (host:" + hostName + ", port:" + portNumber + ")");
			socket = new Socket(hostName, portNumber);
//			System.out.println("klient polaczony z serwerem");
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		Thread t = new Thread(() -> listening());
		t.start();

		return true;
	}

	private void listening() {
		String fromUser, fromServer;
		try {
			while ((fromServer = in.readLine()) != null) {
				if(console != null)
					console.println("-> " + fromServer);
				fromUser = "echo: " + fromServer;
				send(fromUser);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void send(String message){
		out.println(message);
	}
}
