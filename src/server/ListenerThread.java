package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.SocketException;

import shared.ChatProtocol;


public class ListenerThread extends Thread {
	public static final String DEFAULT_CHATROOM = "0";
	public static final String SUCCESS = "1";
	public static final String FAIL = "0";
	
	private User user;
	private BufferedReader reader;
	private BufferedWriter writer;
	private ChatServer server;
	
	public ListenerThread(User user, ChatServer server) {
		try {
			this.user = user;
			this.server = server;
			
			int i;
			reader = new BufferedReader(new InputStreamReader(user.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(user.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String str = new String();
		boolean running = true;
		Pattern p = Pattern.compile("(?=\").+|[^\\s]+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		try {
			while(running && (str = reader.readLine()) != null) {
				try {
					System.out.println("CLIENT: "+str);
					Matcher m = p.matcher(str);
					List<String> matches = new ArrayList<String>();
					while(m.find()){
					    matches.add(m.group());
					}
					String[] args = matches.toArray(new String[0]);
					switch(ChatProtocol.valueOf(args[0])) {
						case MESSAGE:
							int id = Integer.valueOf(args[1]);
							Chatroom chat = server.getChatroom(id);
							chat.pushMessage(ChatProtocol.MESSAGE, "\""+user.getName()+": "+args[2].substring(1, args[2].length() - 1)+"\"");
							break;
						case LOGIN:
							user.setName(args[1]);
							sendMessage(ChatProtocol.LOGIN, SUCCESS, String.valueOf(user.getId()), args[1]);
							break;
						case ADMIN_LOGIN:
							String pw = args[2];
							if(pw.equals("admin")){
								user.setName(args[1]);
								sendMessage(ChatProtocol.ADMIN_LOGIN, SUCCESS);
								break;
							}
							sendMessage(ChatProtocol.ADMIN_LOGIN, FAIL);
							break;
						case LOGOUT:
							server.leaveAllChatrooms(user);
							sendMessage(ChatProtocol.LOGOUT, SUCCESS);
							running = false;
							break;
						case JOIN_CHATROOM:
							id = Integer.valueOf(args[1]);
							server.joinChatroom(id, user);
							sendMessage(ChatProtocol.JOIN_CHATROOM, SUCCESS, args[1]);
							chat = server.getChatroom(id);
							for (User u : chat.getUsers()) {
								if(u.getId() != user.getId()) {
									sendMessage(ChatProtocol.USER_JOINED, String.valueOf(id), SUCCESS, String.valueOf(u.getId()), u.getName());
								}
							}
							break;
						case LEAVE_CHATROOM:
							id = Integer.valueOf(args[1]);
							server.leaveChatroom(id, user);
							sendMessage(ChatProtocol.LEAVE_CHATROOM, SUCCESS);
							chat = server.getChatroom(id);
							chat.pushMessage(ChatProtocol.USER_LEFT, String.valueOf(id), SUCCESS, String.valueOf(user.getId()), user.getName());
							break;
						case CREATE_CHATROOM:
							id = server.createChatroom(user);
							int otherUserId = Integer.valueOf(args[1]);
							User other = server.getUser(otherUserId);
							other.joinChatroom(id);
							chat = server.getChatroom(id);
							chat.addUser(other);
							chat.pushMessage(ChatProtocol.CREATE_CHATROOM, SUCCESS);
							for (User u : chat.getUsers()) {
								chat.pushMessage(ChatProtocol.USER_JOINED, SUCCESS, String.valueOf(u.getId()), u.getName());
							}
							break;
						case SET_CHATROOM_TITLE:
							id = Integer.valueOf(args[1]);
							chat = server.getChatroom(id);
							chat.setTitle(args[2]);
							sendMessage(ChatProtocol.SET_CHATROOM_TITLE, SUCCESS);
							break;
						case USER_KICKED:
							id = Integer.valueOf(args[1]);
							//kick user with name kickName.
							User u =server.getUser(id);
							BufferedWriter tmpWriter = new BufferedWriter(new OutputStreamWriter(u.getOutputStream()));
							tmpWriter.write(ChatProtocol.LOGOUT.toString() + " " + String.valueOf(SUCCESS));
							tmpWriter.newLine();
							tmpWriter.flush();
							tmpWriter.close();
							server.leaveAllChatrooms(u);
							sendMessage(ChatProtocol.USER_KICKED, SUCCESS, u.getName());
							break;
						case SEND_FILE:
							id = Integer.valueOf(args[1]);
							int i = 0;
							String[] damp = new String[args.length - 1];
							while(args[i + 1] != null && i < 6){
								damp[i] = args[i + 1];
								i++;
							}
							sendMessage(ChatProtocol.REQUEST_ACCEPT, damp);
							sendMessage(ChatProtocol.SEND_FILE, SUCCESS);
							break;
						case RECEIVE_FILE:
							id = Integer.valueOf(args[1]);
							sendMessage(ChatProtocol.RECEIVE_FILE, SUCCESS);
							break;
						default:
							writer.write(args[0]+" "+FAIL);
							writer.newLine();
							writer.flush();
							throw new Exception();
					}
				} catch (UnsupportedOperationException e) {
					System.out.println("ERROR - Invalid command: '"+str+"', by: "+user.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			System.out.println("User disconnected: "+user.getName());
			server.leaveAllChatrooms(user);
		} catch (IOException e) {
			e.printStackTrace();
		}
		disconnect();
	}

	private void disconnect() {
		try {
			reader.close();
			writer.close();
			user.closeConnection();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(ChatProtocol type, String... args) {
		StringBuilder sb = new StringBuilder(type.toString());
		for(String arg : args) {
			sb.append(" "+arg);
		}
		
		try {
			writer.write(sb.toString());
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
