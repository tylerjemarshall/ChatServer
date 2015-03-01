package client;



import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.util.Scanner;


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
	private JButton closeB = new JButton("Close");
	private JButton openB = new JButton("Open");
	private JButton sendB = new JButton("Send");
	private JButton quitB = new JButton("Quit");
		
	private JTextField portTxF = new JTextField(5555);
	private JTextField hostTxF = new JTextField("127.0.0.1");
	private JTextField messageTxF = new JTextField("");
		
	private JLabel portLB = new JLabel("Port: ", JLabel.RIGHT);
	private JLabel hostLB = new JLabel("Host: ", JLabel.RIGHT);
	private JLabel messageLB = new JLabel("Message: ", JLabel.RIGHT);
		
	private JTextArea messageList = new JTextArea();
	
	private ChatClient client;
	private Profile profile = new Profile();
	
	public ChatClient getChatClient()
	{
		return this.client;
	}
	
	public void setChatClient(ChatClient chatClient)
	{
		this.client = chatClient;
	}
	
	private boolean controlsEnabled = false;


	public  GUIConsole ( String[] args)
	{
		super("Simple Chat GUI");
		
	    String host = args[0];
	    int port = Integer.parseInt(args[1]);
	    String userName = args[2];
		
			
			setSize(300, 400);
		
			messageList.setWrapStyleWord(true);
			messageList.setSize(300, 1000);

		    profile.setFont("Tahoma",Font.PLAIN,12);
			messageList.setFont(profile.getFont());

			setLayout( new BorderLayout(5,5));
			final Panel bottom = new Panel();
			add( "Center", messageList );
			add( "South" , bottom);
			
			bottom.setLayout( new GridLayout(5,2,5,5));
			bottom.add(hostLB); 		bottom.add(hostTxF);
			bottom.add(portLB); 		bottom.add(portTxF);
			bottom.add(messageLB); 		bottom.add(messageTxF);
			bottom.add(openB); 			bottom.add(sendB);
			bottom.add(closeB); 		bottom.add(quitB);
			  	

			hostTxF.setText(host);
			
			portTxF.setText("" + port);
			
	
			//This handles closing the client with the X Button
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			addWindowListener(new java.awt.event.WindowAdapter() {
			    @Override
			    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
			        if (JOptionPane.showConfirmDialog(bottom, 
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
			while(!controlsEnabled) //Can't proceed until client is declared.
			{
//				boolean success = false;
				//while(!success)
				{
					LoginDialog loginDlg = new LoginDialog(args, this);
					
					
			        loginDlg.setVisible(true);
			        // if logon successfully
			        if(loginDlg.isSucceeded()){
			            System.out.println("Success!");
//			            success = true;
//			            loginDlg.dispose();
			            controlsEnabled = true;
			        }
			        else 
		        	{
//		        		success = false;
		        		System.out.println("Failed to login, trying again.");
		        	}
				}
				
				
			    
			    
				
//				
//				Scanner cin = new Scanner( System.in );
//				
//				try 
//			    {
//			      client= new ChatClient(host, port, this, userName);
//			      controlsEnabled = true;
//			      display("Welcome " + userName);
//			      cin.close();
//			    } 
//			    catch(IOException e) 
//			    {
//			      System.out.println("GUIConsole - Can't initialize client! (" + host + " & " + port + ").");
//			      System.out.println("Please enter new Host: ");
//			      host = cin.next();
//			      System.out.println("Please enter new Port: ");
//			      port = cin.nextInt();
//			      controlsEnabled = false; 
//			    }
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
					if (JOptionPane.showConfirmDialog(bottom, 
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
			
			//This handles the Close Connection button
			closeB.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					client.handleMessageFromClientUI("Good Bye!");
					try 
					{
						client.sendToServer("#logout");
						//client.closeConnection();
					} 
					catch (IOException e1) 
					{
						display("Failed to close connection!");	
			}}});
			
			//This handles the Open Connection button.
			openB.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						client.tryToConnect(hostTxF.getText(), Integer.parseInt(portTxF.getText()));
						controlsEnabled = true;
					} catch (Exception e1) {
						display(e1.toString());
						controlsEnabled = false;
					}
				}
			});
			setVisible(true);
	}

	
	/**
	 * Method handles when the GUI tries to send a message.
	 */
	
	private void handleGUIMessage() {
		if (messageTxF.getText().isEmpty()) {
			display("Please Enter Something");
		} else if (controlsEnabled) {
			
			String msg = messageTxF.getText();
			System.out.println("msg: " + msg);
			if(msg.indexOf("#") == 0)
				handleGUIClientCommand(msg);	
			else
				send();
		} else
			display("Error: Controls are disabled");
	}

	
	/**
	 * Handles commands from within the GUI that partains to only the GUI.
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
	}

	public void display(String message, String user) // not sure if this is a
														// correct solution
	{
		messageList.insert(user + "> " + message + "\n", 0);
	}

	/**
	 * Method to send message to the Server
	 */
	public void send() {
		client.handleMessageFromClientUI(messageTxF.getText());
		messageTxF.setText("");
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
	    	host = "localhost";
	    }
	    
	    try//tries to set port to command line args
	    {
	      port = Integer.parseInt(args[1]); //Get port from command line
	    }
	    catch(Throwable t)
	    {
	    	args[1] = DEFAULT_PORT + "";
	      port = DEFAULT_PORT; //Set port to 5555
	    }
	    try//tries to set userName to command line.
	    {	
	      userName = args[2]; //Get port from command line
	    }
	    catch(Throwable t)
	    {
	    	args[2] = "User";
	      userName = "User"; //Set userName to "User" if fails
	    }
	    
	    
	    
	    //while(!login(new JFrame("Login"), args));
	    //login(new JFrame("Login"), args);		
	    @SuppressWarnings("unused")
	    
		GUIConsole clientConsole = new GUIConsole(args);
	    
	}
	
	
	


}
