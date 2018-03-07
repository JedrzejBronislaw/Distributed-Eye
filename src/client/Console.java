package client;

import javafx.scene.control.TextArea;

public class Console {

	TextArea textArea;

	public Console(TextArea textArea) {
		this.textArea = textArea;
	}

	public void println(String text){
		textArea.appendText(text + "\n");
	}

	public void clear(){
		textArea.clear();
	}

}
