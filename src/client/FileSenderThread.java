package client;

import java.net.*;
import java.io.*;

public class FileSenderThread extends Thread {
	 ClientGUI gui;
	 int chatid;
	 int userid;
	 String host;
	 int port;
	 File file;
	
	public FileSenderThread(ClientGUI gui, int chatid, int userid, String host, int port, File file){
		this.gui = gui;
		this.chatid =chatid;
		this.userid = userid;
		this.host = host;
		this.port = port;
		this.file = file;
	}
	 
     public void run() {
          
		try {
			Socket socket = new Socket(InetAddress.getByName(host), port);
			// System.out.println("Accepted connection : " + socket);
			byte [] bytearray  = new byte [(int)file.length()];
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);
			bin.read(bytearray,0,bytearray.length);
			OutputStream os = socket.getOutputStream();
			//System.out.println("Sending Files...");
			os.write(bytearray,0,bytearray.length);
			os.flush();
			socket.close();
			//System.out.println("File transfer complete");
		  
		} catch(SocketTimeoutException e){
			// client.fileTransferTimedOut(id, file.getName());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
    }
}
