package server;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Vector;

import shared.ChatProtocol;


public class ChatServer {
	public static final String SUCCESS = "1";
	public static final String FAIL = "0";

	private volatile HashMap<Integer, Chatroom> chatrooms;
	private volatile HashMap<Integer, User> users;
	private int chat_id = 0;

	public static void main(String[] args) {
		new ChatServer(3000);
	}
	
	public ChatServer(int port) {
		chatrooms = new HashMap<Integer, Chatroom>();
		users = new HashMap<Integer, User>();
		
		try {
			SocketAddress address = new InetSocketAddress(port); 
			ServerSocket socket = new ServerSocket();
			socket.bind(address);
			System.out.println("Chat server - started!");
			
			while(true) {
				Socket client = socket.accept();
				User user = new User(users.size(), "Guest "+users.size(), client);
				Thread in = new ListenerThread(user, this);
				in.start();
				System.out.println("Client connected: " + client.getInetAddress());
				users.put(users.size(), user);
			}

		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public User getUser(Integer id) {
		return users.get(id);
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
	
	public synchronized int createChatroom(User user) {
		return createChatroom(chat_id, user);
	}
	
	public synchronized int createChatroom(int id, User user) {
		user.joinChatroom(id);
		Chatroom chat = new Chatroom(user);
		chatrooms.put(id, chat);
		SenderThread out = new SenderThread(chat, id);
		out.start();
		chat.pushMessage(ChatProtocol.MESSAGE,"\"User has joined the chat: "+user.getName()+"\"");
		chat.pushMessage(ChatProtocol.USER_JOINED, SUCCESS, String.valueOf(user.getId()), user.getName());
		return chat_id++;
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

	public void leaveAllChatrooms(User user) {
		Vector<Integer> chatrooms = new Vector<Integer>(user.getChatrooms());
		for(int id : chatrooms) {
			leaveChatroom(id, user);
		}
	}
}