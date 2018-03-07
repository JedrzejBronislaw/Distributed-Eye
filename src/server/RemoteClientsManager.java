package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import lombok.NonNull;
import lombok.Setter;

public class RemoteClientsManager {

	private List<RemoteClient> clientList = new ArrayList<>();
	@Setter
	private @NonNull Consumer<Integer> newNumberOfClientsEvent = x->{};


	public void add(RemoteClient newClient) {
		clientList.add(newClient);
		newNumberOfClientsEvent.accept(numberOfClients());
	}
	public void add(Socket newClient) {
		clientList.add(new RemoteClient(newClient));
		newNumberOfClientsEvent.accept(numberOfClients());
	}

	public int numberOfClients() {
		return clientList.size();
	}

	public void sendMessage(int clientIndex, String message) {
		clientList.get(clientIndex).sendMessage(message);
	}
}
