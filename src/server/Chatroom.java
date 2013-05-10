package server;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class Chatroom {
	private String string;
	private volatile Vector<Socket> users;
	
	public Chatroom(Socket firstUser){
		string = "";
		users = new Vector<Socket>(Arrays.asList(firstUser));
	}
	
	synchronized void addUser(Socket user) {
		users.add(user);
	}
	
	synchronized void removeUser(Socket user) {
		users.remove(user);
	}
	
	public List<Socket> getUsers() {
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
