package server;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class Chatroom {
	private String title;
	private String nextMessage;
	private Vector<User> users;
	
	public Chatroom(User firstUser) {
		title = "#";
		nextMessage = "";
		users = new Vector<User>(Arrays.asList(firstUser));
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void addUser(User user) {
		pushMessage("User has joined the chat: "+user.getName());
		users.add(user);
	}
	
	public void removeUser(User user) {
		users.remove(user);
		pushMessage("User has left the chat: "+user.getName());
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	synchronized void pushMessage(String input){
		try {
			while (!nextMessage.isEmpty()) {
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyAll();
		nextMessage = input;
	}
	
	synchronized public String popMessage(){
		try {
			while (nextMessage.isEmpty()) {
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp = nextMessage;
		nextMessage = "";
		notifyAll();
		return temp;
	}
}
