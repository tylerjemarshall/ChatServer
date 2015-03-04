package client;

import java.io.IOException;

public class RoomValidation {

	public static boolean authenticate(String[] args, GUIConsole parent)
			throws IOException {


		String room = args[0];

		int limit = 0;
		try {
			if (isNumber(args[1])) {
				limit = Integer.parseInt(args[1]);
			} else
				throw new IOException("Please enter a valid number.");
		} catch (Exception e) {
			throw new IOException("IDK");
		}

		if (limit < 2)
			throw new IOException("Room size too small");

		String password = args[2];

		Boolean open = Boolean.valueOf(args[3]);

		System.out.println(room + " " + limit + " " + open + " " + password);

		RoomInfo newRoom = new RoomInfo ( room, limit, open, password );
		
		      
//		      try
//		      {
//		         e = (Employee) in.readObject();
//		         in.close();
//		         fileIn.close();
//		      }catch(IOException i)
//		      {
//		         i.printStackTrace();
//		         return;
//		      }catch(ClassNotFoundException c)
//		      {
//		         System.out.println("Employee class not found");
//		         c.printStackTrace();
//		         return;
//		      }
//		
//		parent.getChatClient().sendToServer(new RoomInfo( room, limit, open, password ));
//
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
			throw new IOException("fuck");
		}
		return isNumber;
	}

}
