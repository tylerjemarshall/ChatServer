package server;

import java.io.*;
import java.util.*;

import client.ChatIF;
import client.RoomInfo;

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

	//private EchoServer server;
	private RoomList roomList = new RoomList();

	ChatIF serverUI;

	// Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 * 
	 * @param port
	 *            The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
	}

	/**
	 * Constructs an instance of the echo server.
	 * 
	 * @param port
	 *            The port number to connect on.
	 * @param serverUI
	 *            Extends EchoServer
	 */
	public EchoServer(int port, ChatIF serverUI) {
		super(port);
		this.serverUI = serverUI;
		roomList.setLog(serverUI);

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
			display(ex.toString()
					+ "\nEcho Server - Error while reading from console!");
		}
	}

	/**Method used to display the message in the server's console.
	 * > message
	 * @param message The message to be displayed in the server's console
	 */
	public void display(String message) {
		System.out.println("> " + message);
	}
	/**Method used to display the message in the server's console.
	 * > message
	 * @param message The message to be displayed in the server's console
	 * @param user The prefix that can be added before the message.
	 */
	public void display(String message, String user) {
		System.out.println(user + "> " + message);
	}

	/**
	 * This method handles any messages received from the client.
	 * 
	 * @param msg
	 *            The message received from the client.
	 * @param client
	 *            The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof String) {
			String message = (String) msg;
			if (message.indexOf("#") == 0) {
				handleClientCommand(msg, client);
			} else {
				try {
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					display(msg.toString(), client.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				message = "#from " + client.toStringShort() + " " + message;
				this.sendToARoom(message, client.getClientInfo().getRoom());
			}
		}
		else if (msg instanceof char[]){
			char[] passwordSent = (char[]) msg;
			try
			{
				if (Arrays.equals(passwordSent, roomList.getRoom(client.getTempRoom()).getPassword()))
				{
					if (roomList.moveClient(client, client.getTempRoom())) display("Moved client succesfully"); else display("Failed to move client");
					tryToSendToClient("You have switched rooms to " + client.getClientInfo().getRoom(), client);
					sendToARoom(client + " just joined " + client.getTempRoom(), client.getTempRoom());
					updateClient(client);
				}
				else 
				{
				
					tryToSendToClient("Incorrect password", client);
					updateClient(client);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
		// client requesting room being created
		else if (msg instanceof RoomInfo) {
			String oldRoom = client.getClientInfo().getRoom();
			try
			{
				RoomInfo roomInfo = (RoomInfo) msg;
				if (roomList.getRoom(roomInfo.getRoom()) != null) { 
					tryToSendToClient("Room already exists. Going to try and join", client);
					roomList.moveClient(client, roomInfo.getRoom());
					updateClient(client);
					updateRoom(oldRoom);
				} else {
					
					Room newRoom = new Room();
					newRoom.setName(roomInfo.getRoom());
					newRoom.setPassword(roomInfo.getPassword());

					newRoom.setLimit(roomInfo.getLimit());
					newRoom.setReserved(roomInfo.isReserved());
					
					roomList.add(newRoom);
					roomList.moveClient(client, newRoom.getName());
			
					Collections.sort(roomList);

					updateClient(client);
					updateRoom(oldRoom);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			
		}
		else {
			display("Recieved an object from " + client);
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
			display("Message Empty");
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
	 * Method that handles commands from the Server Most of these are broken FYI
	 * 
	 * @param message
	 *            The command being sent.
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

				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.exit(0);
			break;
		case "#stop":
			stopListening();
			break;
		case "#close":
			try {
				close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "#start":
			try {
				listen();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case "#getport":
			try {
				serverUI.display("Port is: " + getPort());
			} catch (Exception e) {
				e.printStackTrace();
				serverUI.display("Port is: null");
			}
			break;
		case "#setport":
			if (isNumber(truncMsg)) {
				setPort(Integer.parseInt(truncMsg));
				serverUI.display("Port set to: " + getPort());
			} else {
				serverUI.display("Invalid Number");
			}
			break;
		case "#allowyell":
			String user = (truncMsg.indexOf(" ") == -1) ? truncMsg : truncMsg
					.substring(0, truncMsg.indexOf(" ")).trim();
			Boolean yell = Boolean
					.valueOf((truncMsg.indexOf(" ") == -1) ? truncMsg
							: truncMsg.substring(truncMsg.indexOf(" "),
									truncMsg.length()).trim());
			if (isNumber(user)) {
				roomList.getClientById(Integer.parseInt(user)).getClientInfo()
						.setYellable(yell);
			} else {
				serverUI.display("Invalid number");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * Method that handles the commands coming from the client.
	 * 
	 * @param objMsg
	 *            The message containing the command.
	 * @param client
	 *            The client that send the command.
	 */
	public void handleClientCommand(Object objMsg, ConnectionToClient client) {
		// Converts the string to usable variables.
		String msg = objMsg.toString();
		String cmd = "";
		cmd = (msg.indexOf(" ") == -1) ? msg : msg.substring(0,
				msg.indexOf(" "));
		int end = msg.length();
		int space = (msg.indexOf(" ") == -1) ? cmd.length() : msg.indexOf(" ");
		String truncMsg = msg.substring(space, end).trim();

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
				whisperToClient(Integer.parseInt(user), whisper, client);
			else
				tryToSendToClient("Must enter clients ID", client);
			break;
		case "#y":
		case "#yell":
			truncMsg = truncMsg.toUpperCase();
			
			if(client.getClientInfo().isYellable())
				sendToAllRooms(client + " Just yelled " + truncMsg + "!");
			else
				tryToSendToClient("You are not allowed to yell", client);
			break;
		case "#j":
		case "#join":
			String oldRoom = client.getClientInfo().getRoom();
			if (roomList.getRoom(truncMsg).isReserved())
			{
				client.setTempRoom(truncMsg);
				tryToSendToClient("#roomauth", client);
			}
			else
			{
				if (roomList.moveClient(client, truncMsg)) display("Moved client succesfully"); else display("Failed to move client");
				tryToSendToClient("You have switched rooms to " + client.getClientInfo().getRoom(), client);
				sendToARoom(client + " Just joined " + truncMsg, truncMsg);
				updateClient(client);
				updateRoom(oldRoom);
			}
			break;
		case "#info":
			sendToAClient((int) client.getId(), client + " is in room: " + client.getClientInfo().getRoom());
			break;
		case "#exit":
		case "#quit":
			sendToARoom(client + " has left the chat!", client.getClientInfo().getRoom());
			break;
		case "#logout":
		case "#logoff":
			sendToARoom(client + " has logged off!", client.getClientInfo().getRoom());
			logoff(client);
			break;
		case "#setlimit":
			try
			{
				if (isNumber(truncMsg))
				{
					int limit = (Integer.parseInt(truncMsg) < 1) ? 1 : Integer.parseInt(truncMsg); //if truncMsg less than 1, will set to 1.
					roomList.getRoomByClient(client).setLimit(limit);
				}//note: i just realized could of also did client.getInfo().getRoom() instead. 
				else
					tryToSendToClient("Must enter a #", client);	
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			break;
		case "#lc":
		case "#listclients":	
		case "#listrooms":
		case "#list":
			Collections.sort(roomList);
			tryToSendToClient(roomList.toString(), client);
			break;
		case "#refresh":
			updateClient(client);
			break;
		// Game Stuff
		case "#game": {
			break;
		}
		case "#move":
			sendToARoom(msg, client.getClientInfo().getRoom());
			break;
		case "#clearboard":
			sendToARoom(msg, client.getClientInfo().getRoom());
			break;

		default:
			tryToSendToClient("Invalid command: " + cmd, client);
		}
	}

	
	public void updateClient(ConnectionToClient client)
	{
		tryToSendToClient(client.getClientInfo(), client);
		sendToAllRooms(roomList.toStringArray());
		updateRoom(client.getClientInfo().getRoom());
	}
	
	public void updateRoom(String room)
	{
		try
		{
			sendToARoom(roomList.getRoom(room).toClientInfoArray(), room);
		}
		catch (NullPointerException npe){};
		
	}
	
	
	/**
	 * Method that checks to see if a String contains only numerical digits.
	 * Returns true if the String is a number.
	 * 
	 * @param number
	 *            The number being checked.
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
	 * @param client
	 *            The client that is logging in.
	 * @param user
	 *            The userName that the client is going to use.
	 */
	private boolean login(ConnectionToClient client, String user) {
		return login(client, user, "commons");
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
	private boolean login(ConnectionToClient client, String user, String room) {
		//ClientInfo info;
		try {
			
			int id = roomList.getClientCount() + 1;
			//info = new ClientInfo(client, user, id, room);
			//client.setClientInfo(info);
			client.setId(id);
			client.setClientName(user);
			boolean exit = (roomList.add(client, room)) ? true :false;
			display("Added client to room");
			
			sendToARoom(client + " just logged in.", room);
			updateClient(client);
			return exit;
		} catch (Exception e) {
			e.printStackTrace();
			display("Failed to add client to room");
			//info = new ClientInfo(client, user, 1, room);
			return false;
		}
	}
	

	public boolean logoff(ConnectionToClient client) {
		try {
			client.close();
		} catch (IOException e) {}
		return roomList.remove(client);
	}
	
//	public boolean logoff(ConnectionToClient client)
//	{
//		return logoff(roomList.getInfoByClient(client));
//	}
	
	/**
	 * Method that handles the messages being sent to a specific client.
	 * 
	 * @param user
	 *            The users ID the message is being sent to.
	 * @param message
	 *            The message that is being sent.
	 */
	public void sendToAClient(int user, String message) {
		ConnectionToClient tempClient = roomList.getClientById(user);
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
	public void whisperToClient(int user, String message, ConnectionToClient clientFrom) {
		ConnectionToClient clientTo = roomList.getClientById(user);
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
		try
		{
			Room tempRoom = roomList.getRoom(room);
			if (tempRoom.equals(null) || msg.equals(null)) {
				display("Message or room is null. Not sending message.");
			} else {
				for (int r = 0; r < tempRoom.size(); r++) {
					tryToSendToClient(msg, tempRoom.get(r));
				}
				
			}
		}
		catch (NullPointerException npe){};
		
	}

	/**
	 * Method that handles messages being sent to all rooms.
	 * 
	 * @param msg
	 *            Message being sent
	 */
	public void sendToAllRooms(Object msg) {
		//String message = (String) msg;
		for (int i = 0; i < roomList.size(); i++) {
			Room currentRoom = roomList.get(i);
			for (int r = 0; r < currentRoom.size(); r++) {
				tryToSendToClient(msg, currentRoom.get(r));
			}
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		display("Server listening for connections on port "
				+ getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * stops listening for connections.
	 */
	protected void serverStopped() {
		display("Server has stopped listening for connections.");
	}

	// Class methods ***************************************************



	/**
	 * @param message
	 *            Message being sent to client
	 * @param client
	 *            Client the message is being sent to
	 */

	private void tryToSendToClient(Object message, ConnectionToClient client) {
		try {
			client.sendToClient(message);
		} catch (IOException e) {
			display("Failed to send to client " + client);
			e.printStackTrace();
		} catch (NullPointerException npe)
		{
			display("Can't send null objects!");
			npe.printStackTrace();
		}
	}

	protected void clientConnected(ConnectionToClient client) {
		try {
			display("Client " + client
					+ " connected from " + client);
			
			
			
		} catch (Exception e) {
			display("Client connected from " + client);
		}
	}

	protected void clientDisconnected(ConnectionToClient client) {
		try {

			display("Client " + client
					+ " disconnected from " + client.getInetAddress());
		} catch (Exception e) {
			display(client + " disconnected. Error: " + e.getMessage());
		}
		finally
		{
			String room = client.getClientInfo().getRoom();
			
			
			if(!roomList.remove(client)) 
				serverUI.display("Failed to remove client from RoomList");
			else
				serverUI.display("Removed client from RoomList");
			
			sendToAllRooms(roomList.toStringArray());
			updateRoom(room);
		}
	}

	protected void clientException(ConnectionToClient client,
			Throwable exception) {
		try {
			clientDisconnected(client);
		} catch (Exception e) {}
	}

}