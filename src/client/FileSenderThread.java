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
			byte [] bytearray  = new byte [(int)file.length()];
			FileInputStream fin = new FileInputStream(file);
			BufferedInputStream bin = new BufferedInputStream(fin);
			bin.read(bytearray,0,bytearray.length);
			OutputStream os = socket.getOutputStream();
			os.write(bytearray,0,bytearray.length);
			os.flush();
			socket.close();
			gui.fileTransferComplete(chatid, file.getName());

	} catch(SocketTimeoutException e){
		gui.fileTransferTimedOut(chatid, file.getName());

	} catch (IOException e) {
		e.printStackTrace();
	} catch(Exception e){
		e.printStackTrace();
	}
}
}
