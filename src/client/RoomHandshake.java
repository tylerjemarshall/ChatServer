package client;

public class RoomHandshake {

	public static boolean authenticate(char[] password, GUIConsole parent)
			throws Exception {
		try {
			parent.getChatClient().sendToServer(password);
		} catch (Exception e) {
			throw new Exception("Unable to send to server: " + e.getMessage());
		}
		return true;
	}
}
