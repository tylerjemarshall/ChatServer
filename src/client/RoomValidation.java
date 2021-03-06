package client;

public class RoomValidation {
	public static boolean authenticate(String name, String slimit,
			char[] password, Boolean reserved, GUIConsole parent)
			throws Exception {

		// A name for room is required.
		// I'm not allowing Parentheses because i use them the represent the
		// size of room.
		// I have a limit of 18 characters in the room, i feel like i might want
		// to shorten this.
		// Can't have spaces in the name. Used to collect information from a
		// String.
		String room = name;
		if (room.isEmpty())
			throw new NumberFormatException("Name required");
		if (room.contains("(") || room.contains(")"))
			throw new NumberFormatException("Name can't have (Parentheses)");
		if (room.length() > 18)
			throw new NumberFormatException("Name too long");
		room = room.replace(" ", "-");

		// Client needs to enter an Integer for the room size, not a string.
		// Range = 2 to 30.
		int limit = 0;
		try {
			if (isNumber(slimit)) {
				limit = Integer.parseInt(slimit);
			}

		} catch (Exception e) {
			throw new NumberFormatException(e.getMessage());
		}

		if (limit < 2)
			throw new NumberFormatException("Room size too small");
		if (limit > 30)
			throw new NumberFormatException("Room size too big");

		// I don't have many rules for the password, i feel like the client
		// should have control over this.
		// Only limitation is the length.

		System.out.println("Password is...");
		System.out.println(password);
		if (password.length > 18)
			throw new NumberFormatException("Password too long");

		// This part just enforces client to have a password if room is private.
		// If it's public it ignores entered password.
		if (!reserved)
			password = null;

		if (reserved && password.length == 0)
			throw new NumberFormatException("Password required");

		System.out.println("room reserved is: " + reserved);

		// Adds the information to the object RoomInfo, to send to the server to
		// create the room.
		RoomInfo newRoom = new RoomInfo(room, limit, reserved, password);

		try {
			parent.getChatClient().sendToServer(newRoom);
		} catch (Exception e) {
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
