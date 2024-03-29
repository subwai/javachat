package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

import shared.ChatProtocol;

public class User {
	private int id;
	private String name;
	private Vector<Integer> currentChatrooms;
	private Socket socket;
	
	public User(int id, String name, Socket socket) {
		this.id = id;
		this.name = name;
		this.socket = socket;
		currentChatrooms = new Vector<Integer>();
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean joinChatroom(Integer id) {
		return currentChatrooms.add(id);
	}
	
	public boolean leaveChatroom(Integer id) {
		return currentChatrooms.remove(id);
	}
	
	public Vector<Integer> getChatrooms() {
		return currentChatrooms;
	}
	
	public void closeConnection() throws IOException {
		socket.close();
	}

	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
	
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}

	public void sendMessage(ChatProtocol type, String... args) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(getOutputStream()));
		StringBuilder sb = new StringBuilder(type.toString());
		for(String arg : args) {
			sb.append(" "+arg);
		}
		
		try {
			writer.write(sb.toString());
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
