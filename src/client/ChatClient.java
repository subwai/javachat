package client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import shared.ChatProtocol;


public class ChatClient {
	
	private ClientGUI gui;
	private BufferedWriter writer;

	private String address;
	private int port;
	
	private File file;

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
			Thread reader = new ClientListenerThread(socket, gui, this);
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

	public void disconnectFromServer() {
		try {
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
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
	
	public void sendFileToServer(Object[] args) {
		ServerSocket serverSocket;
		String[] sendingInfo = new String[3];
		try {
			serverSocket = new ServerSocket();
			int i = serverSocket.getLocalPort();
			Thread sender = new FileSenderThread(serverSocket, (File) args[0]);
			sender.start();
			sendingInfo[0] = String.valueOf(i);
			sendingInfo[1] = (String) args[1];
			sendingInfo[2] = String.valueOf(((File) args[0]).length());
			sendingInfo[3] = gui.getUsername();
			sendMessage(ChatProtocol.SEND_FILE, sendingInfo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		public void receiveFile(String address, int port, File file, int size) {
			Thread receiver = new FileReceiverThread(address, port, file, size);
			receiver.start();
			sendMessage(ChatProtocol.RECEIVE_FILE);
	}
	
}
