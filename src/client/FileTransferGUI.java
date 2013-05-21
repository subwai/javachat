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

import shared.ChatProtocol;


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

	ChatClient client;
	int chatid;
	int userid;
	String user;
	Object[] args;
	
	FileTransferGUI(ChatClient client, String user, int userid, int chatid){
		super("Send file to " +user);
		this.user = user;
		this.userid = userid;
		this.chatid = chatid;
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
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args){
		JFrame j = new FileTransferGUI(null, "Naxon", 1, 1);
		j.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == send){
			if (args != null) {
				client.setupFileSender(chatid, userid, String.valueOf(args[0]), (File)args[1]);
			}
			return;
		}
<<<<<<< HEAD
		} else if (e.getSource() == browse){
			if(chooser.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION){
=======

		if (e.getSource() == browse) {
			JFrame j = new JFrame();
			j.setVisible(true);
			if(chooser.showOpenDialog(j) == JFileChooser.APPROVE_OPTION){
>>>>>>> 463d27a3c596ed27bcfc4c131c3c31aced265d70
				
				File file = chooser.getSelectedFile();
				args = new Object[2];
				args[0] = chooser.getName(file);
				args[1] = file;
				searchPath.setText(file.getAbsolutePath());
			}
			return;
		}
	}
}