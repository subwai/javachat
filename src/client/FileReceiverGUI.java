package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import shared.UneditableTextField;

public class FileReceiverGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	//to hold the path of the file destination
	private JTextField fd;
	//to hold the name of the file
	private UneditableTextField fn;
	//buttons for deciding to download or not
	private JButton accept, decline;
	private JButton browseForDest;
	
	private JPanel topPanel, centerPanel, bottomPanel;
	private JPanel centerNorth, centerSouth;
	
	private JLabel fileType;
	
	
	public FileReceiverGUI(){
		
		Border border = new LineBorder(Color.black);
		setLayout(new BorderLayout());
		setSize(400, 200);
		accept = new JButton("Accept");
		accept.addActionListener(this);
		//knapparna fungerar inte än
		decline = new JButton("Decline");
		decline.addActionListener(this);
		
		//filechooser istället?
		fd = new JTextField("Please choose a destination");
		fd.setColumns(21);
		fd.setBorder(border);
		
		fn = new UneditableTextField("");
		fn.setColumns(21);
		fn.setBorder(border);
		
		bottomPanel = new JPanel();
		bottomPanel.add(accept, BorderLayout.EAST);
		bottomPanel.add(decline, BorderLayout.WEST);
		add(bottomPanel, BorderLayout.SOUTH);
		
		browseForDest = new JButton("Browse");
		browseForDest.addActionListener(this);
		
		fileType = new JLabel("Type of file");
		
		centerPanel = new JPanel();
		centerNorth = new JPanel();
		centerSouth = new JPanel();
		
		centerNorth.add(fd, BorderLayout.NORTH);
		centerNorth.add(browseForDest, BorderLayout.WEST);
		centerSouth.add(fn, BorderLayout.SOUTH);
		centerSouth.add(fileType, BorderLayout.WEST);
		centerPanel.add(centerNorth, BorderLayout.NORTH);
		centerPanel.add(centerSouth, BorderLayout.SOUTH);
		
		add(centerPanel, BorderLayout.CENTER);
		
		topPanel = new JPanel();
		topPanel.add(new UneditableTextField("Please make a choice"), BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public static void main(String[] args){
		JFrame j = new FileReceiverGUI();
		j.setVisible(true);
	}

}
