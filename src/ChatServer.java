import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Vector;

public class ChatServer {


	public static void main(String[] args) {

		int port = 3000;
		try{
			SocketAddress address = new InetSocketAddress(port); 
			ServerSocket socket = new ServerSocket();
			socket.bind(address);
			
			ChatMessageHandler handler = new ChatMessageHandler();
			Socket client;
			
			int n = 1;
			while(true){
				client = socket.accept();
				Thread in = new ListenerThread("Guest "+n, client, handler);
				in.start();
				System.out.println("Client connected: " + client.getInetAddress());
				n++;
			}

		}catch(IOException e){
			e.printStackTrace();
		}

	}

}