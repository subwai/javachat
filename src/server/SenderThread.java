package server;

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
				String msg = chatroom.popMessage();
				if (!msg.isEmpty()) {
					for (User user : chatroom.getUsers()) {
						user.getOutputStream().write((ChatProtocol.MESSAGE+" "+id+" "+msg).getBytes());
					}
				}
			}
		} catch (Exception e) {
			
		}
	}
}
