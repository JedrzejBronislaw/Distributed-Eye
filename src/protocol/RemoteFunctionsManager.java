package protocol;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class RemoteFunctionsManager {

	@RequiredArgsConstructor
	public enum Name {
		Ping("ping"), ClientName("name"), Add("add");

		@NonNull @Getter private String text;

		public static Name valueOfText(String text){
			Name name = null;

			for(Name n : Name.values())
				if (n.getText().equals(text)){
					name = n;
					break;
				}
			if (name == null)
				throw new IllegalArgumentException("unknown message name (" + text + ")");

			return name;
		}
	}

	private List<RemoteFunction> functions = new ArrayList<>();

	private RemoteFunctionsManager() {
	}

	public RemoteFunction getTask(Message message){
		for(RemoteFunction f : functions)
			if (f.fit(message))
				return f.getNewInstance(message.getContent());

		return null;
	}

	private void add(RemoteFunction function) {
		functions.add(function);
	}


	public static RemoteFunctionsManager create(){
		RemoteFunctionsManager manager = new RemoteFunctionsManager();

		manager.add(new Add(""));

		return manager;
	}

}
