package clientpackage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ChatClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InetAddress address;
		Socket socket;
		try{
			address = InetAddress.getByName(args[0]);
			socket = new Socket(address, Integer.valueOf(args[1]));
			
			Thread reader = new ClientReaderThread(socket);
			Thread writer = new ClientWriterThread(socket);
			reader.start();
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
