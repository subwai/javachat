package client;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



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
	// to hold the server address an the port number
	private JTextField tfServer, tfPort;
	// to Logout and get the list of the users
	private JButton login, logout, whoIsIn;
	// for the chat room
	private JTextArea ta;
	// if it is for connection
	private boolean connected;
	// the Client object
	//private Client client;
	// the default port number
	private int defaultPort;
	private String defaultHost;
	
	private DefaultListModel nameListModel;
	private JList nameList;

	// Constructor connection receiving a socket number
	ClientGUI(String host, int port) {

		super("Chat Client");
		defaultPort = port;
		defaultHost = host;
		
		// The NorthPanel with:
		JPanel northPanel = new JPanel(new GridLayout(3,1));
		// the Label and the TextField
		label = new JLabel("Enter your username below", SwingConstants.CENTER);
		northPanel.add(label);
		tf = new JTextField("Anonymous");
		tf.setBackground(Color.WHITE);
		northPanel.add(tf);
		add(northPanel, BorderLayout.NORTH);

		// The CenterPanel which is the chat room
		ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		add(centerPanel, BorderLayout.CENTER);
		
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
		add(eastpanel,BorderLayout.EAST);

		// the 3 buttons
		login = new JButton("Login");
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.addActionListener(this);
		logout.setEnabled(false);		// you have to login before being able to logout

		JPanel southPanel = new JPanel();
		southPanel.add(login);
		southPanel.add(logout);
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
		// reset port number and host name as a construction time
		tfPort.setText("" + defaultPort);
		tfServer.setText(defaultHost);
		// let the user change them
		tfServer.setEditable(false);
		tfPort.setEditable(false);
		// don't react to a <CR> after the username
		tf.removeActionListener(this);
		connected = false;
	}
		
	/*
	* Button or JTextField clicked
	*/
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// if it is the Logout button
		if(o == logout) {
		//	client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
			return;
		}
		// ok it is coming from the JTextField
		if(connected) {
			// just have to send the message
		//	client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, tf.getText()));				
			tf.setText("");
			return;
		}
		

		if(o == login) {
			// ok it is a connection request
			String username = tf.getText().trim();
			// empty username ignore it
			if(username.length() == 0)
				return;
			
			// disable login button/
				login.setEnabled(false);
			// enable the 2 buttons
			logout.setEnabled(true);

			// disable the Server and Port JTextField
			// Action listener for when the user enter a message
		tf.addActionListener(this);
		}

	}

	// to start the whole thing the server
	public static void main(String[] args) {
		new ClientGUI("localhost", 1500);
	}
	
	private void UpdateNameList() {
		nameListModel.removeAllElements();
        ArrayList<String> names =  null;
        for(String name: names){
        	nameListModel.addElement(name);
        }
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
	public void valueChanged(ListSelectionEvent e) {
//		if (nameList.isSelectionEmpty()) {
//			return;
//		}
//		String Name = (String) nameList.getSelectedValue();
//		fillNameList(name);
	}
}

