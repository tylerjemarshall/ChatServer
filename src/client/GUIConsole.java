package client;



import game.TicTacToe;

import javax.swing.*;

import java.awt.event.*;
import java.awt.*;
import java.io.IOException;



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
	private static GUIConsole clientConsole = null;
	private JButton sendB = new JButton("Send");
	private JButton quitB = new JButton("Quit");
	private JTextField messageTxF = new JTextField("");		
	private JTextArea messageList = new JTextArea();
	
	private ChatClient client;
	private Profile profile = new Profile();
	private String[] args;
	
	
	private String[] roomArray;
	private JButton refresh = new JButton("Refresh Rooms");
	@SuppressWarnings("rawtypes")
	private JComboBox roomList = new JComboBox();
	
	private Object currentRoom = "commons";
	
	
	//Makes the client accessible from LoginDialog
	@SuppressWarnings("rawtypes")
	public void setRoomList(JComboBox arg)
	{
		this.roomList = arg;
	}
	
	@SuppressWarnings("rawtypes")
	public JComboBox getRoomList()
	{
		return this.roomList;
	}
	
	public ChatClient getChatClient()
	{
		return this.client;
	}
	
	public void setChatClient(ChatClient chatClient)
	{
		this.client = chatClient;
	}
	
	public String[] getRoomArray() {
		return roomArray;
	}

	public void setRoomArray(String[] roomArray) {
		this.roomArray = roomArray;
	}

	public String[] getArgs()
	{
		return this.args;
	}

	public  GUIConsole ( String[] args)
	{
		super("Simple Chat GUI");
		this.args = args;
	
		setSize(300, 400);
	
		messageList.setWrapStyleWord(true);
		messageList.setSize(300, 1000);

	    profile.setFont("Tahoma",Font.PLAIN,12);
		messageList.setFont(profile.getFont());

		
//		roomList.
		final Panel roomBox = new Panel();
		
		
		setLayout( new BorderLayout(0,0));
		final Panel southBox = new Panel();
			final Panel southInput = new Panel();
			final Panel southButtons = new Panel();
		add( "North" , roomBox);
			roomBox.setLayout(new GridLayout(1, 2, 5, 5));
				roomBox.add(roomList);
				roomBox.add(refresh);
		add( "Center", messageList );
		add( "South" , southBox);
			southBox.setLayout( new GridLayout(2,1,5,5));
				southBox.add(southInput);
					southInput.setLayout(new GridLayout(1, 1, 5, 5));
					southInput.add(messageTxF);
				southBox.add(southButtons);
					southButtons.setLayout(new GridLayout(1, 2, 5, 5));
					southButtons.add(quitB);	southButtons.add(sendB);
			
			
		
		//making messageList look nice
		messageList.setLineWrap(true);
		messageList.setEditable(false);
		
		JScrollPane scroll = new JScrollPane (messageList);
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    add(scroll);
	
	    
	    //centering the window to middle of screen.
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);


	    //Refreshes the combo box
	    refresh.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		
	    		client.handleMessageFromClientUI("#getlist");
	    	}
	    });
	    
		// Handles Combo Box
		roomList.addActionListener(new ActionListener() {
			@SuppressWarnings("rawtypes")
			public void actionPerformed(ActionEvent e) {
				
		        JComboBox cb = (JComboBox)e.getSource();
		        
		        String room = (String)cb.getSelectedItem();
		        Object tempRoom = cb.getSelectedItem();
		        String truncRoom = (room.indexOf("(") == -1) ? room : room.substring(0, room.indexOf("("));
		        
		        //don't change room if you are already in the room
		        if(room.equals("Create Room"))
		        	{
		        	RoomDialogue roomDlg = new RoomDialogue(getClientConsole());
			        roomDlg.setVisible(true);

		        	}
		        else if(!currentRoom.equals(tempRoom))
		        	client.handleMessageFromClientUI("#join " + truncRoom);
		        
		        
		        
		        currentRoom=tempRoom;

			}
		});
	    
	    
		//This creates the Login Dialog which will establish the connection and welcomes the user.
		LoginDialog loginDlg = new LoginDialog(args, this);
        loginDlg.setVisible(true);



        
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
		quitB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (JOptionPane.showConfirmDialog(southBox,
						"Are you sure to close this window?",
						"Really Closing?", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					client.handleMessageFromClientUI("Good Bye!");
					try {
						client.sendToServer("#logout");
					} catch (IOException ioe) {
						display("Unable to close connection, terminating client");
					} finally {
						System.exit(0);
					}
				}
			}
		});
		setVisible(true);		
	}

	
	/**
	 * Method handles when the GUI tries to send a message.
	 */
	
	private void handleGUIMessage() {
		if (!messageTxF.getText().isEmpty()) {
			String msg = messageTxF.getText();
			System.out.println("msg: " + msg);
			if(msg.indexOf("#") == 0)
				handleGUIClientCommand(msg);	
			else
				send();
		} 
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
				client.setGame(new TicTacToe(this));
				client.getGame().setOnline(true);
				try {
					client.tryToSendToServer(msg);
				} catch (Exception e) {
					e.printStackTrace();
					display("Failed to send command to server.");
				}
				break;
		  case "#fontsize": 
			  profile.setFontSize(Integer.parseInt(truncMsg));
			  messageList.setFont(profile.getFont());	  
			  break;
		  case "#fontstyle":
			  profile.setFontStyle(truncMsg);
			  messageList.setFont(profile.getFont());	  
			  break;
		  case "#fontname":
			  profile.setFontName((truncMsg));
			  messageList.setFont(profile.getFont());	  
			  break;
		   default:
			   send();
		  }
		  messageTxF.setText("");
	}

	/**
	 * Displays message in GUIConsole's Message List.
	 */
	@Override
	public void display(String message) {
			messageList.insert("> " + message + "\n", 0);
	}
	/**
	 * Displays message in GUIConsole's Message List with a UserName before the message.
	 */
	@Override
	public void display(String message, String user) 
	{
		messageList.insert(user + "> " + message + "\n", 0);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendToUI(Object o) {
		if(o instanceof String[])
		{
			String[] newString = (String[]) o;
			DefaultComboBoxModel cbm = new DefaultComboBoxModel(
                  newString);
          roomList.setModel(cbm);
          roomList.setSelectedItem(currentRoom);
          roomList.addItem("Create Room");
          
          roomList.repaint();
          
          
		}
		
	}
	
	/**
	 * Method to send message to the Server. If client is not connected, will create LoginDialog
	 */
	public void send() {
		if(!client.isConnected())
		{
			LoginDialog loginDlg = new LoginDialog(args, this);
	        loginDlg.setVisible(true);
		}
		client.handleMessageFromClientUI(messageTxF.getText());
		messageTxF.setText("");
	}
	
	
	
	/**
	 * Main method, creates the JFrame for GUIConsole.
	 * @param args
	 * @param args[0] = host (Default: localhost)
	 * @param args[1] = port (Default: 5555)
	 * @param args[2] = userName (Default: User)
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		String userName = "", host = "";
		int port = 0;
		try {
			host = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			args[0] = "localhost";
		}
		try {
			port = Integer.parseInt(args[1]);
		} catch (Throwable t) {
			args[1] = DEFAULT_PORT + "";
		}
		try {
			userName = args[2];
		} catch (Throwable t) {
			args[2] = "User";
		}
		setClientConsole(new GUIConsole(args));
	}

	public static GUIConsole getClientConsole() {
		return clientConsole;
	}

	public static void setClientConsole(GUIConsole clientConsole) {
		GUIConsole.clientConsole = clientConsole;
	}


}
