package client;

import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.text.html.FormSubmitEvent;

import lombok.Setter;
import protocol.Add;
import protocol.Message;
import protocol.RemoteFunction;
import protocol.RemoteFunctionsManager;
import protocol.RemoteFunctionsManager.Name;

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

			Message message = Message.createAnswer(Name.ClientName, "client 101");
			send(message);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		Thread t = new Thread(() -> listening());
		t.start();

		return true;
	}

	private void listening() {
		String fromServer;
		RemoteFunctionsManager functionsManager = RemoteFunctionsManager.create();
		Message message;

		try {
			while ((fromServer = in.readLine()) != null) {
				if(console != null)
					console.println("-> " + fromServer);
				try{
					message = new Message(fromServer);
				} catch (IllegalArgumentException e)
				{
					System.out.println("wrong message: "+ fromServer);
					continue;
				}
				RemoteFunction function = functionsManager.getTask(message);

				System.out.println("message: " + message.getName());
				System.out.println("function: " + ((function == null) ? "NULL" : function.getName()));

				if (function != null)
					send(message.generateAnswer(function.exe()));
//				if (message.getName() == Message.Name.Add &&
//					message.getType() == Message.Type.Request)
//					send(message.generateAnswer(new Add(message.getContent()).exe()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void send(Message message) {
		if(console != null)
			console.println("<- " + message);

		out.println(message);
	}
}
