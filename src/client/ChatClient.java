package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import shared.ChatProtocol;


public class ChatClient {
	
	private ClientGUI gui;
	private BufferedWriter writer;

	private String address;
	private int port;
	

	public static void main(String[] args) {
		new ClientGUI("localhost", 3000);
	}

	public ChatClient(String address, int port, ClientGUI gui) {
		this.address = address;
		this.port = port;
		this.gui = gui;
	}

	public void connectToServer() {
		try {
			Socket socket = new Socket(InetAddress.getByName(address), port);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			this.gui = gui;
			Thread reader = new ClientListenerThread(socket, gui);
			reader.start();
		} catch (UnknownHostException e){
			System.out.println("Felaktig serveradress");
			System.exit(0);
		} catch (NumberFormatException e){
			System.out.println("Felaktigt portnummer");
			System.exit(0);
		} catch (IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public void sendMessage(ChatProtocol type, String... args) {
		StringBuilder sb = new StringBuilder(type.toString());
		for(String arg : args) {
			sb.append(" "+arg);
		}
		
		try {
			writer.write(sb.toString());
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
