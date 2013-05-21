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
	public static final String DEFAULT_CHATROOM = "0";
	public static final String SUCCESS = "1";
	public static final String FAIL = "0";
	
	private ClientGUI gui;
	private Socket socket;
	private BufferedReader reader;
	private ChatClient client;
	
	public ClientListenerThread(Socket socket, ClientGUI gui, ChatClient client) {
		this.client = client;
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
		boolean running = true;
		Pattern p = Pattern.compile("(?=\").+|[^\\s]+",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		try {
			while(running && (str = reader.readLine()) != null) {
				try {
					System.out.println("SERVER: "+str);
					Matcher m = p.matcher(str);
					List<String> matches = new ArrayList<String>();
					while(m.find()){
					    matches.add(m.group());
					}
					String[] args = matches.toArray(new String[0]);
					switch(ChatProtocol.valueOf(args[0])) {
						case MESSAGE:
							int id = Integer.valueOf(args[1]);
							String message = args[2];
							// Update the tab with chatroom: id.
							gui.pushText(id, message.substring(1, message.length() - 1));
							break;
						case LOGIN:
							if (args[1].equals(SUCCESS)) {
								// Disable login buttons
								client.sendMessage(ChatProtocol.JOIN_CHATROOM, DEFAULT_CHATROOM);	
								Boolean admin = false;
								gui.login(admin);
							}
							break;
						case ADMIN_LOGIN:
							if (args[1].equals(SUCCESS)) {
								// Disable login buttons
								client.sendMessage(ChatProtocol.JOIN_CHATROOM, DEFAULT_CHATROOM);
								Boolean admin = true;
								gui.login(admin);
							}
							break;	
						case LOGOUT:
							if (args[1].equals(SUCCESS)) {
								// Enable login buttons
								gui.logout();
								running = false;
							}
							break;
						case JOIN_CHATROOM: // Already existing chatroom with more than 2 users
							if (args[1].equals(SUCCESS)) {
								id = Integer.valueOf(args[2]);
								gui.addChat(id);
							}
							break;
						case LEAVE_CHATROOM:
							if (args[1].equals(SUCCESS)) {
								// Remove chatroom window
								id = Integer.valueOf(args[2]);
								gui.removeChat(id);
							}
							break;
						case CREATE_CHATROOM: // Newly created chatroom. Both of the two initial users will call this.
							if (args[2].equals(SUCCESS)) {
								id = Integer.valueOf(args[1]);
								gui.addChat(id);
							}
							break;
						case SET_CHATROOM_TITLE:
							if (args[1].equals(SUCCESS)) {
								// Set chatroom title
							}
							break;
						case USER_JOINED:
							if (args[2].equals(SUCCESS)) {
								// update client user list;
								gui.addLoggedinUser(Integer.valueOf(args[1]), Integer.valueOf(args[3]), args[4]);
							}
							break;
						case USER_LEFT:
							// update client user list;
							break;
						case USER_KICKED:
							if (args[1].equals(SUCCESS)) {
								// kick user
								String selectedUser = args[2];
							}
						case SEND_FILE:
							if (args[1].equals(SUCCESS)) {
								
								String selectedUser = args[2];
							}
							break;
						default:
							throw new UnsupportedOperationException();
					}
		
				} catch (UnsupportedOperationException e) {
					System.out.println("ERROR - Invalid command: '"+str+"', by: SERVER");
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
