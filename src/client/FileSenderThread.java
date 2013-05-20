package client;
	import java.net.*;
import java.io.*;
	public class FileSenderThread extends Thread {
		 ServerSocket serverSocket;
		 File file;
		
		public FileSenderThread(ServerSocket socket, File file){
			serverSocket = socket;
			this.file = file;
			try {
				serverSocket.setSoTimeout(10000);
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	 
	     public void run() {
	          
	    	 try{
	              Socket socket = serverSocket.accept();
	              System.out.println("Accepted connection : " + socket);
	              byte [] bytearray  = new byte [(int)file.length()];
	              FileInputStream fin = new FileInputStream(file);
	              BufferedInputStream bin = new BufferedInputStream(fin);
	              bin.read(bytearray,0,bytearray.length);
	              OutputStream os = socket.getOutputStream();
	              System.out.println("Sending Files...");
	              os.write(bytearray,0,bytearray.length);
	              os.flush();
	              socket.close();
	              System.out.println("File transfer complete");
	              
	    	 } catch(SocketTimeoutException e){
	    		 try {
					serverSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	    	 } 
	    	 
	    	 catch(Exception e){
	    		 e.printStackTrace();
	    	 }
	            }
	}
