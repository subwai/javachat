package server;
import java.net.Socket;


public class NotifierThread extends Thread {
	private Chatroom chatroom;
	
	public NotifierThread(Chatroom chatroom) {
		this.chatroom = chatroom;
	}
	
	public void run() {
		try {
			while(true) {
				String msg = chatroom.popMessage();
				if (!msg.isEmpty()) {
					for (Socket user : chatroom.getUsers()) {
						user.getOutputStream().write(msg.getBytes());
					}
				}
			}
		} catch (Exception e) {
			
		}
	}
}
