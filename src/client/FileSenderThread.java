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
	String txt;

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
			byte [] byteArray  = new byte [(int)file.length()];
			BufferedReader br = new BufferedReader(new FileReader(file));
			
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while(line != null){
					sb.append(line);
					sb.append("\n");
					line= br.readLine();
					System.out.println(sb.toString());
				}
				txt = sb.toString();
				
				byteArray = txt.getBytes();
				OutputStream os = socket.getOutputStream();
				//System.out.println("Sending Files...");
				os.write(byteArray);
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
