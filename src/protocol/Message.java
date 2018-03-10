package protocol;

import java.util.regex.Pattern;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import protocol.RemoteFunctionsManager.Name;

public class Message {

	@RequiredArgsConstructor
	public enum Type {Request("r"), Answer("a"); @NonNull @Getter private String text;}
	private static final String separator = "|";

	@NonNull
	private String rawMessage;

	@Getter private Type type;
	@Getter private Name name;
	@Getter private String content;

	public Message(String rawMessage) {
		this.rawMessage = rawMessage;
		separate();
	}

	private Message(){}

	private void separate() {
		String[] m = rawMessage.split(Pattern.quote(separator), 3);
		if (m.length < 3)
			throw new IllegalArgumentException("\"" + rawMessage + "\" - too few parts (" + m.length + ")");

		String rawName = m[0];
		String rawType = m[1];
		content = m[2];

		type = null;
		for(Type t : Type.values())
			if (t.getText().equals(rawType)){
				type = t;
				break;
			}
		if (type == null)
			throw new IllegalArgumentException("\"" + rawMessage + "\" - wrong message type");

		name = Name.valueOfText(rawName);
	}

	public Message generateAnswer(String content){
		Message answer = new Message();
		answer.content = content;
		answer.name = this.name;
		answer.type = Type.Answer;
		answer.rawMessage = answer.toString();

		return answer;
	}

	public static Message createAnswer(Name name, String content) {
		Message answer = new Message();
		answer.type = Type.Answer;
		answer.content = content;
		answer.name = name;
		answer.rawMessage = answer.toString();

		return answer;
	}

	public static Message createRequest(Name name, String content) {
		Message answer = new Message();
		answer.type = Type.Request;
		answer.content = content;
		answer.name = name;
		answer.rawMessage = answer.toString();

		return answer;
	}

	@Override
	public String toString() {
		return	name.getText() + separator +
				type.getText() + separator +
				content;// + separator;

	}
}
