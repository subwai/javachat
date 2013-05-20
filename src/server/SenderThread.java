package server;

import java.util.AbstractMap.SimpleEntry;

import shared.ChatProtocol;


public class SenderThread extends Thread {
	private Chatroom chatroom;
	private int id;
	
	public SenderThread(Chatroom chatroom, int id) {
		this.chatroom = chatroom;
		this.id = id;
	}
	
	@Override
	public void run() {
		try {
			while(true) {
				SimpleEntry<ChatProtocol, String[]> msg = chatroom.popMessage();
				StringBuilder sb = new StringBuilder(msg.getKey().toString());
				sb.append(" "+id);
				for(String arg : msg.getValue()) {
					sb.append(" "+arg);
				}
				sb.append("\n");
				for (User user : chatroom.getUsers()) {
					user.getOutputStream().write(sb.toString().getBytes());
				}
			}
		} catch (Exception e) {
			
		}
	}
}
