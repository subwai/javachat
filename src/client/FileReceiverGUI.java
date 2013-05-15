package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
	
	private File file2;
	
	private File file;

	public FileReceiverGUI(Socket socket, String userRecieveFrom, File file) {
		super("File from " + userRecieveFrom);
		this.file = file;
		Border border = new LineBorder(Color.black);
		setLayout(new BorderLayout());
		setSize(700, 200);
		accept = new JButton("Accept");
		accept.addActionListener(this);
		// knapparna fungerar inte �n
		decline = new JButton("Decline");
		decline.addActionListener(this);

		// filechooser ist�llet?
		fd = new JTextField("Please choose a destination");
		fd.setColumns(40);
		fd.setBorder(border);

		fn = new UneditableTextField(file.getName());
		fn.setColumns(21);
		fn.setBorder(border);

		bottomPanel = new JPanel();
		bottomPanel.add(accept, BorderLayout.EAST);
		bottomPanel.add(decline, BorderLayout.WEST);
		add(bottomPanel, BorderLayout.SOUTH);

		browse = new JButton("Browse");
		browse.addActionListener(this);

		chooser = new JFileChooser();

		fileType = new JLabel("File of interest");

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
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		chooser = new JFileChooser();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == browse) {
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
				file2 = chooser.getSelectedFile();
				fd.setText(searchPath);
			} else {
				System.out.println("No Selection ");
			}

		} else if (e.getSource() == accept) {
			File newFile = new File(file2.getAbsolutePath() + file.getName());
			OutputStream out;
			try {
				out = new FileOutputStream(newFile);
				out.close();
				System.out.println(newFile.getName());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} else if (e.getSource() == decline) {

		}

	}

	public static void main(String[] args) {
		File f = new File("C:\\a\\b\\javachat.txt");
		f.mkdirs();
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(f.getAbsolutePath());
		JFrame j = new FileReceiverGUI(new Socket(), "Max", f);
		j.setVisible(true);
	}

}
