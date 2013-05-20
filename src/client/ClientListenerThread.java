package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		Pattern p = Pattern.compile("(?=\").+|[^\\s]+",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		try {
			while((str = reader.readLine()) != null) {
				try {
					System.out.println("SERVER: "+str);
<<<<<<< HEAD
					String[] args = str.split(" ");
					int id = 0;
=======
					Matcher m = p.matcher(str);
					List<String> matches = new ArrayList<String>();
					while(m.find()){
					    matches.add(m.group());
					}
					String[] args = matches.toArray(new String[0]);
>>>>>>> ac48377fcdcc14c7287c17276ba0831d3e51d364
					switch(ChatProtocol.valueOf(args[0])) {
						case MESSAGE:
							int id = Integer.valueOf(args[1]);
							String message = args[2];
							// Update the tab with chatroom: id.
							gui.pushText(id, message.substring(1, message.length() - 1));
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
								id = Integer.valueOf(args[1]);
								gui.removeChat(id);
							}
							break;
						case CREATE_CHATROOM:
							if (Integer.valueOf(args[1]) == SUCCESS) {
								String selectedUser = args[2];
								// Open chatroom window
								id = Integer.valueOf(args[1]);
								gui.addChat(id, selectedUser);
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
						case SEND_FILE:
							if (Integer.valueOf(args[1]) == SUCCESS) {
								
								String selectedUser = args[2];
							}
							break;
						default:
							throw new UnsupportedOperationException();
					}
<<<<<<< HEAD
				} catch (Exception e) {
					e.printStackTrace();
=======
				} catch (UnsupportedOperationException e) {
>>>>>>> ac48377fcdcc14c7287c17276ba0831d3e51d364
					System.out.println("ERROR - Invalid command: '"+str+"', by: SERVER");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			System.out.println("ERROR - Server disconnected.");
			gui.logout();
		} catch (IOException e) {
			e.printStackTrace();
		}
		disconnect();
	}

	private void disconnect() {
		try {
			reader.close();
			socket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
