package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import shared.ChatProtocol;


public class ListenerThread extends Thread {
	public static final int DEFAULT_CHATROOM = 0;
	
	private User user;
	private BufferedReader reader;
	private ChatServer server;
	
	
	public ListenerThread(String name, User user, ChatServer server) {
		super(name);
		try {
			this.user = user;
			this.server = server;
			
			reader = new BufferedReader(new InputStreamReader(user.getInputStream()));
			server.joinChatroom(DEFAULT_CHATROOM, user);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String str = new String();
		boolean running = true;
		try {
			while(running) {
				str = reader.readLine();
				String[] args = str.split(" ");
				switch(ChatProtocol.valueOf(args[0])) {
					case MESSAGE:
						int id = Integer.valueOf(args[1]);
						Chatroom chat = server.getChatroom(id);
						chat.pushMessage(this.getName()+": "+args[2]+"\n");
						break;
					case LOGIN:
						this.setName(args[1]);
						break;
					case LOGOUT:
						server.leaveAllChatrooms(user);
						user.closeConnection();
						running = false;
						break;
					case JOIN_CHATROOM:
						id = Integer.valueOf(args[1]);
						server.joinChatroom(id, user);
						break;
					case LEAVE_CHATROOM:
						id = Integer.valueOf(args[1]);
						server.leaveChatroom(id, user);
						break;
					case CREATE_CHATROOM:
						server.createChatroom(user);
						break;
					default:
						throw new Exception();
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR - Invalid command: '"+str+"', by: "+this.getName());
		}
	}
}
