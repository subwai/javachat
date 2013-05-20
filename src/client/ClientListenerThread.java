package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JFrame;

import shared.ChatProtocol;


public class ClientListenerThread extends Thread {
	public static final int SUCCESS = 1;
	public static final int FAIL = 0;
	
	private ClientGUI gui;
	private Socket socket;
	private BufferedReader reader;
	
	public ClientListenerThread(Socket socket, ClientGUI gui) {
		this.gui = gui;
		try {
			this.socket = socket;
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String str = new String();
		try {
			while((str = reader.readLine()) != null) {
				System.out.println("SERVER: "+str);
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
							Boolean admin = false;
							gui.login(admin);
						}
						break;
					case ADMIN_LOGIN:
						if (Integer.valueOf(args[1]) == SUCCESS) {
							// Disable login buttons
							Boolean admin = true;
							gui.login(admin);
						}
						break;	
					case LOGOUT:
						if (Integer.valueOf(args[1]) == SUCCESS) {
							// Enable login buttons
							gui.logout();
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
							String selectedUser = args[2];
							// Open chatroom window
							JFrame j = new Client2ClientGUI("localhost", 3000, selectedUser);
							j.setVisible(true);
						}
						break;
					case SET_CHATROOM_TITLE:
						if (Integer.valueOf(args[1]) == SUCCESS) {
							// Set chatroom title
						}
						break;
					case USER_KICKED:
						if (Integer.valueOf(args[1]) == SUCCESS) {
							// kick user
							String selectedUser = args[2];
						}
						break;
					default:
						throw new Exception();
				}
			}
			socket.close();
		} catch (Exception e) {
			System.out.println("ERROR - Invalid command: '"+str+"', by: SERVER");
		}
	}
}
