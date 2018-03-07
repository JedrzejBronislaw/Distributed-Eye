package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class Server {
	private List<RemoteClient> clientList = new ArrayList<>();
	private Thread thread;
	@Getter
	private int portNumber;

	public void startServer(int portNumber) {
		this.portNumber = portNumber;
		thread = new Thread(()->startAndAcceptingNewClients());
		thread.start();
	}

	private void startAndAcceptingNewClients(){

		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
			while (true) {
				System.out.println("Serwer czeka na polaczenie (port:" + portNumber + ")");
				Socket clientSocket = serverSocket.accept();
				System.out.println("serwer polaczony z klientem");

				RemoteClient newClient = new RemoteClient(clientSocket);
				clientList.add(newClient);

				System.out.println("liczba klientow: " + clientList.size());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(int clientIndex, String message) {
		clientList.get(clientIndex).sendMessage(message);
	}

}
