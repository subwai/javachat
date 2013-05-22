package client;

import java.net.*;
import java.io.*;
public class FileReceiverThread extends Thread  {
	
	private ClientGUI gui;
    private int chatid;
    private ServerSocket socket;
    private File file;
    private int size;

	public FileReceiverThread(ClientGUI gui, int chatid, ServerSocket socket, File file, int size){
        this.gui = gui;
        this.chatid = chatid;
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
    	int bytesRead;
        int current = 0;
        try{
        Socket sender = socket.accept();
        byte [] byteArray  = new byte [size+1];
        InputStream is = sender.getInputStream();
        
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bytesRead = is.read(byteArray,0,byteArray.length);
        current = bytesRead;
        do {
            bytesRead =
               is.read(byteArray, current, (byteArray.length-current));
            if(bytesRead >= 0){ current += bytesRead;}
         } while(bytesRead > -1);
        bos.write(byteArray, 0, current);
        bos.flush();
        sender.close();
        socket.close();
        gui.fileTransferComplete(chatid, file.getName());
        }
        catch(Exception e){
        	e.printStackTrace();
        	gui.fileTransferFailed(chatid, file.getName());
        }
      }
}