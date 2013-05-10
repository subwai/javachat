package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import shared.ChatProtocol;


public class ListenerThread extends Thread {
	public static final int DEFAULT_CHATROOM = 0;
	public static final int SUCCESSED = 1;
	public static final int FAILED = 0;
	
	private User user;
	private BufferedReader reader;
	private BufferedWriter writer;
	private ChatServer server;
	
	public ListenerThread(String name, User user, ChatServer server) {
		super(name);
		try {
			this.user = user;
			this.server = server;
			
			reader = new BufferedReader(new InputStreamReader(user.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(user.getOutputStream()));
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
						writer.write(ChatProtocol.LOGIN+" "+SUCCESSED);
						break;
					case LOGOUT:
						server.leaveAllChatrooms(user);
						writer.write(ChatProtocol.LOGOUT+" "+SUCCESSED);
						user.closeConnection();
						running = false;
						break;
					case JOIN_CHATROOM:
						id = Integer.valueOf(args[1]);
						server.joinChatroom(id, user);
						writer.write(ChatProtocol.JOIN_CHATROOM+" "+SUCCESSED);
						break;
					case LEAVE_CHATROOM:
						id = Integer.valueOf(args[1]);
						server.leaveChatroom(id, user);
						writer.write(ChatProtocol.LEAVE_CHATROOM+" "+SUCCESSED);
						break;
					case CREATE_CHATROOM:
						server.createChatroom(user);
						writer.write(ChatProtocol.CREATE_CHATROOM+" "+SUCCESSED);
						break;
					case SET_CHATROOM_TITLE:
						id = Integer.valueOf(args[1]);
						chat = server.getChatroom(id);
						chat.setTitle(args[2]);
						writer.write(ChatProtocol.SET_CHATROOM_TITLE+" "+SUCCESSED);
					default:
						writer.write(FAILED);
						throw new Exception();
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR - Invalid command: '"+str+"', by: "+this.getName());
		}
	}
}
