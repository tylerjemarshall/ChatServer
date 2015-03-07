package client;



public class RoomValidation {

	public static boolean authenticate(String[] args, GUIConsole parent)
			throws Exception {


		
		// A name for room is required, for obvious reasons. 
		// I'm not allowing Parentheses because i use them the represent the size of room.
		// I have a limit of 20 characters in the room, i feel like i might want to shorten this.
		// Lastly, i don't want spaces in the name. I use spaces to collect information from a String.
		String room = args[0];
		if (room.isEmpty()) throw new NumberFormatException("Name required");
		if (room.contains("(") || room.contains(")") ) throw new NumberFormatException("Name can't have (Parentheses)");
		if (room.length() > 20) throw new NumberFormatException("Name too long");
		room = room.replace(" ", "-");
		
		// First, client needs to enter an Integer for the room size, not a string.
		// Secondly, i just check the range, 2 to 30. This can be altered.
		int limit = 0;
		try 
		{
			if (isNumber(args[1])) 
			{
				limit = Integer.parseInt(args[1]);
			}
				
		} 
		catch (Exception e) 
		{
			throw new NumberFormatException(e.getMessage());
		}

		if (limit < 2) throw new NumberFormatException("Room size too small");
		if (limit > 30) throw new NumberFormatException("Room size too big");

		
		// I don't have many rules for the password, i feel like the client should have control over this. 
		// Only limitation is the length.
		String password = args[2];
		
		if (password.length() > 20) throw new NumberFormatException("Password too long");
		

		// This part just enforces client to have a password if room is private. If it's public it ignores entered password.
		Boolean open = Boolean.valueOf(args[3]);
		if (open) password = "";
		
		if (!open && password.isEmpty()) throw new NumberFormatException("Password required");
		

		// Adds the information to the object RoomInfo, to send to the server to create the room.
		RoomInfo newRoom = new RoomInfo ( room, limit, open, password );

		try
		{
			parent.getChatClient().sendToServer(newRoom);
		}
		catch (Exception e)
		{
			throw new Exception("Unable to send to server: " + e.getMessage());
		}
		
		 return true;
		
	}

	private static boolean isNumber(final String number) throws Exception {
		boolean isNumber = false;
		if (number == null)
			isNumber = false;
		try {
			Integer.parseInt(number);
			isNumber = true;
		} catch (NumberFormatException ne) {
			throw new NumberFormatException("Not a number: " + ne.getMessage());
		}
		return isNumber;
	}

}
