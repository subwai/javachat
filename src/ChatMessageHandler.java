import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;


public class ChatMessageHandler {
	
	private HashMap<Integer, Chatroom> chatrooms;
	
	public ChatMessageHandler() {
		chatrooms = new HashMap<Integer, Chatroom>();
	}
	
	public void addChatroom(Socket user) {
		Chatroom chatroom = new Chatroom(user);
		chatrooms.put(chatrooms.size(), chatroom);
		WriterThread out = new WriterThread(chatroom);
		out.start();
	}
	
	public Chatroom getChatroom(Integer id) {
		return chatrooms.get(id);
	}
	
	public void joinChatroom(Integer id, Socket client) {
		if (chatrooms.get(id) == null) {
			createChatroom(id, client);
		} else {
			chatrooms.get(id).add_client(client);
		}
	}
	
	public Chatroom createChatroom(Integer id, Socket client) {
		Chatroom chatroom = new Chatroom(client);
		WriterThread out = new WriterThread(chatroom);
		out.start();
		return chatroom;
	}
}
