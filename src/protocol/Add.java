package protocol;

import java.util.regex.Pattern;

public class Add extends RemoteFunction{

	private static String name = "add";
	private int n;
	private int[] numbers;
	private int result;

	public Add(String input) {
		super(name, input);
	}

	@Override
	protected void parseInput(String input) {
		String[] strNums = input.split(Pattern.quote("+"));

		n = strNums.length;
		numbers = new int[n];

		int j=0;
		for(int i=0; i<n ;i++)
			try{
				numbers[j++] = Integer.parseInt(strNums[i]);
			} catch(NumberFormatException e){
				n--;
				System.out.println(strNums[i] + " is not a number");
			}
	}

	@Override
	protected void execute() {
		result = 0;

		for(int i=0; i<n; i++)
			result += numbers[i];
	}

	@Override
	protected String convertResultsToString() {
		return result+"";
	}

	@Override
	public RemoteFunction getNewInstance(String input) {
		return new Add(input);
	}

	public boolean fit(Message message){
		System.out.println("fit ("+message.getName()+", "+name+"): " + message.getName().getText().equals(name));
		return message.getName().getText().equals(name);
	}

	public static String generateInput(int[] numbers) {
		if (numbers == null)
			throw new IllegalArgumentException("Numbers couldn't be null.");
		if (numbers.length == 0)
			throw new IllegalArgumentException("Numbers couldn't be empty.");

		int n = numbers.length;
		StringBuffer input = new StringBuffer();

		input.append(numbers[0]);
		for(int i=1; i<n; i++)
			input.append("+" + numbers[i]);

		return input.toString();
	}

}
