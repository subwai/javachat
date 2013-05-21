package server;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import shared.ChatProtocol;


public class Chatroom {
	public static final String SUCCESS = "1";
	public static final String FAIL = "0";

	private String title;
	private volatile SimpleEntry<ChatProtocol,String[]> nextMessage;
	private volatile Vector<User> users;
	
	public Chatroom(User firstUser) {
		title = "#";
		nextMessage = null;
		users = new Vector<User>(Arrays.asList(firstUser));
	}
	
	synchronized public List<User> getUsers() {
		return users;
	}
	
	synchronized public void addUser(User user) {
		users.add(user);
	}
	
	synchronized public void removeUser(User user) {
		users.remove(user);
	}
	
	synchronized public String getTitle() {
		return title;
	}
	
	synchronized public void setTitle(String title) {
		this.title = title;
	}
	
	synchronized void pushMessage(ChatProtocol type, String... args){
		try {
			while (nextMessage != null) {
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyAll();
		nextMessage = new SimpleEntry<ChatProtocol, String[]>(type, args);
	}
	
	synchronized public SimpleEntry<ChatProtocol,String[]> popMessage(){
		try {
			while (nextMessage == null) {
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleEntry<ChatProtocol,String[]> temp = nextMessage;
		nextMessage = null;
		notifyAll();
		return temp;
	}
}
