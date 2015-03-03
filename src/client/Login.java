package client;

import java.io.IOException;

public class Login {

	
	public static boolean authenticate(String[] args, GUIConsole parent) throws IOException {
		try 
	    {
		  parent.setChatClient(new ChatClient(args[0], Integer.parseInt(args[1]), parent, args[2]));
	    } 
	    catch(IOException e) 
	    {
	    	throw new IOException("Can't connection to server!");
	    }

       if (args[2].toLowerCase().equals("tyler") && args[3].equals("secret")) { //password.equals("secret")
    	   System.out.println("Password " + args[3] + " accepted");
            return true;
        }
       else if (args[2].toLowerCase().equals("robby") && args[3].equals("fuku")) { //password.equals("secret")
    	   System.out.println("Password " + args[3] + " accepted");
            return true;
       }
       else if (args[2].toLowerCase().equals("eric") && args[3].equals("pass")) { //password.equals("secret")
    	   System.out.println("Password " + args[3] + " accepted");
            return true;
       }
       else
       {
    	   
    	   System.out.println("Password " + args[3] + " rejected");
    	   parent.getChatClient().closeConnection();
    	   return false;
       }
        
    }
}
