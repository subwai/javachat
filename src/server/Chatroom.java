package server;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import shared.ChatProtocol;


public class Chatroom {
	private String title;
	private SimpleEntry<ChatProtocol,String> nextMessage;
	private Vector<User> users;
	
	public Chatroom(User firstUser) {
		title = "#";
		nextMessage = null;
		users = new Vector<User>(Arrays.asList(firstUser));
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void addUser(User user) {
		pushMessage(ChatProtocol.MESSAGE,"User has joined the chat: "+user.getName());
		users.add(user);
	}
	
	public void removeUser(User user) {
		users.remove(user);
		pushMessage(ChatProtocol.MESSAGE,"User has left the chat: "+user.getName());
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	synchronized void pushMessage(ChatProtocol type, String input){
		try {
			while (nextMessage != null) {
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyAll();
		nextMessage = new SimpleEntry<ChatProtocol, String>(type,input);
	}
	
	synchronized public SimpleEntry<ChatProtocol,String> popMessage(){
		try {
			while (nextMessage == null) {
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleEntry<ChatProtocol,String> temp = nextMessage;
		nextMessage = null;
		notifyAll();
		return temp;
	}
}
