package client;

import java.net.*;
import java.io.*;

public class FileSenderThread extends Thread {
	ClientGUI gui;
	int chatid;
	String host;
	int port;
	File file;

	public FileSenderThread(ClientGUI gui, int chatid, String host, int port, File file){
		this.gui = gui;
		this.chatid = chatid;
		this.host = host;
		this.port = port;
		this.file = file;
	}

	public void run() {

		try {
			Socket socket = new Socket(InetAddress.getByName(host), port);
			int bytesRead;
            byte [] buffer  = new byte [1024];
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);
			OutputStream os = socket.getOutputStream();
			while ((bytesRead = bin.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
				os.flush();
			}
			os.close();
			bin.close();
			socket.close();
			gui.pushText(chatid, file.getName() + " has been sent successfully");

	} catch(SocketTimeoutException e){
		gui.pushText(chatid, "An error occured while sending " + file.getName());

	} catch (IOException e) {
		e.printStackTrace();
	} catch(Exception e){
		e.printStackTrace();
	}
}
}
