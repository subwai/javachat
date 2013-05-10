package server;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class Chatroom {
	private String string;
	private volatile Vector<User> users;
	
	public Chatroom(User firstUser){
		string = "";
		users = new Vector<User>(Arrays.asList(firstUser));
	}
	
	synchronized void addUser(User user) {
		pushMessage("User has joined the chat: "+user.getName());
		users.add(user);
	}
	
	synchronized void removeUser(User user) {
		users.remove(user);
		pushMessage("User has left the chat: "+user.getName());
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	synchronized void pushMessage(String input){
		try {
			while (!string.isEmpty()) {
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		notifyAll();
		string = input;
	}
	
	synchronized public String popMessage(){
		try {
			while (string.isEmpty()) {
				wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String temp = string;
		string = "";
		notifyAll();
		return temp;
	}
}
