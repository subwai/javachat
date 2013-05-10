package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class ChatClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String address = args[0];
		int port = Integer.valueOf(args[1]);
		
		try {
			Socket socket = new Socket(InetAddress.getByName(address), port);
			ClientGUI gui = new ClientGUI(address, port);
			
			Thread reader = new ClientReaderThread(socket);
			reader.start();
			Thread writer = new ClientWriterThread(socket);
			writer.start();
			
			System.out.println("Start chatting:");
			
		} catch (UnknownHostException e){
			System.out.println("Felaktig serveradress");
			System.exit(0);
		} catch (NumberFormatException e){
			System.out.println("Felaktigt portnummer");
			System.exit(0);
		} catch (IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}
}
