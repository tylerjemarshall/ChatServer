import java.io.*;
import java.util.*;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 */
/**
 * @author Tyler M
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************

	final public static int DEFAULT_PORT = 5555;

	private EchoServer server;
	private RoomList roomList = new RoomList();
	
	ChatIF serverUI;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 * 
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	/**
	 * Constructs an instance of the echo server.
	 * 
	 * @param port The port number to connect on.
	 * @param serverUI  Extends EchoServer
	 */
	public EchoServer(int port, ChatIF serverUI) {
		super(port);
		this.serverUI = serverUI;

	}

	// Instance methods ************************************************

	/**
	 * This method handles any messages received from the server.
	 */

	public void accept() {
		try {
			BufferedReader fromConsole = new BufferedReader(
					new InputStreamReader(System.in));
			String message;

			while (true) {
				message = fromConsole.readLine();
				handleMessageFromServerUI(message);
			}
		} catch (Exception ex) {
			System.out
					.println(ex.toString()
							+ "\nEcho Server - Unexpected error while reading from console!");
		}
	}

	public void display(String message) {
		System.out.println("> " + message);
	}

	public void display(String message, String user) {
		System.out.println(user + "> " + message);
	}

	/**
	 * This method handles any messages received from the client.
	 * 
	 * @param msg The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (((String) msg).charAt(0) == '#') {
			handleClientCommand(msg, client);
		} else {
			ClientInfo info = roomList.getInfoByClient(client);
			try {
				serverUI.display(msg.toString(), info.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			String message = "#from " + info + " " + msg.toString();
			this.sendToARoom(message, info.getRoom());
		}
	}
	
	

	/**
	 * Method that handles the message sent from the ServerUI
	 * 
	 * @param message
	 *            The message being sent from the ServerUI
	 */
	public void handleMessageFromServerUI(String message) {
		if (message.isEmpty()) {
			serverUI.display("Message Empty");
		} else if (message.indexOf("#") == 0) {

			handleServerCommand(message);
		} else {

			try {
				serverUI.display(message, "SERVER MSG");
				sendToAllRooms("#server " + message);
			} catch (Exception e) {
				serverUI.display("Could not send message to clients.  ");

			}
		}
	}

	/**
	 * Method that handles commands from the Server
	 * Most of these are broken FYI
	 * 
	 * @param message The command being sent.
	 */
	private void handleServerCommand(String msg) {
		String cmd;
		cmd = (msg.indexOf(" ") == -1) ? msg : msg.substring(0,
				msg.indexOf(" "));
		int end = msg.length();
		int space = (msg.indexOf(" ") == -1) ? cmd.length() : msg.indexOf(" ");

		String truncMsg = msg.substring(space, end).trim();

		switch (cmd) {
		case "#quit":
			try {

				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(0);
			break;
		case "#stop":
			server.stopListening();
			break;
		case "#close":
			try {
				server.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "#start":
			try {
				server.listen();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "#getPort":
			serverUI.display("Port is: " + server.getPort());
			break;
		case "#setPort":
			server.setPort(Integer.parseInt(truncMsg));
			serverUI.display("Port set to: " + server.getPort());
			break;
		case "#allowYell":
			String user = (truncMsg.indexOf(" ") == -1) ? truncMsg : truncMsg
					.substring(0, truncMsg.indexOf(" ")).trim();
			Boolean yell = Boolean
					.valueOf((truncMsg.indexOf(" ") == -1) ? truncMsg
							: truncMsg.substring(truncMsg.indexOf(" "),
									truncMsg.length()).trim());
			serverUI.display(user + " can yell is " + yell);
			break;
		default:
			break;
		}
	}

	/**
	 * Method that handles the commands coming from the client.
	 * 
	 * @param objMsg The message containing the command.
	 * @param client The client that send the command.
	 */
	public void handleClientCommand(Object objMsg, ConnectionToClient client) {

		
		//Converts the string to usable variables.
		String msg = objMsg.toString();
		String cmd = "";
		cmd = (msg.indexOf(" ") == -1) ? msg : msg.substring(0,
				msg.indexOf(" "));
		int end = msg.length();
		int space = (msg.indexOf(" ") == -1) ? cmd.length() : msg.indexOf(" ");
		String truncMsg = msg.substring(space, end).trim();
		
		//Declares info and tries to Initialize it.
		ClientInfo info = null;
		try {info=roomList.getInfoByClient(client);}catch(Exception e){}
		
		
		
		switch (cmd) {
		case "#logon":
		case "#login":
			if (truncMsg.isEmpty())
				truncMsg = "User";
			login(client, truncMsg);
			break;
		case "#whisper":
		case "#w":
			String user = (truncMsg.indexOf(" ") == -1) ? truncMsg : truncMsg
					.substring(0, truncMsg.indexOf(" ")).trim();
			String whisper = (truncMsg.indexOf(" ") == -1) ? truncMsg
					: truncMsg.substring(truncMsg.indexOf(" "),
							truncMsg.length()).trim();
			if (isNumber(user))
				whisperToClient(Integer.parseInt(user), whisper, info);
			else
				tryToSendToClient("Must enter clients ID" , info);
			break;
		case "#yell":
			truncMsg = truncMsg.toUpperCase();
			sendToAllRooms(info
					+ " Just yelled " + truncMsg + "!");
			break;
		case "#join":
			roomList.remove(info);
			roomList.add(info, truncMsg);
			tryToSendToClient("You have switched rooms to " + truncMsg, info);
			sendToARoom(info + " Just joined " + truncMsg, truncMsg);
			break;
		case "#info":
			sendToAClient(info.getId(), info + " is in room: "
							+ info.getRoom());
			break;
		case "#exit":
		case "#quit":
			sendToARoom(info + " has left the chat!", info.getRoom());
			break;
		case "#logout":
		case "#logoff":
				try {
					roomList.remove(info);
				} catch (Exception oob) {
					serverUI.display(oob.toString() + "\nFailed to delete " + info);
				}
				sendToARoom(info + " has logged off!", info.getRoom());
			try {
				System.out.println("Attempting to close connction to client");
				client.close();
			} catch (IOException e1) {
				System.out.println("Failed to close connection.");
			}
	
			break;
		case "#listclients":
			break;
		case "#listrooms":
			Collections.sort(roomList);
			tryToSendToClient(roomList.toString(), info);
			break;
		// Game Stuff
		case "#game":
		{
			
		}
		case "#move":
			sendToARoom(msg, client.getInfo("room").toString());
			break;
		case "#clearboard":
			sendToARoom(msg, client.getInfo("room").toString());
			break;
		default:
			display("Invalid Command " + cmd + " sent by "
					+ info);
		}

	}

	/**
	 * Method that checks to see if a String contains only numerical digits.
	 * Returns true if the String is a number.
	 * 
	 * @param number The number being checked.
	 * @return Returns true if the string is a number.
	 */
	private static boolean isNumber(final String number) {
		boolean isNumber = false;
		if (number == null)
			isNumber = false;
		try {
			Integer.parseInt(number);
			isNumber = true;
		} catch (NumberFormatException ne) {
			isNumber = false;
		}
		return isNumber;
	}

	
	/**
	 * Method that logs the client into the room Commons when connecting to the
	 * server. The server also assigns a unique ID to the client during this
	 * time.
	 * 
	 * @param client The client that is logging in.
	 * @param user The userName that the client is going to use.
	 */
	private void login(ConnectionToClient client, String user) {
		login(client, user, "commons");
	}

	/**
	 * Method that logs the client into the room when connecting to the server.
	 * The server also assigns a unique ID to the client during this time.
	 * 
	 * @param client
	 *            The client that is logging in.
	 * @param user
	 *            The userName that the client is going to use.
	 * @param room
	 *            The room that the client is joining.
	 */
	private void login(ConnectionToClient client, String user, String room) {
		ClientInfo tempClient;
			try
			{
				int id = roomList.getClientCount() + 1;
				tempClient = new ClientInfo(client, user, id, room);
				roomList.add(tempClient, room);
				System.out.println("Added client to room");
			}
			catch (Exception e)
			{
				e.printStackTrace();
				System.out.println("Failed to add client to room");
				tempClient = new ClientInfo(client, user, 1, room);
			}
			sendToARoom(tempClient + " just logged in.", room);
		}

	/**
	 * Method that handles the messages being sent to a specific client.
	 * 
	 * @param user
	 *            The users ID the message is being sent to.
	 * @param message
	 *            The message that is being sent.
	 */
	public void sendToAClient(int user, String message) {
		ClientInfo tempClient = roomList.getInfoById(user);
		tryToSendToClient(message, tempClient);
	}


	/**
	 * Method that handles messages being sent to a specific user based on their
	 * ID.
	 * 
	 * @param user
	 *            The users ID of the user you would like to whisper to
	 * @param message
	 *            The message you would like to send to the user
	 * @param clientFrom
	 *            The client that is sending the whisper
	 */

	public void whisperToClient(int user, String message, ClientInfo clientFrom) {
		ClientInfo clientTo = roomList.getInfoById(user);

		message = clientFrom + " whispered to you: " + message;
		tryToSendToClient(message, clientTo);
	}

	/**
	 * Method that handles messages being sent to a specific room.
	 * 
	 * @param msg
	 *            Message being sent
	 * @param room
	 *            Room the message is being sent to
	 */
	public void sendToARoom(Object msg, String room) {
		String message = (String) msg;
		Room tempRoom = roomList.getRoom(room);
		if (!tempRoom.equals(null)) {
			System.out.println("Found room " + tempRoom);
			for (int r = 0; r < tempRoom.size(); r++) {
				tryToSendToClient(message, tempRoom.get(r));
			}
		} else {
			System.out.println("Couldn't find room, not sending message.");
		}

	}
	/**
	 * Method that handles messages being sent to all rooms.
	 * 
	 * @param msg
	 *            Message being sent
	 */
	public void sendToAllRooms(Object msg) {
		String message = (String) msg;
		for (int i = 0; i < roomList.size(); i++) {
			Room currentRoom = roomList.get(i);
			for (int r = 0; r < currentRoom.size(); r++) {

				tryToSendToClient(message, currentRoom.get(r));

			}
		}
	}
	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port "
				+ getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * stops listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the server instance (there
	 * is no UI in this phase).
	 * 
	 * @param args
	 *            [0] The port number to listen on. Defaults to 5555 if no
	 *            argument is entered.
	 */
	public static void main(String[] args) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			port = DEFAULT_PORT; // Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println(ex.toString()
					+ "EchoServer - Could not listen for clients!\n"
					+ ex.toString());
		}
	}

	/**
	 * @param message
	 *            Message being sent to client
	 * @param client
	 *            Client the message is being sent to
	 */
	private void tryToSendToClient(String message, ClientInfo info)
	{
		try {
			info.getClient().sendToClient(message);
		} catch (IOException e) {
			System.out.println("Failed to send to client "
					+ info);
			e.printStackTrace();
		}
		
	}

	protected void clientConnected(ConnectionToClient client) {
		System.out.println("Client " + roomList.getInfoByClient(client)
				+ " connected from " + client);
		
	}

	protected void clientDisconnected(ConnectionToClient client) {
		System.out.println("Client " + roomList.getInfoByClient(client)
				+ " disconnected from " + client);
	}

	protected void clientException(ConnectionToClient client,
			Throwable exception) {
		clientDisconnected(client);
	}

}