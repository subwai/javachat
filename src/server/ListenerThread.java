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
	public static final String DENIED = "2";
	
	private User user;
	private BufferedReader reader;
	private ChatServer server;
	
	public ListenerThread(User user, ChatServer server) {
		try {
			this.user = user;
			this.server = server;
			reader = new BufferedReader(new InputStreamReader(user.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String str = new String();
		boolean running = true;
		Pattern p = Pattern.compile("(?=\").+\"|[^\\s]+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		try {
			while(running && (str = reader.readLine()) != null) {
				try {
					System.out.println("Client: "+str);
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
							if(!server.userExists(args[1])){
								user.setName(args[1]);
								user.sendMessage(ChatProtocol.LOGIN, SUCCESS, String.valueOf(user.getId()), args[1]);
								break;
							}
							user.sendMessage(ChatProtocol.LOGIN, FAIL);
							server.removeUser(user.getId());
							break;
						case ADMIN_LOGIN:
							String pw = args[2];
							if(pw.equals("admin")){
								user.setName(args[1]);
								user.sendMessage(ChatProtocol.ADMIN_LOGIN, SUCCESS);
								break;
							}
							user.sendMessage(ChatProtocol.ADMIN_LOGIN, FAIL);
							server.removeUser(user.getId());
							break;
						case LOGOUT:
							server.leaveAllChatrooms(user);
							user.sendMessage(ChatProtocol.LOGOUT, SUCCESS);
							running = false;
							server.removeUser(user.getId());
							break;
						case JOIN_CHATROOM:
							id = Integer.valueOf(args[1]);
							server.joinChatroom(id, user);
							user.sendMessage(ChatProtocol.JOIN_CHATROOM, SUCCESS, args[1]);
							chat = server.getChatroom(id);
							for (User u : chat.getUsers()) {
								if(u.getId() != user.getId()) {
									user.sendMessage(ChatProtocol.USER_JOINED, String.valueOf(id), SUCCESS, String.valueOf(u.getId()), u.getName());
								}
							}
							break;
						case LEAVE_CHATROOM:
							id = Integer.valueOf(args[1]);
							server.leaveChatroom(id, user);
							user.sendMessage(ChatProtocol.LEAVE_CHATROOM, SUCCESS, String.valueOf(id));
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
							user.sendMessage(ChatProtocol.SET_CHATROOM_TITLE, SUCCESS);
							break;
						case USER_KICKED:
							id = Integer.valueOf(args[1]);
							//kick user with name kickName.
							User u = server.getUser(id);
							u.sendMessage(ChatProtocol.LOGOUT, SUCCESS);
							server.leaveAllChatrooms(u);
							server.removeUser(u.getId());
							user.sendMessage(ChatProtocol.USER_KICKED, SUCCESS, u.getName());
							break;
						case SEND_FILE:
							id = Integer.valueOf(args[2]);
							u = server.getUser(id);
							u.sendMessage(ChatProtocol.SEND_REQUEST, args[1], String.valueOf(user.getId()), args[3], args[4]);
							break;
						case SEND_REQUEST:
							id = Integer.valueOf(args[3]);
							u = server.getUser(id);
							if (args[1].equals(SUCCESS)) {
								u.sendMessage(ChatProtocol.SEND_FILE, SUCCESS, args[2], String.valueOf(user.getId()), args[4], args[5]);
							} else if(args[1].equals(DENIED)) {
								u.sendMessage(ChatProtocol.SEND_FILE, DENIED, args[2], args[4]);
							} else {
								u.sendMessage(ChatProtocol.SEND_FILE, FAIL, args[2], args[4]);
							}
							break;
						default:
							user.getOutputStream().write((args[0]+" "+FAIL+"\n").getBytes());
							throw new Exception();
					}
				} catch (UnsupportedOperationException e) {
					System.out.println("ERROR - Invalid command: '"+str+"', by: "+user.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (SocketException e) {
			System.out.println("Client disconnected: "+user.getName());
			server.leaveAllChatrooms(user);
			server.removeUser(user.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
		disconnect();
	}

	private void disconnect() {
		try {
			reader.close();
			user.closeConnection();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
