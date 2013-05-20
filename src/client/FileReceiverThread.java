package client;

import java.net.*;
import java.io.*;
public class FileReceiverThread extends Thread  {
	
	int size;
	File file;
	InetAddress address;
	int port;
	
	public FileReceiverThread(InetAddress address, int port, File file, int size){
		this.size = size;
		this.file = file;
		 this.address = address;
		 this.port = port;
	}
 
    public void run() {
        try{
        int bytesRead;
        int currentTot = 0;
        Socket socket = new Socket(address,port);
        byte [] bytearray  = new byte [size];
        InputStream is = socket.getInputStream();
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
        socket.close();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
      }
}