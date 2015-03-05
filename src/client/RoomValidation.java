package client;



public class RoomValidation {

	public static boolean authenticate(String[] args, GUIConsole parent)
			throws Exception {


		String room = args[0];
		if (room.isEmpty()) throw new NumberFormatException("Name required");
		//if (room.contains("(") || room.contains(")") || room.contains(" ")) throw new NumberFormatException("Name can't have () or spaces");
		
		if (room.length() > 20) throw new NumberFormatException("Name too long");
		
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

		
		String password = args[2];
		
		if (password.length() > 20) throw new NumberFormatException("Password too long");

		Boolean open = Boolean.valueOf(args[3]);
		if (open) password = "";
		
		if (!open && password.isEmpty()) throw new NumberFormatException("Password required");
		

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
