import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Chatroom {
	private String string;
	private volatile List<Socket> clients;
	
	public Chatroom(){
		string = "";
		clients = new ArrayList<Socket>();
	}
	
	synchronized void add_client(Socket client) {
		clients.add(client);
	}
	
	synchronized void remove_client(Socket client) {
		clients.remove(client);
	}
	
	public List<Socket> get_clients() {
		return clients;
	}
	
	synchronized void set(String input){
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
	
	synchronized public String pop(){
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
