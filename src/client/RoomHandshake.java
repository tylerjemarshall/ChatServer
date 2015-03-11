package client;

public class RoomHandshake {

	public static boolean authenticate(char[] password, GUIConsole parent) throws Exception{
	
	// char[] newCharArray = new RoomInfo (password);
		
//		if (password.equals(msg))
//		{
			try
			{
				parent.getChatClient().sendToServer(password);
			}
			catch (Exception e)
			{
				throw new Exception("Unable to send to server: " + e.getMessage());
			}
			
			return true;
//		}
//		else
//		{
//			return false;
//		}
		

	
	}
}
