package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import shared.UneditableTextField;

public class FileReceiverGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	// to hold the path of the file destination
	private JTextField fd;
	// to hold the name of the file
	private UneditableTextField fn;
	// buttons for deciding to download or not
	private JButton accept, decline;
	private JButton browse;

	private JPanel topPanel, centerPanel, bottomPanel;
	private JPanel centerNorth, centerSouth;

	private JLabel fileType;

	private JFileChooser chooser;

	private String choosertitle, searchPath;

	public FileReceiverGUI() {

		Border border = new LineBorder(Color.black);
		setLayout(new BorderLayout());
		setSize(700, 200);
		accept = new JButton("Accept");
		accept.addActionListener(this);
		// knapparna fungerar inte än
		decline = new JButton("Decline");
		decline.addActionListener(this);

		// filechooser istället?
		fd = new JTextField("Please choose a destination");
		fd.setColumns(40);
		fd.setBorder(border);

		fn = new UneditableTextField("");
		fn.setColumns(21);
		fn.setBorder(border);

		bottomPanel = new JPanel();
		bottomPanel.add(accept, BorderLayout.EAST);
		bottomPanel.add(decline, BorderLayout.WEST);
		add(bottomPanel, BorderLayout.SOUTH);

		browse = new JButton("Browse");
		browse.addActionListener(this);

		chooser = new JFileChooser();

		fileType = new JLabel("Type of file");

		centerPanel = new JPanel();
		centerNorth = new JPanel();
		centerSouth = new JPanel();

		centerNorth.add(fd, BorderLayout.NORTH);
		centerNorth.add(browse, BorderLayout.WEST);
		centerSouth.add(fn, BorderLayout.SOUTH);
		centerSouth.add(fileType, BorderLayout.WEST);
		centerPanel.add(centerNorth, BorderLayout.NORTH);
		centerPanel.add(centerSouth, BorderLayout.SOUTH);

		add(centerPanel, BorderLayout.CENTER);

		topPanel = new JPanel();
		topPanel.add(new UneditableTextField("Please make a choice"),
				BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == browse) {
			chooser = new JFileChooser();
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle(choosertitle);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);
			//
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				System.out.println("getCurrentDirectory(): "
						+ chooser.getCurrentDirectory());
				System.out.println("getSelectedFile() : "
						+ chooser.getSelectedFile());
				searchPath = chooser.getSelectedFile().toString();
				fd.setText(searchPath);
			} else {
				System.out.println("No Selection ");
			}

		} else if (e.getSource() == accept) {

		} else if (e.getSource() == decline) {

		}

	}

	public static void main(String[] args) {
		JFrame j = new FileReceiverGUI();
		j.setVisible(true);
	}

}
