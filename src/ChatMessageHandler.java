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
		NotifierThread out = new NotifierThread(chatroom);
		out.start();
	}
	
	public Chatroom getChatroom(Integer id) {
		return chatrooms.get(id);
	}
	
	public void joinChatroom(Integer id, Socket user) {
		if (chatrooms.get(id) == null) {
			createChatroom(id, user);
		} else {
			chatrooms.get(id).addUser(user);
		}
	}
	
	public Chatroom createChatroom(Integer id, Socket user) {
		Chatroom chatroom = new Chatroom(user);
		NotifierThread out = new NotifierThread(chatroom);
		out.start();
		return chatroom;
	}
}
