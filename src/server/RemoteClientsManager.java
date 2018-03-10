package server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import protocol.Message;
import protocol.RemoteFunctionsManager.Name;

public class RemoteClientsManager {

//	@RequiredArgsConstructor
	class RemoteClientRecord{
		@Getter
		@NonNull
		private RemoteClient client;
		@Getter
		private String name;
		@Getter @Setter
		private boolean busy;

		public RemoteClientRecord(RemoteClient remoteClient){
			if (remoteClient == null) throw new NullPointerException("remoteClient");
			this.client = remoteClient;

			client.setClientNameChangeEvent((name) -> {
				System.out.println("Client changed name: " + this.name + " -> " + name);
				this.name = name;
			});

			client.setAnswerEvent((messageType)->{});//TODO

			client.sendMessage(Message.createRequest(Name.ClientName, ""));
		}

		public void sendMessage(Message message) {
			client.sendMessage(message);
		}
//		public void sendMessage(Message message) {
//			// TODO Auto-generated method stub
//
//		}
	}

	private List<RemoteClientRecord> clientList = new ArrayList<>();
	@Setter
	private Consumer<Integer> newNumberOfClientsEvent = null;
	public void add(RemoteClient newClient) {
		clientList.add(new RemoteClientRecord(newClient));
		if (newNumberOfClientsEvent != null)
			newNumberOfClientsEvent.accept(numberOfClients());
	}

	public void add(Socket newClient) {
		clientList.add(new RemoteClientRecord(new RemoteClient(newClient)));
		if (newNumberOfClientsEvent != null)
			newNumberOfClientsEvent.accept(numberOfClients());
	}

	public int numberOfClients() {
		return clientList.size();
	}

//	public void sendMessage(int clientIndex, String message) {
//		clientList.get(clientIndex).sendMessage(message);
//	}

	public void sendMessage(int clientIndex, Message message) {
		clientList.get(clientIndex).sendMessage(message);
	}

	public int lastClientIndex() {
		return clientList.size()-1;
	}
}
