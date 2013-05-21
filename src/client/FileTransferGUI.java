package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


import shared.UneditableTextField;


public class FileTransferGUI extends JFrame implements ActionListener {
	JButton send, browse;
	JFileChooser chooser;
	JPanel centerPanel;
	JPanel bottomPanel;
	JPanel topPanel;
	UneditableTextField searchPath;
	UneditableTextField progress;
	Socket socket;
	String user;
	Object[] args;
	ChatClient client;
	int id;
	
	FileTransferGUI(ChatClient client, String user, int id){
		super("Send file to " +user);
		this.user = user;
		this.id = id;
		chooser = new JFileChooser();
		this.client = client;
		Border border = new LineBorder(Color.black);
		setLayout(new BorderLayout());
		setSize(400, 130);
		send = new JButton("Send");
		send.addActionListener(this);
		browse = new JButton("Browse");
		browse.addActionListener(this);
		searchPath = new UneditableTextField("");
		searchPath.setColumns(21);
		searchPath.setBorder(border);
		add(searchPath, BorderLayout.CENTER);
		add(browse, BorderLayout.WEST);
		add(send, BorderLayout.EAST);
		add(new UneditableTextField("Choose a file and press send"), BorderLayout.NORTH);
		progress = new UneditableTextField("");
		progress.setBorder(new TitledBorder(border, "Status"));
		add(progress, BorderLayout.SOUTH);
		args = new Object[4];
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args){
		JFrame j = new FileTransferGUI(null, "Naxon", 1);
		j.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == send){
			if(args[0] != null || args[1] != null){
			client.sendFileToServer(args, id);
			args = new Object[4];
			
		}
		} else if (e.getSource() == browse){
			if(chooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION){
				
				File file = chooser.getSelectedFile();
				args[0] = file;
				args[1] = chooser.getName(file);
				args[2] = user;
				args[3] = String.valueOf(id);
				searchPath.setText(file.getAbsolutePath()); 
				
				
			}
		}
		
	}
}