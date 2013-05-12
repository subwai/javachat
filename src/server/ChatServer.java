package server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Vector;

public class ChatServer {
	
	private volatile HashMap<Integer, Chatroom> chatrooms;
	private int chat_id = 0;

	public static void main(String[] args) {
		int port = 3000;
		
		try {
			SocketAddress address = new InetSocketAddress(port); 
			ServerSocket socket = new ServerSocket();
			socket.bind(address);
			
			ChatServer server = new ChatServer();
			System.out.println("Chat server - started!");
			
			int n = 1;
			while(true) {
				Socket client = socket.accept();
				User user = new User("Guest "+n, client);
				Thread in = new ListenerThread("Guest "+n, user, server);
				in.start();
				System.out.println("Client connected: " + client.getInetAddress());
				n++;
			}

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public ChatServer() {
		chatrooms = new HashMap<Integer, Chatroom>();
	}
	
	public Chatroom getChatroom(Integer id) {
		return chatrooms.get(id);
	}
	
	public synchronized void joinChatroom(Integer id, User user) {
		Chatroom c = chatrooms.get(id);
		if (c != null) {
			user.joinChatroom(id);
			c.addUser(user);
		} else {
			createChatroom(id, user);
		}
	}
	
	public synchronized void createChatroom(User user) {
		createChatroom(chat_id, user);
	}
	
	public synchronized void createChatroom(int id, User user) {
		user.joinChatroom(id);
		Chatroom chatroom = new Chatroom(user);
		chatrooms.put(id, chatroom);
		SenderThread out = new SenderThread(chatroom, id);
		out.start();
		chat_id++;
	}
	
	public synchronized void createPrivateChatroom(User user1, User user2) {
		createPrivateChatroom(chat_id, user1, user2);
	}
	
	public synchronized void createPrivateChatroom(int id, User user1, User user2) {
		Chatroom chatroom = new Chatroom(user1);
		chatrooms.put(id, chatroom);
		user1.joinChatroom(id);
		chatroom.addUser(user2);
		user2.joinChatroom(id);
		SenderThread out = new SenderThread(chatroom, id);
		out.start();
		chat_id++;
	}
	
	public void leaveChatroom(int id, User user) {
		Chatroom c = chatrooms.get(id);
		if (c != null) {
			user.leaveChatroom(id);
			c.removeUser(user);
			if (c.getUsers().isEmpty()) {
				chatrooms.remove(id);
			}
		}
	}

	public void leaveAllChatrooms(User user) {
		Vector<Integer> chatrooms = new Vector<Integer>(user.getChatrooms());
		for(int id : chatrooms) {
			leaveChatroom(id, user);
		}
	}
}