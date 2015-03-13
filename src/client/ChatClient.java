package client;

import game.TicTacToe;

import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 * 
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	private String userName = "User";
	private TicTacToe game;
	private Profile profile;
	private String[] roomList;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 * 
	 * @param host
	 *            The server to connect to.
	 * @param port
	 *            The port number to connect on.
	 * @param clientUI
	 *            The interface type variable.
	 */

	public ChatClient(String host, int port, ChatIF clientUI)
			throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
	}

	// Instance methods ************************************************

	/**
	 * This method tries to connect to server than throws Exception
	 * 
	 * @param host
	 *            The server to connect to.
	 * @param port
	 *            The port number to connect on.
	 * @param clientUI
	 *            The interface type variable.
	 * @param userName
	 *            The user name for client.
	 */
	public ChatClient(String host, int port, ChatIF clientUI, String userName)
			throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		this.userName = userName;
		openConnection();
		try {
			sendToServer("#login " + userName);
		} catch (IOException e) {
			throw e;
		}

	}

	/**
	 * This method handles all data that comes in from the server.
	 * 
	 * @param msg
	 *            The message from the server.
	 */
	public void handleMessageFromServer(Object message) {
		if (message instanceof String) {
			
			
			
			String msg = message.toString();

			if (msg.indexOf("#") == 0) {
				handleCommandFromServer(msg);

			} else {
				clientUI.display(msg);
			}
		}
		else
		{	
			//sends the object to GUIConsole to handle.
			clientUI.sendToUI(message);
		}
		

	}

	public void handleCommandFromServer(String msg)
	{
		if (msg.indexOf("#") == 0) {
			String cmd;
			cmd = (msg.indexOf(" ") == -1) ? msg : msg.substring(0,
					msg.indexOf(" "));
			int end = msg.length();
			int space = (msg.indexOf(" ") == -1) ? end : msg.indexOf(" ");
			String truncMsg = msg.substring(space, end).trim();

			switch (cmd) {
			//A color must be in here to be able to forward to GUIConsole
			case "#cred":
			case "#cblue":
			case "#cgreen":
			case "#cpink":
			case "#cgrey":
			case "#cgray":
				//System.out.println("Recieved color command:" + cmd);
				clientUI.display(msg);
				break;
				
			case "#roomauth":
				clientUI.sendToUI(msg);
				break;
			case "#from":
				String user = (truncMsg.indexOf(" ") == -1) ? truncMsg
						: truncMsg.substring(0, truncMsg.indexOf(" ")).trim();
				String message = (truncMsg.indexOf(" ") == -1) ? truncMsg
						: truncMsg.substring(truncMsg.indexOf(" "),
								truncMsg.length()).trim();
				clientUI.display(message, user);
				break;
			case "#server":
				clientUI.display("#cred " +  truncMsg, "SERVER MSG");
				break;
			case "#move":
				System.out.println("Entered move case");
				String x = (truncMsg.indexOf(" ") == -1) ? "-1" : truncMsg
						.substring(0, truncMsg.indexOf(" ")).trim();
				int space2 = (truncMsg.indexOf(" ") == -1) ? -1 : truncMsg
						.indexOf(" ");
				String y = (truncMsg.indexOf(" ") == -1) ? "-1" : truncMsg
						.substring(space2, truncMsg.length()).trim();

				try
				{
					if (x == "-1" || y == "-1") {
					System.out
							.println("Invalid move...\nx: " + x + "\ny: " + y);
				} else
					game.movePiece(Integer.parseInt(x), Integer.parseInt(y),
							true);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				break;
			case "#clearboard":
				game.clearBoard(true);
				break;
			default:
				break;
			}
		}
		
	}
	
	
	/**
	 * This method handles all data coming from the UI
	 * 
	 * @param message
	 *            The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		if (message.indexOf("#") == 0) {
			handleClientCommand(message);

		} else {
			try {
				tryToSendToServer(message);
			} catch (Exception e) {
				clientUI.display(e.toString()
						+ "\nChatClient - Couldn't send to server");
			}
		}
	}

	/**
	 * This method handles console commands.
	 */
	public void handleClientCommand(String message) {
		// makes command case insensitive
		message = message.toLowerCase().trim();

		// declarations
		String cmd = "";
		cmd = (message.indexOf(" ") == -1) ? message : message.substring(0,
				message.indexOf(" "));
		int end = message.length();
		int space = (message.indexOf(" ") == -1) ? end : message.indexOf(" ");
		String truncMsg = message.substring(space, end).trim();
//		if (truncMsg.contains("help")) {
//			handleClientCommandHelp(cmd);
//			return;
//		}

		switch (cmd) {
		case "#sethost":
			String newHost = message.substring(space, end).trim();

			String hostName = newHost;

			setHost(hostName);

			clientUI.display("Host now set to " + getHost());
			break;

		case "#setport":
			String newPort = message.substring(space, end).trim();

			int portNum = Integer.parseInt(newPort);

			setPort(portNum);

			clientUI.display("Port now set to " + getPort());
			break;
		case "#logon":
		case "#login":
			try {
				openConnection();
				sendToServer(message);
			} catch (IOException e) {
				try {
					clientUI.display("Unable to connect to " + getInetAddress()
							+ "\n" + e.toString());
				} catch (Exception e1) {

					clientUI.display("Failed to connect on current port.");

				}
			}
			break;
		case "#logoff":
		case "#logout":
			try {
				sendToServer(message);
			} catch (IOException e) {
				try {
					clientUI.display("Unable to close connection to "
							+ getInetAddress() + "\n" + e.toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					clientUI.display("Unable to close connection");
				}
			}
			clientUI.display("Disconnected from server");
			break;

		case "#help":
//			handleClientCommandHelp(cmd);
			clientUI.display("Commands are: ...\n" +
					"#info\n" +
					"#list\n");
			break;
		case "#info":
			try {
				clientUI.display("PORT: " + getPort() + "\n> HOST: "
						+ getHost() + "\n> IP:   " + getInetAddress());
			} catch (Exception e) {
				clientUI.display("PORT: " + getPort() + "\n> HOST: "
						+ getHost());
			}

		default:
			
			try {
				sendToServer(message);
			} catch (Exception e) {
				e.printStackTrace();
				clientUI.display("Failed to send command to server.");
			}
		}
	}

	/**
	 * This method handles help for console commands.
	 */
	private void handleClientCommandHelp(String cmd) {
		clientUI.display("Help for:" + cmd);
	}

	/**
	 * This method tries to send message to server then throws Exception
	 */
	public void tryToSendToServer(Object msg) throws Exception {
		try {
			sendToServer(msg);
		} catch (IOException e) {
			try {
				tryToConnect();
				sendToServer(msg);
			} catch (Exception ce) {
				throw ce;
			}
		}
	}
	
	/**
	 * This method tries to connect to server then throws Exception
	 */
	public void tryToConnect() throws Exception {
		if (isConnected()) {
			throw new Exception("You are already connected");
		} else {
			try {
				
				//LoginDialog loginDlg = new LoginDialog();
				openConnection();
			} catch (Exception e) {

				throw new Exception("Unable to connect");
			}
			try {
				sendToServer("#login " + userName);
			} catch (Exception e2) {
				throw new Exception("Failed to send userName to server");
			}
		}

	}

	/**
	 * This method tries to connect to server.
	 * 
	 * @param host
	 *            Sets host before connecting
	 * @param port
	 *            Sets port before connecting
	 */
	public void tryToConnect(String host, int port) throws Exception {
		setHost(host);
		setPort(port);
		tryToConnect();
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public TicTacToe getGame() {
		return game;
	}

	public void setGame(TicTacToe game) {
		this.game = game;
	}

	public String[] getRoomList() {
		return roomList;
	}


	/**
	 * This method terminates the client.
	 */

	public void quit() {
		try {

			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
	
	protected void connectionException(Exception e)
	{
		clientUI.display("Lost connection to server!");
		clientUI.sendToUI("#logon");
	}
	
}
// End of ChatClient class
