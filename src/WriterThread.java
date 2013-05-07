import java.net.Socket;


public class WriterThread extends Thread {
	private Chatroom chatroom;
	
	public WriterThread(Chatroom chatroom) {
		this.chatroom = chatroom;
	}
	
	public void run() {
		try {
			while(true) {
				String msg = chatroom.pop();
				if (!msg.isEmpty()) {
					for (Socket c : chatroom.get_clients()) {
						c.getOutputStream().write(msg.getBytes());
					}
				}
			}
		} catch (Exception e) {
			
		}
	}
}
