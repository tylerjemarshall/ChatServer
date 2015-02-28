package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
 
public class LoginDialog extends JDialog {
 
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//    private JTextField tfUsername;
//    private JPasswordField pfPassword;
//    private JLabel lbUsername;
//    private JLabel lbPassword;
//    private JButton btnLogin;
//    private JButton btnCancel;
    private boolean succeeded;
 
	// My Font.
	private Font font = new javax.swing.plaf.FontUIResource("Tahoma",Font.BOLD,16);
	
	// Text Fields
	private JTextField hostTxF;// = new JTextField(args[0]); //host
	private JTextField portTxF;// = new JTextField(args[1]); //port
	private JTextField userTxF;// = new JTextField(args[2]); //user
	private JPasswordField passPwF = new JPasswordField(); //user
	
	// Labels
	private JLabel welcomeLB = new JLabel("Welcome!", JLabel.CENTER);
	
	private JLabel portLB = new JLabel("Port: ", JLabel.LEFT);
	private JLabel hostLB = new JLabel("Host: ", JLabel.LEFT);
	private JLabel userLB = new JLabel("Username: ", JLabel.LEFT);
	private JLabel passLB = new JLabel("Password: ", JLabel.LEFT);
	
	private  JLabel displayLB = new JLabel("Error messages go here", JLabel.CENTER);
	
	//Buttons
	private JButton loginBtn = new JButton("Login");
	private JButton exitBtn = new JButton("Exit");

 
	//Passed Variables
    private ChatClient client;
    private Frame parent;
    private String[] args;
    
    
    public LoginDialog(String[] args, Frame parent, ChatClient client ) {
        super(parent, "Login", true);
        this.client = client;
        this.parent = parent;
        this.args = args;
        //
//        JPanel panel = new JPanel(new GridBagLayout());
//        GridBagConstraints cs = new GridBagConstraints();
 
//        cs.fill = GridBagConstraints.HORIZONTAL;
 
//        lbUsername = new JLabel("Username: ");
//        cs.gridx = 0;
//        cs.gridy = 0;
//        cs.gridwidth = 1;
//        panel.add(lbUsername, cs);
        
        
//        
// 
//        tfUsername = new JTextField(20);
//        cs.gridx = 1;
//        cs.gridy = 0;
//        cs.gridwidth = 2;
//        panel.add(tfUsername, cs);
// 
//        lbPassword = new JLabel("Password: ");
//        cs.gridx = 0;
//        cs.gridy = 1;
//        cs.gridwidth = 1;
//        panel.add(lbPassword, cs);
// 
//        pfPassword = new JPasswordField(20);
//        cs.gridx = 1;
//        cs.gridy = 1;
//        cs.gridwidth = 2;
//        panel.add(pfPassword, cs);
//        panel.setBorder(new LineBorder(Color.GRAY));
// 
//        btnLogin = new JButton("Login");
// 
        
        hostTxF = new JTextField(args[0]);
        portTxF = new JTextField(args[1]);
        userTxF = new JTextField(args[2]);
        
        
    	
    	loginBtn.setFont(font);
    	exitBtn.setFont(font);
    
    	
    	JPanel box = new JPanel();
    	
    	JPanel center = new JPanel();
    	JPanel north = new JPanel();
    	JPanel south = new JPanel();
    	

//    	
//    	super.setSize(500, 450);
//    	this.setSize(500, 450);
//    	box.setSize(500, 450);
//    	box.setFont(font);
    	

    	box.setLayout( new BorderLayout(6,5));
    	//box.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
//    	box.setBorder(new LineBorder(Color.GRAY));
    	
    	
    	box.add("North", north);
    	box.add("Center", center);
    	box.add("South", south);
    	
    	north.setLayout(new GridLayout(1, 1, 30, 30));
    	center.setLayout(new GridLayout(5, 2, 2, 2));
    	south.setLayout(new GridLayout(1, 1, 30, 30));
    	
    	north.add(welcomeLB); 		
    	
    	center.add(portLB);			center.add(portTxF);
    	center.add(hostLB);			center.add(hostTxF);
    	center.add(userLB);			center.add(userTxF);
    	center.add(passLB);			center.add(passPwF);
    	center.add(exitBtn);		center.add(loginBtn);
    	
    	south.add(displayLB);
    
    	box.setVisible(true);
        
        
        
        loginBtn.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
            	System.out.println("You pressed login!");
            	
               // if (Login.authenticate(getUsername(), getPassword())) 
                //String[] args, GUIConsole console, ChatClient client
                	if (Login.authenticate(getArgs(),getParent(), getClient())) 
                {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Hi " + getUsername() + "! You have successfully logged in.",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                    succeeded = true;
                    
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDialog.this,
                            "Invalid username or password",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    // reset username and password
                    userTxF.setText("");
                    passPwF.setText("");
                    displayLB.setText("Failed to login!");
                    succeeded = false;
 
                }
            }
        });
        //exitBtn = new JButton("Cancel");
        exitBtn.addActionListener(new ActionListener() {
 
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        JPanel bp = new JPanel();
        bp.add(loginBtn);
        bp.add(exitBtn);
 
        getContentPane().add(center, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);
 
        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }
 
    public String getUsername() {
        return userTxF.getText().trim();
    }
 
    public String getPassword() {
        return new String(passPwF.getPassword());
    }
 
    public Frame getParent() {
    	return parent;
    }
    
    public ChatClient getClient() {
		return client;
	}
    
    public String[] getArgs()
    {
    	return args;
    }


	public boolean isSucceeded() {
        return succeeded;
    }
}



//
//public static ChatClient login(JFrame frame, String[] args)
//{
//	//Initialize the return
//	ChatClient theclient = null;
//	
//	// My Font.
//	Font font = new javax.swing.plaf.FontUIResource("Tahoma",Font.BOLD,16);
//	
//	// Text Fields
//	final JTextField hostTxF = new JTextField(args[0]); //host
//	final JTextField portTxF = new JTextField(args[1]); //port
//	final JTextField userTxF = new JTextField(args[2]); //user
//	
//	// Labels
//	JLabel welcomeLB = new JLabel("Welcome!", JLabel.CENTER);
//	
//	JLabel portLB = new JLabel("Port: ", JLabel.LEFT);
//	JLabel hostLB = new JLabel("Host: ", JLabel.LEFT);
//	JLabel userLB = new JLabel("Username: ", JLabel.LEFT);
//	
//	final JLabel displayLB = new JLabel("Error messages go here", JLabel.CENTER);
//	
//	//Buttons
//	Button loginBtn = new Button("Login");
//	Button exitBtn = new Button("Exit");
//
// 
//	
//	
//	
//	loginBtn.setFont(font);
//	exitBtn.setFont(font);
//
//	frame.setSize(300, 180);
//	frame.setFont(font);
//	
//	frame.setLayout( new BorderLayout(5,5));
//	frame.getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.BLACK));
//	
//	
//	final Panel center = new Panel();
//	final Panel north = new Panel();
//	final Panel south = new Panel();
//	
//	frame.add("North", north);
//	frame.add("Center", center);
//	frame.add("South", south);
//	
//	north.setLayout(new GridLayout(1, 1, 30, 30));
//	center.setLayout(new GridLayout(4, 2, 2, 2));
//	south.setLayout(new GridLayout(1, 1, 30, 30));
//	
//	north.add(welcomeLB); 		
//	
//	center.add(portLB);			center.add(portTxF);
//	center.add(hostLB);			center.add(hostTxF);
//	center.add(userLB);			center.add(userTxF);
//	center.add(exitBtn);		center.add(loginBtn);
//	
//	south.add(displayLB);
//
//	frame.setVisible(true);
//	
//	//JButton numGenerator = new JButton("Generate Number");
//    frame.add(loginBtn);
//    
//    
//    
    
    
    
//    LoginListenerIF loginListener = new LoginListenerIF() {
//    	public ChatClient login(ChatClient client) {
//            return client;
//        }};
//    
//        
//    
//    LoginClass loginClass = new LoginClass(loginListener, args);
//    
//    loginBtn.addActionListener(loginClass);
//           
//    ActionListener actionListener = new ActionListener() {
//		public void actionPerformed(ActionEvent e) {}};
//    
//	loginBtn.addActionListener(actionListener);
//
//    return theclient;
//
//
//}
//

//
//public interface LoginListenerIF {
//    public ChatClient login(ChatClient client);
////    public ChatClient chatClient = null;
//}
//
//public class LoginClass implements ActionListener{
//    private LoginListenerIF listener;
////    public ChatClient chatClient = null;
//    
//   
//
//    public LoginClass(LoginListenerIF listener, String[] args) {
//        this.listener = listener;
//        
//    }
//
//    public void actionPerformed(ActionEvent e){
//    	//String[] args = {hostTxF.getText(), portTxF.getText(), userTxF.getText()};
//    	
//		System.out.println("Creating GUI");
////	      client= new ChatClient(args[0], Integer.parseInt(args[1]), new GUIConsole(args), args[2]);
////			ChatClient client = null;
//     if (listener != null) {
//		listener.login(client);
//     }
//    	
//    }
//}