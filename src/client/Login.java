package client;

import java.awt.Frame;
import java.io.IOException;
import java.util.Scanner;

public class Login {

	
	public static boolean authenticate(String[] args, Frame parent, ChatClient client) throws IOException {
        // hardcoded username and password
		
		
		Scanner cin = new Scanner( System.in );
		
		try 
	    {
			
	      client= new ChatClient(args[0], Integer.parseInt(args[1]), parent, args[2]);
	      cin.close();
	    } 
	    catch(IOException e) 
	    {
	    	cin.close();
	    	throw new IOException("Can't Initialize Client!");
//	      System.out.println("GUIConsole - Can't initialize client! (" + host + " & " + port + ").");
//	      System.out.println("Please enter new Host: ");
//	      host = cin.next();
//	      System.out.println("Please enter new Port: ");
//	      port = cin.nextInt();
//	      controlsEnabled = false; 
	    	
	    }
		
		
		
		
		
        if (args[2].equals("tyler") && true) { //password.equals("secret")
            return true;
        }
        return false;
    }
}
