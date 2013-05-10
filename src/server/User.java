package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

public class User {
	private String name;
	private Vector<Integer> currentChatrooms;
	private Socket socket;
	
	public User(String name, Socket socket) {
		this.name = name;
		this.socket = socket;
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
		getOutputStream().write("GOODBYE".getBytes());
		socket.close();
	}

	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}
	
	public OutputStream getOutputStream() throws IOException {
		return socket.getOutputStream();
	}
}
