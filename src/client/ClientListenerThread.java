package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import shared.ChatProtocol;


public class ClientListenerThread extends Thread {
	public static final int SUCCESS = 1;
	public static final int FAIL = 0;
	
	private BufferedReader reader;
	
	public ClientListenerThread(Socket client) {
		try {
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String str = new String();
		try {
			while(true) {
				str = reader.readLine();
				String[] args = str.split(" ");
				switch(ChatProtocol.valueOf(args[0])) {
					case MESSAGE:
						int id = Integer.valueOf(args[1]);
						String chatMessage = args[2];
						// Update the tab with chatroom: id.
						break;
					case LOGIN:
						if (Integer.valueOf(args[1]) == SUCCESS) {
							// Disable login buttons
						}
						break;
					case LOGOUT:
						if (Integer.valueOf(args[1]) == SUCCESS) {
							// Enable login buttons
						}
						break;
					case JOIN_CHATROOM:
						if (Integer.valueOf(args[1]) == SUCCESS) {
							// Open chatroom window
						}
						break;
					case LEAVE_CHATROOM:
						if (Integer.valueOf(args[1]) == SUCCESS) {
							// Remove chatroom window
						}
						break;
					case CREATE_CHATROOM:
						if (Integer.valueOf(args[1]) == SUCCESS) {
							// Open chatroom window
						}
						break;
					case SET_CHATROOM_TITLE:
						if (Integer.valueOf(args[1]) == SUCCESS) {
							// Set chatroom title
						}
						break;
					default:
						throw new Exception();
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR - Invalid command: '"+str+"', by: SERVER");
		}
	}
}
