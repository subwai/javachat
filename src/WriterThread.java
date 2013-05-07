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
					Socket echo_client = chatroom.pop_echo_client();
					if (echo_client != null) {
						echo_client.getOutputStream().write(msg.getBytes());
					} else {
						for (Socket c : chatroom.get_clients()) {
							c.getOutputStream().write(msg.getBytes());
						}
					}
				}
			}
		} catch (Exception e) {
			
		}
	}
}
