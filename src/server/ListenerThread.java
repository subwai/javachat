package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import shared.ChatProtocol;


public class ListenerThread extends Thread {
	public static final int DEFAULT_CHATROOM = 0;
	public static final String SUCCESS = "1";
	public static final String FAIL = "0";
	
	private User user;
	private BufferedReader reader;
	private BufferedWriter writer;
	private ChatServer server;
	
	public ListenerThread(String name, User user, ChatServer server) {
		super(name);
		try {
			this.user = user;
			this.server = server;
			
			int i;
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
			while(running && (str = reader.readLine()) != null) {
				System.out.println("CLIENT: "+str);
				String[] args = str.split(" ");
				switch(ChatProtocol.valueOf(args[0])) {
					case MESSAGE:
						int id = Integer.valueOf(args[1]);
						Chatroom chat = server.getChatroom(id);
						chat.pushMessage(ChatProtocol.MESSAGE, this.getName()+": "+args[2]+"\n");
						break;
					case LOGIN:
						user.setName(args[1]);
						sendMessage(ChatProtocol.LOGIN, SUCCESS);
						break;
					case ADMIN_LOGIN:
						user.setName(args[1]);
						String pw = args[2];
						if(pw.equals("admin")){
							sendMessage(ChatProtocol.ADMIN_LOGIN, SUCCESS);
							break;
						}
						sendMessage(ChatProtocol.ADMIN_LOGIN, FAIL);
						break;
					case LOGOUT:
						server.leaveAllChatrooms(user);
						sendMessage(ChatProtocol.LOGOUT, SUCCESS);
						running = false;
						break;
					case JOIN_CHATROOM:
						id = Integer.valueOf(args[1]);
						server.joinChatroom(id, user);
						sendMessage(ChatProtocol.JOIN_CHATROOM, SUCCESS);
						break;
					case LEAVE_CHATROOM:
						id = Integer.valueOf(args[1]);
						server.leaveChatroom(id, user);
						sendMessage(ChatProtocol.LEAVE_CHATROOM, SUCCESS);
						break;
					case CREATE_CHATROOM:
						server.createChatroom(user);
						sendMessage(ChatProtocol.CREATE_CHATROOM, SUCCESS);
						break;
					case SET_CHATROOM_TITLE:
						id = Integer.valueOf(args[1]);
						chat = server.getChatroom(id);
						chat.setTitle(args[2]);
						sendMessage(ChatProtocol.SET_CHATROOM_TITLE, SUCCESS);
						break;
					case USER_KICKED:
						String kickName = args[1];
						//kick user with name kickName.
						break;
					default:
						writer.write(args[0]+" "+FAIL);
						writer.newLine();
						writer.flush();
						throw new Exception();
				}
			}
			user.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR - Invalid command: '"+str+"', by: "+user.getName());
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
