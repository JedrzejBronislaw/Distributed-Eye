package protocol;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class RemoteFunction {

	@NonNull
	@Getter
	protected String name;
	@NonNull
	@Getter
	private String input;

	final public String exe(){
		parseInput(input);
		execute();
		return convertResultsToString();
	}

	protected abstract void parseInput(String input);
	protected abstract void execute();
	protected abstract String convertResultsToString();

	public abstract boolean fit(Message message);

	public abstract RemoteFunction getNewInstance(String input);
}
