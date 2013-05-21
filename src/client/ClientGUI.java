package client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import shared.ChatProtocol;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.AbstractMap.SimpleEntry;


/*
 * The Client with its GUI
 */
public class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private final int thisChatroom = 0;
	// will first hold "Username:", later on "Enter message"
	private JLabel label;
	// to hold the Username and later on the messages
	private JTextField tf;
	// to Logout and get the list of the users
	private JButton login, logout, startSession, kick, send;
	// for the chat room
	private JTextArea ta;
	// if it is for connection
	private boolean connected, admin;
	
	private ChatClient client;
	
	private DefaultListModel nameListModel;
	private JList nameList;
	
	private HashMap<String, Integer> userIds;
	private HashMap<Integer, Client2ClientGUI> chatrooms;
	
	private String selectedUser, username;
	
	// Constructor connection receiving a socket number
	ClientGUI(String host, int port) {
		super("Chat Client");
		client = new ChatClient(host, port, this);
		chatrooms = new HashMap<Integer, Client2ClientGUI>();
		
		// The northPanel which is the chat room
		ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
		JPanel northPanel = new JPanel(new GridLayout(1,1));
		northPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		
		//east namepanel
		userIds = new HashMap<String, Integer>();
		JLabel users = new JLabel("Online Users", SwingConstants.CENTER);
		nameListModel = new DefaultListModel();
		nameList = new JList(nameListModel);
		nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nameList.setPrototypeCellValue("123456789012");
		nameList.addListSelectionListener(new NameSelectionListener());
		JScrollPane p1 = new JScrollPane(nameList);
		kick = new JButton("Kick user");
		kick.setToolTipText("Kick selected user");
		kick.addActionListener(this);
		kick.setVisible(false);
		kick.setEnabled(false);
		JPanel eastpanel = new JPanel();
		eastpanel.setLayout(new BorderLayout(2, 1));
		eastpanel.add(users,BorderLayout.NORTH);
		eastpanel.add(p1, BorderLayout.CENTER);
		eastpanel.add(kick, BorderLayout.SOUTH);


		// the 3 buttons
		login = new JButton("Login");
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		startSession = new JButton("Start private session");
		startSession.setToolTipText("Start Private Session with the user selected in the list");
		startSession.addActionListener(this);
		startSession.setVisible(true); // set to FALSE when done testing
		startSession.setEnabled(false);
		logout.setEnabled(false);		// you have to login before being able to logout


		// The centerPanel with:
		JPanel centerPanel = new JPanel();
		// the Label and the TextField
		label = new JLabel("Enter your username below", SwingConstants.CENTER);
		tf = new JTextField("Anonymous");
		tf.setBackground(Color.WHITE);
		tf.addActionListener(this);
		send = new JButton("Send");
		send.setEnabled(false);
		send.addActionListener(this);
		centerPanel.setLayout(new BorderLayout(0, 2));
		centerPanel.add(label, BorderLayout.NORTH);
		centerPanel.add(tf, BorderLayout.CENTER);
		centerPanel.add(send, BorderLayout.EAST);
		

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout(100, 2));
		southPanel.add(login, BorderLayout.WEST);
		southPanel.add(logout, BorderLayout.EAST);
		southPanel.add(startSession, BorderLayout.CENTER);
		southPanel.add(centerPanel, BorderLayout.NORTH);

		
		add(northPanel, BorderLayout.CENTER);
		add(eastpanel,BorderLayout.EAST);
		add(southPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();

	}

	// called by the Client to append text in the TextArea 
	void append(String str) {
		ta.append(str+System.getProperty("line.separator"));
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		label.setText("Enter your username below");
		tf.setText("Anonymous");
		startSession.setVisible(false);
		startSession.setEnabled(false);
		// don't react to a <CR> after the username
		tf.removeActionListener(this);
		connected = false;
	}
	
	public void pushText(int id, String message) {
		if (id == thisChatroom) {
			append(message);
		} else {
			chatrooms.get(id).append(message);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// if it is the Logout button
		if(o == logout) {
			client.sendMessage(ChatProtocol.LOGOUT);
			return;
		}
		if(o == startSession){
			client.sendMessage(ChatProtocol.CREATE_CHATROOM, String.valueOf(userIds.get(selectedUser)));
			return;
		}
		if(o == kick){
			client.sendMessage(ChatProtocol.USER_KICKED, String.valueOf(userIds.get(selectedUser)));
			return;
		}
		if(o == login) {
			// ok it is a connection request

			username = tf.getText().trim();
			// empty username ignore it
			if(username.length() != 0) {
				client.connectToServer();
				if(username.equals("admin")){
					String password = JOptionPane.showInputDialog(this, "Admin password:");
					client.sendMessage(ChatProtocol.ADMIN_LOGIN, username, password);
				} else {
					client.sendMessage(ChatProtocol.LOGIN, username);
				}
				tf.setText("");
			}
		}
		// ok it is coming from the JTextField
		if((o == send && connected) || connected) {
			if (!tf.getText().equals("")) {
				// just have to send the message
				client.sendMessage(ChatProtocol.MESSAGE, String.valueOf(thisChatroom), "\""+tf.getText()+"\"");				
				tf.setText("");
				tf.requestFocusInWindow();
			}
			return;
		}
	}
	
	protected void addChat(int chatID){
		if (chatID != thisChatroom) {
			Client2ClientGUI p2p = new Client2ClientGUI(client, chatID);
			chatrooms.put(chatID, p2p);
		}
	}
	protected void removeChat(int chatID){
		Client2ClientGUI p2p = chatrooms.remove(chatID);
		p2p.dispose();
	}
	
	public String getUsername(){
		return username;
	}

	protected void login(Boolean admin){
		this.admin = admin;
		if(admin){
			kick.setVisible(true);
		}
		// disable login button
		login.setEnabled(false);
		label.setVisible(false);
		// enable the 3 buttons
		logout.setEnabled(true);
		send.setEnabled(true);
		startSession.setVisible(true);
		startSession.setEnabled(false);
		// Action listener for when the user enter a message
		tf.addActionListener(this);
		connected = true;
		ta.setText("Welcome to the Chat room\n");
	}

	protected void logout() {
		this.admin = false;
		kick.setVisible(false);
		// enable login button
		login.setEnabled(true);
		label.setVisible(true);
		// disable the 3 buttons
		send.setEnabled(false);
		logout.setEnabled(false);
		startSession.setVisible(false);
		startSession.setEnabled(false);
		// Action listener for when the user enter a message
		tf.removeActionListener(this);
		userIds = new HashMap<String, Integer>();
		nameListModel.clear();
		connected = false;
		tf.setText("Anonymous");
		username = null;
		client.disconnectFromServer();
	}

	protected void addLoggedinUser(int chatID, int userid, String name) {
		if (chatID != 0) {
			chatrooms.get(chatID).addChatee(userid, name);
		} else {
			userIds.put(name, userid);
			nameListModel.addElement(name);
		}
	}
	

	/**
	 * A class that listens for clicks in the name list.
	 */
	class NameSelectionListener implements ListSelectionListener {
		/**
		 * Called when the user selects a name in the name list. Fetches
		 * performance dates from the database and displays them in the date
		 * list.
		 * 
		 * @param e
		 *            The selected list item.
		 */
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (nameList.isSelectionEmpty()) {
				startSession.setEnabled(false);
				selectedUser = "";
				return;
				
			}
			selectedUser = (String)nameList.getSelectedValue();
			if(admin){
				if(selectedUser.equals(username)){
					startSession.setEnabled(false);
					kick.setEnabled(false);
				}else{
					startSession.setEnabled(true);
					kick.setEnabled(true);
				}
			}else{
				if(selectedUser.equals(username)){
					startSession.setEnabled(false);
				}else{
					startSession.setEnabled(true);
				}
			}
		}
	}
}


