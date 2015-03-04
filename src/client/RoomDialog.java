package client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

//import javax.swing.border.*;

public class RoomDialog extends JDialog {

	/**
	 * 
	 */
	
	
	
	
	//Need to modify this class so it takes input from user to create a room. Validation will happen all in here.
	//Needed information from user:
	//	String name(limit to 10 characters and maybe certain characters)
	
	//	int limit (between 1 and 10)
	//	boolean private/public
	//		if private, then create a new JPasswordField for password.
	//		String password(limit to 10 characters and maybe certain characters OR can be blank)
	
	//	Need to create an Envelope / Object to send to the server which will hold the variables specified above, server will take it, and create the room then add client to the room.
	
	
	
	
	private static final long serialVersionUID = 1L;

	// private JTextField tfUsername;
	// private JPasswordField pfPassword;
	// private JLabel lbUsername;
	// private JLabel lbPassword;
	// private JButton btnLogin;
	// private JButton btnCancel;
	private boolean succeeded;

	// My Font.
	private Font font = new javax.swing.plaf.FontUIResource("Tahoma",
			Font.BOLD, 16);

	// Text Fields
	private JTextField hostTxF;// = new JTextField(args[0]); //host
	private JTextField portTxF;// = new JTextField(args[1]); //port
	private JTextField userTxF;// = new JTextField(args[2]); //user
	private JPasswordField passPwF = new JPasswordField(); // user

	// Labels
	private JLabel welcomeLB = new JLabel("Welcome!", JLabel.CENTER);

	private JLabel portLB = new JLabel("Port: ", JLabel.LEFT);
	private JLabel hostLB = new JLabel("Host: ", JLabel.LEFT);
	private JLabel userLB = new JLabel("Username: ", JLabel.LEFT);
	private JLabel passLB = new JLabel("Password: ", JLabel.LEFT);

	private JLabel displayLB = new JLabel("Error messages go here",
			JLabel.CENTER);

	// Buttons
	private JButton loginBtn = new JButton("Login");
	private JButton exitBtn = new JButton("Exit");

	// Passed Variables
	private GUIConsole parent;


	public RoomDialog(String[] args, GUIConsole parent) { //String[] args, 
		super(parent, "Login", true);
		this.parent = parent;
		
		
		
		hostTxF = new JTextField(args[0]);
		portTxF = new JTextField(args[1]);
		userTxF = new JTextField(args[2]);

		loginBtn.setFont(font);
		exitBtn.setFont(font);

		JPanel box = new JPanel();

		JPanel center = new JPanel();
		JPanel north = new JPanel();
		JPanel south = new JPanel();

		box.setLayout(new BorderLayout(6, 5));

		box.add("North", north);
		box.add("Center", center);
		box.add("South", south);

		north.setLayout(new GridLayout(1, 1, 30, 30));
		center.setLayout(new GridLayout(5, 2, 2, 2));
		south.setLayout(new GridLayout(1, 1, 30, 30));

		north.add(welcomeLB);

		center.add(portLB);		center.add(portTxF);
		center.add(hostLB);		center.add(hostTxF);
		center.add(userLB);		center.add(userTxF);
		center.add(passLB);		center.add(passPwF);
		center.add(exitBtn);	center.add(loginBtn);

		south.add(displayLB);


		getContentPane().add(box, BorderLayout.CENTER);
		
		setSize(300, 200);
		setResizable(false);
		setAlwaysOnTop(true);
		setLocationRelativeTo(parent);
		
		
		passPwF.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("You pressed login!");
				try {
					if (Login.authenticate(getArgs(), getParent())) {
						JOptionPane.showMessageDialog(RoomDialog.this, "Hi "
								+ getUsername()
								+ "! You have successfully logged in.",
								"Login", JOptionPane.INFORMATION_MESSAGE);
						succeeded = true;
						dispose();
					} else {
						JOptionPane.showMessageDialog(RoomDialog.this,
								"Invalid username or password", "Login",
								JOptionPane.ERROR_MESSAGE);
						//userTxF.setText(""); only clears password field.
						passPwF.setText("");
						displayLB.setText("Incorrect username or password!");
						succeeded = false;
					}
				} catch (IOException e1) {
					displayLB.setText("Failed to connect!");
					succeeded = false;
				}
			
			}
		});

		loginBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				System.out.println("You pressed login!");
				try {
					if (Login.authenticate(getArgs(), getParent())) {
						JOptionPane.showMessageDialog(RoomDialog.this, "Hi "
								+ getUsername()
								+ "! You have successfully logged in.",
								"Login", JOptionPane.INFORMATION_MESSAGE);
						succeeded = true;
						dispose();
					} else {
						JOptionPane.showMessageDialog(RoomDialog.this,
								"Invalid username or password", "Login",
								JOptionPane.ERROR_MESSAGE);
						//userTxF.setText(""); only clears password field.
						passPwF.setText("");
						displayLB.setText("Incorrect username or password!");
						succeeded = false;
					}
				} catch (IOException e1) {
					displayLB.setText("Failed to connect!");
					succeeded = false;
				}
			
			}
		});
		exitBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	public String getUsername() {
		return userTxF.getText().trim();
	}

	public String getPassword() {
		return new String(passPwF.getPassword());
	}

	public GUIConsole getParent() {
		return parent;
	}

	@SuppressWarnings("deprecation")
	public String[] getArgs() {
		return new String[]{hostTxF.getText(), portTxF.getText(), userTxF.getText(), passPwF.getText()};
	}

	public boolean isSucceeded() {
		return succeeded;
	}
}