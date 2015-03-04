package server;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.ChatIF;


public class ServerConsole implements ChatIF {

	private EchoServer server;
	final public static int DEFAULT_PORT = 5555;
	
	
	
	public ServerConsole(int port) {
		
		try 
		  {
			  server= new EchoServer(port, this); 
			  server.listen();
		  } 
		  catch(IOException e) 
		  {
			  System.out.println(e.toString() + "Error: Can't setup connection!"
	                + " Terminating server.");
			  System.exit(1);
		  }
	}

	public static void main(String[] args) {
		
	    int port = 0;  //The port number

	    try
	    {
	      port = Integer.parseInt(args[0]);
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	    	port = DEFAULT_PORT;
	    }
	    
	    ServerConsole chat= new ServerConsole(port); //create new client console pass in the host and default port.
	    chat.accept();  //Wait for console data
	  }

	
	
	
	
	public void display(String message) 
	{
		System.out.println("> " + message);
	}
	
	  public void display(String message, String user) 
	  {
	    System.out.println(user + "> " + message);
	  }
	  
		@Override
		public void sendToUI(Object o) {
			// TODO Auto-generated method stub
			
		}
	
	public void accept() 
	  {
	    try
	    {
	      BufferedReader fromConsole = 
	        new BufferedReader(new InputStreamReader(System.in));
	      String message;

	      while (true) 
	      {
	        message = fromConsole.readLine();
	        server.handleMessageFromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        (ex.toString() + "\nServerConsole - Unexpected error while reading from console!");
	    }
	  }



}