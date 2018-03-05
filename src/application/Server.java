package application;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class Server {
//	Timer timer;
//	long timeStart;
//	long timeStop;
//	private int timelong = 30;

	PrintWriter out;

	public void startServer(int portNumber){
//		try (
//			    ServerSocket serverSocket = new ServerSocket(portNumber);
//			    Socket clientSocket = serverSocket.accept();
//			    PrintWriter out =
//			        new PrintWriter(clientSocket.getOutputStream(), true);
//			    BufferedReader in = new BufferedReader(
//			        new InputStreamReader(clientSocket.getInputStream()));
//			) {
//		    String inputLine, outputLine;

		    // Initiate conversation with client
//		    KnockKnockProtocol kkp = new KnockKnockProtocol();
//		    outputLine = kkp.processInput(null);
//		    out.println(outputLine);

//		    while ((inputLine = in.readLine()) != null) {
//		        outputLine = //kkp.processInput(inputLine);
//		        out.println(outputLine);
//		        if (outputLine.equals("Bye."))
//		            break;
//		    }



		    EventQueue.invokeLater(()->{
		    	String input;
		    	try{
//		    		(
		    	ServerSocket serverSocket = new ServerSocket(portNumber);
		    	System.out.println("Serwer czeka na polaczenie (port:"+portNumber+")");
		    	Socket clientSocket = serverSocket.accept();
		    	System.out.println("serwer polaczony z klientem");
		    	 out =
		    			new PrintWriter(clientSocket.getOutputStream(), true);
		    	BufferedReader in = new BufferedReader(
				        new InputStreamReader(clientSocket.getInputStream()));

//		    	)
			    while ((input = in.readLine()) != null) {
//			    	final String input = inputLine.replaceAll(Pattern.quote("$"), "\n");

//			    	if (input.startsWith("c|"))//clear screan
//			    	{
////
////				    	Platform.runLater(()->textArea.setText(new String(input)));
//				    	Platform.runLater(()->textArea.setText(""));
//			    	}
//			    	if (input.startsWith("m|"))//clear screan
//			    	{
//			    		String message = input.substring(2);
//			    		Platform.runLater(()->textArea.setText(textArea.getText()+message+"\n"));
//			    	}
//			    	if (input.startsWith("a|"))//add minute
//			    	{
//			    		int plus;
//			    		try{
//			    			plus = Integer.parseInt(input.substring(2));
//			    		} catch(NumberFormatException e) {
//			    			plus = 0;
//			    		}
//
//			    		timeStop += (plus*60*1000000000l);
//
//			    	}
//			    	if (input.startsWith("s|"))//start
//			    	{
//			    		if (timer != null) timer.cancel();
//
//			    		timeStart = System.nanoTime();
//			    		timeStop = timeStart+(timelong*60*1000000000l);
//			    		System.out.println("start: "+timeStart);
//			    		System.out.println("stop:  "+timeStop);
//			    		timer = new Timer();
//			    		timer.schedule(new TimerTask() {
//
//
//							@Override
//							public void run() {
//								long timeCurr = System.nanoTime();
//								timeStart = System.nanoTime();
//
//								System.out.println("cur  : " + timeCurr);
//								if (timeCurr >= timeStop) {
//									timer.cancel();
//									Platform.runLater(()->time.setText("KONIEC CZASU :("));
//									return;
//								}
//
//								long secTotal = (timeStop-timeCurr)/1000000000;
//								int min = (int) (secTotal/60);
//								long sec = secTotal%60;
//
//								if (min > 0)
//									Platform.runLater(()->time.setText(min + " min " + sec + " s"));
//								else
//									Platform.runLater(()->time.setText(sec + " s"));
//
//							}
//						}, 0, 1000);
//			    	}

			    	System.out.println("input: "+input);
//			    	Platform.runLater(()->textArea.setText(new String(input)));
//			    			new Runnable() {
//
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									textArea.setText(input);
//								}
//							}
//			    			);
			    }
		    	} catch (IOException e){
		    		e.printStackTrace();
		    	}
		    });

//		} catch(IOException e){
//			e.printStackTrace();
//		}
	}


//	@FXML
	public void sendMessage(String m){
		String fromUser;

	    fromUser = m;//textArea.getText();//stdIn.readLine();
//	    String[] lines =fromUser.split(Pattern.quote("\n"));
	    out.println(fromUser);
//	    if (fromUser != null) {
//	    	out.println("c|");
//	    	for(String l : lines)
//	    	{
//		        System.out.println("Client: " + l);
//		        out.println("m|"+l);
//	    	}
//	    }
	}
}
