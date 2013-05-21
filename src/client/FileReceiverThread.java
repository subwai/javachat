package client;

import java.net.*;
import java.io.*;
public class FileReceiverThread extends Thread  {
	
	private ClientGUI gui;
    private int chatid;
    private int userid;
    private ServerSocket socket;
    private File file;
    private int size;

	public FileReceiverThread(ClientGUI gui, int chatid, int userid, ServerSocket socket, File file, int size){
        this.gui = gui;
        this.chatid = chatid;
        this.userid = userid;
        this.socket = socket;
        this.file = file;
        this.size = size;

        try {
            socket.setSoTimeout(10000);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
 
    public void run() {
        try{
        int bytesRead;
        int currentTot = 0;
        Socket sender = socket.accept();
        byte [] bytearray  = new byte [size];
        InputStream is = sender.getInputStream();
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bytesRead = is.read(bytearray,0,bytearray.length);
        currentTot = bytesRead;
 
        do {
           bytesRead =
              is.read(bytearray, currentTot, (bytearray.length-currentTot));
           if(bytesRead >= 0) currentTot += bytesRead;
        } while(bytesRead > -1);
 
        bos.write(bytearray, 0 , currentTot);
        bos.flush();
        bos.close();
        sender.close();
        socket.close();
        // client.fileTransferComplete(id, file.getName());
        }
        catch(Exception e){
        	// client.fileTransferFailed(id, file.getName());
        	e.printStackTrace();
        }
      }
}