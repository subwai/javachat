package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;


public class ClientWriterThread extends Thread {
	private Socket client;
	private BufferedReader reader;
	
	public ClientWriterThread(Socket client) {
		this.client = client;
		try {
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			while(!client.isClosed()) {
				String str = reader.readLine();
				if (str == null) {
					client.close();
					break;
				}
				System.out.println(str);
			}
			System.out.println("Closed connection.");
		} catch (Exception e) {
			
		}
	}
}
