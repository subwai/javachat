import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;


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
		try {
			while(true) {
				str = reader.readLine();
				switch(MessageType.valueOf(str.substring(0, 1))) {
					case MESSAGE:
						char id = str.charAt(3);
						Chatroom chat = handler.getChatroom(Integer.valueOf(id));
						chat.pushMessage(getName()+": "+str.substring(3)+"\n");
						break;
					case JOIN:
						id = str.charAt(3);
						handler.joinChatroom(Integer.valueOf(id), user);
						break;
					case CREATE:
						id = str.charAt(3);
						handler.createChatroom(Integer.valueOf(id), user);
						break;
					default:
						throw new Exception();
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR - Invalid command: '"+str+"', by: "+getName());
		}
	}
}
