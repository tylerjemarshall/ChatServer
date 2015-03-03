package client;



import game.TicTacToe;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
//import java.util.Scanner;


/**
 * @author marshall0530
 */
/**
 * @author Tyler M
 *
 */
public class GUIConsole extends JFrame implements ChatIF {
	final public static int DEFAULT_PORT = 5555;
	
	private static final long serialVersionUID = 1L;
//	private JButton closeB = new JButton("Close");
//	private JButton openB = new JButton("Open");
	private JButton sendB = new JButton("Send");
	private JButton quitB = new JButton("Quit");
		
//	private JTextField portTxF = new JTextField(5555);
//	private JTextField hostTxF = new JTextField("127.0.0.1");
	private JTextField messageTxF = new JTextField("");
		
//	private JLabel portLB = new JLabel("Port: ", JLabel.RIGHT);
//	private JLabel hostLB = new JLabel("Host: ", JLabel.RIGHT);
	//private JLabel messageLB = new JLabel("Message: ", JLabel.RIGHT);
		
	private JTextArea messageList = new JTextArea();
	
	private ChatClient client;
	private Profile profile = new Profile();
	private String[] args;
	
	
	
	//Makes the client accessible from LoginDialog
	//static GUIConsole clientConsole;
	
	public ChatClient getChatClient()
	{
		return this.client;
	}
	
	public void setChatClient(ChatClient chatClient)
	{
		this.client = chatClient;
	}
	
	public String[] getArgs()
	{
		
		return null;
	}
	
	
	//private boolean controlsEnabled = false;


	public  GUIConsole ( String[] args)
	{
		super("Simple Chat GUI");
		this.args = args;
		
	    //String host = args[0];
	    //int port = Integer.parseInt(args[1]);
	    //String userName = args[2];
		
			
		setSize(300, 400);
	
		messageList.setWrapStyleWord(true);
		messageList.setSize(300, 1000);

	    profile.setFont("Tahoma",Font.PLAIN,12);
		messageList.setFont(profile.getFont());

		setLayout( new BorderLayout(5,5));
		final Panel southBox = new Panel();
			final Panel southInput = new Panel();
			final Panel southButtons = new Panel();
		add( "Center", messageList );
		
		add( "South" , southBox);
		southBox.setLayout( new GridLayout(2,1,5,5));
			southBox.add(southInput);
				southInput.setLayout(new GridLayout(1, 1, 5, 5));
				southInput.add(messageTxF);
			southBox.add(southButtons);
				southButtons.setLayout(new GridLayout(1, 2, 5, 5));
				southButtons.add(quitB);
				southButtons.add(sendB);
		
		
		messageList.setLineWrap(true);
		messageList.setEditable(false);
		
		JScrollPane scroll = new JScrollPane (messageList);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	          scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    add(scroll);
				
	    
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
	    
	    
//		southBox.add(southInput);
		
		
		
//		south.setLayout( new GridLayout(1, 1, 5, 5));
		
		//bottom.add(hostLB); 		bottom.add(hostTxF);
		//bottom.add(portLB); 		bottom.add(portTxF);
		//bottom.add(messageLB); 		
//		southBox.add(messageTxF);
//		south.add(quitB);			south.add(sendB);
		 		
		//going to remove close and open, and host and port
		 		//bottom.add(closeB);
		 		//bottom.add(openB);
		  	

//		hostTxF.setText(args[0]);
//		portTxF.setText("" + Integer.parseInt(args[1]));
		

		//This handles closing the client with the X Button
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        if (JOptionPane.showConfirmDialog(southBox, 
		            "Are you sure to close this window?", "Really Closing?", 
		            JOptionPane.YES_NO_OPTION,
		            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
		        	client.handleMessageFromClientUI("Good Bye!");
		        	try 
		        	{
						client.closeConnection();
					} 
		        	catch (IOException e) 
		        	{
						display("Unable to close connection, terminating client");
					}
		        	finally
		        	{
		        		System.exit(0);
		        	}
		        }
		}});
		
		//This establishes the connection and welcomes the user
		{
			{
				LoginDialog loginDlg = new LoginDialog(args, this);
		        loginDlg.setVisible(true);
		        // if Login successfully
		        if(loginDlg.isSucceeded()){
		            System.out.println("Success!");
		        }
		        else 
	        	{
	        		System.out.println("Failed to login, trying again.");
	        	}
			}
		}
		
		// This handles the Enter Key being pressed when sending message
		messageTxF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleGUIMessage();
			}
		});
		// This handles the Send Button being pressed, same as above
		sendB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleGUIMessage();
			}
		});
		//This handles when the Quit button is pressed
		quitB.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(southBox, 
			            "Are you sure to close this window?", "Really Closing?", 
			            JOptionPane.YES_NO_OPTION,
			            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			        	client.handleMessageFromClientUI("Good Bye!");
			        	try 
			        	{
							client.sendToServer("#logout");
						} 
			        	catch (IOException ioe) 
			        	{
							display("Unable to close connection, terminating client");
						}
			        	finally
			        	{
			        		System.exit(0);
			        	}
			        }
			}
		});
		
//		//This handles the Close Connection button
//		closeB.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				client.handleMessageFromClientUI("Good Bye!");
//				try 
//				{
//					client.sendToServer("#logout");
//					//client.closeConnection();
//				} 
//				catch (IOException e1) 
//				{
//					display("Failed to close connection!");	
//		}}});
		
//		//This handles the Open Connection button.
//		openB.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				try {
//					client.tryToConnect(hostTxF.getText(), Integer.parseInt(portTxF.getText()));
//					//controlsEnabled = true;
//				} catch (Exception e1) {
//					display(e1.toString());
//					//controlsEnabled = false;
//				}
//			}
//		});
		setVisible(true);
	}

	
	/**
	 * Method handles when the GUI tries to send a message.
	 */
	
	private void handleGUIMessage() {
		if (messageTxF.getText().isEmpty()) {
			display("Please Enter Something");
		} else if (true) {
			
			String msg = messageTxF.getText();
			System.out.println("msg: " + msg);
			if(msg.indexOf("#") == 0)
				handleGUIClientCommand(msg);	
			else
				send();
		} //else
			//display("Error: Controls are disabled");
	}

	
	/**
	 * Handles commands from within the GUI that pertains to only the GUI.
	 * 
	 * @param msg The command being sent for GUIConsole to handle.
	 */
	private void handleGUIClientCommand(String msg) {
		//makes command case insensitive
		  msg = msg.toLowerCase().trim();
		  
		  //declarations
		  String cmd = "";
		  cmd = (msg.indexOf(" ") == -1) ? msg : msg.substring(0, msg.indexOf(" "));
		  int end = msg.length();
		  int space = (msg.indexOf(" ") == -1) ? end : msg.indexOf(" ");
		  String truncMsg = msg.substring(space, end).trim();  	  
		  switch (cmd)
		  {
		  case "#game":

				TicTacToe game = new TicTacToe(this);
				//game.setClient(this);
				//game.set
				game.setOnline(true);
//				try {
//					client.sendToServer(msg);
//				} catch (IOException e2) {
//					this.display("Failed to send command to server.");
//				}
				break;
		  
		  
		  
		  case "#fontsize": 
			  profile.setFontSize(Integer.parseInt(truncMsg));
			  messageList.setFont(profile.getFont());	  
			  messageTxF.setText("");
			  break;
		  case "#fontstyle":
			  profile.setFontStyle(truncMsg);
			  messageList.setFont(profile.getFont());	  
			  messageTxF.setText("");
			  break;
		  case "#fontname":
			  profile.setFontName((truncMsg));
			  messageList.setFont(profile.getFont());	  
			  messageTxF.setText("");
			  break;
		   default:
			   send();
		  }
	}


	/**
	 * Implemented Methods from ChatIF
	 */
	
	@Override
	public void display(String message) {
			messageList.insert("> " + message + "\n", 0);
			//messageList.setCaretPosition(0);
	}

	public void display(String message, String user) 
	{
		messageList.insert(user + "> " + message + "\n", 0);
		//messageList.setCaretPosition(0);
	}

	/**
	 * Method to send message to the Server
	 */
	public void send() {
		if(!client.isConnected())
		{
			LoginDialog loginDlg = new LoginDialog(args, this);
	        loginDlg.setVisible(true);
			
		}
		client.handleMessageFromClientUI(messageTxF.getText());
		messageTxF.setText("");
		//messageList.setCaretPosition(0);
	}
	/**
	 * Main method, creates the JFrame for GUIConsole.
	 * 
	 * @param args
	 * @param args[0] = host
	 * @param args[1] = port
	 * @param args[2] = userName
	 * @param args[3] = room (not yet)
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//Initializes temporary local variables
		String userName = "";
	    String host = "";
	    int port = 0;

	    try//tries to set host to command line args
	    {
	      host = args[0];
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	    	args[0] = "localhost";
//	    	host = "localhost";
	    }
	    
	    try//tries to set port to command line args
	    {
	      port = Integer.parseInt(args[1]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	    	args[1] = DEFAULT_PORT + "";
//	      port = DEFAULT_PORT; //Set port to 5555
	    }
	    try//tries to set userName to command line.
	    {	
	      userName = args[2]; //Get port from command line
	    }
	    catch(Throwable t)
	    {
	    	args[2] = "User";
//	      userName = "User"; //Set userName to "User" if fails
	    }
	    
	    
	    
	    //while(!login(new JFrame("Login"), args));
	    //login(new JFrame("Login"), args);	
	    
		GUIConsole clientConsole = new GUIConsole(args);
	
	    
	}
	
	
	


}
