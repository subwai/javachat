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
        try{
            Socket sender = socket.accept();
            int bytesRead;
            byte [] buffer  = new byte [1024];
            InputStream is = sender.getInputStream();
            
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            while((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
                bos.flush();
            }
            bos.close();
            sender.close();
            socket.close();
            gui.pushText(chatid, file.getName() + " has been recieved successfully");
        }
        catch(Exception e){
        	e.printStackTrace();
    		gui.pushText(chatid, "An error occured while sending " + file.getName());
        }
    }
}