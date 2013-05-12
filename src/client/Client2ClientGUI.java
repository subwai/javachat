
	package client;

	import javax.swing.*;
	import javax.swing.event.ListSelectionEvent;
	import javax.swing.event.ListSelectionListener;

	import shared.ChatProtocol;

	import java.awt.*;
	import java.awt.event.*;
import java.net.Socket;
import java.util.ArrayList;


	/*
	 * The Client with its GUI
	 */
	public class Client2ClientGUI extends JFrame implements ActionListener {

		private static final long serialVersionUID = 1L;
		// will first hold "Username:", later on "Enter message"
		private JLabel label;
		// to hold the Username and later on the messages
		private JTextField tf;
		// to Logout and get the list of the users
		private JButton file, close;
		// for the chat room
		private JTextArea ta;
		// if it is for connection
		
		private ChatClient client;
		// the name of the other chatter
		private String chatee;
		
		private DefaultListModel nameListModel;
		private JList nameList;
		
		private Socket socket;
		
		// Constructor connection receiving a socket number
		Client2ClientGUI(String host, int port, String user) {
			super("Chat Client 2");
			client = new ChatClient(host, port);
			chatee = user;
			
			// The northPanel which is the chat room
			ta = new JTextArea("Welcome to the Chat room\n", 80, 80);
			JPanel northPanel = new JPanel(new GridLayout(1,1));
			northPanel.add(new JScrollPane(ta));
			ta.setEditable(false);
			
			// The centerPanel with:
			JPanel centerPanel = new JPanel(new GridLayout(3,1));
			// the Label and the TextField
			label = new JLabel("Private Session with "+chatee, SwingConstants.CENTER);
			centerPanel.add(label);
			tf = new JTextField(" ");
			tf.setBackground(Color.WHITE);
			tf.addActionListener(this);
			centerPanel.add(tf);



			// the 3 buttons
			file = new JButton("Transfer File");
			file.setToolTipText("Opens a pane to select which file you wish to send to "+ chatee);
			close = new JButton("End Private Session");
			file.addActionListener(this);
			close.addActionListener(this);
			JPanel southPanel = new JPanel();
			southPanel.setLayout(new BorderLayout(1, 3));
			southPanel.add(file, BorderLayout.WEST);
			southPanel.add(close, BorderLayout.EAST);
			
			add(centerPanel, BorderLayout.NORTH);
			add(northPanel, BorderLayout.CENTER);
			
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
			
		/*
		* Button or JTextField clicked
		*/
		@Override
		public void actionPerformed(ActionEvent e) {
			Object o = e.getSource();
			if(o == file){
				JFrame j = new FileTransferGUI(socket, "Naxon");
				j.setVisible(true);
			} else if(o == close){setVisible(false);} else
			{
				// just have to send the message
			//TODO send message tf.getText() to server.	
				client.sendMessage(ChatProtocol.MESSAGE, tf.getText());				
				tf.setText("");
				return;
			}
			
				
			}

		
	}

