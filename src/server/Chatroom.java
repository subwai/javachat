package server;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class Chatroom {
	private String string;
	private Vector<User> users;
	
	public Chatroom(User firstUser){
		string = "";
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
