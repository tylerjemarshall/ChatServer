package client;


import java.io.*;
//import java.util.Scanner;


/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 

 */
public class ClientConsole implements ChatIF 
{
  //Class variables *************************************************
  
  /**
   * The default port to connect on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Instance variables **********************************************
  
  /**
   * The instance of the client that created this ConsoleChat.
   */
  ChatClient client;

  
  //Constructors ****************************************************

  /**
   * Constructs an instance of the ClientConsole UI.
   *
   * @param host The host to connect to.
   * @param port The port to connect on.
   */
  public ClientConsole(String host, int port) 
  {
    try 
    {
      client= new ChatClient(host, port, this);
    } 
    catch(IOException exception) 
    {
      System.out.println("Error: Can't setup connection!"
                + " Terminating client.");
      
      System.exit(1);
    }
  }

  
  public ClientConsole(String host, int port, String userName) 
  {
    try 
    {
      client= new ChatClient(host, port, this, userName);
    } 
    catch(IOException e) 
    {
      System.out.println("Error: " + e.toString() + 
    		  "\nUse #setport and #sethost and #connect");
      //System.exit(1);
    }
  }

  
  
  //Instance methods ************************************************
  
  /**
   * This method waits for input from the console.  Once it is 
   * received, it sends it to the client's message handler.
   */
  public void accept() throws Exception
  {
    try
    {
      BufferedReader fromConsole = 
        new BufferedReader(new InputStreamReader(System.in));
      String message;

      while (true) 
      {
        message = fromConsole.readLine();
        client.handleMessageFromClientUI(message);
      }
    } 
    catch (Exception ex) 
    {
    	//ex.printStackTrace(); //for testing purposes.
      System.out.println("ClientConsole - Unable to open connection - try again");
      //this breaks when client unsuccessfully connects - needs fix.
    }
  }

  /**
   * This method overrides the method in the ChatIF interface.  It
   * displays a message onto the screen.
   *
   * @param message The string to be displayed.
   */
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
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of the Client UI.
   *
   * @param args[0] The host to connect to.
   */
  public static void main(String[] args) 
  {
	String userName = "";
    String host = "";
    int port = 0;  //The port number

    try
    {
      host = args[0]; //try to set server address to the command line
    }
    catch(ArrayIndexOutOfBoundsException e)
    {
      host = "localhost"; //if fails will try locally
    }
    
    try
    {
      port = Integer.parseInt(args[1]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //if fails set port to default (5555)
    }
  
    
    try
    {
    	
      userName = args[2]; //Get userName from Command Line
    }
    catch(Throwable t)
    {
      userName = "User"; //if fails set userName to "User"
    }
    
    try
    {
    	ClientConsole chat= new ClientConsole(host, port, userName);
    	chat.accept();  //Wait for console data
    }
    catch(Throwable t)
    {
    	System.out.println(t.toString() + "\nFailed to create ClientConsole");
    }
    
    
  }



}
//End of ConsoleChat class
