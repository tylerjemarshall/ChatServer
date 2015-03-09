package client;

import java.io.IOException;

public class Login {

	
	public static boolean authenticate(String[] args, GUIConsole parent) throws Exception {
		try 
	    {
		  parent.setChatClient(new ChatClient(args[0], Integer.parseInt(args[1]), parent, args[2]));
	    } 
	    catch(IOException e) 
	    {
	    	throw new IOException("Can't connect to server!");
	    }

		
       if (args[2].toLowerCase().equals("tyler") && args[3].equals("secret")) { 
            return true;
        }
       else if (args[2].toLowerCase().equals("robby") && args[3].equals("fuku")) { 
    	   
            return true;
       }
       else if (args[2].toLowerCase().equals("eric") && args[3].equals("pass")) { 
    	   
            return true;
       }
       else if (args[2].toLowerCase().equals("user") && args[3].equals("")) { 
    	   
           return true;
      }
       else
       {
    	   
    	   parent.getChatClient().closeConnection();
    	   throw new Exception("Username or password incorrect");
       }
        
    }
}
