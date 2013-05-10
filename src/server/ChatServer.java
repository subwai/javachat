package server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;

public class ChatServer {
	
	private volatile HashMap<Integer, Chatroom> chatrooms;

	public static void main(String[] args) {
		int port = 3000;
		
		try {
			SocketAddress address = new InetSocketAddress(port); 
			ServerSocket socket = new ServerSocket();
			socket.bind(address);
			
			ChatServer server = new ChatServer();
			
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
		int id = chatrooms.size();
		createChatroom(id, user);
	}
	
	public synchronized void createChatroom(int id, User user) {
		user.joinChatroom(id);
		Chatroom chatroom = new Chatroom(user);
		chatrooms.put(chatrooms.size(), chatroom);
		SenderThread out = new SenderThread(chatroom);
		out.start();
	}

	public void leaveAllChatrooms(User user) {
		for(int id : user.getChatrooms()) {
			user.leaveChatroom(id);
			chatrooms.get(id).removeUser(user);
		}
	}

}