package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	FileTransferGUI(String user){
		super("Send file to " +user);
		chooser = new JFileChooser();
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
	}
	
	public static void main(String[] args){
		JFrame j = new FileTransferGUI("Naxon");
		j.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == send){
			
		} else if (e.getSource() == browse){
			JFrame j = new JFrame();
			j.add(chooser);
			j.setSize(600, 400);
			j.setVisible(true);
		}
		
	}
 
}
