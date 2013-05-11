package client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import shared.ChatProtocol;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


/*
 * The Client with its GUI
 */
public class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// will first hold "Username:", later on "Enter message"
	private JLabel label;
	// to hold the Username and later on the messages
	private JTextField tf;
	// to Logout and get the list of the users
	private JButton login, logout;
	// for the chat room
	private JTextArea ta;
	// if it is for connection
	private boolean connected;
	
	private ChatClient client;
	
	private DefaultListModel nameListModel;
	private JList nameList;
	
	public static void main(String[] args) {
		new ClientGUI("localhost", 3000);
	}
	
	// Constructor connection receiving a socket number
	ClientGUI(String host, int port) {
		super("Chat Client");
		client = new ChatClient(host, port);
		
		// The northPanel which is the chat room
		ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
		JPanel northPanel = new JPanel(new GridLayout(1,1));
		northPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		
		// The centerPanel with:
		JPanel centerPanel = new JPanel(new GridLayout(3,1));
		// the Label and the TextField
		label = new JLabel("Enter your username below", SwingConstants.CENTER);
		centerPanel.add(label);
		tf = new JTextField("Anonymous");
		tf.setBackground(Color.WHITE);
		centerPanel.add(tf);

		
		
		//east namepanel
		JLabel users = new JLabel("Online Users", SwingConstants.CENTER);
		nameListModel = new DefaultListModel();
		nameList = new JList(nameListModel);
		nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nameList.setPrototypeCellValue("123456789012");
		nameList.addListSelectionListener(new NameSelectionListener());
		JScrollPane p1 = new JScrollPane(nameList);
		JPanel eastpanel = new JPanel();
		eastpanel.setLayout(new BorderLayout(2, 1));
		eastpanel.add(users,BorderLayout.NORTH);
		eastpanel.add(p1, BorderLayout.CENTER);

		// the 3 buttons
		login = new JButton("Login");
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		logout.setEnabled(false);		// you have to login before being able to logout

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout(1, 2));
		southPanel.add(login, BorderLayout.WEST);
		southPanel.add(logout, BorderLayout.EAST);
		
		add(centerPanel, BorderLayout.NORTH);
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
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	// called by the GUI is the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);
		label.setText("Enter your username below");
		tf.setText("Anonymous");
		// don't react to a <CR> after the username
		tf.removeActionListener(this);
		connected = false;
	}
		
	/*
	* Button or JTextField clicked
	*/
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// if it is the Logout button
		if(o == logout) {
			client.sendMessage(ChatProtocol.LOGOUT, "");
			return;
		}
		// ok it is coming from the JTextField
		if(connected) {
			// just have to send the message
		//TODO send message tf.getText() to server.	
			client.sendMessage(ChatProtocol.MESSAGE, tf.getText());				
			tf.setText("");
			return;
		}
		

		if(o == login) {
			// ok it is a connection request
			String username = tf.getText().trim();
			// empty username ignore it
			if(username.length() != 0) {
				client.sendMessage(ChatProtocol.LOGIN, username);
			}
			// disable login button/
			login.setEnabled(false);
			// enable the 2 buttons
			logout.setEnabled(true);
			
			//TODO Send login request to server.
			// Action listener for when the user enter a message
			tf.addActionListener(this);
		}

	}
	
	private void UpdateNameList() {
		nameListModel.removeAllElements();
        ArrayList<String> names =  null;
        for(String name: names){
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
				return;
			}
			String Name = (String) nameList.getSelectedValue();
			//TODO send chatroom request to server with wanted chatpartner "Name"
		}
	}
}

