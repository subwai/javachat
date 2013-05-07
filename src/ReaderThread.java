import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;


public class ReaderThread extends Thread {
	private Socket client;
	private BufferedReader reader;
	private Chatroom chatroom;
	
	public ReaderThread(String name, Socket client, Chatroom chatroom) {
		super(name);
		try {
			this.client = client;
			this.chatroom = chatroom;
			
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			System.out.println("test");
		} catch (Exception e) {
			
		}
	}
	
	public void run() {
		try {
			
			while(true) {
				String str = reader.readLine();
				if (str.startsWith("/M ")) {
					chatroom.set(getName()+": "+str.substring(3)+"\n");
				}
				else if (str.startsWith("/E ")) {
					chatroom.set(getName()+": "+str.substring(3)+"\n", client);
				}
				else if (str.startsWith("/Q")) {
					chatroom.remove_client(client);
					client.close();
				}
				else {
					chatroom.set(getName()+": "+str+"\n");
					System.out.println(getName()+": "+str+"\n");
				}
			}
		} catch (Exception e) {
			
		}
	}
}
