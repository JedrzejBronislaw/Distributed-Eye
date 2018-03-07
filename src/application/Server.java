package application;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class Server {
	private List<RemoteClient> clientList = new ArrayList<>();
	// PrintWriter out;

	public void startServer(int portNumber) {
		// try (
		// ServerSocket serverSocket = new ServerSocket(portNumber);
		// Socket clientSocket = serverSocket.accept();
		// PrintWriter out =
		// new PrintWriter(clientSocket.getOutputStream(), true);
		// BufferedReader in = new BufferedReader(
		// new InputStreamReader(clientSocket.getInputStream()));
		// ) {
		// String inputLine, outputLine;

		// Initiate conversation with client
		// KnockKnockProtocol kkp = new KnockKnockProtocol();
		// outputLine = kkp.processInput(null);
		// out.println(outputLine);

		// while ((inputLine = in.readLine()) != null) {
		// outputLine = //kkp.processInput(inputLine);
		// out.println(outputLine);
		// if (outputLine.equals("Bye."))
		// break;
		// }

		EventQueue.invokeLater(() -> {
			String input;
			try {
				// (
				ServerSocket serverSocket = new ServerSocket(portNumber);

				while (true) {
					System.out.println("Serwer czeka na polaczenie (port:" + portNumber + ")");
					Socket clientSocket = serverSocket.accept();
					System.out.println("serwer polaczony z klientem");

					RemoteClient newClient = new RemoteClient(clientSocket);
					clientList.add(newClient);

					System.out.println("liczba klientow: "+clientList.size());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			// )

		});

		// } catch(IOException e){
		// e.printStackTrace();
		// }
	}

	public void sendMessage(int clientIndex, String message) {
		clientList.get(clientIndex).sendMessage(message);
	}


}
