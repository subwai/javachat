package client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import shared.ChatProtocol;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


/*
 * The Client with its GUI
 */
public class Client2ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private int thisChatroom;
	// will first hold "Username:", later on "Enter message"
	private JLabel label;
	// to hold the Username and later on the messages
	private JTextField tf;
	// to Logout and get the list of the users
	private JButton sendFile, close, send;
	// for the chat room
	private JTextArea ta;
	// if it is for connection
	
	private ChatClient client;
	// the name of the other chatter
	private String chatee;
	private HashMap<String, Integer> userIds;

	private String username;
	
	private Socket socket;
	
	private File file;
	
	private Point screen;
	
	// Constructor connection receiving a socket number
	Client2ClientGUI(ChatClient client, int thisChatroom, String username, Point screen) {
		super("Chat Client");
		this.screen = screen;
		this.client = client;
		this.thisChatroom = thisChatroom;
		this.username = username;
		userIds = new HashMap<String, Integer>();
		
		// The northPanel which is the chat room
		ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
		JPanel northPanel = new JPanel(new BorderLayout(0, 2));
		label = new JLabel("Private Session with ...", SwingConstants.CENTER);
		northPanel.add(label, BorderLayout.NORTH);
		northPanel.add(new JScrollPane(ta), BorderLayout.CENTER);
		ta.setEditable(false);
		
		// The centerPanel with:
		JPanel centerPanel = new JPanel(new GridLayout(3,1));
		// the Label and the TextField
		
		tf = new JTextField("");
		tf.setBackground(Color.WHITE);
		tf.addActionListener(this);
		send = new JButton("Send");
		send.addActionListener(this);
		centerPanel.setLayout(new BorderLayout(0, 2));
		centerPanel.add(tf, BorderLayout.CENTER);
		centerPanel.add(send, BorderLayout.EAST);

		// the 3 buttons
		sendFile = new JButton("Transfer File");
		sendFile.setToolTipText("Opens a pane to select which file you wish to send to a user");
		close = new JButton("End Private Session");
		sendFile.addActionListener(this);
		close.addActionListener(this);
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout(1, 3));
		southPanel.add(sendFile, BorderLayout.WEST);
		southPanel.add(close, BorderLayout.EAST);
		southPanel.add(centerPanel, BorderLayout.NORTH);

		add(northPanel, BorderLayout.CENTER);
		add(southPanel, BorderLayout.SOUTH);

		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();
		setLocation(screen);
		
		
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				//TODO Insert leave chat command.
				Client2ClientGUI.this.client.sendMessage(ChatProtocol.LEAVE_CHATROOM, String.valueOf(Client2ClientGUI.this.thisChatroom));
			}
		});
	}

	public void append(String str) {
		ta.append(str+System.getProperty("line.separator"));
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	
	public void setFile(File file){
		this.file = file;
	}
	
	public void sendFile(WindowEvent e){
		client.sendMessage(ChatProtocol.LEAVE_CHATROOM);
		System.exit(0);
	}

	public void addChatee(int userid, String name) {
		userIds.put(name, userid);
		// This is fine since we only expect 2 person conversations. However if more people would join, this will not work.
		if (!name.equals(username)) {
			chatee = name;
			label.setText("Private Session with "+name);
			sendFile.setToolTipText("Opens a pane to select which file you wish to send to "+name);
		}
	}
	
	public void removeChatee(int userid, String name){
		userIds.remove(name);
		append(name + " has left the chat session.");
		send.setEnabled(false);
		sendFile.setEnabled(false);
		tf.removeActionListener(this);
		
		
	}
		
	/*
	* Button or JTextField clicked
	*/
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o == sendFile && chatee != null){
			screen = getLocation();
			JFrame j = new FileTransferGUI(client, chatee, userIds.get(chatee), thisChatroom, screen);
			j.setVisible(true);
			return;
		}

		if (o == close){
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			return;
		}
		if (o == send || true) { // Allways true but done to show that clicking "send" or pressing enter is valid
			if (!tf.getText().equals("")) {
				// just have to send the message
				client.sendMessage(ChatProtocol.MESSAGE, String.valueOf(thisChatroom), "\""+tf.getText()+"\"");				
				tf.setText("");
				tf.requestFocusInWindow();
			}
			return;
		}	
	}
}

