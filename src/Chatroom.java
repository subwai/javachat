import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


public class Chatroom {
	private String string;
	private volatile Vector<Socket> clients;
	private volatile Socket echo_client;
	
	public Chatroom(Socket firstUser){
		string = "";
		clients = new Vector<Socket>(Arrays.asList(firstUser));
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
	
	public Socket pop_echo_client() {
		Socket temp = echo_client;
		echo_client = null;
		return temp;
	}
	
	synchronized void set(String input, Socket client){
		this.echo_client = client;
		set(input);
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
