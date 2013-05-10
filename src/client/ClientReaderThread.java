package client;
import java.net.Socket;
import java.util.Scanner;


public class ClientReaderThread extends Thread {
	private Socket client;
	
	public ClientReaderThread(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			Scanner scan = new Scanner(System.in);
			String message;
			
			while(true) {
				message = scan.nextLine()+"\n";
				client.getOutputStream().write(message.getBytes());
			}
		} catch (Exception e) {
			
		}
	}
}
