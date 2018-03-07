package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.Getter;

public class Server {

	@Getter
	private RemoteClientsManager clientManager = new RemoteClientsManager();
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

				clientManager.add(clientSocket);

				System.out.println("liczba klientow: " + clientManager.numberOfClients());
			}
		} catch (BindException e) {
			//TODO
			System.out.println("The port is unavailable.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(int clientIndex, String message) {
		clientManager.sendMessage(clientIndex,message);
	}

}
