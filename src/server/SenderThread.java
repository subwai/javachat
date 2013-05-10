package server;
import java.net.Socket;


public class SenderThread extends Thread {
	private Chatroom chatroom;
	
	public SenderThread(Chatroom chatroom) {
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
