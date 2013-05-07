import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;


public class ReaderThread extends Thread {
	private Socket client;
	private BufferedReader reader;
	private ChatMessageHandler handler;
	
	public ReaderThread(String name, Socket client, ChatMessageHandler handler) {
		super(name);
		try {
			this.client = client;
			this.handler = handler;
			
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			handler.joinChatroom(0, client);
			System.out.println("SYSTEM: Client joined chatroom: "+getName()+handler.getChatroom(0));
		} catch (Exception e) {
			
		}
	}
	
	public void run() {
		try {
			
			while(true) {
				String str = reader.readLine();
				if (str.startsWith("/M ")) {
					//chatroom.set(getName()+": "+str.substring(3)+"\n");
				}
			}
		} catch (Exception e) {
			
		}
	}
}
