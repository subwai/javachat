package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shared.ChatProtocol;


public class ListenerThread extends Thread {
	private Socket user;
	private BufferedReader reader;
	private ChatMessageHandler handler;
	
	public ListenerThread(String name, Socket user, ChatMessageHandler handler) {
		super(name);
		try {
			this.user = user;
			this.handler = handler;
			
			reader = new BufferedReader(new InputStreamReader(user.getInputStream()));
			handler.joinChatroom(0, user);
			System.out.println("SYSTEM: Client joined chatroom: "+getName()+handler.getChatroom(0));
		} catch (Exception e) {
			
		}
	}
	
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
						Chatroom chat = handler.getChatroom(id);
						chat.pushMessage(this.getName()+": "+args[2]+"\n");
						break;
					case LOGIN:
						this.setName(args[1]);
						break;
					case LOGOUT:
						handler.leaveAllChatrooms(user);
						user.close();
						running = false;
						break;
					case JOIN_CHATROOM:
						id = Integer.valueOf(args[1]);
						handler.joinChatroom(id, user);
						break;
					case CREATE_CHATROOM:
						id = Integer.valueOf(args[1]);
						handler.createChatroom(id, user);
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
