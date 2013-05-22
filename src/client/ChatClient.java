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
	public static final String DENIED = "2";
	public static final String SUCCESS = "1";
	public static final String FAIL = "0";

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
	
	public void setupFileSender(int chatid, int userid, String fileName, File file) {
		this.file = file;
		sendMessage(ChatProtocol.SEND_FILE, String.valueOf(chatid), String.valueOf(userid), fileName, String.valueOf(file.length()));
	}

	public void startSendingFile(int chatid, int userid, String host, int port) {
		Thread sender = new FileSenderThread(gui, chatid, host, port, file);
		sender.start();
	}

	public void setupFileReciever(int chatid, int userid, File file, int size) {
		try {
			ServerSocket socket = new ServerSocket(0);
			int port = socket.getLocalPort();
			String host = "localhost";
			Thread receiver = new FileReceiverThread(gui, chatid, socket, file, size);
			receiver.start();
			sendMessage(ChatProtocol.SEND_REQUEST, SUCCESS, String.valueOf(chatid), String.valueOf(userid), host, String.valueOf(port));
		} catch (IOException e) {
			e.printStackTrace();
			sendMessage(ChatProtocol.SEND_REQUEST, FAIL, String.valueOf(chatid), String.valueOf(userid), file.getName());
		}
	}
	

	public void fileTransferDenied(int chatid, int userid, String filename){
		sendMessage(ChatProtocol.SEND_REQUEST, DENIED, String.valueOf(chatid), String.valueOf(userid), filename);
	}

	public boolean checkUniqueUser(String user){
		
		return true;
	}
	
}
