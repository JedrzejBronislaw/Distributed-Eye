package protocol;

import java.util.Random;

public class ClientName extends RemoteFunction {

	private static String name = "name";
	private String clientName;

	public ClientName(String input) {
		super(name, input);
	}

	@Override
	protected void parseInput(String input) {
	}

	@Override
	protected void execute() {
		Random r = new Random();
		clientName = "client " + r.nextInt(100);	//TODO polaczyc z prawdziwa nazwa
	}

	@Override
	protected String convertResultsToString() {
		return clientName;
	}

	@Override
	public boolean fit(Message message) {
		return message.getName().getText().equals(name);
	}

	@Override
	public RemoteFunction getNewInstance(String input) {
		return new ClientName(input);
	}

}
